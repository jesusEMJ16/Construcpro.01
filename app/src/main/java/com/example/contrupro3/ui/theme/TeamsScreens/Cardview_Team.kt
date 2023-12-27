package com.example.contrupro3.ui.theme.TeamsScreens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.contrupro3.R
import com.example.contrupro3.SendNotificationToUser
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.TeamsModels.TeamCard_ViewModel
import com.example.contrupro3.models.TeamsModels.TeamMember
import com.example.contrupro3.models.TeamsModels.Teams
import com.example.contrupro3.models.UserModels.ActionButton
import com.example.contrupro3.models.UserModels.IconModel
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
    val teamLoadingStatus = remember { mutableStateOf("Loading") }
    authRepository.loadEquipo(teamId, team, projectId, { teamLoadingStatus.value = it })

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
        if (teamLoadingStatus.value === "Loaded") {
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
                    InformationCard(userId, projectId, team, TeamCard_ViewModel)
                    Spacer(modifier = Modifier.height(10.dp))
                    MembersCard(authRepository, userId, projectId, team, TeamCard_ViewModel)
                }
            }
        }
    }
}

@Composable
private fun InformationCard(
    userId: String,
    projectId: String,
    team: MutableState<Teams?>,
    TeamCard_ViewModel: TeamCard_ViewModel
) {
    val name = TeamCard_ViewModel.teamName.observeAsState("${team.value?.name}")
    val description =
        TeamCard_ViewModel.teamDescription.observeAsState("${team.value?.description}")
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
                value = name.value,
                onValueChange = { TeamCard_ViewModel.onFieldsUpdated(it, description.value) },
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
                        TeamCard_ViewModel.onFieldsUpdated("", description.value)
                    }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Borrar nombre"
                        )
                    }
                }
            )
            if (name.value !== team.value?.name) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (name.value.length < 6) {
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
                        text = "${name.value.length}/30",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Light,
                            color = if (name.value.length < 6 || name.value.length > 30) myBlue else Color.Black
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
                value = description.value,
                onValueChange = { TeamCard_ViewModel.onFieldsUpdated(name.value, it) },
                maxLines = 1,
                placeholder = {
                    if (team.value?.description != null && team.value?.description != null) {
                        Text("${description.value}")
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
                        TeamCard_ViewModel.onFieldsUpdated(name.value, "")
                    }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Borrar descripción"
                        )
                    }
                }
            )
            if (description.value !== team.value?.description) {
                Text(
                    text = "${description.value.length}/200",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light,
                        color = if (description.value.length > 200) myBlue else Color.Black
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
    if (name.value !== team.value?.name || description.value !== team.value?.description) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    TeamCard_ViewModel.onFieldsUpdated(
                        "${team.value?.name}",
                        "${team.value?.description}"
                    )
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
                        db.collection("Users")
                            .document(userId)
                            .collection("Projects")
                            .document(projectId)
                            .collection("Teams")
                            .document(team.value?.id.toString())

                    if (name.value !== team.value?.name) {
                        val newTeam = Teams(
                            team.value?.id,
                            name.value,
                            team.value?.creatorName,
                            team.value?.creatorUID,
                            team.value?.description
                        )
                        team.value = newTeam
                        collection.update("name", "${name.value}")
                    }
                    if (description.value !== team.value?.description) {
                        val newTeam = Teams(
                            team.value?.id,
                            team.value?.name,
                            team.value?.creatorName,
                            team.value?.creatorUID,
                            description.value
                        )
                        team.value = newTeam
                        collection.update("description", "${description.value}")
                    }
                    inputMethodManager.hideSoftInputFromWindow(currentLocalView.windowToken, 0)
                    Toast.makeText(
                        context,
                        "Información Actualizada",
                        Toast.LENGTH_LONG
                    ).show()
                },
                enabled = name.value.length >= 6 && name.value.length <= 30 && description.value.length <= 200,
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
    authRepository: AuthRepository,
    userId: String,
    projectId: String,
    team: MutableState<Teams?>,
    TeamCard_ViewModel: TeamCard_ViewModel
) {
    val showInviteDialog = remember { mutableStateOf(false) }
    val membersList = remember { mutableStateOf<List<TeamMember>>(emptyList()) }
    authRepository.loadMembersOfTeam(membersList, userId, projectId, team.value?.id.toString())

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Miembros del equipo",
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
                items(membersList.value.filter { a -> a.inviteStatus == "Accepted" }.size) { index ->
                    val member =
                        membersList.value.filter { a -> a.inviteStatus == "Accepted" }[index]
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable { }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(end = 3.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.user_icon),
                                    contentDescription = "Icono del usuario",
                                    modifier = Modifier.offset(y = 3.dp)
                                )
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Top
                            ) {
                                Text(
                                    text = "${member.name} ${member.lastName}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "${member.email}",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(start = 3.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { showInviteDialog.value = true },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = myOrangehigh
                    ),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Text(text = "Invitar")
                }
            }
        }
    }

    if (showInviteDialog.value) InviteDialog(
        authRepository,
        userId,
        team.value?.id,
        projectId,
        TeamCard_ViewModel,
        team.value!!.name.toString()
    ) {
        showInviteDialog.value = false
    }
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
    return email.matches(emailRegex)
}

@Composable
fun InviteDialog(
    authRepository: AuthRepository,
    userId: String,
    teamId: String?,
    projectId: String,
    TeamCard_ViewModel1: TeamCard_ViewModel,
    teamName: String,
    onDismiss: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val context = LocalContext.current
    val ownerName = authRepository.getLoggedInUserName().observeAsState("")

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .padding(10.dp),
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
                    text = "Invitar Usuario",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = myBlue
                )
                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = Color.Black, thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Email del usuario",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .offset(x = 5.dp)
                            .padding(5.dp)
                    )
                }
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    maxLines = 1,
                    placeholder = {
                        Text(text = "example@gmail.com")
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
                            email.value = ""
                        }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Borrar email"
                            )
                        }
                    }
                )
                if (email.value.length > 0 && !isValidEmail(email.value)) {
                    Text(
                        text = "Email no valido",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Light,
                            color = myBlue
                        ),
                        modifier = Modifier.align(Alignment.End)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = myOrangehigh,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .width(100.dp)
                            .padding(start = 0.dp, top = 5.dp, end = 5.dp, bottom = 0.dp)
                    ) {
                        Text(
                            text = "Cerrar",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Button(
                        onClick = {
                            AddMemberToDatabase(userId, teamId.toString(), projectId, email.value)
                            SendNotificationToUser(
                                title = "Has sido invitado a un grupo.",
                                description = "Has sido invitado a un grupo llamado '$teamName' de ${ownerName.value}.",
                                actionButton = ActionButton("Aceptar", "accept"),
                                additionalInfo = mapOf<String, Any>(
                                    "ownerId" to authRepository.getLoggedInUserUID().value.toString(),
                                    "teamId" to teamId.toString(),
                                    "projectId" to projectId,
                                    "email" to email.value
                                ),
                                icon = IconModel("GroupAdd", "Green"),
                                methodToGetUserId = "email"
                            )
                            onDismiss()
                            Toast.makeText(
                                context,
                                "Usuario Invitado",
                                Toast.LENGTH_LONG
                            ).show()
                            email.value = ""
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = myOrangehigh,
                            contentColor = Color.White
                        ),
                        enabled = isValidEmail(email.value),
                        modifier = Modifier
                            .width(100.dp)
                            .padding(start = 0.dp, top = 5.dp, end = 5.dp, bottom = 0.dp)
                    ) {
                        Text(
                            text = "Invitar",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

fun AddMemberToDatabase(userId: String, teamId: String, projectId: String, email: String) {
    val firebase = FirebaseFirestore.getInstance()
    val collection = firebase
        .collection("Users")
        .document(userId)
        .collection("Projects")
        .document(projectId)
        .collection("Teams")
        .document(teamId)
        .collection("Members")

    val newMember = TeamMember(
        id = null,
        name = null,
        lastName = null,
        email = email,
        role = null,
        phoneNumber = null,
        inviteStatus = "Pending"
    )

    collection.add(newMember)
        .addOnSuccessListener { userDoc ->
            val newDoc = newMember.copy(id = userDoc.id)
            collection.document(userDoc.id)
                .set(newDoc)
        }
}
