package com.example.contrupro3.ui.theme.TeamsScreens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.contrupro3.R
import com.example.contrupro3.modelos.AuthRepository
import com.example.contrupro3.modelos.Equipos
import com.example.contrupro3.modelos.Project
import com.example.contrupro3.ui.theme.myBlue
import com.example.contrupro3.ui.theme.myOrangehigh
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun CardViewTeamsScreen(
    navController: NavHostController,
    authRepository: AuthRepository,
    userId: String,
    teamId: String
) {
    val teamsList = remember { mutableStateOf<Equipos?>(null) }
    val projectsList = remember { mutableStateOf(emptyList<Project>()) }

    LaunchedEffect(Unit) {
        authRepository.loadEquipo(teamId, teamsList)
        authRepository.loadProjectsFromFirebase(projectsList)
    }

    if (teamsList.value === null) {
        Box(modifier = Modifier.fillMaxSize()) {
        }
    } else {
        val viewModel: CardViewTeamsViewModel = viewModel()
        val team: Equipos? = teamsList.value
        val teamName by viewModel.teamName
        val action by viewModel.action
        val showProjectsButtons = remember { mutableStateOf(false) }
        val currentProjectsFiltered =
            projectsList.value.filter { p -> team?.projectsLinked!!.contains(p.id.toString()) }
        viewModel.teamName.value = team?.name.toString()
        viewModel.teamDescription.value = team?.description.toString()
        viewModel.currentProjects.value = currentProjectsFiltered

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(teamName, style = MaterialTheme.typography.titleLarge)
                                Text(
                                    team?.creatorName ?: "",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            IconButton(
                                onClick = {
                                    showProjectsButtons.value = !showProjectsButtons.value
                                },
                                modifier = Modifier.padding(5.dp)
                            ) {
                                if (showProjectsButtons.value) {
                                    Icon(
                                        Icons.Default.Link,
                                        contentDescription = null,
                                        tint = myBlue,
                                        modifier = Modifier
                                            .size(30.dp)
                                            .rotate(-45f)
                                    )
                                } else Icon(
                                    Icons.Default.Link,
                                    contentDescription = null,
                                    tint = myBlue,
                                    modifier = Modifier
                                        .size(30.dp)
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
            },
            floatingActionButton = {
                CompositionLocalProvider(
                    LocalContentColor provides colorResource(id = R.color.white)
                ) {
                    if (showProjectsButtons.value === true) {
                        Row {
                            FloatingActionButton(
                                onClick = {
                                    if (showProjectsButtons.value) viewModel.action.value =
                                        "remove projects"
                                },
                                containerColor = myOrangehigh
                            ) {
                                androidx.compose.material.Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            FloatingActionButton(
                                onClick = {
                                    if (showProjectsButtons.value) viewModel.action.value =
                                        "list projects"
                                },
                                containerColor = myOrangehigh
                            ) {
                                androidx.compose.material.Icon(
                                    Icons.Default.List,
                                    contentDescription = "Ver lista"
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            FloatingActionButton(
                                onClick = {
                                    if (showProjectsButtons.value) viewModel.action.value =
                                        "add projects"
                                },
                                containerColor = myOrangehigh
                            ) {
                                androidx.compose.material.Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Añadir"
                                )
                            }
                        }
                    }
                }
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (action) {
                    "add projects" -> {
                        Dialog(onDismissRequest = { viewModel.action.value = "" }) {
                            AddProyects(
                                navController,
                                authRepository,
                                userId,
                                team?.id.toString(),
                                projectsList
                            )
                        }
                    }

                    "remove projects" -> {
                        Dialog(onDismissRequest = { viewModel.action.value = "" }) {
                            RemoveProjects(
                                navController,
                                authRepository,
                                userId,
                                team?.id.toString(),
                                projectsList
                            )
                        }
                    }

                    "list projects" -> {
                        Dialog(onDismissRequest = { viewModel.action.value = "" }) {
                            ListProjects(navController, authRepository, userId)
                        }
                    }
                }

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
                    InformationCard(navController, authRepository, userId, team)
                    Spacer(modifier = Modifier.height(10.dp))
                    MembersCard(navController, authRepository, userId, team)
                }
            }
        }
    }
}

@Composable
private fun InformationCard(
    navController: NavHostController,
    authRepository: AuthRepository,
    userId: String,
    team: Equipos?
) {
    val viewModel: CardViewTeamsViewModel = viewModel()
    val teamName by viewModel.teamName
    val teamDescription by viewModel.teamDescription
    var name by remember { mutableStateOf("$teamName") }
    var description by remember { mutableStateOf("$teamDescription") }
    val context = LocalContext.current
    val currentLocalView = LocalView.current
    val inputMethodManager = LocalView.current.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

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
                        db.collection("Usuarios").document(userId).collection("Equipos")
                            .document(team?.id.toString())

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
    team: Equipos?
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

@SuppressLint("UnrememberedMutableState")
@Composable
fun AddProyects(
    navController: NavHostController,
    authRepository: AuthRepository,
    userId: String,
    teamId: String,
    projectsList: MutableState<List<Project>>
) {
    val proyectsSelected = remember { mutableStateListOf<String>() }
    val viewModel: CardViewTeamsViewModel = viewModel()
    val context = LocalContext.current
    val currentProjects by viewModel.currentProjects
    val teamsList = remember { mutableStateOf<Equipos?>(null) }
    authRepository.loadEquipo(teamId, teamsList)
    val team: Equipos? = teamsList.value

    androidx.compose.material.Card(
        modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(if (projectsList.value.size > 0) 0.7f else 0.35f)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enlazar Proyectos",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = myBlue
            )
            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = Color.Black, thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))
            if (projectsList.value.size > 0) {
                Text(
                    text = "Selecciona los proyectos que estaran enlazados con este equipo",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                    textAlign = TextAlign.Center
                )
            } else Text(
                text = "No hay proyectos creados para enlazar",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(15.dp))
            if (projectsList.value.size > 0) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                ) {
                    items(projectsList.value.sortedBy { it.projectName }) { proyect ->
                        var checked by remember { mutableStateOf(proyectsSelected.contains(proyect.id.toString())) }
                        var isPressed by remember { mutableStateOf(false) }
                        val scope = rememberCoroutineScope()

                        Spacer(modifier = Modifier.height(5.dp))
                        Divider(color = Color.LightGray, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (isPressed) Color.LightGray else Color.White)
                                .clickable(
                                    onClick = {
                                        if (team?.projectsLinked
                                                ?.toList()
                                                ?.contains(proyect.id.toString()) == false
                                        ) {
                                            isPressed = true
                                            checked = !checked
                                            scope.launch {
                                                delay(100)
                                                isPressed = false
                                            }
                                        }
                                    }
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${proyect.projectName}",
                                modifier = Modifier.offset(x = 10.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Checkbox(
                                enabled = team?.projectsLinked?.toList()
                                    ?.contains(proyect.id.toString()) != true,
                                checked = checked,
                                onCheckedChange = { newChange ->
                                    if (!proyectsSelected.contains(proyect.id.toString())) proyectsSelected.add(
                                        proyect.id.toString()
                                    ) else proyectsSelected.remove(proyect.id.toString())
                                    checked = newChange
                                },
                                colors = CheckboxDefaults.colors(
                                    uncheckedColor = myBlue,
                                    checkedColor = myOrangehigh,
                                    checkmarkColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { viewModel.action.value = "" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myOrangehigh,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(start = 0.dp, top = 5.dp, end = 5.dp, bottom = 0.dp)
                ) {
                    Text(
                        text = "Cancelar",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Button(
                    onClick = {
                        val projectsNameSelected = proyectsSelected
                        for (d in team?.projectsLinked!!) {
                            proyectsSelected.add(d)
                        }
                        val db = FirebaseFirestore.getInstance()
                        val collection = db.collection("Usuarios")
                            .document(userId)
                            .collection("Equipos")
                            .document(team.id.toString())

                        try {
                            viewModel.action.value = ""
                            collection
                                .update("projectsLinked", proyectsSelected)
                                .addOnSuccessListener {
                                    val projectsFiltered = projectsList.value.filter { p ->
                                        projectsNameSelected.contains(p.id.toString()) && !currentProjects.contains(
                                            p
                                        )
                                    }
                                    val newList = currentProjects + projectsFiltered
                                    viewModel.currentProjects.value = newList
                                    proyectsSelected.clear()
                                    Toast.makeText(
                                        context,
                                        "Proyectos enlazados correctamente",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Ocurrio un problema al actualizar el equipo.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    enabled = proyectsSelected.size >= 1,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myOrangehigh,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 5.dp,
                        end = 5.dp,
                        bottom = 0.dp
                    )
                ) {
                    Text(
                        text = "Agregar",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun RemoveProjects(
    navController: NavHostController,
    authRepository: AuthRepository,
    userID: String,
    teamId: String?,
    projectsList: MutableState<List<Project>>
) {
    val proyectsSelected = remember { mutableStateListOf<String>() }
    val viewModel: CardViewTeamsViewModel = viewModel()
    val context = LocalContext.current
    val proyectsCurrent = viewModel.currentProjects
    val teamsList = remember { mutableStateOf<Equipos?>(null) }
    authRepository.loadEquipo(teamId.toString(), teamsList)
    val team: Equipos? = teamsList.value

    androidx.compose.material.Card(
        modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(if (proyectsCurrent.value.size > 0) 0.7f else 0.35f)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Remover Proyectos",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = myBlue
            )
            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = Color.Black, thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))
            if (proyectsCurrent.value.size > 0) {
                Text(
                    text = "Selecciona los proyectos que seran removidos de este equipo",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                    textAlign = TextAlign.Center
                )
            } else Text(
                text = "No hay proyectos enlazados para remover",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(15.dp))
            if (proyectsCurrent.value.size > 0) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                ) {
                    items(proyectsCurrent.value.sortedBy { it.projectName }) { proyect ->
                        var checked by remember { mutableStateOf(false) }
                        var isPressed by remember { mutableStateOf(false) }
                        val scope = rememberCoroutineScope()

                        Spacer(modifier = Modifier.height(5.dp))
                        Divider(color = Color.LightGray, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (isPressed) Color.LightGray else Color.White)
                                .clickable(
                                    onClick = {
                                        isPressed = true
                                        checked = !checked
                                        scope.launch {
                                            delay(100)
                                            isPressed = false
                                        }
                                    }
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${proyect.projectName}",
                                modifier = Modifier.offset(x = 10.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { newChange ->
                                    if (!proyectsSelected.contains(proyect.id.toString())) proyectsSelected.add(
                                        proyect.id.toString()
                                    ) else proyectsSelected.remove(proyect.id.toString())
                                    checked = newChange
                                },
                                colors = CheckboxDefaults.colors(
                                    uncheckedColor = myBlue,
                                    checkedColor = myOrangehigh,
                                    checkmarkColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { viewModel.action.value = "" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myOrangehigh,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(start = 0.dp, top = 5.dp, end = 5.dp, bottom = 0.dp)
                ) {
                    Text(
                        text = "Cancelar",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Button(
                    onClick = {
                        val newList = proyectsCurrent.value.filter {
                            proyectsSelected.contains(it.id.toString()) === false
                        }
                        val db = FirebaseFirestore.getInstance()
                        val collection = db.collection("Usuarios")
                            .document(userID)
                            .collection("Equipos")
                            .document(team?.id.toString())

                        try {
                            collection
                                .update("projectsLinked", newList.map {
                                    it.id.toString()
                                })
                                .addOnSuccessListener {
                                    viewModel.currentProjects.value = newList
                                    proyectsSelected.clear()
                                    viewModel.action.value = ""
                                    Toast.makeText(
                                        context,
                                        "Proyectos removidos correctamente",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Ocurrio un problema al actualizar el equipo.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    enabled = proyectsSelected.size >= 1,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myOrangehigh,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 5.dp,
                        end = 5.dp,
                        bottom = 0.dp
                    )
                ) {
                    Text(
                        text = "Eliminar",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ListProjects(
    navController: NavHostController,
    authRepository: AuthRepository,
    userId: String
) {
    val viewModel: CardViewTeamsViewModel = viewModel()
    val currentProjects by viewModel.currentProjects

    androidx.compose.material.Card(
        modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(if (currentProjects.size > 0) 0.7f else 0.35f)
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
            Spacer(modifier = Modifier.height(10.dp))
            if (currentProjects.size > 0) {
                Text(
                    text = "Estos son los proyectos enlazados actualmente",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "No hay proyectos enlazados actualmente",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                    textAlign = TextAlign.Center
                )
            }
            if (currentProjects.size > 0) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                ) {
                    items(currentProjects.sortedBy { it.projectName }) { proyect ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = Color.LightGray, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${proyect.projectName}",
                                modifier = Modifier.offset(x = 10.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { viewModel.action.value = "" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myOrangehigh,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(start = 0.dp, top = 5.dp, end = 5.dp, bottom = 0.dp)
                ) {
                    Text(
                        text = "Cerrar",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

class CardViewTeamsViewModel : ViewModel() {
    val teamName = mutableStateOf("")
    val teamDescription = mutableStateOf("")
    val membersList = mutableListOf<String>()
    val nameEnabled = mutableStateOf(false)
    val descriptionEnabled = mutableStateOf(false)
    val currentProjects = mutableStateOf(emptyList<Project>())
    val action = mutableStateOf("")
}