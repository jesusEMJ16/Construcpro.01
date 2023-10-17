package com.example.contrupro3.ui.theme.ProjectsScreens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Range
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.util.toRange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.contrupro3.R
import com.example.contrupro3.modelos.AuthRepository
import com.example.contrupro3.modelos.Project
import com.example.contrupro3.ui.theme.HamburgueerMenu
import com.example.contrupro3.ui.theme.myBlue
import com.example.contrupro3.ui.theme.myOrangehigh
import com.example.contrupro3.ui.theme.myOrangelow
import com.example.contrupro3.ui.theme.mywhie
import com.google.firebase.firestore.FirebaseFirestore
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun ProjectView(navController: NavController, authRepository: AuthRepository, userID: String) {
    var selectedFilter by remember { mutableStateOf("Fecha de inicio") }
    var isFilterAscending by remember { mutableStateOf(false) }
    var isFilterMenuOpen by remember { mutableStateOf(false) }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val loggedInUserUID: String by authRepository.getLoggedInUserUID().observeAsState("")
    val projectsList = remember { mutableStateOf(emptyList<Project>()) }
    val isAddProjectDialogOpen = remember { mutableStateOf(false) }
    val loggedInUserName: String by authRepository.getLoggedInUserName().observeAsState("")
    authRepository.loadProjectsFromFirebase(projectsList)
    val viewModel: ProjectViewModel = viewModel()
    val projectsSelectedToRemove = viewModel.projectsSelectedToRemove
    val showRemoveProyectsDialog = viewModel.showDeleteProyectsDialog

    val filteredProjects = remember(projectsList, selectedFilter, isFilterAscending, searchQuery) {
        derivedStateOf {
            var filteredList = projectsList.value.filter {
                it.projectName.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }
            when (selectedFilter) {
                "Nombre" -> {
                    filteredList = if (isFilterAscending) {
                        filteredList.sortedBy { it.projectName }
                    } else {
                        filteredList.sortedByDescending { it.projectName }
                    }
                }

                "Fecha de inicio" -> {
                    filteredList = if (isFilterAscending) {
                        filteredList.sortedBy { it.startDate }
                    } else {
                        filteredList.sortedByDescending { it.startDate }
                    }
                }

                "Fecha de finalización" -> {
                    filteredList = if (isFilterAscending) {
                        filteredList.sortedBy { it.endDate }
                    } else {
                        filteredList.sortedByDescending { it.endDate }
                    }
                }
            }
            filteredList
        }
    }

    Scaffold(
        floatingActionButton = {
            CompositionLocalProvider(
                LocalContentColor provides colorResource(id = R.color.white)
            ) {
                if(projectsSelectedToRemove.size > 0) {
                    Row {
                        FloatingActionButton(
                            onClick = { projectsSelectedToRemove.clear() },
                            containerColor = myOrangehigh
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Cancelar",
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        FloatingActionButton(
                            onClick = { viewModel.showDeleteProyectsDialog.value = true },
                            containerColor = myOrangehigh
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Remover proyectos"
                            )
                        }
                    }
                } else {
                    Row {
                        FloatingActionButton(
                            onClick = {
                                isSearchExpanded = !isSearchExpanded
                                if (!isSearchExpanded) {
                                    searchQuery = ""
                                }
                            },
                            containerColor = myOrangehigh
                        ) {
                            Icon(
                                if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = "Buscar Proyectos",
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        FloatingActionButton(
                            onClick = { isFilterMenuOpen = true },
                            containerColor = myOrangehigh
                        ) {
                            Icon(
                                Icons.Default.FilterAlt,
                                contentDescription = "Filtros"
                            )
                        }
                        DropdownMenu(
                            expanded = isFilterMenuOpen,
                            onDismissRequest = { isFilterMenuOpen = false }
                        ) {
                            DropdownMenuItem(onClick = {
                                selectedFilter = "Nombre"
                                isFilterMenuOpen = false
                                isFilterAscending = !isFilterAscending
                            }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Nombre")
                                    Spacer(Modifier.width(10.dp))
                                    Icon(
                                        if (selectedFilter == "Nombre" && isFilterAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            DropdownMenuItem(onClick = {
                                selectedFilter = "Fecha de inicio"
                                isFilterMenuOpen = false
                                isFilterAscending = !isFilterAscending
                            }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Fecha de inicio")
                                    Spacer(Modifier.width(10.dp))
                                    Icon(
                                        if (selectedFilter == "Fecha de inicio" && isFilterAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            DropdownMenuItem(onClick = {
                                selectedFilter = "Fecha de finalización"
                                isFilterMenuOpen = false
                                isFilterAscending = !isFilterAscending
                            }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Fecha de finalización")
                                    Spacer(Modifier.width(10.dp))
                                    Icon(
                                        if (selectedFilter == "Fecha de finalización" && isFilterAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        FloatingActionButton(
                            onClick = { isAddProjectDialogOpen.value = true },
                            containerColor = myOrangehigh
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Agregar Proyecto"
                            )
                        }
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mywhie)
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.offset(y = 15.dp)
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Proyectos",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = myBlue, thickness = 1.dp)
                Spacer(modifier = Modifier.height(5.dp))
                when (filteredProjects.value.size) {
                    0 -> {
                        Text(
                            text = "No tienes proyectos creados.",
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }

                    1 -> {
                        Text(
                            text = "Tienes 1 proyecto creado.",
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }

                    else -> {
                        Text(
                            text = "Tienes ${filteredProjects.value.size} proyectos creados.",
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }
                }
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(filteredProjects.value) { project ->
                        ProjectCard(
                            project = project,
                            navController = navController,
                            userID,
                            authRepository,
                            projectsList
                        )
                        Spacer(Modifier.height(15.dp))
                    }
                }
            }
        }
    }

    if(showRemoveProyectsDialog.value === true) RemoveProjectsSelected(userID)
    HamburgueerMenu(navController = navController, authRepository = authRepository)
    if (isAddProjectDialogOpen.value) {
        Dialog(onDismissRequest = { isAddProjectDialogOpen.value = false }) {
            RegisterCard(isAddProjectDialogOpen, loggedInUserName, loggedInUserUID, filteredProjects)
        }
    }
    if (isSearchExpanded) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .offset(x = 80.dp, y = 640.dp)
                .navigationBarsPadding()
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { newValue ->
                    searchQuery = newValue
                    if (newValue.isEmpty()) {
                        authRepository.loadProjectsFromFirebase(projectsList)
                    }
                },
                label = { Text("Buscar Proyecto") },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
fun RemoveProjectsSelected(userID: String) {
    val viewModel: ProjectViewModel = viewModel()
    val projectsSelectedToRemove = viewModel.projectsSelectedToRemove
    val context = LocalContext.current

    if(projectsSelectedToRemove.size > 0) {
        AlertDialog(
            onDismissRequest = { viewModel.showDeleteProyectsDialog.value = false },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = myOrangehigh,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Cancelar",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            val db = FirebaseFirestore.getInstance()
                            val collection = db.collection("Usuarios").document(userID).collection("Proyectos")

                            for(proyect in projectsSelectedToRemove) {
                                collection.document(proyect).delete()
                            }
                            viewModel.showDeleteProyectsDialog.value = false
                            Toast.makeText(
                                context,
                                "Proyectos removidos",
                                Toast.LENGTH_LONG
                            ).show()
                            viewModel.projectsSelectedToRemove.removeAll(projectsSelectedToRemove)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = myOrangehigh,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Aceptar",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            },
            title = {
                Row {
                    Box() {
                        Icon(Icons.Default.WarningAmber, contentDescription = null, tint = myOrangehigh)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Advertencia",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = myBlue
                        )
                    )
                }
            },
            text = {
                Text(
                    text = "Los proyectos no se podran volver a recuperar. ¿Esta seguro de esto?",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light)
                )
            }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectCard(
    project: Project,
    navController: NavController,
    userID: String,
    authRepository: AuthRepository,
    projectsList: MutableState<List<Project>>
) {
    val viewModel: ProjectViewModel = viewModel()
    val projectsSelectedToRemove = viewModel.projectsSelectedToRemove
    val showDatePicker = remember { mutableStateOf(false) }

    Spacer(Modifier.height(15.dp))
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Card(
            colors = CardDefaults.cardColors(
                containerColor = myOrangelow
            ),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .wrapContentSize(Alignment.Center)
                .indication(
                    interactionSource = MutableInteractionSource(),
                    indication = LocalIndication.current
                )
                .combinedClickable(onClick = {
                    if (projectsSelectedToRemove.size > 0) {
                        if (!projectsSelectedToRemove.contains(project.id)) {
                            projectsSelectedToRemove.add(project.id.toString())
                        } else projectsSelectedToRemove.remove(project.id.toString())
                    } else {
                        navController.navigate("cardview_projects_screen/$userID/${project.id.toString()}")
                    }
                }, onLongClick = {
                    if (!projectsSelectedToRemove.contains(project.id)) {
                        projectsSelectedToRemove.add(project.id.toString())
                    } else projectsSelectedToRemove.remove(project.id.toString())
                }),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(15.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        Text(
                            text = project.projectName,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    if(projectsSelectedToRemove.contains(project.id.toString())) {
                        Box() {
                            Icon(Icons.Default.CheckBox, contentDescription = null, tint = myBlue, modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    if(projectsSelectedToRemove.size > 0 && !projectsSelectedToRemove.contains(project.id.toString())) {
                        Box() {
                            Icon(Icons.Default.CheckBoxOutlineBlank, contentDescription = null, tint = myBlue, modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Transparent, shape = CircleShape)
                            .border(1.dp, Color.LightGray, CircleShape)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Creador",
                            modifier = Modifier.size(12.dp),
                            tint = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = project.creatorName,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Transparent, shape = CircleShape)
                            .border(1.dp, Color.LightGray, CircleShape)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Número de participantes",
                            modifier = Modifier.size(12.dp),
                            tint = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "${project.members.size}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Transparent, shape = CircleShape)
                            .border(1.dp, Color.LightGray, CircleShape)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Checklist,
                            contentDescription = "Número de tareas",
                            modifier = Modifier.size(12.dp),
                            tint = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "${project.tasks.size}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(5.dp))

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Transparent, shape = CircleShape)
                            .border(1.dp, Color.LightGray, CircleShape)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DocumentScanner,
                            contentDescription = "Número de documentos",
                            modifier = Modifier.size(12.dp),
                            tint = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "${project.documents.size}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Transparent, shape = CircleShape)
                            .border(1.dp, Color.LightGray, CircleShape)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Fecha de creación",
                            modifier = Modifier.size(12.dp),
                            tint = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = project.startDate,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.clickable { showDatePicker.value = true }
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Transparent, shape = CircleShape)
                            .border(1.dp, Color.LightGray, CircleShape)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Fecha de Terminacion",
                            modifier = Modifier.size(12.dp),
                            tint = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = project.endDate,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterCard(
    isAddProjectDialogOpen: MutableState<Boolean>, loggedInUserName: String,
    loggedInUserUID: String,
    projects: State<List<Project>>
) {
    val startDateText = remember { mutableStateOf("") }
    val endDateText = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val context = LocalContext.current
    val nameRepliqued = remember {  mutableStateOf(false)}

    Card(
        modifier = Modifier.padding(16.dp),
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
                text = "Crear Proyecto",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = myBlue
            )
            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = Color.Black, thickness = 1.dp)
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = name.value,
                onValueChange = {
                    name.value = it
                    if (projects.value.find {
                            it.projectName?.trim().equals(name.value.trim(), ignoreCase = true)
                        } != null) nameRepliqued.value = true else nameRepliqued.value = false
                },
                label = { Text(text = "Nombre del proyecto") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    cursorColor = myBlue,
                    focusedBorderColor = Color(0xA9D8D8D8),
                    unfocusedBorderColor = Color(0xA9D8D8D8),
                    backgroundColor = Color(0x79D8D8D8)
                ),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if(name.value.length < 6) {
                    Text(
                        text = "* Requerido",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light, color = myBlue),
                        modifier = Modifier.offset(x = 20.dp)
                    )
                } else if(nameRepliqued.value === true) {
                    Text(
                        text = "* Nombre duplicado",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light, color = myBlue),
                        modifier = Modifier.offset(x = 20.dp)
                    )
                } else Text(text = " ")
                Text(
                    text = "${name.value.length}/30",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light, color = if(name.value.length > 30) myBlue else Color.Black),
                    modifier = Modifier
                        .offset(x = -20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                label = { Text(text = "Descripción del proyecto") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    cursorColor = myBlue,
                    focusedBorderColor = Color(0xA9D8D8D8),
                    unfocusedBorderColor = Color(0xA9D8D8D8),
                    backgroundColor = Color(0x79D8D8D8)
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(100.dp)
            )
            Text(
                text = "${description.value.length}/200",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light, color = if(description.value.length > 200) myBlue else Color.Black),
                modifier = Modifier
                    .offset(x = -20.dp)
                    .align(Alignment.End)
            )
            Spacer(modifier = Modifier.height(15.dp))
            CalendarSample3(startDateText, endDateText)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val scope = rememberCoroutineScope()
                Button(
                    enabled = name.value.length >= 6 && name.value.length <= 200 && nameRepliqued.value === false && description.value.length <= 200,
                    onClick = {
                        scope.launch {
                            val newDoc = Project(
                                null,
                                name.value.lowercase(Locale.getDefault())
                                    .replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.getDefault()
                                        ) else it.toString()
                                    },
                                loggedInUserName,
                                loggedInUserUID,
                                description.value,
                                startDateText.value,
                                endDateText.value,
                                emptyList(),
                                emptyList()
                            )

                            val db = FirebaseFirestore.getInstance()
                            val collectionReference = db.collection("Usuarios")
                                .document(loggedInUserUID)
                                .collection("Proyectos")

                            collectionReference
                                .add(newDoc)
                                .addOnSuccessListener { documentReference ->
                                    val docUpdated = newDoc.copy(id = documentReference.id)
                                    collectionReference
                                        .document(documentReference.id)
                                        .set(docUpdated)
                                        .addOnSuccessListener {
                                            name.value = ""
                                            description.value = ""
                                            startDateText.value = ""
                                            endDateText.value = ""
                                            isAddProjectDialogOpen.value = false

                                            Toast.makeText(
                                                context,
                                                "Proyecto creado correctamente",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(myBlue, contentColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Guardar")
                }

                Button(
                    onClick = {
                        isAddProjectDialogOpen.value = false
                    },
                    colors = ButtonDefaults.buttonColors(myBlue, contentColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar")
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarSample3(
    startDateText: MutableState<String>,
    endDateText: MutableState<String>
) {
    val timeBoundary = LocalDate.now().let { now -> now.minusYears(2)..now.plusYears(10) }
    val selectedRange = remember {
        val default =
            LocalDate.now().minusYears(2).let { time -> time..time.plusDays(8) }
        mutableStateOf(default.toRange())
    }

    val showDialog = remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = { showDialog.value = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = myOrangehigh
            )
        ) {
            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Seleccionar Fecha")
        }

        if (showDialog.value) {
            Dialog(
                onDismissRequest = { showDialog.value = false },
            ) {
                CalendarDialog(
                    state = rememberUseCaseState(visible = showDialog.value),
                    config = CalendarConfig(
                        yearSelection = true,
                        monthSelection = true,
                        boundary = timeBoundary,
                        style = CalendarStyle.MONTH,
                    ),
                    selection = CalendarSelection.Period(
                        selectedRange = selectedRange.value
                    ) { startDate, endDate ->
                        selectedRange.value = Range(startDate, endDate)
                        startDateText.value = startDate.toString()
                        endDateText.value = endDate.toString()
                        showDialog.value = false
                    }
                )
            }
        }
        if(startDateText.value.length > 1 && endDateText.value.length > 1) {
            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "Inicio: ${startDateText.value}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light,
                        fontSize = 8.sp
                    ),
                )
                Text(
                    text = "Fin: ${endDateText.value}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light,
                        fontSize = 8.sp,
                    ),
                )
            }
        }
    }
}

class ProjectViewModel : ViewModel() {
    var projectsSelectedToRemove = mutableStateListOf<String>()
    val showDeleteProyectsDialog = mutableStateOf(false)
}