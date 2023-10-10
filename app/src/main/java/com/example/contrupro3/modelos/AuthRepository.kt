package com.example.contrupro3.modelos

import android.util.Log
import androidx.compose.runtime.MutableState
import com.google.firebase.firestore.DocumentReference
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser

class AuthRepository(private val auth: FirebaseAuth) {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    fun loadProjectsFromFirebase(projectsList: MutableState<List<Project>>) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()
        if (user != null) {
            val projectsCollection = firestore.collection("Usuarios")
                .document(user.uid)
                .collection("Proyectos")

            projectsCollection.get()
                .addOnSuccessListener { documents ->
                    val loadedProjects = documents.map { document ->
                        val project = document.toObject(Project::class.java)
                        project.copy(id = document.id)
                    }
                    projectsList.value = loadedProjects
                }
                .addOnFailureListener { exception ->
                }
        }
    }
    fun loadProject(projectId: String, project: MutableState<Project?>) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()

        if (user != null) {
            val docProject = firestore.collection("Usuarios")
                .document(user.uid)
                .collection("Proyectos")
                .document(projectId)

            docProject.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val loadedDocument = document.toObject(Project::class.java)
                        if (loadedDocument != null) project.value = loadedDocument
                    }
                }
        }
    }

    fun loadEquiposFromFirebase(equiposList: MutableState<List<Equipos>>) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()
        if (user != null) {
            val equiposCollection = firestore.collection("Usuarios")
                .document(user.uid)
                .collection("Equipos")

            equiposCollection.get()
                .addOnSuccessListener { documents ->
                    val loadedEquipos = documents.map { document ->
                        val equipo = document.toObject(Equipos::class.java)
                        equipo.copy(id = document.id)
                    }
                    equiposList.value = loadedEquipos
                }
        }
    }
    fun loadEquipo(equipoID: String, equipo: MutableState<Equipos?>) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()

        if (user != null) {
            val equipoDocument = firestore.collection("Usuarios")
                .document(user.uid)
                .collection("Equipos")
                .document(equipoID)

            equipoDocument.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val loadedEquipo = document.toObject(Equipos::class.java)
                        equipo.value = loadedEquipo
                    } else {
                        equipo.value = null
                    }
                }
                .addOnFailureListener { exception ->
                    equipo.value = null
                }
        }
    }

    fun loadDocumentsFromFirebase(documentsList: MutableState<List<DocumentModel>>) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()
        if (user != null) {
            val documentsCollection = firestore.collection("Usuarios")
                .document(user.uid)
                .collection("Documentos")

            documentsCollection.get()
                .addOnSuccessListener { documents ->
                    val loadedDocs = documents.map { document ->
                        val doc = document.toObject(DocumentModel::class.java)
                        doc.copy(id = document.id)
                    }
                    documentsList.value = loadedDocs // Actualiza el valor del MutableState aquí
                }
                .addOnFailureListener { exception ->
                    // Manejar el error en caso de que la carga de equipos falle
                }
        }
    }
    fun loadDocument(documentoId: String, documento: MutableState<DocumentModel?>) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()

        if (user != null) {
            val docDocument = firestore.collection("Usuarios")
                .document(user.uid)
                .collection("Documentos")
                .document(documentoId)

            docDocument.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val loadedDocument = document.toObject(DocumentModel::class.java)
                        documento.value = loadedDocument
                    } else {
                        documento.value = null
                    }
                }
                .addOnFailureListener { exception ->
                    documento.value = null
                }
        }
    }




    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
    fun getLoggedInUserUID(): LiveData<String> {
        val user = FirebaseAuth.getInstance().currentUser
        return MutableLiveData(user?.uid ?: "")
    }
    fun getLoggedInUserName(): LiveData<String> {
        val userNameLiveData = MutableLiveData<String>()
        val user = auth.currentUser
        if (user != null) {
            val firestore = FirebaseFirestore.getInstance()
            val docRef: DocumentReference = firestore.collection("Usuarios").document(user.uid)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("Nombre") ?: "Nombre desconocido"
                        val lastName = document.getString("Apellido") ?: "Apellido desconocido"
                        val fullName = "${name.capitalize()} ${lastName.capitalize()}"
                        val truncatedFullName = if (fullName.length > 18) {
                            fullName.substring(0, 18) + "..."
                        } else {
                            fullName
                        }

                        userNameLiveData.value = truncatedFullName
                        Log.d("MyApp", "Documento del usuario: $document")
                        Log.d("MyApp", "Nombre del usuario: $truncatedFullName")
                    } else {
                        userNameLiveData.value = "Usuario no autenticado"
                    }
                }
                .addOnFailureListener { exception ->
                    userNameLiveData.value = "Error al obtener el nombre del usuario"
                    Log.e("MyApp", "Error al obtener el nombre del usuario", exception)
                }
        } else {
            userNameLiveData.value = "Usuario no autenticado"
        }
        return userNameLiveData
    }


    fun validateInput(
        name: String,
        lastName: String,
        email: String,
        phonenumber: String,
        role: String,
        password: String,
        repeatPassword: String

    ): String {
        // Comprueba si los campos están vacíos
        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || phonenumber.isEmpty() || role.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            return "Todos los campos son obligatorios."
        }

        // Validar que el nombre y apellido sólo contengan letras
        if (!name.matches(Regex("^[a-zA-Z]+$")) || !lastName.matches(Regex("^[a-zA-Z]+$"))) {
            return "El nombre y apellido deben contener sólo letras."
        }

        // Validar que el número de teléfono sólo contenga números
        if (!phonenumber.matches(Regex("^[0-9]+$"))) {
            return "El número de teléfono debe contener sólo números."
        }

        // Validar que el telefono contenga 10 digitos
        if (phonenumber.length != 10) {
            return "El numero de telefono no es valido"
        }

        // Validar que el correo electrónico termine en @hotmail.com o @gmail.com
        if (!email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"))) {
            return "El correo electrónico no es válido."
        }

        // Validar que se haya seleccionado un rol
        if (role == "Selecciona un rol...") {
            return "Debe seleccionar un rol."
        }

        // Validar que la contraseña y la repetición de la contraseña coincidan
        if (password != repeatPassword) {
            return "Las contraseñas no coinciden."
        }

        // Validar que la contraseña tenga al menos 6 caracteres
        if (password.length < 6) {
            return "La contraseña debe tener al menos 6 caracteres."
        }

        return ""
    }

    fun getAuth(): FirebaseAuth {
        return auth
    }

    fun isEmailVerified(): Boolean {
        val user = auth.currentUser
        return user?.isEmailVerified ?: false
    }

    fun registerUser(
        name: String,
        lastName: String,
        email: String,
        phonenumber: String,
        password: String,
        role: String,
        onFail: (String) -> Unit,
        onSuccess: () -> Unit // Nuevo parámetro
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val capitalizedFirstName = name.capitalize()
                    val capitalizedLastName = lastName.capitalize()

                    val userData = hashMapOf(
                        "Nombre" to capitalizedFirstName,
                        "Apellido" to capitalizedLastName,
                        "Correo electrónico" to email,
                        "Numero de telefono" to phonenumber,
                        "Rol" to role
                        // Agrega otros campos que desees almacenar en Firestore
                    )

                    if (user != null) {
                        val firestore = FirebaseFirestore.getInstance()
                        firestore.collection("Usuarios")
                            .document(user.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                // Los datos del usuario se guardaron en Firestore correctamente
                                user.sendEmailVerification().addOnCompleteListener { emailTask ->
                                    if (emailTask.isSuccessful) {
                                        // El correo electrónico se envió con éxito
                                        onSuccess() // Llamada al callback de éxito
                                    } else {
                                        // El envío del correo electrónico falló
                                        onFail("EMAIL_VERIFICATION_FAILED")
                                    }
                                }
                            }
                            .addOnFailureListener { exception: Exception ->
                                // Error al guardar los datos del usuario en Firestore
                                onFail("FIRESTORE_ERROR")
                            }
                    }

                    // ... (puedes hacer algo con el usuario, como navegar a la página principal)
                } else {
                    // La autenticación falló
                    val exception = task.exception
                    if (exception is FirebaseAuthUserCollisionException) {
                        // Correo electrónico ya en uso
                        onFail("EMAIL_ALREADY_IN_USE")
                    } else {
                        // Otro error de autenticación
                        // ...
                    }
                }
            }
    }


    fun logoutUser(onComplete: (Boolean) -> Unit) {
        auth.signOut()
        onComplete(auth.currentUser == null) // Invocará onComplete con true si el usuario se desconectó correctamente
    }

    fun resetPassword(email: String, onSuccess: () -> Unit, onFail: () -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFail()
            }
        }
    }


    fun loginUser(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFail: (String) -> Unit
    ) {
        // Verificar el formato del correo electrónico
        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        if (!email.matches(emailRegex)) {
            onFail("EMAIL_INVALID")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso, ahora obtenemos el nombre del usuario de Firestore
                    val db = FirebaseFirestore.getInstance()
                    val docRef = db.collection("Usuarios").document(auth.currentUser?.uid ?: "")
                    docRef.get().addOnSuccessListener { document ->
                        if (document != null) {
                            // Aquí, asumo que tienes un campo "Nombre" y "Apellido" en tu documento.
                            val nombre = document.getString("Nombre") ?: ""
                            val apellido = document.getString("Apellido") ?: ""
                            val fullName = "$nombre $apellido"
                            onSuccess(fullName)
                        } else {
                            onFail("Failed to get user document.")
                        }
                    }.addOnFailureListener {
                        onFail(it.message ?: "Unknown error.")
                    }
                } else {
                    // Error en el inicio de sesión
                    val exception = task.exception
                    if (exception is FirebaseAuthInvalidUserException) {
                        // Usuario no existente
                        onFail("USER_NOT_FOUND")
                    } else if (exception is FirebaseAuthInvalidCredentialsException) {
                        // Contraseña no válida
                        onFail("PASSWORD_INVALID")
                    } else {
                        // Otro error
                        onFail("UNKNOWN_ERROR")
                    }
                }
            }
    }
}