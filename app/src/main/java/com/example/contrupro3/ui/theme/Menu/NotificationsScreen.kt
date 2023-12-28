package com.example.contrupro3.ui.theme.Menu

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.GroupRemove
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.TeamsModels.TeamMember
import com.example.contrupro3.models.UserModels.ActionButton
import com.example.contrupro3.models.UserModels.NotificationModel
import com.example.contrupro3.ui.theme.myBlue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.log

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationsDialog(
    authRepository: AuthRepository,
    userId: String,
    newNotificationsCounter: String,
    onDismiss: () -> Unit
) {
    val notificationsList = remember { mutableStateOf<List<NotificationModel>>(emptyList()) }
    authRepository.loadNotifications(notificationsList)
    if (newNotificationsCounter.toInt() > 0) UpdateNotifyStatus(userId)

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .width(300.dp)
                .height(520.dp)
                .offset(x = 10.dp, y = -30.dp)
                .zIndex(1f),
            shape = RoundedCornerShape(15.dp),
            elevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Notificaciones",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.offset(x = 10.dp)
                    )
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Configuraciones",
                        tint = myBlue
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Divider(thickness = 1.dp, modifier = Modifier.fillMaxWidth(), color = Color.Black)
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                if (notificationsList.value.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No hay notificaciones en la bandeja.",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(notificationsList.value.filter { n -> n.status == "unread" }
                            .sortedByDescending { it.sendAt }.size) { index ->
                            if (index == 0 && notificationsList.value.filter { n -> n.status == "unread" }.size > 0) {
                                Text(
                                    text = "Recientes",
                                    style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
                                )
                                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                            }
                            var notify =
                                notificationsList.value.filter { n -> n.status == "unread" }
                                    .sortedByDescending { it.sendAt }[index]
                            Notification(notify, authRepository) { onDismiss() }
                            Spacer(modifier = Modifier.padding(vertical = 15.dp))
                        }
                        items(notificationsList.value.filter { n -> n.status == "readed" }
                            .sortedByDescending { it.sendAt }.size) { index ->
                            if (index == 0 && notificationsList.value.filter { n -> n.status == "readed" }.size > 0) {
                                Text(
                                    text = "Vistos",
                                    style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
                                )
                                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                            }
                            var notify =
                                notificationsList.value.filter { n -> n.status == "readed" }
                                    .sortedByDescending { it.sendAt }[index]
                            Notification(notify, authRepository) { onDismiss() }
                            Spacer(modifier = Modifier.padding(vertical = 15.dp))
                        }
                    }
                }
            }
        }
    }
}

fun UpdateNotifyStatus(userId: String) {
    val firebase = FirebaseFirestore.getInstance()
    val notifyCollection = firebase
        .collection("Users")
        .document(userId)
        .collection("Notifications")

    notifyCollection.whereEqualTo("status", "unread").get().addOnSuccessListener { QuerySnapshot ->
        if (!QuerySnapshot.isEmpty) {
            for (document in QuerySnapshot) {
                val notify = document.toObject(NotificationModel::class.java)
                val newStatus = mapOf(
                    "status" to "readed"
                )
                notifyCollection.document(notify.id.toString()).update(newStatus)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Notification(
    notify: NotificationModel,
    authRepository: AuthRepository,
    onDismiss: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box() {
            CustomIcon(notify.icon?.icon!!, notify.icon.color!!)
        }
        Column {
            Text(
                text = notify.description.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                modifier = Modifier.padding(start = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeAgoFormatted(timestamp = notify.sendAt!!.toLong())
                AcceptButton(notify, authRepository) { onDismiss() }
            }
        }
    }
}

@Composable
fun AcceptButton(
    notify: NotificationModel,
    authRepository: AuthRepository,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val loggedInUserUID = authRepository.getLoggedInUserUID().observeAsState("")

    if (notify.actionButton?.action == "accept") {
        TextButton(
            onClick = { AcceptActions(notify, authRepository, context, loggedInUserUID) },
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Gray.copy(alpha = 0.1f)
            ),
            modifier = Modifier
                .width(85.dp)
                .size(30.dp)
        ) {
            Text(
                text = "Aceptar",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Black,
                    fontSize = 10.sp
                )
            )
        }
    }
}

fun AcceptActions(
    notify: NotificationModel,
    authRepository: AuthRepository,
    context: Context,
    loggedInUserUID: State<String>
) {
    if(notify.actionButton?.clicked == false) {
        val firebase = FirebaseFirestore.getInstance()
        val user = authRepository.getCurrentUser()
        val ownerId = notify.additionalInfo?.get("ownerId")
        val projectId = notify.additionalInfo?.get("projectId")
        val teamId = notify.additionalInfo?.get("teamId")
        val collection = firebase
            .collection("Users")
            .document(ownerId.toString())
            .collection("Projects")
            .document(projectId.toString())
            .collection("Teams")
            .document(teamId.toString())
            .collection("Members")

        when (notify.icon?.icon.toString()) {
            "GroupAdd" -> {
                collection.whereEqualTo("email", user?.email.toString()).get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            for (document in querySnapshot) {
                                val memberData = document.toObject(TeamMember::class.java)
                                val userCollection = firebase
                                    .collection("Users")
                                    .document(user?.uid.toString())

                                userCollection.get().addOnSuccessListener { documentSnapshot ->
                                    if (documentSnapshot.exists()) {
                                        val userData = documentSnapshot.toObject(TeamMember::class.java)
                                        val dataToSave = TeamMember(
                                            id = memberData.id,
                                            name = userData?.name,
                                            lastName = userData?.lastName,
                                            email = memberData.email,
                                            role = null,
                                            phoneNumber = userData?.phoneNumber,
                                            inviteStatus = "Accepted"
                                        )

                                        collection
                                            .document(memberData.id.toString())
                                            .set(dataToSave)
                                        Toast.makeText(
                                            context,
                                            "Invitación aceptada",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                    }
            }
        }
        UpdateClickedNotification(notify, firebase, loggedInUserUID)
    } else {
        Toast.makeText(
            context,
            "Ya has aceptado esta notificación",
            Toast.LENGTH_LONG
        ).show()
    }

}

fun UpdateClickedNotification(
    notify: NotificationModel,
    firebase: FirebaseFirestore,
    loggedInUserUID: State<String>
) {
    val collection = firebase
        .collection("Users")
        .document(loggedInUserUID.value)
        .collection("Notifications")
        .document(notify.id.toString())

    val updatedButton = ActionButton(
        notify.actionButton?.text,
        notify.actionButton?.action,
        true
    )
    notify.actionButton?.clicked = true
    collection.update("actionButton", updatedButton )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeAgoFormatted(timestamp: Long) {
    val now = Instant.now().toEpochMilli()
    val timeAgo = getTimeAgo(timestamp, now)
    Text(
        text = "$timeAgo",
        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun getTimeAgo(timestamp: Long, currentTime: Long): String {
    val instantTimestamp = Instant.ofEpochMilli(timestamp)
    val instantCurrent = Instant.ofEpochMilli(currentTime)
    val duration = Duration.between(instantTimestamp, instantCurrent)

    return when {
        duration.toMinutes() < 1 -> "Hace unos segundos"
        duration.toHours() < 1 -> "Hace ${duration.toMinutes()} minutos"
        duration.toDays() < 1 -> "Hace ${duration.toHours()} horas"
        else -> {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val localDateTime = LocalDateTime.ofInstant(instantTimestamp, ZoneOffset.UTC)
            formatter.format(localDateTime)
        }
    }
}

@Composable
fun CustomIcon(icon: String, color: String) {
    val iconMappings = mapOf(
        "GroupAdd" to Icons.Default.GroupAdd,
        "GroupRemove" to Icons.Default.GroupRemove
    )
    val iconColor = mapOf(
        "Green" to Color.Green,
        "Red" to Color.Red
    )
    val imageVector = iconMappings[icon] ?: Icons.Default.NotificationsNone
    val colorFormat = iconColor[color] ?: myBlue
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        tint = colorFormat,
        modifier = Modifier
            .size(30.dp)
    )
}