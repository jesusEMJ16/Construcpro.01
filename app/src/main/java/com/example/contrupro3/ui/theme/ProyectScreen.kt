package com.example.contrupro3.ui.theme

import android.annotation.SuppressLint
import com.maxkeppeler.sheets.calendar.CalendarDialog
import android.os.Build
import android.util.Log
import android.util.Range
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.SwipeProgress
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Filter1
import androidx.compose.material.icons.filled.Filter9Plus
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.FilterCenterFocus
import androidx.compose.material.icons.filled.FilterHdr
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterVintage
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.util.toRange
import com.example.contrupro3.R
import com.example.contrupro3.modelos.AuthRepository
import com.example.contrupro3.modelos.Project
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.firebase.firestore.FirebaseFirestore
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import kotlinx.coroutines.delay
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
    val projects = remember { mutableStateListOf<Project>() }

    authRepository.loadProjectsFromFirebase(projectsList)

    val filteredProjects = remember(projectsList, selectedFilter, isFilterAscending, searchQuery) {
        derivedStateOf {
            var filteredList = projectsList.value.filter { it.projectName.contains(searchQuery, ignoreCase = true) }
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
                Row {
                    FloatingActionButton(
                        onClick = {
                            isSearchExpanded = !isSearchExpanded
                            if (!isSearchExpanded) { // Si se está cerrando la búsqueda
                                searchQuery = "" // Restablecer searchQuery a una cadena vacía
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
                    FloatingActionButton(onClick = { isFilterMenuOpen = true },
                        containerColor = myOrangehigh) {
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
                    FloatingActionButton(onClick = { isAddProjectDialogOpen.value = true },
                        containerColor = myOrangehigh) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Agregar Proyecto"
                        )
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
                        text = "Proyectos",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Divider(color = Color.LightGray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(5.dp))
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        // Spacer(modifier = Modifier.height(15.dp))
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(filteredProjects.value) { project ->
                                ProjectCard(
                                    project = project,
                                    navController = navController,
                                    userID,
                                    authRepository,
                                    projectsList  // Asegúrate de pasar projectsList aquí
                                )
                                Spacer(Modifier.height(15.dp))
                            }
                        }
                    }
                }
            }
        }
    )

    HamburgueerMenu(navController = navController, authRepository = authRepository)
    if (isAddProjectDialogOpen.value) {
        Dialog(onDismissRequest = { isAddProjectDialogOpen.value = false }) {
            RegisterCard(isAddProjectDialogOpen, loggedInUserName, loggedInUserUID, projects)
        }
    }
    if (isSearchExpanded) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .navigationBarsPadding(),
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { newValue ->
                    searchQuery = newValue
                    if (newValue.isEmpty()) {
                        // Restablecer la lista de proyectos a su estado original
                        authRepository.loadProjectsFromFirebase(projectsList)
                    }
                },
                label = { Text("Buscar Proyectos") },
                modifier = Modifier
                    .fillMaxWidth()  // Hace que el TextField ocupe todo el ancho de su contenedor
                    .padding(top = 730.dp)  // Añade un espacio de 10.dp antes del TextField
                    .navigationBarsPadding()
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectCard(project: Project, navController: NavController, userID: String, authRepository: AuthRepository, projectsList: MutableState<List<Project>>) {

    val showDeleteDialog = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val showDatePicker = remember { mutableStateOf(false) }
    val projectID = project.id

    Spacer(Modifier.height(15.dp))
    Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
    ){
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
            .combinedClickable(
                onClick = {
                    navController.navigate("card_screen/${project.projectName}/${project.creatorName}")
                },
                onLongClick = { showDeleteDialog.value = true }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(15.dp),
    ) {
        Column(modifier = Modifier
            .padding(10.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = project.projectName,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(Alignment.Center)
                )

                IconButton(
                    onClick = { showDialog.value = true },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(20.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Estado del proyecto",
                        tint = Color.Black,
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(2.dp))

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
                    text = "${project.numParticipants}",
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
                    text = "${project.numTask}",
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
                    text = "${project.numDocuments}",
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


        fun deleteProject(userID: String, projectID: String) {
            val db = FirebaseFirestore.getInstance()
            Log.d("Delete Project2", "Deleting project with User ID: $userID, Project ID: $projectID")

            db.collection("Usuarios")
                .document(userID)
                .collection("Proyectos")
                .document(projectID)
                .delete()
                .addOnSuccessListener {
                    Log.d("Delete Project", "DocumentSnapshot successfully deleted!")
                    // Actualizar el contador de proyectos
                    projectsList.value = projectsList.value.filter { it.id != projectID }
                    // Cargar nuevamente la lista de proyectos desde Firebase
                    authRepository.loadProjectsFromFirebase(projectsList)
                }
                .addOnFailureListener { e ->
                    Log.w("Delete Project", "Error deleting document", e)
                }
        }

        if (showDeleteDialog.value) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog.value = false },
                title = { Text(text = "Confirmar eliminación") },
                text = { Text(text = "¿Estás seguro de que quieres eliminar este proyecto?") },
                confirmButton = {
                    Button(
                        onClick = {
                            if (projectID != null) {
                                Log.d("Delete Project", "User ID: $userID, Project ID: $projectID")
                                //eliminar
                                deleteProject(userID,projectID)
                                authRepository.loadProjectsFromFirebase(projectsList)
                            }
                            showDeleteDialog.value = false
                        }
                    ) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDeleteDialog.value = false }
                    ) {
                        Text("No")
                    }
                }
            )
        }
            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text(text = "Estado del proyecto") },
                    text = { Text(text = project.estado) },
                    confirmButton = {
                        Button(
                            onClick = { showDialog.value = false }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterCard(isAddProjectDialogOpen: MutableState<Boolean>, loggedInUserName: String,
                 loggedInUserUID: String,
                 projects: MutableList<Project>)
    {
        fun validateProjectInput(projectName: String, descripcion: String): String {
            // Comprueba si los campos están vacíos
            if (projectName.isEmpty() || descripcion.isEmpty()) {
                return "Nombre de proyecto y descripcion deben ser llenados."
            }

            // Aquí puedes añadir más validaciones si las necesitas...

            return ""
        }
        var errorDialogVisible by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }

    val startDateText = remember { mutableStateOf("") }
    val endDateText = remember { mutableStateOf("") }
    val projectName = remember { mutableStateOf("") }
    val startDate = remember { mutableStateOf("") }
    val endDate = remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nuevo Proyecto",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = projectName.value,
                onValueChange = { projectName.value = it },
                label = { Text("Nombre del proyecto") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .shadow(5.dp)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .shadow(5.dp)
                    .height(100.dp)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(10.dp))

            CalendarSample3(startDateText, endDateText)

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val validationMessage = validateProjectInput(projectName.value, descripcion)
                        if (validationMessage.isNotEmpty()) {
                            // En caso de un mensaje de validación, actualiza los estados del diálogo de error
                            errorDialogVisible = true
                            errorMessage = validationMessage
                            return@Button
                        }
                        val numTask = 0
                        val numParticipants = 0
                        val numDocuments = 0
                        val estado = "Iniciador"

                        val newProject = Project(
                            null,
                            projectName.value.capitalize(Locale.getDefault()),
                            loggedInUserName, // creador o usuario
                            loggedInUserUID, // id
                            descripcion.capitalize(Locale.getDefault()),
                            startDateText.value,
                            endDateText.value,
                            numParticipants,
                            numTask,
                            numDocuments,
                            estado
                        )

                        val db = FirebaseFirestore.getInstance()
                        db.collection("Usuarios")
                            .document(loggedInUserUID)
                            .collection("Proyectos")
                            .whereEqualTo("projectName", newProject.projectName) // Aquí, 'nombre' es el campo en Firestore que contiene el nombre del proyecto
                            .get()
                            .addOnSuccessListener { documents ->
                                if (documents.isEmpty()) { // Aquí se llama a la función isEmpty()
                                    // No existe un proyecto con el mismo nombre, así que podemos crear el nuevo proyecto
                                    db.collection("Usuarios")
                                        .document(loggedInUserUID)
                                        .collection("Proyectos")
                                        .add(newProject)
                                        .addOnSuccessListener { documentReference ->
                                            val projectID = documentReference.id
                                            // Aquí, `projectID` es el ID del nuevo proyecto.
                                            // Proyecto guardado exitosamente
                                            projectName.value = ""
                                            startDate.value = ""
                                            endDate.value = ""
                                            isAddProjectDialogOpen.value = false
                                        }
                                        .addOnFailureListener { e ->
                                            // Error al guardar el proyecto
                                        }
                                } else {
                                    // Existe un proyecto con el mismo nombre, muestra un mensaje de error
                                    errorDialogVisible = true
                                    errorMessage = "Ya existe un proyecto con este nombre."
                                }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(myOrange, contentColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Guardar")
                }



                Button(
                    onClick = { isAddProjectDialogOpen.value = false },
                    colors = ButtonDefaults.buttonColors(myOrange, contentColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar")
                }
                if (errorDialogVisible) {
                    AlertDialog(
                        onDismissRequest = {
                            errorDialogVisible = false
                        },
                        title = { Text("Error en el registro del proyecto") },
                        text = { Text(errorMessage) },
                        confirmButton = {
                            Button(onClick = {
                                errorDialogVisible = false
                            }) {
                                Text("Cerrar")
                            }
                        }
                    )
                }
            }
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
    val timeBoundary = LocalDate.now().let { now -> now.minusYears(2)..now }
    val selectedRange = remember {
        val default = LocalDate.now().minusYears(2).let { time -> time.plusDays(5)..time.plusDays(8) }
        mutableStateOf(default.toRange())
    }

    val showDialog = remember { mutableStateOf(false) }

    Column {
        TextButton(onClick = { showDialog.value = true }) {
            Icon(
                Icons.Filled.CalendarToday,
                contentDescription = stringResource(R.string.calendar_icon),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Seleccionar fechas",
                color = Color.Black
            )
        }

        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                text = {
                    CalendarDialog(
                        state = rememberUseCaseState(visible = true),
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
                },
                confirmButton = {},
                dismissButton = {}
            )
        }

        Text("Fecha de inicio: ${startDateText.value}")
        Text("Fecha de fin: ${endDateText.value}")
        print("FECHAS"+ endDateText +"asdads" + endDateText)
    }
}

