package com.example.contrupro3.ui.theme.TasksScreen


import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FolderOpen
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
import androidx.compose.runtime.State
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
import com.example.contrupro3.ProjectSelection
import com.example.contrupro3.R
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.ProjectsModels.ProjectModel
import com.example.contrupro3.models.TasksModels.TaskModel
import com.example.contrupro3.models.TasksModels.TasksScreen_ViewModel
import com.example.contrupro3.ui.theme.Menu.HamburgueerMenu
import com.example.contrupro3.ui.theme.myBlue
import com.example.contrupro3.ui.theme.myOrangehigh
import com.example.contrupro3.ui.theme.myOrangelow
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun TasksScreen(
    navController: NavController,
    authRepository: AuthRepository,
    userID: String,
    TasksScreen_ViewModel: TasksScreen_ViewModel
) {
    val loggedInUserUID: String by authRepository.getLoggedInUserUID().observeAsState("")
    val loggedInUserName: String by authRepository.getLoggedInUserName().observeAsState("")
    val tasksSelectedToRemove =
        TasksScreen_ViewModel.tasksSelectedToRemove.observeAsState(emptyList())
    val showTaskDeleteDialog = TasksScreen_ViewModel.showDeleteTasksDialog.observeAsState(false)
    val tasksList = remember { mutableStateOf<List<TaskModel>>(emptyList()) }
    val project = TasksScreen_ViewModel.projectSelected.observeAsState(null)
    val openSelectProjectDialog = remember(project) { mutableStateOf(project.value == null) }
    val isAddTeamDialogOpen = remember { mutableStateOf(false) }
    val filteredTasks = FilterTasks(tasksList, TasksScreen_ViewModel)

    if (project.value !== null) {
        TasksScreen_ViewModel.onProjectSaved(project.value!!)
        authRepository.loadTasksFromFirebase(tasksList, project.value?.id.toString())
    }

    Scaffold(
        floatingActionButton = {
            CompositionLocalProvider(
                LocalContentColor provides colorResource(id = R.color.white)
            ) {
                if (tasksSelectedToRemove.value.isNotEmpty()) {
                    Row {
                        FloatingActionButton(
                            onClick = {
                                TasksScreen_ViewModel.onRemoveTasksChanged(
                                    emptyList(),
                                    false
                                )
                            },
                            containerColor = myOrangehigh
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Cancelar",
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        FloatingActionButton(
                            onClick = {
                                TasksScreen_ViewModel.onRemoveTasksChanged(
                                    tasksSelectedToRemove.value,
                                    true
                                )
                            },
                            containerColor = myOrangehigh
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar"
                            )
                        }
                    }
                } else {
                    FiltersDropdowMenu(
                        TasksScreen_ViewModel,
                        project,
                        { openSelectProjectDialog.value = true }) {
                        isAddTeamDialogOpen.value = true
                    }
                }
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
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
                        text = "Tareas",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        )
                    )
                    if (project.value !== null) Text(
                        text = "(${project.value?.name})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    if (project.value == null) {
                        Text(
                            text = "No se ha seleccionado un proyecto.",
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    } else {
                        when (filteredTasks.size) {
                            0 -> {
                                Text(
                                    text = "No tienes tareas creadas.",
                                    modifier = Modifier.padding(bottom = 10.dp)
                                )
                            }

                            1 -> {
                                Text(
                                    text = "Tienes 1 tarea creada.",
                                    modifier = Modifier.padding(bottom = 10.dp)
                                )
                            }

                            else -> {
                                Text(
                                    text = "Tienes ${filteredTasks.size} tareas creadas.",
                                    modifier = Modifier.padding(bottom = 10.dp)
                                )
                            }
                        }
                    }
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(filteredTasks.size) { index ->
                            val task = filteredTasks[index]
                            TaskCard(
                                task,
                                project.value?.id.toString(),
                                navController,
                                userID,
                                authRepository,
                                tasksList,
                                TasksScreen_ViewModel
                            )
                            Spacer(Modifier.height(15.dp))
                            if(filteredTasks.size - 1 == index) {
                                Spacer(modifier = Modifier.padding(vertical = 50.dp))
                            }
                        }
                    }
                }
            }
        }
    )

    HamburgueerMenu(navController, authRepository)
    if (openSelectProjectDialog.value) ProjectSelection(
        userID,
        authRepository,
        { openSelectProjectDialog.value = false }
    ) { p -> TasksScreen_ViewModel.onProjectSaved(p) }
    if (showTaskDeleteDialog.value) RemoveTasksSelected(userID, project.value, TasksScreen_ViewModel)
    if (isAddTeamDialogOpen.value) {
        Dialog(onDismissRequest = { isAddTeamDialogOpen.value = false }) {
            RegisterCardTask(
                isAddTeamDialogOpen,
                loggedInUserName,
                loggedInUserUID,
                filteredTasks,
                project.value
            )
        }
    }
}

@Composable
private fun FilterTasks(
    tasksList: MutableState<List<TaskModel>>,
    TasksScreen_ViewModel: TasksScreen_ViewModel
): List<TaskModel> {
    val filterSelected = TasksScreen_ViewModel.filterSelected.observeAsState("Fecha de inicio")
    val isFilterAscending = TasksScreen_ViewModel.isFilterAscending.observeAsState(false)
    val searchQuery = TasksScreen_ViewModel.searchQuery.observeAsState("")

    val filteredTasks =
        remember(tasksList, filterSelected, isFilterAscending, searchQuery) {
            derivedStateOf {
                var filteredList = tasksList.value.filter { task ->
                    task.name.toString().contains(
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
                }
                filteredList
            }
        }
    return filteredTasks.value
}

@Composable
private fun FiltersDropdowMenu(
    TasksScreen_ViewModel: TasksScreen_ViewModel,
    project: State<ProjectModel?>,
    openSelectProjectDialog: () -> Unit,
    openAddTasks: () -> Unit
) {
    val isFilterMenuOpen = TasksScreen_ViewModel.isFilterMenuOpen.observeAsState(false)
    val isSearchExpanded = TasksScreen_ViewModel.isSearchExpanded.observeAsState(false)
    val isFilterAscending = TasksScreen_ViewModel.isFilterAscending.observeAsState(false)
    val filterSelected = TasksScreen_ViewModel.filterSelected.observeAsState("Fecha de inicio")

    Row {
        FloatingActionButton(
            onClick = { openSelectProjectDialog() },
            containerColor = myOrangehigh
        ) {
            Icon(
                Icons.Default.FolderOpen,
                contentDescription = "Seleccionar Proyecto"
            )
        }
        if (project.value !== null) {
            Spacer(modifier = Modifier.width(16.dp))
            FloatingActionButton(
                onClick = {
                    TasksScreen_ViewModel.onFilterSelectionChanged(
                        true,
                        isSearchExpanded.value,
                        filterSelected.value,
                        isFilterAscending.value
                    )
                },
                containerColor = myOrangehigh
            ) {
                Icon(
                    Icons.Default.FilterAlt,
                    contentDescription = "Filtros"
                )
            }
            DropdownMenu(
                expanded = isFilterMenuOpen.value,
                onDismissRequest = {
                    TasksScreen_ViewModel.onFilterSelectionChanged(
                        false,
                        isSearchExpanded.value,
                        filterSelected.value,
                        isFilterAscending.value
                    )
                }
            ) {
                DropdownMenuItem(onClick = {
                    TasksScreen_ViewModel.onFilterSelectionChanged(
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
                    TasksScreen_ViewModel.onFilterSelectionChanged(
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
                    TasksScreen_ViewModel.onFilterSelectionChanged(
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
            }
            Spacer(modifier = Modifier.width(16.dp))
            FloatingActionButton(
                onClick = { openAddTasks() },
                containerColor = myOrangehigh
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Crear Tarea"
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskCard(
    task: TaskModel,
    projectId: String,
    navController: NavController,
    userID: String,
    authRepository: AuthRepository,
    taskList: MutableState<List<TaskModel>>,
    TasksScreen_ViewModel: TasksScreen_ViewModel
) {
    val tasksSelectedToRemove =
        TasksScreen_ViewModel.tasksSelectedToRemove.observeAsState(emptyList())
    val showDeleteTaskDialog = TasksScreen_ViewModel.showDeleteTasksDialog.observeAsState(false)

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
                .combinedClickable(
                    onClick = {
                        if (tasksSelectedToRemove.value.size > 0) {
                            if (!tasksSelectedToRemove.value.contains(task)) {
                                val newList = tasksSelectedToRemove.value + task
                                TasksScreen_ViewModel.onRemoveTasksChanged(
                                    newList,
                                    showDeleteTaskDialog.value
                                )
                            } else TasksScreen_ViewModel.onRemoveTasksChanged(
                                tasksSelectedToRemove.value.filter { p -> p !== task },
                                showDeleteTaskDialog.value
                            )
                        } else navController.navigate("cardview_tasks_screen/${userID}/${projectId}/${task.id}")
                    },
                    onLongClick = {
                        if (!tasksSelectedToRemove.value.contains(task)) {
                            val newList = tasksSelectedToRemove.value + task
                            TasksScreen_ViewModel.onRemoveTasksChanged(
                                newList,
                                showDeleteTaskDialog.value
                            )
                        } else TasksScreen_ViewModel.onRemoveTasksChanged(
                            tasksSelectedToRemove.value.filter { p -> p !== task },
                            showDeleteTaskDialog.value
                        )
                    }
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(15.dp),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.name.toString(),
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    if (tasksSelectedToRemove.value.contains(task)) {
                        Box {
                            Icon(
                                Icons.Default.CheckBox,
                                contentDescription = null,
                                tint = myBlue,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    if (tasksSelectedToRemove.value.size > 0 && !tasksSelectedToRemove.value.contains(task)
                    ) {
                        Box {
                            Icon(
                                Icons.Default.CheckBoxOutlineBlank,
                                contentDescription = null,
                                tint = myBlue,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
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
                            imageVector = Icons.Default.Person,
                            contentDescription = "Creador",
                            modifier = Modifier.size(12.dp),
                            tint = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = task.creatorName.toString(),
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
                        text = "null",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun RemoveTasksSelected(
    userID: String,
    project: ProjectModel?,
    TasksScreen_ViewModel: TasksScreen_ViewModel
) {
    val tasksSelectedToRemove =
        TasksScreen_ViewModel.tasksSelectedToRemove.observeAsState(emptyList())
    val context = LocalContext.current

    if (tasksSelectedToRemove.value.size > 0) {
        AlertDialog(
            onDismissRequest = {
                TasksScreen_ViewModel.onRemoveTasksChanged(
                    tasksSelectedToRemove.value,
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
                        onClick = {
                            TasksScreen_ViewModel.onRemoveTasksChanged(emptyList(), false)
                        },
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
                            val collectionRef =
                                db.collection("Users")
                                    .document(userID)
                                    .collection("Projects")
                                    .document(project?.id!!)
                                    .collection("Tasks")

                            for (team in tasksSelectedToRemove.value) {
                                collectionRef.document("${team.id}").delete()
                            }
                            TasksScreen_ViewModel.onRemoveTasksChanged(emptyList(), false)
                            Toast.makeText(
                                context,
                                "Tareas removidas",
                                Toast.LENGTH_SHORT
                            ).show()
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
                    text = "Las tareas no se podran volver a recuperar. ¿Esta seguro de esto?",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light)
                )
            }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun RegisterCardTask(
    isAddTaskDialogOpen: MutableState<Boolean>, loggedInUserName: String,
    loggedInUserUID: String,
    tasks: List<TaskModel>,
    project: ProjectModel?
) {
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val nameRepliqued = remember { mutableStateOf(false) }
    val context = LocalContext.current

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
                text = "Crear Tarea",
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
                    if (tasks.find {
                            it.name?.trim().equals(name.value.trim(), ignoreCase = true)
                        } != null) nameRepliqued.value = true else nameRepliqued.value = false
                },
                label = { Text(text = "Nombre de la tarea") },
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
                onValueChange = { description.value = it },
                label = { Text(text = "Descripción de la tarea") },
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
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val scope = rememberCoroutineScope()
                Button(
                    enabled = name.value.length >= 6 && name.value.length <= 25 && nameRepliqued.value === false && description.value.length <= 200,
                    onClick = {
                        scope.launch {
                            val newDoc = TaskModel(
                                null,
                                name.value.lowercase(Locale.getDefault())
                                    .replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.getDefault()
                                        ) else it.toString()
                                    },
                                loggedInUserName,
                                loggedInUserUID,
                                description.value
                            )

                            val db = FirebaseFirestore.getInstance()
                            val collectionReference = db.collection("Users")
                                .document(loggedInUserUID)
                                .collection("Projects")
                                .document(project?.id!!)
                                .collection("Tasks")

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
                                            isAddTaskDialogOpen.value = false
                                            Toast.makeText(
                                                context,
                                                "Tarea creada",
                                                Toast.LENGTH_SHORT
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
                        isAddTaskDialogOpen.value = false
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