package com.example.contrupro3.ui.theme.DocumentsScreens

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.contrupro3.R
import com.example.contrupro3.modelos.AuthRepository
import com.example.contrupro3.modelos.DocumentModel
import com.example.contrupro3.ui.theme.HamburgueerMenu
import com.example.contrupro3.ui.theme.myOrange
import com.example.contrupro3.ui.theme.myOrangehigh
import com.example.contrupro3.ui.theme.myOrangelow
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.util.Locale
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


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
    val viewModel: DocumentsScreenViewModel = viewModel()
    val documentsList = viewModel.documentsList
    authRepository.loadDocumentsFromFirebase(documentsList)
    val documentsSelectedToRemove = viewModel.documentsSelectedToRemove

    var filteredDocuments =
        remember(documentsList, selectedFilter, isFilterAscending, searchQuery) {
            derivedStateOf {
                var filteredList = documentsList.value.filter {
                    it.name?.contains(
                        searchQuery,
                        ignoreCase = true
                    ) == true
                }
                when (selectedFilter) {
                    "Nombre" -> {
                        filteredList = if (isFilterAscending) {
                            filteredList.sortedBy { it.name }
                        } else {
                            filteredList.sortedByDescending { it.name }
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
                if (documentsSelectedToRemove.size > 0) {
                    Row {
                        FloatingActionButton(
                            onClick = { viewModel.documentsSelectedToRemove.clear() },
                            containerColor = myOrangehigh
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Cancelar",
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        FloatingActionButton(
                            onClick = { viewModel.showDeleteDocumentsDialog.value = true },
                            containerColor = myOrangehigh
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar"
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
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
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
                        Text(
                            text = "No tienes documentos publicados.",
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }

                    1 -> {
                        Text(
                            text = "Tienes 1 documento creado.",
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }

                    else -> {
                        Text(
                            text = "Tienes ${documentsList.value.size} documentos creados.",
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }
                }
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(filteredDocuments.value) { document ->
                        DocumentCard(
                            document = document,
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
    HamburgueerMenu(navController = navController, authRepository = authRepository)
    if (isAddDocumentDialogOpen.value) {
        Dialog(onDismissRequest = { isAddDocumentDialogOpen.value = false }) {
            RegisterCardDocument(
                isAddDocumentDialogOpen,
                loggedInUserName,
                loggedInUserUID,
                documentsList
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DocumentCard(
    document: DocumentModel,
    navController: NavHostController,
    authRepository: AuthRepository,
    documentsList: MutableState<List<DocumentModel>>,
    userID: String
) {
    var showViewer by remember { mutableStateOf(false) }
    val viewModel: DocumentsScreenViewModel = viewModel()
    val documentsSelectedToRemove = viewModel.documentsSelectedToRemove
    val showDeleteDocumentsDialog = viewModel.showDeleteDocumentsDialog

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
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .wrapContentSize(Alignment.Center)
                .combinedClickable(
                    onClick = {
                        if (documentsSelectedToRemove.size > 0) {
                            if (!documentsSelectedToRemove.contains(document)) {
                                documentsSelectedToRemove.add(document)
                            } else documentsSelectedToRemove.remove(document)
                        } else {
                            navController.navigate("cardview_documents_screen/${userID}/${document.id}")
                        }
                    },
                    onLongClick = {
                        if (!documentsSelectedToRemove.contains(document)) {
                            documentsSelectedToRemove.add(document)
                        } else documentsSelectedToRemove.remove(document)
                    }
                )
        ) {
            if (showViewer) PdfViewerScreen(navController, document.fileUrl.toString())
            if (showDeleteDocumentsDialog.value === true) RemoveDocumentsSelected(userID = userID)

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
                            text = document.name.toString(),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    if(documentsSelectedToRemove.contains(document)) {
                        Box {
                            Icon(
                                Icons.Default.CheckBox,
                                contentDescription = null,
                                tint = myOrange,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    if(documentsSelectedToRemove.size > 0 && !documentsSelectedToRemove.contains(document)) {
                        Box {
                            Icon(
                                Icons.Default.CheckBoxOutlineBlank,
                                contentDescription = null,
                                tint = myOrange,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
                Image(
                    painter = rememberImagePainter(
                        data = document.previewUrl,
                        builder = {
                            placeholder(R.drawable.pdf_image)
                            error(R.drawable.error_document)
                        }
                    ), contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .size(300.dp)
                        .background(Color.White)
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterCardDocument(
    isAddDocumentDialogOpen: MutableState<Boolean>,
    loggedInUserName: String,
    loggedInUserUID: String,
    documentsFiltered: MutableState<List<DocumentModel>>
) {
    val documentName = remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var requiredVisible by remember { mutableStateOf(true) }
    var documentUriName by remember { mutableStateOf("") }
    var pdfUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var showLoadingSpinner by remember { mutableStateOf(false) }
    var enabledSaveButton by remember { mutableStateOf(false) }
    var nameRepliqued by remember { mutableStateOf(false) }
    var isLoadingSpinnerActived by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear Documento",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = myOrange
            )
            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = Color.Black, thickness = 1.dp)
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = documentName.value,
                onValueChange = {
                    documentName.value = it
                    nameRepliqued = documentsFiltered.value.find {
                        it.name?.trim().equals(documentName.value.trim(), ignoreCase = true)
                    } != null
                },
                label = { Text(text = "Nombre del documento") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color(0xA9D8D8D8),
                    unfocusedBorderColor = Color(0xA9D8D8D8),
                    backgroundColor = Color(0x79D8D8D8)
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                singleLine = true,
                maxLines = 1
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (documentName.value.length < 6) {
                    Text(
                        text = "* Requerido",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Light,
                            color = myOrange
                        ),
                        modifier = Modifier.offset(x = 20.dp)
                    )
                } else if (nameRepliqued === true) {
                    Text(
                        text = "* Nombre duplicado",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Light,
                            color = myOrange
                        ),
                        modifier = Modifier.offset(x = 20.dp)
                    )
                } else Text(text = " ")
                Text(
                    text = "${documentName.value.length}/30",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light,
                        color = if (documentName.value.length > 30) myOrange else Color.Black
                    ),
                    modifier = Modifier
                        .offset(x = -20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color(0xA9D8D8D8),
                    unfocusedBorderColor = Color(0xA9D8D8D8),
                    backgroundColor = Color(0x79D8D8D8)
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(100.dp)
            )
            Text(
                text = "${description.length}/200",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Light,
                    color = if (description.length > 200) myOrange else Color.Black
                ),
                modifier = Modifier
                    .offset(x = -20.dp)
                    .align(Alignment.End)
            )
            Spacer(modifier = Modifier.height(15.dp))
            val contentResolver = LocalContext.current.contentResolver
            val launcher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = {
                        pdfUri = it
                    }
                )
            Column(
                modifier = Modifier.padding(start = 10.dp, top = 0.dp, end = 10.dp, bottom = 2.dp)
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myOrangehigh
                    ),
                    onClick = { launcher.launch("application/pdf") },
                    enabled = !isLoadingSpinnerActived
                ) {
                    Text("Seleccionar archivo")
                }
                if (requiredVisible) {
                    Text(
                        text = "* Requerido",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Light,
                            color = myOrange
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                pdfUri?.let { uri ->
                    requiredVisible = false
                    val uriName = getFileName(uri, contentResolver)
                    documentUriName = uriName
                    if (documentName.value.length >= 6) enabledSaveButton = true

                    Text(
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.ExtraLight,
                            color = Color.Gray,
                            fontSize = 2.5.em
                        ),
                        text = "$uriName".substring(0, 25) + "...",
                        modifier = Modifier.offset(x = 15.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            if (showLoadingSpinner) {
                Text(
                    text = "Subiendo Archivo...",
                    style = MaterialTheme.typography.bodySmall.copy(color = myOrange)
                )
                Spacer(modifier = Modifier.height(10.dp))
                CircularProgressIndicator(
                    color = myOrange
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val scope = rememberCoroutineScope()

                if (!isLoadingSpinnerActived) {
                    Button(
                        enabled = documentName.value.length >= 6 && documentName.value.length <= 30 && documentUriName.length > 1 && nameRepliqued === false && description.length <= 200,
                        onClick = {
                            isLoadingSpinnerActived = true
                            showLoadingSpinner = true
                            scope.launch {
                                val docReferences = uploadToStorage(pdfUri!!, context)
                                if (docReferences.pdfRef !== "null" && docReferences.previewRef !== "null") {
                                    val newDoc = DocumentModel(
                                        null,
                                        documentName.value.lowercase(Locale.getDefault())
                                            .replaceFirstChar {
                                                if (it.isLowerCase()) it.titlecase(
                                                    Locale.getDefault()
                                                ) else it.toString()
                                            },
                                        loggedInUserName,
                                        loggedInUserUID,
                                        description,
                                        docReferences.pdfRef,
                                        docReferences.pdfDownloadUrl,
                                        docReferences.previewDownloadUrl,
                                        docReferences.previewRef,
                                        emptyList(),
                                        emptyList(),
                                        emptyList()
                                    )

                                    val db = FirebaseFirestore.getInstance()
                                    val collectionReference = db.collection("Usuarios")
                                        .document(loggedInUserUID)
                                        .collection("Documentos")

                                    collectionReference
                                        .add(newDoc)
                                        .addOnSuccessListener { documentReference ->
                                            val docUpdated = newDoc.copy(id = documentReference.id)
                                            collectionReference
                                                .document(documentReference.id)
                                                .set(docUpdated)
                                                .addOnSuccessListener {
                                                    documentName.value = ""
                                                    description = ""
                                                    showLoadingSpinner = false
                                                    isAddDocumentDialogOpen.value = false

                                                    Toast.makeText(
                                                        context,
                                                        "Archivo agregado correctamente",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                        }
                                } else {
                                    isLoadingSpinnerActived = false
                                    showLoadingSpinner = false
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
                        onClick = { isAddDocumentDialogOpen.value = false },
                        colors = ButtonDefaults.buttonColors(myOrange, contentColor = Color.White),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}

suspend fun uploadToStorage(
    uri: Uri,
    context: Context
): StorageReferences = suspendCancellableCoroutine { continuation ->
    val storage = Firebase.storage
    val storageRef = storage.reference
    val uniqueImageName = UUID.randomUUID()
    val pdfRef = storageRef.child("pdfs/$uniqueImageName.pdf")
    var previewByteArray: ByteArray? = null

    try {
        val pdfFileDescriptor: ParcelFileDescriptor? =
            context.contentResolver.openFileDescriptor(uri, "r")

        pdfFileDescriptor?.use { fileDescriptor ->
            val pdfRenderer = PdfRenderer(fileDescriptor)

            if (pdfRenderer.pageCount > 0) {
                val firstPage = pdfRenderer.openPage(0)
                val width = 400
                val height = 600
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                firstPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                firstPage.close()
                pdfRenderer.close()
                previewByteArray = bitmapToByteArray(bitmap)
            }
        }

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val byteArray = inputStream.readBytes()
            val uploadTask = pdfRef.putBytes(byteArray)

            uploadTask.addOnSuccessListener {
                pdfRef.downloadUrl.addOnSuccessListener { pdfDownloadURL ->
                    val previewRef = storageRef.child("previews/$uniqueImageName.pdf")

                    if (previewByteArray != null) {
                        previewRef.putBytes(previewByteArray!!)
                            .addOnSuccessListener { previewDownloadUri ->
                                previewRef.downloadUrl.addOnSuccessListener { downloadURL ->
                                    val storageReferences =
                                        StorageReferences(
                                            pdfRef.toString(),
                                            pdfDownloadURL.toString(),
                                            downloadURL.toString(),
                                            previewRef.toString()
                                        )
                                    continuation.resume(storageReferences)
                                }
                            }
                    }
                }
            }.addOnFailureListener { e ->
                val storageReferences = StorageReferences("null", "null", "null", "null")
                continuation.resume(storageReferences)
                Toast.makeText(
                    context,
                    "Carga Fallida",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        continuation.resumeWithException(e)
    }
}

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

fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

data class StorageReferences(
    val pdfRef: String,
    val pdfDownloadUrl: String,
    val previewDownloadUrl: String,
    val previewRef: String
)

@Composable
fun PdfViewerScreen(navController: NavHostController, pdfUrl: String) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == ComponentActivity.RESULT_OK) {
            // Handle any result data if needed
        }
    }

    DisposableEffect(Unit) {
        val encodedPdfUrl = URLEncoder.encode(pdfUrl, "UTF-8")
        val viewerUrl = "https://docs.google.com/viewer?url=$encodedPdfUrl"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(viewerUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        launcher.launch(intent)

        onDispose {}
    }
}

@Composable
fun RemoveDocumentsSelected(userID: String) {
    val viewModel: DocumentsScreenViewModel = viewModel()
    val documentsSelectedToRemove = viewModel.documentsSelectedToRemove
    val documentsList = viewModel.documentsList
    val context = LocalContext.current

    if (documentsSelectedToRemove.size > 0) {
        AlertDialog(
            onDismissRequest = { viewModel.showDeleteDocumentsDialog.value = false },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            viewModel.showDeleteDocumentsDialog.value = false
                            viewModel.documentsSelectedToRemove.clear()
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
                            val storage = Firebase.storage
                            val collectionRef =
                                db.collection("Usuarios").document(userID).collection("Documentos")

                            for (document in documentsSelectedToRemove) {
                                storage.getReferenceFromUrl("${document.fileRef}").delete()
                                storage.getReferenceFromUrl("${document.previewRef}").delete()
                                collectionRef.document("${document.id}").delete()
                            }
                            viewModel.documentsList.value = documentsList.value.filter { d -> !documentsSelectedToRemove.contains(d) }
                            viewModel.showDeleteDocumentsDialog.value = false
                            Toast.makeText(
                                context,
                                "Documentos removidos",
                                Toast.LENGTH_LONG
                            ).show()
                            viewModel.documentsSelectedToRemove.clear()
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
                            color = myOrange
                        )
                    )
                }
            },
            text = {
                Text(
                    text = "Los documentos no se podran volver a recuperar. ¿Esta seguro de esto?",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light)
                )
            }
        )
    }
}

class DocumentsScreenViewModel : ViewModel() {
    val documentsSelectedToRemove = mutableStateListOf<DocumentModel>()
    val showDeleteDocumentsDialog = mutableStateOf(false)
    val documentsList = mutableStateOf(emptyList<DocumentModel>())
}