package com.example.contrupro3.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.contrupro3.modelos.AuthRepository
import com.example.contrupro3.modelos.Equipos
import com.google.firebase.firestore.FirebaseFirestore

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardViewTeam(navController: NavHostController, authRepository: AuthRepository, equipoID: String) {
    val EquipoList = remember { mutableStateOf<Equipos?>(null) }
    authRepository.loadEquipo(equipoID, EquipoList)
    var showDialog by remember { mutableStateOf(false) }

    val equipo: Equipos? = EquipoList.value

    Log.d("Log descripcion", "descripcion : ${equipo?.descripcion}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = equipo?.nombreEquipo ?: "",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = equipo?.creatorName ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = myOrange)

                    }
                },
            )
        }
    ) {
        // Contenido del Scaffold (fuera del bloque de configuración del TopAppBar)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val context = LocalContext.current
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = myOrangelow
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .indication(
                            interactionSource = MutableInteractionSource(),
                            indication = LocalIndication.current
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(15.dp),
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        Text(text = "Descripcion", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(5.dp))
                        Divider(color = Color.LightGray, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = equipo?.descripcion ?: "no existe descripcion",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontFamily = MaterialTheme.typography.labelMedium.fontFamily
                            ),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = myOrangelow
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .indication(
                            interactionSource = MutableInteractionSource(),
                            indication = LocalIndication.current
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(15.dp),
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        Text(text = "Integrantes", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(5.dp))
                        Divider(color = Color.LightGray, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Button(
                            onClick = {
                                showDialog = true
                            },
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                            colors = ButtonDefaults.buttonColors(
                                myOrange,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .padding(8.dp)
                        ) {
                            Text("Invitar")
                        }
                    }

                    if (showDialog) {
                        InvitarDialog(
                            onInvite = { email ->
                                if (isValidEmail(email)) {
                                    val userEmail = authRepository.getCurrentUser()?.email
                                    Log.d("Log correo", "el correo es : $userEmail")
                                    if (userEmail != null && userEmail != email) {
                                        val firestore = FirebaseFirestore.getInstance()
                                        val usuarioId = authRepository.getCurrentUser()?.uid
                                        val equipoId = equipo?.id ?: ""

                                        if (usuarioId != null) {
                                            val invitacionesCollection =
                                                firestore.collection("Usuarios")
                                                    .document(usuarioId)
                                                    .collection("Invitaciones")

                                            Log.d("equipoID","equipo id: $equipoID")
                                            Log.d("email","email id: $email")

                                            invitacionesCollection.whereEqualTo("email", email)
                                                .get().addOnSuccessListener { querySnapshot ->
                                                    if (querySnapshot.documents.isEmpty()) {
                                                        // Si no hay documentos con ese email, entonces agregamos la nueva invitación.
                                                        invitacionesCollection.add(
                                                            mapOf(
                                                                "equipoId" to equipoId,
                                                                "email" to email,
                                                                "estado" to "pendiente"
                                                            )
                                                        ).addOnSuccessListener {
                                                            Toast.makeText(
                                                                context,
                                                                "Invitación enviada",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }.addOnFailureListener {
                                                            Toast.makeText(
                                                                context,
                                                                "Error al enviar la invitación",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "El correo ya ha sido invitado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }.addOnFailureListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Error al verificar la invitación",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            if (userEmail == email) "No puedes invitarte a ti mismo" else "El correo ya ha sido invitado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "El correo electrónico no es válido",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                showDialog = false
                            },
                            onDismiss = {
                                showDialog = false
                            }
                        )
                    }
                }
            }
        }
    }
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
    return email.matches(emailRegex)
}

@Composable
fun InvitarDialog(
    onInvite: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Invitar a un nuevo miembro") },
        text = {
            Column {
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                        isEmailValid = isValidEmail(it) // Validar el correo al cambiarlo
                    },
                    label = { Text("Dirección de correo electrónico") }
                )
                if (!isEmailValid) {
                    Text(
                        text = "Correo electrónico no válido",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isEmailValid) {
                        onInvite(email)
                        onDismiss()
                    }
                },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    myOrange,
                    contentColor = Color.White
                ),
            ) {
                Text("Invitar")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    myOrange,
                    contentColor = Color.White
                ),
            ) {
                Text("Cancelar")
            }
        }
    )
}
