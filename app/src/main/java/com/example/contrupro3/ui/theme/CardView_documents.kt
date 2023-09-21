package com.example.contrupro3.ui.theme

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.InteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrackChanges
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.contrupro3.R
import com.example.contrupro3.modelos.AuthRepository
import com.example.contrupro3.modelos.DocumentModel
import com.example.contrupro3.modelos.Project
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
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
    val proyectsEnabled by viewModel.proyectsEnabled
    var showAlert by remember { mutableStateOf(false) }
    var action by remember { mutableStateOf("") }

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
                Row {
                    if(proyectsEnabled) {
                        FloatingActionButton(
                            onClick = { showAlert = true },
                            containerColor = myOrangehigh
                        ) {
                            androidx.compose.material.Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar proyecto del documento",
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        FloatingActionButton(
                            onClick = {
                                action = "add proyect"
                            },
                            containerColor = myOrangehigh
                        ) {
                            androidx.compose.material.Icon(
                                Icons.Default.Add,
                                contentDescription = "Añadir proyecto al documento"
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
                    Dialog(onDismissRequest = { action = "" }) {
                        AddProyects(navController, authRepository, document, projectsList)
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
                        backgroundColor = if (nameEnabled) Color(0x79D8D8D8) else Color(0x5EFDD0D0),
                        focusedBorderColor = Color.Transparent,
                        cursorColor = myOrange,
                        disabledBorderColor = Color.Transparent,
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.nameEnabled.value = !viewModel.nameEnabled.value
                            viewModel.descriptionEnabled.value = false
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
                    placeholder = { Text(text = "${document?.description}") },
                    singleLine = true,
                    enabled = descriptionEnabled,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        backgroundColor = if (descriptionEnabled) Color(0x79D8D8D8) else Color(
                            0x5EFDD0D0
                        ),
                        focusedBorderColor = Color.Transparent,
                        cursorColor = myOrange,
                        disabledBorderColor = Color.Transparent,
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.descriptionEnabled.value = !viewModel.descriptionEnabled.value
                            viewModel.nameEnabled.value = false
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
                    text = "Proyectos enlazados",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .offset(x = 5.dp)
                        .padding(5.dp)
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        if(document?.proyectsLinked?.size === 0) Text(text = "No hay proyectos enlazados.")
                    },
                    enabled = false,
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
                            viewModel.proyectsEnabled.value = !viewModel.proyectsEnabled.value
                            viewModel.nameEnabled.value = false
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
            }
        }
    }
}

@Composable
fun AddProyects(
    navController: NavHostController,
    authRepository: AuthRepository,
    document: DocumentModel?,
    projectsList: MutableState<List<Project>>
) {
    var proyectName by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Seleccionar Proyecto",
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
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)) {
                items(projectsList.value) { proyect ->
                    var checked by remember { mutableStateOf(false) }
                    var isPressed by remember { mutableStateOf(false) }
                    val scope = rememberCoroutineScope()

                    Divider(color = Color.LightGray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if(isPressed) Color.LightGray else Color.White)
                            .clickable(
                                onClick = {
                                    isPressed = true
                                    checked = !checked
                                    scope.launch {
                                        delay(100)
                                        isPressed = false
                                    }
                                }
                            )
                            .indication(
                                interactionSource = MutableInteractionSource(),
                                indication = LocalIndication.current
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "${proyect.projectName}", modifier = Modifier.offset(x = 10.dp), style = MaterialTheme.typography.bodyMedium)
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { newChange ->
                                checked = newChange
                            },
                            colors = androidx.compose.material3.CheckboxDefaults.colors(
                                uncheckedColor = myOrange,
                                checkedColor = myOrangehigh,
                                checkmarkColor = Color.White
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}

class MyViewModel : ViewModel() {
    val nameEnabled = mutableStateOf(false)
    val descriptionEnabled = mutableStateOf(false)
    val proyectsEnabled = mutableStateOf(true)
}