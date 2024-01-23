const functions = require("firebase-functions");
const admin = require("firebase-admin");
const nodemailer = require("nodemailer");
const { v4: uuidv4 } = require('uuid'); // Añade esto para generar tokens únicos

admin.initializeApp();

const transporter = nodemailer.createTransport({
  service: "gmail",
  auth: {
    user: functions.config().email.user,
    pass: functions.config().email.pass,
  },
});

exports.sendInvitation = functions.firestore
    .document("invitaciones/{invitationId}")
    .onCreate(async (snap, context) => {
      const data = snap.data();

      // Si el estado no es "pendiente", simplemente salimos de la función.
      if (data.estado !== "pendiente") {
        return null;
      }

      const userExists = await admin.auth().getUserByEmail(data.email).catch((error) => null);

      // Generar un token único
      const token = uuidv4();

      // Almacena el token en Firestore con una fecha de expiración
      await snap.ref.update({ token: token, expires: admin.firestore.Timestamp.fromDate(new Date(Date.now() + 24*60*60*1000)) });

      // Crear un link con el token
      const link = `https://us-central1-ConstrucPro.cloudfunctions.net/acceptInvitation?token=${token}`;

      let mailOptions;

      if (!userExists) {
        mailOptions = {
          from: "jesusEMJ16@gmail.com",
          to: data.email,
          subject: "Invitación para registrarse",
          text: (
            "¡Hola! Te invitamos a registrarte en nuestra plataforma ConstrucPro." + 
            "Una vez registrado, podrás unirte al equipo. Haz clic en el siguiente enlace para aceptar: " + link
          ),
        };
      } else {
        mailOptions = {
          from: "jesusEMJ16@gmail.com",
          to: data.email,
          subject: "Invitación para unirte al equipo",
          text: (
            "¡Hola! Te hemos invitado a unirte a nuestro equipo." + 
            "Haz clic en el siguiente enlace para aceptar: " + link
          ),
        };
      }

      return transporter.sendMail(mailOptions, (error, data) => {
        if (error) {
          console.log(error);
          return;
        }
        console.log("Correo enviado exitosamente!");
      });
    });

// Función para aceptar la invitación
exports.acceptInvitation = functions.https.onRequest(async (req, res) => {
    const token = req.query.token;

    if (!token) {
        res.status(400).send('Token no válido');
        return;
    }

    const invitations = await admin.firestore().collection('invitaciones').where('token', '==', token).limit(1).get();

    if (invitations.empty) {
        res.status(404).send('Invitación no encontrada');
        return;
    }

    const invitation = invitations.docs[0];

    if (invitation.data().estado !== 'pendiente' || invitation.data().expires.toDate() < new Date()) {
        res.status(400).send('Invitación ya fue procesada o ha expirado');
        return;
    }

    await invitation.ref.update({
        estado: 'aceptado',
        token: admin.firestore.FieldValue.delete(), // Eliminar el token después de su uso
        expires: admin.firestore.FieldValue.delete() // Eliminar el tiempo de expiración después de su uso
    });

    res.status(200).send('Invitación aceptada exitosamente');
});