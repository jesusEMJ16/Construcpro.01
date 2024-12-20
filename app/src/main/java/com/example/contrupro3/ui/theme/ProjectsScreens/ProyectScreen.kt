package com.example.contrupro3.ui.theme.ProjectsScreens

import android.annotation.SuppressLint
import android.os.Build
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.contrupro3.R
import com.example.contrupro3.TimestampToDate
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.ProjectsModels.ProjectModel
import com.example.contrupro3.models.ProjectsModels.ProjectsScreen_ViewModel
import com.example.contrupro3.ui.theme.Menu.HamburgueerMenu
import com.example.contrupro3.ui.theme.backgroundButtonColor
import com.example.contrupro3.ui.theme.contentButtonColor
import com.example.contrupro3.ui.theme.myBlue
import com.example.contrupro3.ui.theme.myOrangehigh
import com.example.contrupro3.ui.theme.myOrangelow
import com.example.contrupro3.ui.theme.mywhie
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun ProjectView(
    navController: NavController,
    authRepository: AuthRepository,
    userID: String,
    ProjectsScreen_VieVmodel: ProjectsScreen_ViewModel
) {
    val showRemoveProyectsDialog by ProjectsScreen_VieVmodel
        .showDeleteProyectsDialog.observeAsState(false)
    val loggedInUserUID: String by authRepository.getLoggedInUserUID().observeAsState("")
    val loggedInUserName: String by authRepository.getLoggedInUserName().observeAsState("")
    val projectsList = remember { mutableStateOf<List<ProjectModel>>(emptyList()) }
    val isAddProjectDialogOpen = remember { mutableStateOf(false) }
    val firebase = FirebaseFirestore.getInstance()

    authRepository.loadProjectsFromFirebase(projectsList)
    val filteredProjects = FilterProjects(projectsList, ProjectsScreen_VieVmodel)

    Scaffold(
        floatingActionButton = { ActionsButtons(ProjectsScreen_VieVmodel, isAddProjectDialogOpen) }
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
                TitleSection(filteredProjects)
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(filteredProjects.size) { index ->
                        val project = filteredProjects[index]

                        ProjectCard(
                            project,
                            navController,
                            userID,
                            authRepository,
                            filteredProjects,
                            ProjectsScreen_VieVmodel,
                            firebase
                        )
                        Spacer(Modifier.height(15.dp))
                        if (filteredProjects.size - 1 == index) {
                            Spacer(modifier = Modifier.padding(vertical = 50.dp))
                        }
                    }
                }
            }
        }
    }

    if (showRemoveProyectsDialog === true) RemoveProjectsSelected(
        userID,
        ProjectsScreen_VieVmodel
    )
    HamburgueerMenu(navController = navController, authRepository = authRepository)
    if (isAddProjectDialogOpen.value) {
        Dialog(onDismissRequest = { isAddProjectDialogOpen.value = false }) {
            RegisterCard(
                isAddProjectDialogOpen,
                loggedInUserName,
                loggedInUserUID,
                filteredProjects,
                ProjectsScreen_VieVmodel
            )
        }
    }
}

@Composable
fun TitleSection(filteredProjects: List<ProjectModel>) {
    Spacer(modifier = Modifier.height(5.dp))
    Text(
        text = "Proyectos",
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
    )
    Spacer(modifier = Modifier.height(15.dp))
    when (filteredProjects.size) {
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
                text = "Tienes ${filteredProjects.size} proyectos creados.",
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}

@Composable
fun ActionsButtons(
    ProjectsScreen_VieVmodel: ProjectsScreen_ViewModel,
    isAddProjectDialogOpen: MutableState<Boolean>
) {
    val projectsSelectedToRemove by ProjectsScreen_VieVmodel
        .projectsSelectedToRemove.observeAsState(emptyList())

    CompositionLocalProvider(
        LocalContentColor provides colorResource(id = R.color.white)
    ) {
        if (projectsSelectedToRemove.size > 0) {
            Row {
                FloatingActionButton(
                    onClick = {
                        ProjectsScreen_VieVmodel.onRemoveProjectsChanged(
                            emptyList(),
                            false
                        )
                    },
                    containerColor = backgroundButtonColor,
                    contentColor = contentButtonColor
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cancelar",
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                FloatingActionButton(
                    onClick = {
                        ProjectsScreen_VieVmodel.onRemoveProjectsChanged(
                            projectsSelectedToRemove,
                            true
                        )
                    },
                    containerColor = backgroundButtonColor,
                    contentColor = contentButtonColor
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remover proyectos"
                    )
                }
            }
        } else {
            FiltersDropdowMenuProjects(ProjectsScreen_VieVmodel) {
                isAddProjectDialogOpen.value = true
            }
        }
    }
}

@Composable
fun FiltersDropdowMenuProjects(
    projectViewModel: ProjectsScreen_ViewModel,
    openAddProjects: () -> Unit
) {
    val isFilterMenuOpen = projectViewModel.isFilterMenuOpen.observeAsState(false)
    val isSearchExpanded = projectViewModel.isSearchExpanded.observeAsState(false)
    val isFilterAscending = projectViewModel.isFilterAscending.observeAsState(false)
    val filterSelected = projectViewModel.filterSelected.observeAsState("Fecha de creación")

    Row {
        FloatingActionButton(
            onClick = {
                projectViewModel.onFilterSelectionChanged(
                    true,
                    isSearchExpanded.value,
                    filterSelected.value,
                    isFilterAscending.value
                )
            },
            containerColor = backgroundButtonColor,
            contentColor = contentButtonColor
        ) {
            Icon(
                Icons.Default.FilterAlt,
                contentDescription = "Filtros"
            )
        }
        DropdownMenu(
            expanded = isFilterMenuOpen.value,
            onDismissRequest = {
                projectViewModel.onFilterSelectionChanged(
                    false,
                    isSearchExpanded.value,
                    filterSelected.value,
                    isFilterAscending.value
                )
            }
        ) {
            DropdownMenuItem(onClick = {
                projectViewModel.onFilterSelectionChanged(
                    false,
                    isSearchExpanded.value,
                    "Nombre",
                    !isFilterAscending.value
                )
            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Nombre")
                    Spacer(Modifier.width(10.dp))
                    Icon(
                        if (filterSelected.value == "Nombre" && isFilterAscending.value) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            DropdownMenuItem(onClick = {
                projectViewModel.onFilterSelectionChanged(
                    false,
                    isSearchExpanded.value,
                    "Fecha de inicio",
                    !isFilterAscending.value
                )
            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Fecha de inicio")
                    Spacer(Modifier.width(10.dp))
                    Icon(
                        if (filterSelected.value == "Fecha de inicio" && isFilterAscending.value) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            DropdownMenuItem(onClick = {
                projectViewModel.onFilterSelectionChanged(
                    false,
                    isSearchExpanded.value,
                    "Fecha de finalización",
                    !isFilterAscending.value
                )
            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Fecha de finalización")
                    Spacer(Modifier.width(10.dp))
                    Icon(
                        if (filterSelected.value == "Fecha de finalización" && isFilterAscending.value) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            DropdownMenuItem(onClick = {
                projectViewModel.onFilterSelectionChanged(
                    false,
                    isSearchExpanded.value,
                    "Fecha de creación",
                    isFilterAscending.value
                )
            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Fecha de creación")
                    Spacer(Modifier.width(10.dp))
                    Icon(
                        if (filterSelected.value == "Fecha de creación" && isFilterAscending.value) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        FloatingActionButton(
            onClick = { openAddProjects() },
            containerColor = backgroundButtonColor,
            contentColor = contentButtonColor
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Agregar Proyecto"
            )
        }
    }
}

@Composable
fun FilterProjects(
    projectsList: MutableState<List<ProjectModel>>,
    projectViewModel: ProjectsScreen_ViewModel
): List<ProjectModel> {
    val filterSelected = projectViewModel.filterSelected.observeAsState("Fecha de creación")
    val isFilterAscending = projectViewModel.isFilterAscending.observeAsState(false)
    val searchQuery = projectViewModel.searchQuery.observeAsState("")

    val filteredProjects =
        remember(projectsList, filterSelected, isFilterAscending, searchQuery) {
            derivedStateOf {
                var filteredList = projectsList.value.filter { project ->
                    project.name.toString().contains(
                        searchQuery.value,
                        ignoreCase = true
                    )
                }

                when (filterSelected.value) {
                    "Nombre" -> {
                        filteredList = if (isFilterAscending.value) {
                            filteredList.sortedBy { it.name }
                        } else {
                            filteredList.sortedByDescending { it.name }
                        }
                    }

                    "Fecha de inicio" -> {
                        filteredList = if (isFilterAscending.value) {
                            filteredList.sortedBy { it.startDate }
                        } else {
                            filteredList.sortedByDescending { it.startDate }
                        }
                    }

                    "Fecha de finalización" -> {
                        filteredList = if (isFilterAscending.value) {
                            filteredList.sortedBy { it.endDate }
                        } else {
                            filteredList.sortedByDescending { it.endDate }
                        }
                    }

                    "Fecha de creación" -> {
                        filteredList = if (isFilterAscending.value) {
                            filteredList.sortedBy { it.createdAt }
                        } else {
                            filteredList.sortedByDescending { it.createdAt }
                        }
                    }
                }
                filteredList
            }
        }
    return filteredProjects.value
}

@Composable
fun RemoveProjectsSelected(userID: String, projectViewModel: ProjectsScreen_ViewModel) {
    val projectsSelectedToRemove by
        projectViewModel.projectsSelectedToRemove.observeAsState(emptyList())
    val context = LocalContext.current

    if (projectsSelectedToRemove.size > 0) {
        AlertDialog(
            onDismissRequest = {
                projectViewModel.onRemoveProjectsChanged(
                    projectsSelectedToRemove,
                    false
                )
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { projectViewModel.onRemoveProjectsChanged(projectsSelectedToRemove, false) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = backgroundButtonColor,
                            contentColor = contentButtonColor
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
                            val collection =
                                db.collection("Users")
                                    .document(userID)
                                    .collection("Projects")

                            for (proyect in projectsSelectedToRemove) {
                                collection.document(proyect.id.toString()).delete()
                            }
                            projectViewModel.onRemoveProjectsChanged(emptyList(), false)
                            Toast.makeText(
                                context,
                                "Proyectos removidos",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = backgroundButtonColor,
                            contentColor = contentButtonColor
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
                    Box {
                        Icon(
                            Icons.Default.WarningAmber,
                            contentDescription = null,
                            tint = myOrangehigh
                        )
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
    project: ProjectModel,
    navController: NavController,
    userID: String,
    authRepository: AuthRepository,
    projectsList: List<ProjectModel>,
    projectViewModel: ProjectsScreen_ViewModel,
    firebase: FirebaseFirestore
) {
    val projectsSelectedToRemove =
        projectViewModel.projectsSelectedToRemove.observeAsState(emptyList())
    val showDeleteProjectsDialog = projectViewModel.showDeleteProyectsDialog.observeAsState(false)
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
                    if (projectsSelectedToRemove.value.size > 0) {
                        if (!projectsSelectedToRemove.value.contains(project)) {
                            val newList = projectsSelectedToRemove.value + project
                            projectViewModel.onRemoveProjectsChanged(
                                newList,
                                showDeleteProjectsDialog.value
                            )
                        } else projectViewModel.onRemoveProjectsChanged(
                            projectsSelectedToRemove.value.filter { p -> p !== project },
                            showDeleteProjectsDialog.value
                        )
                    } else {
                        navController.navigate("cardview_projects_screen/$userID/${project.id.toString()}")
                    }
                }, onLongClick = {
                    if (!projectsSelectedToRemove.value.contains(project)) {
                        val newList = projectsSelectedToRemove.value + project
                        projectViewModel.onRemoveProjectsChanged(
                            newList,
                            showDeleteProjectsDialog.value
                        )
                    } else projectViewModel.onRemoveProjectsChanged(
                        projectsSelectedToRemove.value.filter { p -> p !== project },
                        showDeleteProjectsDialog.value
                    )
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
                            text = project.name.toString(),
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    if (projectsSelectedToRemove.value.contains(project)) {
                        Box {
                            Icon(
                                Icons.Default.CheckBox,
                                contentDescription = null,
                                tint = backgroundButtonColor,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    if (projectsSelectedToRemove.value.size > 0 && !projectsSelectedToRemove.value.contains(
                            project
                        )
                    ) {
                        Box {
                            Icon(
                                Icons.Default.CheckBoxOutlineBlank,
                                contentDescription = null,
                                tint = backgroundButtonColor,
                                modifier = Modifier.align(Alignment.Center)
                            )
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
                        text = project.creatorName.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                            text = "${project.counters?.members}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                            text = "${project.counters?.tasks}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                            text = "${project.counters?.documents}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                            text = if (project?.startDate == null) "-" else TimestampToDate(project?.startDate),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.clickable { showDatePicker.value = true }
                        )
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                            text = if (project?.endDate == null) "-" else TimestampToDate(project?.endDate),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
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
    projects: List<ProjectModel>,
    ProjectsScreen_ViewModel: ProjectsScreen_ViewModel
) {
    val name = ProjectsScreen_ViewModel.projectName.observeAsState("")
    val description = ProjectsScreen_ViewModel.projectDescription.observeAsState("")
    val currentContext = LocalContext.current
    val nameRepliqued = remember { mutableStateOf(false) }

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
                    ProjectsScreen_ViewModel.onFieldsUpdated(it, description.value)
                    nameRepliqued.value = projects.find {
                        it.name.toString().trim()
                            .equals(name.value.trim(), ignoreCase = true)
                    } != null
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
                if (name.value.length < 6) {
                    Text(
                        text = "* Requerido",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Light,
                            color = myBlue
                        ),
                        modifier = Modifier.offset(x = 20.dp)
                    )
                } else if (nameRepliqued.value === true) {
                    Text(
                        text = "* Nombre duplicado",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Light,
                            color = myBlue
                        ),
                        modifier = Modifier.offset(x = 20.dp)
                    )
                } else Text(text = " ")
                Text(
                    text = "${name.value.length}/25",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light,
                        color = if (name.value.length > 25) Color.Red else Color.Black
                    ),
                    modifier = Modifier
                        .offset(x = -20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = description.value,
                onValueChange = { ProjectsScreen_ViewModel.onFieldsUpdated(name.value, it) },
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
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Light,
                    color = if (description.value.length > 200) Color.Red else Color.Black
                ),
                modifier = Modifier
                    .offset(x = -20.dp)
                    .align(Alignment.End)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val scope = rememberCoroutineScope()
                Button(
                    enabled = name.value.length >= 6 && name.value.length <= 25 && nameRepliqued.value === false && description.value.length <= 200,
                    onClick = {
                        scope.launch {
                            val newDoc = ProjectModel(
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
                                createdAt = (Calendar.getInstance()).timeInMillis.toString()
                            )

                            val db = FirebaseFirestore.getInstance()
                            val collectionReference = db.collection("Users")
                                .document(loggedInUserUID)
                                .collection("Projects")

                            collectionReference
                                .add(newDoc)
                                .addOnSuccessListener { documentReference ->
                                    val docUpdated = newDoc.copy(id = documentReference.id)
                                    collectionReference
                                        .document(documentReference.id)
                                        .set(docUpdated)
                                        .addOnSuccessListener {
                                            ProjectsScreen_ViewModel.onFieldsUpdated("", "")
                                            isAddProjectDialogOpen.value = false

                                            Toast.makeText(
                                                currentContext,
                                                "Proyecto creado",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundButtonColor,
                        contentColor = contentButtonColor
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Guardar")
                }

                Button(
                    onClick = {
                        isAddProjectDialogOpen.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundButtonColor,
                        contentColor = contentButtonColor
                    ),
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