package com.example.contrupro3

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Overlay
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.contrupro3.models.UserModels.ActionButton
import com.example.contrupro3.models.UserModels.IconModel
import com.example.contrupro3.models.UserModels.NotificationModel
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
        val userCollection = usersCollection.whereEqualTo("email", email)

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