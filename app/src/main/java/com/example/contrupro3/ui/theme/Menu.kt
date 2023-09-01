package com.example.contrupro3.ui.theme

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.*
import androidx.compose.ui.unit.dp
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.contrupro3.R
import com.example.contrupro3.modelos.AuthRepository

@Composable
fun HamburgueerMenu(navController: NavController, authRepository: AuthRepository) {

    var isDrawerOpen by remember { mutableStateOf(false) }
    val offsetX by animateDpAsState(if (isDrawerOpen) 0.dp else (-330).dp)
    val loggedInUserName: String by authRepository.getLoggedInUserName().observeAsState("")

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
                    shape = RoundedCornerShape(topEnd = 25.dp, bottomEnd = 25.dp)
                ) {
                    // Contenido del menú desplegable
                    Column(
                        Modifier.padding(0.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Spacer(modifier = Modifier.height(40.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(align = Alignment.Bottom),
                            // verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            val welcomeText = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Bienvenido: ")

                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = myOrange,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append(loggedInUserName)
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .wrapContentHeight(align = Alignment.Bottom)
                            ) {
                                Text(
                                    text = welcomeText,
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = Color.LightGray, thickness = 1.dp)
                        MenuOpciones(navController, authRepository)
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(30.dp))
                            Image(
                                painter = painterResource(R.drawable.logoshape), // reemplace esto con el recurso de su logo
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .height(150.dp)
                                    .width(150.dp)
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
                .size(55.dp) // Ajusta el tamaño del botón
                .padding(12.dp) // Ajusta el espacio interior del botón
        ) {
            Crossfade(targetState = isDrawerOpen) { state ->
                if (state) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Close Menu",
                        modifier = Modifier.size(44.dp),// Ajusta el tamaño del ícono
                        tint = myOrange
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Abrir menú",
                        modifier = Modifier.size(44.dp), // Ajusta el tamaño del ícono
                        tint = myOrange
                    )
                }
            }
        }
    }
}

@Composable
fun MenuOpciones(navController: NavController, authRepository: AuthRepository) {

    val userID = authRepository.getCurrentUser()?.uid // Obtén el userID
    val projectID = "project_id" // Deberías obtener el projectID de alguna manera
    CreateOptionButton("Proyectos", painterResource(R.drawable.project_management_50px)) {
        navController.navigate("project_screen/$userID/$projectID")
    }
    CreateOptionButton("Tareas", painterResource(R.drawable.task_50px)){

    }
    CreateOptionButton("Calendario", painterResource(R.drawable.calendar_50px)){

    }
    CreateOptionButton("Planificacion", painterResource(R.drawable.architect_50px)){

    }
    CreateOptionButton("Comunicacion", painterResource(R.drawable.communication_50px)){

    }
    CreateOptionButton("Materiales y Suministros", painterResource(R.drawable.materials_50px)){

    }
    CreateOptionButton("Equipo y Personal", painterResource(R.drawable.task_50px)){
        navController.navigate("team_screen/$userID")
    }
    CreateOptionButton("Reportes y Analiticas", painterResource(R.drawable.analytics_50px)){

    }
    CreateOptionButton("Prosupuesto y Compras", painterResource(R.drawable.profit_50px)){

    }
    CreateOptionButton("Planos y Documentacion", painterResource(R.drawable.documents_50px)){

    }
    CreateOptionButton("Soporte y Ayuda", painterResource(R.drawable.help_50px)){

    }

    Spacer(modifier = Modifier.height(10.dp))
    Divider(color = Color.LightGray, thickness = 1.dp)
}

@Composable
fun SectionDown(navController: NavController,authRepository: AuthRepository) {
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
                    painterResource(R.drawable.settings_50px),
                    contentDescription = null,
                    tint = myOrange
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
                    painterResource(R.drawable.user_50px),
                    contentDescription = null,
                    tint = myOrange
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
                                            navController.navigate("login_screen"){
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
                    painterResource(R.drawable.deleteuser_50px),
                    contentDescription = null,
                    tint = myOrange
                )
            }
           ConfirmDialog()
        }
    }
}


@Composable
fun CreateOptionButton(text: String, icon: Painter, onClick: () -> Unit) {
    TextButton(
        onClick = onClick ,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(icon, contentDescription = null, tint = myOrange)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
