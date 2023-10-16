package com.example.contrupro3.ui.theme.TeamsScreens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.contrupro3.modelos.AuthRepository
import com.example.contrupro3.modelos.Equipos
import com.example.contrupro3.modelos.Project
import com.example.contrupro3.modelos.UserModel
import com.example.contrupro3.ui.theme.myOrange
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
    viewModel: CardviewTeam_ViewModel
) {
    val teamsList = remember { mutableStateOf<Equipos?>(null) }
    val projectsList = remember { mutableStateOf(emptyList<Project>()) }

    LaunchedEffect(Unit) {
        authRepository.loadEquipo(teamId, teamsList)
        authRepository.loadProjectsFromFirebase(projectsList)
    }

    if (teamsList.value === null) {
        Box(modifier = Modifier.fillMaxSize())
    } else {
        val team = viewModel.team.observeAsState(initial = teamsList.value).value
        val currentProjectsFiltered =
            projectsList.value.filter { p -> team?.projectsLinked!!.contains(p.id.toString()) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    team?.name.toString(),
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    team?.creatorName ?: "",
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
                                tint = myOrange
                            )
                        }
                    }
                )
            },
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Sections(navController, authRepository, userId, team, viewModel)
            }
        }
    }
}

@Composable
fun Sections(
    navController: NavHostController,
    authRepository: AuthRepository,
    userId: String,
    team: Equipos?,
    viewModel: CardviewTeam_ViewModel
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
        InformationCard(navController, authRepository, userId, team, viewModel)
        Spacer(modifier = Modifier.height(10.dp))
        MembersCard(navController, authRepository, userId, team, viewModel)
    }
}

@Composable
private fun InformationCard(
    navController: NavHostController,
    authRepository: AuthRepository,
    userId: String,
    team: Equipos?,
    viewModel: CardviewTeam_ViewModel
) {
    val name: String by viewModel.name.observeAsState(initial = "${team?.name}")
    val description: String by viewModel.description.observeAsState(initial = "${team?.description}")
    val enableSaveButton: Boolean by viewModel.saveNameAndDescriptionButtonEnable.observeAsState(
        initial = false
    )
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
            /* === NameField === */
            Text(
                text = "Información del equipo",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = myOrange,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Nombre del equipo",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .offset(x = 5.dp)
                    .padding(5.dp)
            )
            NameField(name, team) {
                viewModel.onInfoChanged(
                    it,
                    description,
                    team
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (name.length < 6) {
                    Text(
                        text = "* Requerido",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Light,
                            color = myOrange
                        ),
                        modifier = Modifier.offset(x = 5.dp)
                    )
                } else Spacer(modifier = Modifier.width(0.dp))
                Text(
                    text = "${name.length}/30",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light,
                        color = if (name.length < 6 || name.length > 30) myOrange else Color.Black
                    )
                )
            }

            /* === DescriptionField === */
            Text(
                text = "Descripción del equipo",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .offset(x = 5.dp)
                    .padding(5.dp)
            )
            DescriptionField(description, team) {
                viewModel.onInfoChanged(
                    name,
                    it,
                    team
                )
            }
            Text(
                text = "${description.length}/200",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Light,
                    color = if (description.length > 200) myOrange else Color.Black
                ),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }

    if (name.trim() !== team?.name.toString()
            .trim() || description.trim() !== team?.description.toString().trim()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    viewModel.onInfoChanged(
                        "${team?.name.toString()}",
                        "${team?.description.toString()}",
                        team
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
                    inputMethodManager.hideSoftInputFromWindow(currentLocalView.windowToken, 0)
                    val db = FirebaseFirestore.getInstance()
                    val collection =
                        db.collection("Usuarios").document(userId).collection("Equipos")
                            .document(team?.id.toString())

                    if (name !== team?.name.toString().trim()) collection.update(
                        "name",
                        "${name.trim()}"
                    )
                    if (description !== team?.description.toString()
                            .trim()
                    ) collection.update("description", "${description.trim()}")

                    viewModel.onInfoChanged(
                        name.trim(), description.trim(), Equipos(
                            team?.id,
                            name.trim(),
                            team?.creatorName,
                            team?.creatorUID,
                            description.trim(),
                            team?.members,
                            team?.projectsLinked
                        )
                    )
                    Toast.makeText(
                        context,
                        "Datos Actualizados",
                        Toast.LENGTH_LONG
                    ).show()
                },
                enabled = enableSaveButton,
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
fun DescriptionField(description: String, team: Equipos?, onTextFieldChanged: (String) -> Unit) {
    OutlinedTextField(
        value = description,
        onValueChange = { onTextFieldChanged(it) },
        maxLines = 1,
        placeholder = {
            if (team?.description != null && team?.description!!.length > 0) {
                Text("${team?.description}")
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
            cursorColor = myOrange,
            disabledBorderColor = Color.Transparent,
        ),
        trailingIcon = {
            IconButton(onClick = {
                onTextFieldChanged("")
            }) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Borrar descripción"
                )
            }
        }
    )
}

@Composable
fun NameField(name: String, team: Equipos?, onTextFieldChanged: (String) -> Unit) {
    OutlinedTextField(
        value = name,
        onValueChange = { onTextFieldChanged(it) },
        maxLines = 1,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color.Transparent,
            backgroundColor = Color(0x79D8D8D8),
            focusedBorderColor = Color.Transparent,
            cursorColor = myOrange,
            disabledBorderColor = Color.Transparent,
        ),
        trailingIcon = {
            IconButton(onClick = {
                onTextFieldChanged("")
            }) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Borrar nombre"
                )
            }
        }
    )
}

@Composable
private fun MembersCard(
    navController: NavHostController,
    authRepository: AuthRepository,
    userId: String,
    team: Equipos?,
    viewModel: CardviewTeam_ViewModel
) {
    var showAddInviteDialog: Boolean by remember { mutableStateOf(false) }

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
                color = myOrange,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .background(Color(0x79D8D8D8))
            ) {

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    modifier = Modifier.padding(end = 10.dp),
                    onClick = { }, colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = myOrangehigh,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar usuarios")
                }
                IconButton(
                    onClick = {
                        showAddInviteDialog = true
                    }, colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = myOrangehigh,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir usuarios")
                }
            }
        }
        if (showAddInviteDialog) AddInviteDialog(team, viewModel) { showAddInviteDialog = false }
    }
}

@Composable
fun AddInviteDialog(
    team: Equipos?,
    viewModel: CardviewTeam_ViewModel,
    DismissDialog: () -> Unit
) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val enableInviteButton by viewModel.enableInviteButton.observeAsState(initial = false)

    Dialog(onDismissRequest = { DismissDialog() }) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight(0.55f),
            shape = RoundedCornerShape(4.dp),
            elevation = 4.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Invitar Usuario",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = myOrange
                )
                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = Color.Black, thickness = 1.dp)
                Spacer(modifier = Modifier.height(15.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.onAddDialogChanged(it) },
                    label = { Text(text = "Correo electronico") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        cursorColor = myOrange,
                        focusedBorderColor = Color(0xA9D8D8D8),
                        unfocusedBorderColor = Color(0xA9D8D8D8),
                        backgroundColor = Color(0x79D8D8D8)
                    ),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.onAddDialogChanged("")
                        }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Borrar email"
                            )
                        }
                    }
                )
                if (email.length > 0 && enableInviteButton === false) {
                    Text(
                        text = "Correo electronico no válido",
                        modifier = Modifier
                            .align(Alignment.Start)
                            .offset(x = 4.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Light,
                            color = myOrange
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = { DismissDialog() }, colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = myOrangehigh
                    ), modifier = Modifier.padding(end = 15.dp)
                ) {
                    Text(text = "Cancelar")
                }
                Button(
                    enabled = enableInviteButton,
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = myOrangehigh
                    ),
                    modifier = Modifier.padding(end = 15.dp)
                ) {
                    Text(text = "Invitar")
                }
            }
        }
    }
}


/*@SuppressLint("UnrememberedMutableState")
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
                color = myOrange
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
                                    uncheckedColor = myOrange,
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
}*/

/*@Composable
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
                color = myOrange
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
                                    uncheckedColor = myOrange,
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
}*/

/*
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
                color = myOrange
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
                    onClick = {  },
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
}*/
