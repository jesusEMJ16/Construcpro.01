package com.example.contrupro3.ui.theme

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.contrupro3.R
import com.example.contrupro3.modelos.AuthRepository
import com.example.contrupro3.modelos.DocumentModel
import com.example.contrupro3.modelos.Equipos
import com.example.contrupro3.modelos.Project
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardView_Documents(
    navController: NavHostController,
    authRepository: AuthRepository,
    userID: String,
    documentId: String
) {
    val DocumentList = remember { mutableStateOf<DocumentModel?>(null) }
    authRepository.loadDocument(documentId, DocumentList)
    val document: DocumentModel? = DocumentList.value
    val projectsList = remember { mutableStateOf(emptyList<Project>()) }
    authRepository.loadProjectsFromFirebase(projectsList)
    val viewModel: MyViewModel = viewModel()
    val action by viewModel.action
    val projectsEnabled by viewModel.proyectsEnabled
    val teamsEnabled by viewModel.teamsEnabled
    val currentProjectsFiltered = projectsList.value.filter { p -> document?.proyectsLinked!!.contains(p.id.toString()) }
    viewModel.currentProjects.value = currentProjectsFiltered

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = document?.name ?: "",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = document?.creatorName ?: "",
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
        },
        floatingActionButton = {
            CompositionLocalProvider(
                LocalContentColor provides colorResource(id = R.color.white)
            ) {
                if(projectsEnabled === true || teamsEnabled === true) {
                    Row() {
                        FloatingActionButton(
                            onClick = {
                                if(projectsEnabled) {
                                    viewModel.action.value = "remove proyect"
                                } else if(teamsEnabled) {
                                    viewModel.action.value = "remove team"
                                }
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
                                if(projectsEnabled) {
                                    viewModel.action.value = "add proyect"
                                } else if(teamsEnabled) {
                                    viewModel.action.value = "add team"
                                }
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {
            when(action) {
                "add proyect" -> {
                    Dialog(onDismissRequest = { viewModel.action.value = "" }) {
                        AddProyects(navController, authRepository, userID, document, projectsList)
                    }
                }
                "add team" -> {
                    Dialog(onDismissRequest = { viewModel.action.value = "" }) {
                        AddProjects(navController, authRepository, userID, document, projectsList)
                    }
                }
                "remove proyect" -> {
                    Dialog(onDismissRequest = { viewModel.action.value = "" }) {
                        RemoveProjects(navController, authRepository, userID, document, projectsList)
                    }
                }
                "remove team" -> {
                    Dialog(onDismissRequest = { viewModel.action.value = "" }) {
                        RemoveTeams(navController, authRepository, userID, document, projectsList)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .offset(y = 50.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(5.dp))
                if (document?.description?.length ?: 0 > 10) Text(
                    text = "${document?.description}",
                    modifier = Modifier.padding(horizontal = 30.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                InformationCard(navController, authRepository, userID, document, projectsList)
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
fun AddProjects(navController: NavHostController, authRepository: AuthRepository, userID: String, document: DocumentModel?, projectsList: MutableState<List<Project>>) {

}

@Composable
fun RemoveTeams(navController: NavHostController, authRepository: AuthRepository, userID: String, document: DocumentModel?, projectsList: MutableState<List<Project>>) {

}

@Composable
fun InformationCard(
    navController: NavHostController,
    authRepository: AuthRepository,
    userID: String,
    document: DocumentModel?,
    projectsList: MutableState<List<Project>>,
) {
    val viewModel: MyViewModel = viewModel()
    val nameEnabled by viewModel.nameEnabled
    val descriptionEnabled by viewModel.descriptionEnabled
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxWidth()) {
        androidx.compose.material3.Card(
            colors = CardDefaults.cardColors(
                containerColor = myOrangelow
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Información del documento",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = myOrange
                )
                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Nombre del documento",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .offset(x = 5.dp)
                        .padding(5.dp)
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    maxLines = 1,
                    placeholder = { Text(text = "${document?.name}") },
                    singleLine = true,
                    enabled = nameEnabled,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        backgroundColor = if (nameEnabled) Color(0x41FF7239) else Color(0x79D8D8D8),
                        focusedBorderColor = Color.Transparent,
                        cursorColor = myOrange,
                        disabledBorderColor = Color.Transparent,
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.nameEnabled.value = !viewModel.nameEnabled.value
                            viewModel.descriptionEnabled.value = false
                            viewModel.teamsEnabled.value = false
                            viewModel.proyectsEnabled.value = false
                        }) {
                            if (nameEnabled) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Editar nombre del documento"
                                )
                            } else Icon(
                                Icons.Default.Lock,
                                contentDescription = "Editar nombre del documento"
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Descripción del documento",
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
                        if(document?.description != null && document?.description.length > 0) {
                            Text("${document?.description}")
                        } else {
                            Text("No hay descripción establecida.")
                        }
                    },
                    singleLine = true,
                    enabled = descriptionEnabled,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        backgroundColor = if (descriptionEnabled) Color(0x41FF7239) else Color(0x79D8D8D8),
                        focusedBorderColor = Color.Transparent,
                        cursorColor = myOrange,
                        disabledBorderColor = Color.Transparent,
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.descriptionEnabled.value = !viewModel.descriptionEnabled.value
                            viewModel.nameEnabled.value = false
                            viewModel.teamsEnabled.value = false
                            viewModel.proyectsEnabled.value = false
                        }) {
                            if (descriptionEnabled) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Editar nombre del documento"
                                )
                            } else Icon(
                                Icons.Default.Lock,
                                contentDescription = "Editar nombre del documento"
                            )
                        }
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    IntegrationsCard(navController, authRepository, userID, document, projectsList)
}

@Composable
fun IntegrationsCard(
    navController: NavHostController,
    authRepository: AuthRepository,
    userID: String,
    document: DocumentModel?,
    projectsList: MutableState<List<Project>>
) {
    val viewModel: MyViewModel = viewModel()
    val proyectsEnabled by viewModel.proyectsEnabled
    val teamsEnabled by viewModel.teamsEnabled
    val currentProjects by viewModel.currentProjects
    val projectsName = currentProjects.map { p -> p.projectName }.sortedBy { a -> a }.joinToString("\n")
    val currentTeams by viewModel.currentTeams

    Box(modifier = Modifier.fillMaxWidth()) {
        androidx.compose.material3.Card(
            colors = CardDefaults.cardColors(
                containerColor = myOrangelow
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Integraciones al documento",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = myOrange
                )
                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Proyectos agregados",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .offset(x = 5.dp)
                        .padding(5.dp)
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        if(currentProjects.size === 0) {
                            Text(text = "No hay proyectos enlazados.")
                        } else {
                            Text(
                                text = projectsName
                            )
                        }
                    },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        backgroundColor = if (proyectsEnabled) Color(0x41FF7239) else Color(0x79D8D8D8),
                        focusedBorderColor = Color.Transparent,
                        cursorColor = myOrange,
                        disabledBorderColor = Color.Transparent,
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.proyectsEnabled.value = !proyectsEnabled
                            viewModel.nameEnabled.value = false
                            viewModel.teamsEnabled.value = false
                            viewModel.descriptionEnabled.value = false
                        }) {
                            if (proyectsEnabled) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Editar proyectos enlazados"
                                )
                            } else Icon(
                                Icons.Default.Lock,
                                contentDescription = "Editar proyectos enlazados"
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Equipos agregados",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .offset(x = 5.dp)
                        .padding(5.dp)
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(text = "No hay equipos agregados.")
                        /*if(currentProjects.size === 0) {
                        } else {
                            Text(
                                text = projectsName
                            )
                        }*/
                    },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        backgroundColor = if (teamsEnabled) Color(0x41FF7239) else Color(0x79D8D8D8),
                        focusedBorderColor = Color.Transparent,
                        cursorColor = myOrange,
                        disabledBorderColor = Color.Transparent,
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.teamsEnabled.value = !teamsEnabled
                            viewModel.proyectsEnabled.value = false
                            viewModel.nameEnabled.value = false
                            viewModel.descriptionEnabled.value = false
                        }) {
                            if (teamsEnabled) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Editar equipos enlazados"
                                )
                            } else Icon(
                                Icons.Default.Lock,
                                contentDescription = "Editar equipos enlazados"
                            )
                        }
                    }
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun AddProyects(
    navController: NavHostController,
    authRepository: AuthRepository,
    userId: String,
    document: DocumentModel?,
    projectsList: MutableState<List<Project>>
) {
    val DocumentList = remember { mutableStateOf<DocumentModel?>(null) }
    authRepository.loadDocument(document?.id.toString(), DocumentList)
    val document: DocumentModel? = DocumentList.value
    val proyectsSelected = remember { mutableStateListOf<String>() }
    val viewModel: MyViewModel = viewModel()
    val context = LocalContext.current
    val currentProjects by viewModel.currentProjects

    Card(
        modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Añadir Proyectos",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = myOrange
            )
            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = Color.Black, thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Selecciona los proyectos que estaran enlazados con este documento",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(15.dp))
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
                        Text(text = "${proyect.projectName}", modifier = Modifier.offset(x = 10.dp), style = MaterialTheme.typography.bodyMedium)
                        Checkbox(
                            enabled = if(document?.proyectsLinked?.toList()?.contains(proyect.id.toString()) == true) false else true,
                            checked = checked,
                            onCheckedChange = { newChange ->
                                if(!proyectsSelected.contains(proyect.id.toString())) proyectsSelected.add(proyect.id.toString()) else proyectsSelected.remove(proyect.id.toString())
                                checked = newChange
                            },
                            colors = androidx.compose.material3.CheckboxDefaults.colors(
                                uncheckedColor = myOrange,
                                checkedColor = myOrangehigh,
                                checkmarkColor = Color.White
                            )
                        )
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
                        for(d in document?.proyectsLinked!!) {
                            proyectsSelected.add(d)
                        }
                        val db = FirebaseFirestore.getInstance()
                        val collection = db.collection("Usuarios")
                            .document(userId)
                            .collection("Documentos")
                            .document(document?.id.toString())

                        try {
                            collection
                                .update("proyectsLinked", proyectsSelected)
                                .addOnSuccessListener {
                                    val projectsFiltered = projectsList.value.filter { p -> projectsNameSelected.contains(p.id.toString()) && !currentProjects.contains(p) }
                                    val newList = currentProjects + projectsFiltered
                                    viewModel.currentProjects.value = newList
                                    proyectsSelected.clear()
                                    viewModel.action.value = ""
                                    Toast.makeText(
                                        context,
                                        "Proyectos enlazados correctamente",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Ocurrio un problema al actualizar el documento.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    enabled = proyectsSelected.size >= 1,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myOrangehigh,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp, end = 5.dp, bottom = 0.dp)
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
    document: DocumentModel?,
    projectsList: MutableState<List<Project>>
) {
    val DocumentList = remember { mutableStateOf<DocumentModel?>(null) }
    authRepository.loadDocument(document?.id.toString(), DocumentList)
    val document: DocumentModel? = DocumentList.value
    val proyectsSelected = remember { mutableStateListOf<String>() }
    val viewModel: MyViewModel = viewModel()
    val context = LocalContext.current
    val proyectsCurrent = viewModel.currentProjects

    Card(
        modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
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
            Text(
                text = "Selecciona los proyectos que seran removidos de este documento",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(15.dp))
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
                        Text(text = "${proyect.projectName}", modifier = Modifier.offset(x = 10.dp), style = MaterialTheme.typography.bodyMedium)
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { newChange ->
                                if(!proyectsSelected.contains(proyect.id.toString())) proyectsSelected.add(proyect.id.toString()) else proyectsSelected.remove(proyect.id.toString())
                                checked = newChange
                            },
                            colors = androidx.compose.material3.CheckboxDefaults.colors(
                                uncheckedColor = myOrange,
                                checkedColor = myOrangehigh,
                                checkmarkColor = Color.White
                            )
                        )
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
                            .collection("Documentos")
                            .document(document?.id.toString())

                        try {
                            collection
                                .update("proyectsLinked", newList.map {
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
                                "Ocurrio un problema al actualizar el documento.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    enabled = proyectsSelected.size >= 1,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myOrangehigh,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp, end = 5.dp, bottom = 0.dp)
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

class MyViewModel : ViewModel() {
    val nameEnabled = mutableStateOf(false)
    val descriptionEnabled = mutableStateOf(false)
    val proyectsEnabled = mutableStateOf(false)
    val teamsEnabled = mutableStateOf(false)
    val action = mutableStateOf("")
    val currentProjects = mutableStateOf(emptyList<Project>())
    val currentTeams = mutableStateOf(emptyList<Equipos>())
}