package com.example.contrupro3.ui.theme.TeamsScreens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.TeamsModels.TeamCard_ViewModel
import com.example.contrupro3.models.TeamsModels.Teams
import com.example.contrupro3.ui.theme.myBlue
import com.example.contrupro3.ui.theme.myOrangehigh
import com.google.firebase.firestore.FirebaseFirestore


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun CardViewTeamsScreen(
    navController: NavHostController,
    authRepository: AuthRepository,
    userId: String,
    teamId: String,
    projectId: String,
    TeamCard_ViewModel: TeamCard_ViewModel
) {
    val team = remember { mutableStateOf<Teams?>(null) }
    authRepository.loadEquipo(teamId, team, projectId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text("${team.value?.name}", style = MaterialTheme.typography.titleLarge)
                            Text(
                                team.value?.creatorName ?: "",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = myBlue
                        )
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = 60.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                InformationCard(navController, authRepository, userId, projectId, team)
                Spacer(modifier = Modifier.height(10.dp))
                MembersCard(navController, authRepository, userId, projectId, team)
            }
        }
    }
}

@Composable
private fun InformationCard(
    navController: NavHostController,
    authRepository: AuthRepository,
    userId: String,
    projectId: String,
    team: MutableState<Teams?>
) {
    val viewModel: CardViewTeamsViewModel = viewModel()
    val teamName by viewModel.teamName
    val teamDescription by viewModel.teamDescription
    var name by remember { mutableStateOf("$teamName") }
    var description by remember { mutableStateOf("$teamDescription") }
    val context = LocalContext.current
    val currentLocalView = LocalView.current
    val inputMethodManager =
        LocalView.current.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Información del equipo",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = myBlue,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Nombre del equipo",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .offset(x = 5.dp)
                    .padding(5.dp)
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                maxLines = 1,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    backgroundColor = Color(0x79D8D8D8),
                    focusedBorderColor = Color.Transparent,
                    cursorColor = myBlue,
                    disabledBorderColor = Color.Transparent,
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        name = ""
                    }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Borrar nombre"
                        )
                    }
                }
            )
            if (name !== teamName) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (name.length < 6) {
                        Text(
                            text = "* Requerido",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Light,
                                color = myBlue
                            ),
                            modifier = Modifier.offset(x = 5.dp)
                        )
                    } else Spacer(modifier = Modifier.width(0.dp))
                    Text(
                        text = "${name.length}/30",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Light,
                            color = if (name.length < 6 || name.length > 30) myBlue else Color.Black
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Descripción del equipo",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .offset(x = 5.dp)
                    .padding(5.dp)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                maxLines = 1,
                placeholder = {
                    if (teamDescription != null && teamDescription.length > 0) {
                        Text("${teamDescription}")
                    } else {
                        Text("No hay descripción establecida.")
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    backgroundColor = Color(0x79D8D8D8),
                    focusedBorderColor = Color.Transparent,
                    cursorColor = myBlue,
                    disabledBorderColor = Color.Transparent,
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        description = ""
                    }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Borrar descripción"
                        )
                    }
                }
            )
            if (description !== teamDescription) {
                Text(
                    text = "${description.length}/200",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light,
                        color = if (description.length > 200) myBlue else Color.Black
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
    if (name !== teamName || description !== teamDescription) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    name = "$teamName"
                    description = "$teamDescription"
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = myOrangehigh
                ),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = "Cancelar")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    val db = FirebaseFirestore.getInstance()
                    val collection =
                        db.collection("Usuarios")
                            .document(userId)
                            .collection("Projects")
                            .document(projectId)
                            .collection("Teams")
                            .document(team.value?.id.toString())

                    if (name !== teamName) {
                        viewModel.teamName.value = name
                        collection.update("name", "$name")
                    }
                    if (description !== teamDescription) {
                        viewModel.teamDescription.value = description
                        collection.update("description", "$description")
                    }
                    inputMethodManager.hideSoftInputFromWindow(currentLocalView.windowToken, 0)
                    name = teamName
                    description = teamDescription
                    Toast.makeText(
                        context,
                        "Datos Actualizados",
                        Toast.LENGTH_LONG
                    ).show()
                },
                enabled = name.length >= 6 && name.length <= 30 && description.length <= 200,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = myOrangehigh
                ),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = "Guardar")
            }
        }
    }
}

@Composable
private fun MembersCard(
    navController: NavHostController,
    authRepository: AuthRepository,
    userId: String,
    projectId: String,
    team: MutableState<Teams?>
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Lista De Usuarios",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = myBlue,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .background(Color(0x79D8D8D8))
            ) {

            }
        }
    }
}

fun getInvitesFromFirebase(
    teamId: String,
    onSuccess: (List<String>) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()

    firestore.collectionGroup("Invitaciones")
        .whereEqualTo("teamId", teamId)
        .get()
        .addOnSuccessListener { querySnapshot ->
            val integrantes = querySnapshot.documents

            if (integrantes.isNotEmpty()) {
//                onSuccess(integrantes)
            }
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
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
                    myBlue,
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
                    myBlue,
                    contentColor = Color.White
                ),
            ) {
                Text("Cancelar")
            }
        }
    )
}

class CardViewTeamsViewModel : ViewModel() {
    val teamName = mutableStateOf("")
    val teamDescription = mutableStateOf("")
    val membersList = mutableListOf<String>()
    val nameEnabled = mutableStateOf(false)
    val descriptionEnabled = mutableStateOf(false)
    val action = mutableStateOf("")
}