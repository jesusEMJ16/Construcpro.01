package com.example.contrupro3.ui.theme.Menu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Badge
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.contrupro3.R
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.ui.theme.lightblue
import com.example.contrupro3.ui.theme.myBlue
import com.google.firebase.firestore.FirebaseFirestore

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HamburgueerMenu(navController: NavController, authRepository: AuthRepository) {
    var isDrawerOpen by remember { mutableStateOf(false) }
    val offsetX by animateDpAsState(if (isDrawerOpen) 0.dp else (-330).dp, label = "")
    val loggedInUserName: String by authRepository.getLoggedInUserName().observeAsState("")
    val loggedInUserUID: String by authRepository.getLoggedInUserUID().observeAsState("")
    val notificationsDialogEnabled = remember { mutableStateOf(false) }
    val newNotificationsCount = remember { mutableStateOf("0") }
    GetNotificationsCounter(loggedInUserUID) { notifys -> newNotificationsCount.value = notifys }

    LaunchedEffect(key1 = navController.currentBackStackEntry) {
        isDrawerOpen = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .width(330.dp)
                .fillMaxHeight()
                .offset { IntOffset(offsetX.roundToPx(), 0) }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 16.dp)
            ) {
                Card(
                    backgroundColor = Color.White.copy(alpha = 1f),
                    modifier = Modifier
                        .background(Color.Transparent)
                        .fillMaxSize()
                        .shadow(
                            4.dp,
                            shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                        ),
                    shape = RoundedCornerShape(topEnd = 30.dp, bottomEnd = 20.dp)
                ) {
                    Column(
                        Modifier.padding(0.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(align = Alignment.Bottom),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            val welcomeText = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) { append("Bienvenido: ") }
                                withStyle(
                                    style = SpanStyle(
                                        color = lightblue,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) { append(loggedInUserName) }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .padding(vertical = 5.dp)
                                    .wrapContentHeight(align = Alignment.Bottom)
                            ) {
                                Text(
                                    text = welcomeText,
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                                Icon(
                                    if (notificationsDialogEnabled.value) Icons.Default.Notifications else Icons.Default.NotificationsNone,
                                    contentDescription = "Notificaciones",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.TopEnd)
                                        .offset(x = 20.dp, y = -25.dp)
                                        .clickable(
                                            onClick = {
                                                notificationsDialogEnabled.value =
                                                    !notificationsDialogEnabled.value
                                            }
                                        ),
                                    tint = myBlue
                                )

                                if(newNotificationsCount.value.toInt() > 0) {
                                    Badge(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(4.dp)
                                            .offset(x = if(newNotificationsCount.value == "+9") 27.dp else 22.dp, y = -15.dp)
                                            .zIndex(1f),
                                        backgroundColor = myBlue
                                    ) {
                                        Text(
                                            text = newNotificationsCount.value.toString(),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Divider(color = Color.LightGray, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(5.dp))
                        MenuOpciones(navController, authRepository)
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(20.dp))
                            Image(
                                painter = painterResource(R.drawable.logo),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .height(80.dp)
                                    .width(80.dp)
                            )
                        }
                        SectionDown(navController, authRepository)
                    }
                }
            }
        }
        IconButton(
            onClick = { isDrawerOpen = !isDrawerOpen },
            modifier = Modifier
                .size(56.dp) // Ajusta el tamaño del botón
                .padding(6.dp)
        ) {
            Crossfade(targetState = isDrawerOpen, label = "") { state ->
                if (state) {
                    Icon(
                        painterResource(R.drawable.menu_back),
                        contentDescription = "Close Menu",
                        modifier = Modifier.size(50.dp),// Ajusta el tamaño del ícono
                        tint = Color.Unspecified
                    )
                } else {
                    Icon(
                        painterResource(R.drawable.menu_icon),
                        contentDescription = "Abrir menú",
                        modifier = Modifier.size(50.dp), // Ajusta el tamaño del ícono
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
    if (notificationsDialogEnabled.value) NotificationsDialog(authRepository, loggedInUserUID, newNotificationsCount.value) {
        notificationsDialogEnabled.value = false
    }
}

fun GetNotificationsCounter(loggedInUserUID: String, onSuccess: (String) -> Unit) {
    val firebase = FirebaseFirestore.getInstance()
    val notificationsCollection = firebase
        .collection("Users")
        .document(loggedInUserUID)
        .collection("Notifications")

    notificationsCollection.whereEqualTo("status", "unread")
        .get().addOnSuccessListener { QuerySnapshot ->
            if(!QuerySnapshot.isEmpty) {
                if(QuerySnapshot.documents.size > 9) {
                    onSuccess("+9")
                } else onSuccess(QuerySnapshot.documents.size.toString())
            } else onSuccess("0")
        }
}

@Composable
fun MenuOpciones(navController: NavController, authRepository: AuthRepository) {
    val userID = authRepository.getCurrentUser()?.uid

    CreateOptionButton("Proyectos", painterResource(R.drawable.proyect_icon)) {
        navController.navigate("projects_screen/$userID")
    }
    CreateOptionButton("Tareas", painterResource(R.drawable.task_icon)) {

    }
    CreateOptionButton("Calendario", painterResource(R.drawable.calender_icon)) {

    }
    CreateOptionButton("Comunicación", painterResource(R.drawable.comunication_icon)) {

    }
    CreateOptionButton("Equipos", painterResource(R.drawable.task_icon)) {
        navController.navigate("teams_screen/$userID")
    }
    CreateOptionButton("Reportes y Analíticas", painterResource(R.drawable.analityc_icon)) {

    }
    CreateOptionButton("Presupuestos y Compras", painterResource(R.drawable.coston_icon)) {
        navController.navigate("presucom_screen/$userID")
    }
    CreateOptionButton("Planos y Documentación", painterResource(R.drawable.documents_icon)) {
        navController.navigate("documents_screen/$userID")
    }
    CreateOptionButton("Soporte y Ayuda", painterResource(R.drawable.help_icon)) {

    }

    Spacer(modifier = Modifier.height(5.dp))
    Divider(color = Color.LightGray, thickness = 1.dp)
}

@Composable
fun SectionDown(navController: NavController, authRepository: AuthRepository) {
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Bottom),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { /* Configuracion */ },
                colors = ButtonDefaults.textButtonColors(backgroundColor = Color.Transparent),
                elevation = ButtonDefaults.elevation(0.dp),
            ) {
                Icon(
                    painterResource(R.drawable.config_icon),
                    modifier = Modifier.size(35.dp),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }

            TextButton(
                onClick = {
                    navController.navigate("user_screen")
                },
                colors = ButtonDefaults.textButtonColors(backgroundColor = Color.Transparent),
                elevation = ButtonDefaults.elevation(0.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(
                    painterResource(R.drawable.user_icon),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp),
                    tint = Color.Unspecified
                )
            }
            @Composable
            fun ConfirmDialog() {
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                        title = {
                            Text(text = "Confirmar")
                        },
                        text = {
                            Text("¿Estás seguro de que quieres cerrar la sesión?")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    authRepository.logoutUser { success ->
                                        if (success) {
                                            // Navega a la pantalla de inicio de sesión o realiza alguna otra acción
                                            navController.navigate("login_screen") {
                                                popUpTo("project_screen") { inclusive = true }
                                            }
                                        } else {
                                            // Muestra un mensaje de error o realiza alguna otra acción
                                        }
                                        showDialog = false
                                    }
                                }
                            ) {
                                Text("Sí")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showDialog = false
                                }
                            ) {
                                Text("No")
                            }
                        }
                    )
                }
            }
            TextButton(
                onClick = {
                    showDialog = true
                },
                colors = ButtonDefaults.textButtonColors(backgroundColor = Color.Transparent),
                elevation = ButtonDefaults.elevation(0.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(
                    painterResource(R.drawable.close_icon),
                    modifier = Modifier.size(35.dp),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
            ConfirmDialog()
        }
    }
}


@Composable
fun CreateOptionButton(text: String, icon: Painter, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp), // Establece el tamaño del icono a 50dp
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
