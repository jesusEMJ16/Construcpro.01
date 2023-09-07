package com.example.contrupro3.ui.theme

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.contrupro3.R
import com.example.contrupro3.modelos.AuthRepository
import com.example.contrupro3.modelos.DocumentModel
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DocumentsScreen(
    navController: NavHostController,
    authRepository: AuthRepository,
    userID: String
) {
    var selectedFilter by remember { mutableStateOf("Nombre") }
    var isFilterAscending by remember { mutableStateOf(false) }
    var isFilterMenuOpen by remember { mutableStateOf(false) }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val isAddDocumentDialogOpen = remember { mutableStateOf(false) }
    val loggedInUserUID: String by authRepository.getLoggedInUserUID().observeAsState("")
    val loggedInUserName: String by authRepository.getLoggedInUserName().observeAsState("")
    val documents = remember { mutableStateListOf<DocumentModel>() }
    val documentsList = remember { mutableStateOf(emptyList<DocumentModel>()) }
    authRepository.loadDocumentsFromFirebase(documentsList)

    val filteredDocuments =
        remember(documentsList, selectedFilter, isFilterAscending, searchQuery) {
            derivedStateOf {
                var filteredList = documentsList.value.filter {
                    it.docName?.contains(
                        searchQuery,
                        ignoreCase = true
                    ) == true
                }
                when (selectedFilter) {
                    "Nombre" -> {
                        filteredList = if (isFilterAscending) {
                            filteredList.sortedBy { it.docName }
                        } else {
                            filteredList.sortedByDescending { it.docName }
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
                            if (!isSearchExpanded) {
                                searchQuery = ""
                            }
                        },
                        containerColor = myOrangehigh
                    ) {
                        Icon(
                            if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "Buscar Documento",
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
                        DropdownMenuItem(
                            onClick = {
                                selectedFilter = "Nombre"
                                isFilterMenuOpen = false
                                isFilterAscending = !isFilterAscending
                            }
                        ) {
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
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    FloatingActionButton(
                        onClick = { isAddDocumentDialogOpen.value = true },
                        containerColor = myOrangehigh
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Agregar Documento"
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
                        text = "Planos y Documentación",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Divider(color = Color.LightGray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(5.dp))
                    when (documentsList.value.size) {
                        0 -> {
                            Text(text = "No tienes documentos publicados.")
                        }

                        1 -> {
                            Text(text = "Tienes 1 documento creado.")
                        }

                        else -> {
                            Text(text = "Tienes ${documentsList.value.size} documentos creados.")
                        }
                    }
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(filteredDocuments.value) { document ->
                            DocumentCard(
                                document = document, // Pasar el documento individual en lugar de equipos
                                navController = navController,
                                authRepository = authRepository,
                                documentsList = documentsList,
                                userID = userID
                            )
                            Spacer(Modifier.height(15.dp))
                        }
                    }
                }
            }
        }
    )
    HamburgueerMenu(navController = navController, authRepository = authRepository)
    if (isAddDocumentDialogOpen.value) {
        Dialog(onDismissRequest = { isAddDocumentDialogOpen.value = false }) {
            RegisterCardDocument(
                isAddDocumentDialogOpen,
                loggedInUserName,
                loggedInUserUID,
                documents
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DocumentCard(
    document: DocumentModel,
    navController: NavHostController,
    authRepository: AuthRepository,
    documentsList: MutableState<List<DocumentModel>>,
    userID: String
) {
    val documentId = document.id
    var showDeleteDialog by remember { mutableStateOf(false) }

    Spacer(Modifier.height(15.dp))
    Box(
        modifier = Modifier
            .fillMaxSize(),
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
                    navController.navigate("cardDocument_screen/${userID}/${documentId}")
                }, onLongClick = { showDeleteDialog = true }),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(15.dp),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = document.docName.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
                Image(
                    painter = painterResource(id = R.drawable.no_image), contentDescription = null,
                    alignment = Alignment.TopCenter,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }

    fun deleteDocument(userID: String, documentId: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("Usuarios")
            .document(userID)
            .collection("Documentos")
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                // Actualizar la lista de equipos
                documentsList.value = documentsList.value.filter { it.id != documentId }
                // Cargar nuevamente la lista de equipos desde Firebase
                authRepository.loadDocumentsFromFirebase(documentsList)
            }
            .addOnFailureListener { e -> }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Advertencia",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = { Text(text = "¿Esta seguro de que desea eliminar este documento?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        deleteDocument(userID, documentId.toString())
                    },
                    modifier = Modifier
                        .width(100.dp)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myOrangehigh,
                        contentColor = Color.White
                    )
                ) {
                    Text("Si")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                    },
                    modifier = Modifier
                        .width(100.dp)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myOrangehigh,
                        contentColor = Color.White
                    )
                ) {
                    Text("No")
                }
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterCardDocument(
    isAddDocumentDialogOpen: MutableState<Boolean>,
    loggedInUserName: String,
    loggedInUserUID: String,
    document: MutableList<DocumentModel>
) {
    var errorDialogVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val documentName = remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var requiredVisible by remember { mutableStateOf(true) }
    var documentUriName by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Documento Nuevo",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = documentName.value,
                onValueChange = { documentName.value = it },
                label = { Text("Nombre del documento") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .shadow(5.dp)
                    .background(Color.White),
                singleLine = true,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
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
            Spacer(modifier = Modifier.height(20.dp))

            val contentResolver = LocalContext.current.contentResolver
            val launcher =
                rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                    selectedFileUri = uri
                }

            if (requiredVisible) {
                Text(
                    text = "* Requerido",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light),
                    color = Color.Red,
                    fontSize = 3.em,
                    modifier = Modifier
                )
            }

            Column(
                modifier = Modifier.padding(start = 10.dp, top = 1.dp, end = 10.dp, bottom = 2.dp)
            ) {
                Button(
                    onClick = { launcher.launch("application/pdf") }
                ) {
                    Text("Seleccionar archivo")
                }

                selectedFileUri?.let { uri ->
                    requiredVisible = false
                    val uriName = getFileName(uri, contentResolver)
                    documentUriName = uriName
                    Text(
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.ExtraLight,
                            color = Color.Gray,
                            fontSize = 2.5.em
                        ),
                        text = "$uriName".substring(0, 25) + "..."
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val validationMessage =
                            validateDocumentInput(documentName.value, documentUriName)
                        if (validationMessage.isNotEmpty()) {
                            errorDialogVisible = true
                            errorMessage = validationMessage
                            return@Button
                        }

                        val newDoc = DocumentModel(
                            null,
                            documentName.value.toLowerCase().capitalize(),
                            loggedInUserName,
                            loggedInUserUID,
                            description,
                            emptyList(),
                            emptyList()
                        )

                        val db = FirebaseFirestore.getInstance()
                        db.collection("Usuarios")
                            .document(loggedInUserUID)
                            .collection("Documentos")
                            .whereEqualTo("docName", newDoc.docName)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (documents.isEmpty) {
                                    db.collection("Usuarios")
                                        .document(loggedInUserUID)
                                        .collection("Documentos")
                                        .add(newDoc)
                                        .addOnSuccessListener { documentReference ->
                                            val docID = documentReference.id
                                            documentName.value = ""
                                            description = ""

                                            isAddDocumentDialogOpen.value = false
                                        }
                                        .addOnFailureListener { e -> }
                                } else {
                                    errorDialogVisible = true
                                    errorMessage = "Ya existe un documento con este nombre."
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
                    onClick = {
                        isAddDocumentDialogOpen.value = false
                    },
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
                        title = {
                            Text(
                                text = "Error De Registro",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        },
                        text = { Text(errorMessage) },
                        confirmButton = {
                            Button(
                                onClick = {
                                    errorDialogVisible = false
                                },
                                modifier = Modifier
                                    .padding(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = myOrangehigh,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Aceptar")
                            }
                        }
                    )
                }
            }
        }
    }
}

fun validateDocumentInput(
    documentName: String,
    uriName: String
): String {
    if (documentName.isEmpty()) {
        return "Se requiere un nombre para el documento."
    } else if (documentName.length > 30) {
        return "El nombre del archivo no puede superar mas de 30 caracteres."
    } else if (documentName.length < 6) {
        return "El nombre del archivo no puede tener menos de 6 caracteres."
    } else if (uriName.isEmpty()) return "Se requiere subir un archivo pdf."

    return ""
}

/*fun addTeamToDocument(docID: String, memberId: String) {
    val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("Usuarios")
    val query = usersCollection.whereEqualTo("correo", memberId)

    query.get().addOnSuccessListener { querySnapshot ->
        if (!querySnapshot.isEmpty) {
            val docRef = db.collection("Documentos").document(docID)

            docRef.update("members", FieldValue.arrayUnion(memberId))
                .addOnSuccessListener {
                }
                .addOnFailureListener { e ->
                }
        } else {
        }
    }
}*/

@SuppressLint("Range")
private fun getFileName(uri: Uri, contentResolver: ContentResolver): String {
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            return displayName
        }
    }
    return "Archivo Desconocido"
}