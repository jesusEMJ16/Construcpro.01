package com.example.contrupro3

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.contrupro3.models.ProjectsModels.Project
import com.example.contrupro3.models.UserModels.ActionButton
import com.example.contrupro3.models.UserModels.IconModel
import com.example.contrupro3.models.UserModels.NotificationModel
import com.example.contrupro3.ui.theme.myBlue
import com.example.contrupro3.ui.theme.myOrangehigh
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

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
    onProjectSelected: (Project) -> Unit
) {
    val projectList = remember { mutableStateOf<List<Project>>(emptyList()) }
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

                                if(index == 0) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.TopCenter
                                    ) {
                                        Text(
                                            text = "Estos son todos los proyectos administrados por ti.",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
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
                                        painter = painterResource(id = R.drawable.proyect_icon),
                                        contentDescription = "Icono de proyecto",
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(end = 4.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "${proyect.projectName}",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
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