package com.example.contrupro3

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.ProjectsModels.ProjectModel
import com.example.contrupro3.models.TeamsModels.TeamMember
import com.example.contrupro3.models.UserModels.ActionButton
import com.example.contrupro3.models.UserModels.IconModel
import com.example.contrupro3.models.UserModels.NotificationModel
import com.example.contrupro3.ui.theme.myBlue
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun SendNotificationToUser(
    title: String, /* Titulo de la notificación. */
    description: String, /* Descripción de la notificación. */
    actionButton: ActionButton?, /* Botón de acción usando el modelo 'ActionButton' exclusivamente. */
    additionalInfo: Map<String, Any>?, /* Datos para guardar usando un 'Map<String, Any> exclusivamente.' */
    icon: IconModel, /* Icono personalizado de la notificación usando el modelo 'IconModel' exclusivamente. */
    methodToGetUserId: String /* Metodo para obtener el usuario. (email | id) */
) {
    val firebase = FirebaseFirestore.getInstance()
    val usersCollection = firebase
        .collection("Users")

    if (methodToGetUserId == "email") {
        val email = additionalInfo?.get("email").toString()
        val userCollection = usersCollection.whereEqualTo("emailToLowerCase", email.lowercase())

        userCollection.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val userDoc = querySnapshot.documents[0]
                val notifyCollection = firebase
                    .collection("Users")
                    .document(userDoc.id)
                    .collection("Notifications")

                val newNotification = NotificationModel(
                    id = null,
                    title = title,
                    description = description,
                    status = "unread",
                    sendAt = "${Date().time}",
                    actionButton = actionButton,
                    additionalInfo = additionalInfo,
                    icon = icon
                )

                notifyCollection.add(newNotification).addOnSuccessListener {
                    val docUpdated = newNotification.copy(id = it.id)
                    notifyCollection
                        .document(it.id)
                        .set(docUpdated)
                }
            }
        }
    }
}

@Composable
fun ProjectSelection(
    userID: String,
    authRepository: AuthRepository,
    onDismiss: () -> Unit,
    onProjectSelected: (ProjectModel) -> Unit
) {
    val projectList = remember { mutableStateOf<List<ProjectModel>>(emptyList()) }
    var projectLoadedStatus by remember { mutableStateOf("Loading") }

    LaunchedEffect(userID) {
        authRepository.loadProjectsFromFirebase(
            projectList,
            { status -> projectLoadedStatus = status })
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight(0.6f),
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Lista De Proyectos",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = myBlue
                )
                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = Color.Black, thickness = 1.dp)
                Spacer(modifier = Modifier.height(7.dp))
                if (projectLoadedStatus == "Loaded") {
                    if (projectList.value.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            items(projectList.value.size) { index ->
                                val proyect = projectList.value[index]

                                if (index == 0) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.TopCenter
                                    ) {
                                        Text(
                                            text = "Estos son todos los proyectos administrados por ti.",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Light
                                            ),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth(0.7f)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .clickable {
                                            onProjectSelected(proyect)
                                            onDismiss()
                                        },
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.project_icon),
                                        contentDescription = "Icono de proyecto",
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(end = 4.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "${proyect.name}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Text(
                                            text = "${proyect.creatorName}",
                                            style = MaterialTheme.typography.labelMedium,
                                            modifier = Modifier.offset(x = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "No hay proyectos creados",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                            textAlign = TextAlign.Center
                        )
                    }
                } else if (projectLoadedStatus == "Loading") {
                    Text(
                        text = "Cargando Proyectos...",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                        textAlign = TextAlign.Center
                    )
                } else if (projectLoadedStatus == "Failed") {
                    Text(
                        text = "Error al cargar proyectos...",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

fun TimestampToDate(timestamp: String?): String {
    if (timestamp.isNullOrEmpty()) return "Fecha no disponible" // O cualquier otro valor predeterminado que prefieras.

    return try {
        val date = Date(timestamp.toLong())
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        format.format(date)
    } catch (e: NumberFormatException) {
        "Formato inválido" // O manejar el error como consideres adecuado.
    }
}
fun GetTimeAgoFromTimestamp(timestamp: String?): String? {
    if (timestamp == null) return ""
    val currentTime = System.currentTimeMillis()
    val difference = currentTime - timestamp.toString().toLong()
    val isFuture = difference < 0
    val absoluteDifference = abs(difference)

    val seconds = absoluteDifference / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val months = days / 30
    val years = days / 365

    if (!isFuture) {
        return when {
            years > 0 -> "Hace ${years.toInt()} ${if (years.toInt() == 1) "año" else "años"}"
            months > 0 -> "Hace ${months.toInt()} ${if (months.toInt() == 1) "mes" else "meses"}"
            days > 0 -> "Hace ${days.toInt()} ${if (days.toInt() == 1) "día" else "días"}"
            hours > 0 -> "Hace ${hours.toInt()} ${if (hours.toInt() == 1) "hora" else "horas"}"
            minutes > 0 -> "Hace ${minutes.toInt()} ${if (minutes.toInt() == 1) "minuto" else "minutos"}"
            else -> "Hace ${seconds.toInt()} ${if (seconds.toInt() == 1) "segundo" else "segundos"}"
        }
    } else {
        val difference = timestamp.toString().toLong() - currentTime
        val absoluteDifference = abs(difference)
        val seconds = absoluteDifference / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 30
        val years = days / 365

        return when {
            years > 0 -> "En ${years.toInt()} ${if (years.toInt() == 1) "año" else "años"}"
            months > 0 -> "En ${months.toInt()} ${if (months.toInt() == 1) "mes" else "meses"}"
            days > 0 -> "En ${days.toInt()} ${if (days.toInt() == 1) "día" else "días"}"
            hours > 0 -> "En ${hours.toInt()} ${if (hours.toInt() == 1) "hora" else "horas"}"
            minutes > 0 -> "En ${minutes.toInt()} ${if (minutes.toInt() == 1) "minuto" else "minutos"}"
            else -> "En ${seconds.toInt()} ${if (seconds.toInt() == 1) "segundo" else "segundos"}"
        }
    }
    return null
}

fun UpdateProjectCounter(
    projectId: String?,
    ownerId: String?,
    propertiesToUpdate: String,
    toAdd: Boolean,
    counts: Int? = 1
) {
    val firebase = FirebaseFirestore.getInstance()
    val collection = firebase
        .collection("Users")
        .document(ownerId.toString())
        .collection("Projects")
        .document(projectId.toString())

    collection.get().addOnSuccessListener { documentSnapshot ->
        if (documentSnapshot.exists()) {
            val project = documentSnapshot.toObject(ProjectModel::class.java)

            when (propertiesToUpdate) {
                "members" -> collection.update(
                    mapOf(
                        "counters.members" to if (toAdd) project?.counters?.members!! + counts!! else project?.counters?.members!! - counts!!
                    )
                )

                "documents" -> collection.update(
                    mapOf(
                        "counters.documents" to if (toAdd) project?.counters?.documents!! + counts!! else project?.counters?.documents!! - counts!!
                    )
                )

                "tasks" -> collection.update(
                    mapOf(
                        "counters.tasks" to if (toAdd) project?.counters?.tasks!! + counts!! else project?.counters?.tasks!! - counts!!
                    )
                )

                "teams" -> collection.update(
                    mapOf(
                        "counters.teams" to if (toAdd) project?.counters?.teams!! + counts!! else project?.counters?.teams!! - counts!!
                    )
                )
            }
        }
    }
}

fun SendNotificationToProjectMembers(
    title: String, /* Titulo de la notificación. */
    description: String, /* Descripción de la notificación. */
    actionButton: ActionButton? = null, /* Botón de acción usando el modelo 'ActionButton' exclusivamente. */
    additionalInfo: Map<String, Any>?, /* Datos para guardar usando un 'Map<String, Any> exclusivamente.' */
    icon: IconModel, /* Icono personalizado de la notificación usando el modelo 'IconModel' exclusivamente. */
) {
    val ownerId = additionalInfo?.get("ownerId")
    val projectId = additionalInfo?.get("projectId")
    val firebase = FirebaseFirestore.getInstance()
    val teamsCollection = firebase
        .collection("Users")
        .document(ownerId.toString())
        .collection("Projects")
        .document(projectId.toString())
        .collection("Teams")

    teamsCollection.get().addOnSuccessListener { teamsSnapshot ->
        for (teamDocument in teamsSnapshot.documents) {
            val usersCollection = teamDocument.reference.collection("Members")
            usersCollection.whereEqualTo("inviteStatus", "Accepted").get()
                .addOnSuccessListener { membersQuerySnapshot ->
                    for (memberDocument in membersQuerySnapshot.documents) {
                        val member = memberDocument.toObject(TeamMember::class.java)
                        if (member != null) {
                            val memberCollection = firebase
                                .collection("Users")
                                .document(member.userUID.toString())
                                .collection("Notifications")

                            val newNotify = NotificationModel(
                                id = null,
                                title = title,
                                description = description,
                                status = "unread",
                                sendAt = "${Date().time}",
                                actionButton = actionButton,
                                additionalInfo = additionalInfo,
                                icon = icon
                            )

                            memberCollection.add(newNotify)
                                .addOnSuccessListener { documentReference ->
                                    val docUpdated = newNotify.copy(id = documentReference.id)
                                    memberCollection
                                        .document(documentReference.id)
                                        .set(docUpdated)
                                        .addOnSuccessListener {
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e(
                                                "ERROR",
                                                "Ocurrio un error al establecer la id de un documento.\n\n$exception"
                                            )
                                        }
                                }
                                .addOnFailureListener { exception ->
                                    Log.e(
                                        "ERROR",
                                        "Ocurrio un error al añadir una notificación.\n\n$exception"
                                    )
                                }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(
                        "ERROR",
                        "Ocurrio un error al obtener la coleccion de miembros.\n\n$exception"
                    )
                }
        }
    }
        .addOnFailureListener { exception ->
            Log.e("ERROR", "Ocurrio un error al obtener la coleccion de equipos.\n\n$exception")
        }
}