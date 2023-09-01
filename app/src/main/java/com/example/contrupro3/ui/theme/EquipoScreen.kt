package com.example.contrupro3.ui.theme


import androidx.compose.foundation.layout.*

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.contrupro3.R
import com.example.contrupro3.modelos.AuthRepository
import com.example.contrupro3.modelos.Equipos
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun TeamCreationScreen(navController: NavController, authRepository: AuthRepository,userID: String) {

    var selectedFilter by remember { mutableStateOf("Nombre") }
    var isFilterAscending by remember { mutableStateOf(false) }
    var isFilterMenuOpen by remember { mutableStateOf(false) }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val loggedInUserUID: String by authRepository.getLoggedInUserUID().observeAsState("")
    val equiposList = remember { mutableStateOf(emptyList<Equipos>()) }
    val isAddTeamDialogOpen = remember { mutableStateOf(false) }
    val loggedInUserName: String by authRepository.getLoggedInUserName().observeAsState("")
    val equipos = remember { mutableStateListOf<Equipos>() }

    authRepository.loadEquiposFromFirebase(equiposList)

    val filteredEquipos = remember(equiposList, selectedFilter, isFilterAscending, searchQuery) {
        derivedStateOf {
            var filteredList = equiposList.value.filter { it.nombreEquipo?.contains(searchQuery, ignoreCase = true) == true }
            when (selectedFilter) {
                "Nombre" -> {
                    filteredList = if (isFilterAscending) {
                        filteredList.sortedBy { it.nombreEquipo }
                    } else {
                        filteredList.sortedByDescending { it.nombreEquipo }
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
                            contentDescription = "Buscar Equipos",
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
                        onClick = { isAddTeamDialogOpen.value = true },
                        containerColor = myOrangehigh
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Agregar Equipo"
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
                        text = "Equipos",
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
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(filteredEquipos.value) { equipo ->
                                EquipoCard(
                                    equipo = equipo, // Pasar el equipo individual en lugar de equipos
                                    navController = navController,
                                    authRepository = authRepository,
                                    equiposList = equiposList,
                                    userID = userID,
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
    if (isAddTeamDialogOpen.value) {
        Dialog(onDismissRequest = { isAddTeamDialogOpen.value = false }) {
            RegisterCardTeam(isAddTeamDialogOpen,loggedInUserName, loggedInUserUID, equipos)
        }
    }
    if (isSearchExpanded) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { newValue ->
                    searchQuery = newValue
                    if (newValue.isEmpty()) {
                        authRepository.loadEquiposFromFirebase(equiposList)
                    }
                },
                label = { Text("Buscar Equipos") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 730.dp)
                    .navigationBarsPadding()
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EquipoCard(equipo: Equipos, navController: NavController,userID: String, authRepository: AuthRepository, equiposList: MutableState<List<Equipos>>) {
    val equipoID = equipo.id

    val showDeleteDialog = remember { mutableStateOf(false) }
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
                .combinedClickable(
                    onClick = {
                        navController.navigate("cardteam_screen/${equipoID}")
                    },
                    onLongClick = { showDeleteDialog.value = true }
                ),
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
                        text = equipo.nombreEquipo.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(Alignment.Center)
                    )
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
                        text = equipo.creatorName.toString(),
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
                        text = equipo.members.size.toString(), // Cantidad de personas dentro del equipo
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            fun deleteEquipo(userID: String, equipoID: String) {
                val db = FirebaseFirestore.getInstance()
                Log.d("Delete Equipo", "Deleting equipo with User ID: $userID, Equipo ID: $equipoID")

                db.collection("Usuarios")
                    .document(userID)
                    .collection("Equipos")
                    .document(equipoID)
                    .delete()
                    .addOnSuccessListener {
                        Log.d("Delete Equipo", "DocumentSnapshot successfully deleted!")
                        // Actualizar la lista de equipos
                        equiposList.value = equiposList.value.filter { it.id != equipoID }
                        // Cargar nuevamente la lista de equipos desde Firebase
                        authRepository.loadEquiposFromFirebase(equiposList)
                    }
                    .addOnFailureListener { e ->
                        Log.w("Delete Equipo", "Error deleting document", e)
                    }
            }

            if (showDeleteDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog.value = false },
                    title = { Text(text = "Confirmar eliminación") },
                    text = { Text(text = "¿Estás seguro de que quieres eliminar este equipo?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (equipoID != null) {
                                    // Eliminar el equipo
                                    deleteEquipo(userID, equipoID)
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
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterCardTeam(isAddTeamDialogOpen : MutableState<Boolean>,loggedInUserName: String,
                 loggedInUserUID: String,
                 Equipo: MutableList<Equipos>) {
    fun validateTeamInput(teamName: String, descripcion: String): String {
        // Comprueba si los campos están vacíos
        if (teamName.isEmpty() || descripcion.isEmpty()) {
            return "Nombre de equipo y descripcion deben ser llenados."
        }

        return ""
    }

    var errorDialogVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val nombreEquipo = remember { mutableStateOf("") }
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
                text = "Nuevo Equipo",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(10.dp))

            androidx.compose.material.OutlinedTextField(
                value = nombreEquipo.value,
                onValueChange = { nombreEquipo.value = it },
                label = { Text("Nombre del equipo") },
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

            androidx.compose.material.OutlinedTextField(
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

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Agregar personal")

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val validationMessage =
                            validateTeamInput(nombreEquipo.value, descripcion)
                        if (validationMessage.isNotEmpty()) {
                            // En caso de un mensaje de validación, actualiza los estados del diálogo de error
                            errorDialogVisible = true
                            errorMessage = validationMessage
                            return@Button
                        }

                        val newTeam = Equipos(
                            null,
                            nombreEquipo.value.capitalize(Locale.getDefault()),
                            loggedInUserName,
                            loggedInUserUID,
                            descripcion.capitalize(Locale.getDefault()),
                            emptyList()
                        )

                        val db = FirebaseFirestore.getInstance()
                        db.collection("Usuarios")
                            .document(loggedInUserUID)
                            .collection("Equipos")
                            .whereEqualTo("nombreEquipo", newTeam.nombreEquipo)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (documents.isEmpty()) {
                                    // No existe un equipo con el mismo nombre, así que podemos crear el nuevo equipo
                                    db.collection("Usuarios")
                                        .document(loggedInUserUID)
                                        .collection("Equipos")
                                        .add(newTeam)
                                        .addOnSuccessListener { documentReference ->
                                            val teamID = documentReference.id
                                            // Agregar el nuevo miembro al equipo
                                            addMemberToTeam(teamID, loggedInUserUID)
                                            nombreEquipo.value = ""
                                            descripcion = ""

                                            isAddTeamDialogOpen.value = false
                                        }
                                        .addOnFailureListener { e ->
                                            // Error al guardar el equipo
                                        }
                                } else {
                                    // Existe un equipo con el mismo nombre, muestra un mensaje de error
                                    errorDialogVisible = true
                                    errorMessage = "Ya existe un equipo con este nombre."
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
                        isAddTeamDialogOpen.value = false
                        Log.d("CancelarButton", "Botón 'Cancelar' presionado. Valor de isAddTeamDialogOpen: ${isAddTeamDialogOpen.value}")
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
                        title = { Text("Error en el registro del equipo") },
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

// Función para agregar un nuevo miembro al equipo
fun addMemberToTeam(teamId: String, memberId: String) {
    val db = FirebaseFirestore.getInstance()
    // Primero, verifica si el correo electrónico del nuevo miembro existe en tu sistema
    // Puedes usar Firebase Authentication para realizar esta verificación
    // Por ejemplo, podrías tener una colección "Usuarios" donde almacenas información de usuarios
    val usersCollection = db.collection("Usuarios")
    val query = usersCollection.whereEqualTo("correo", memberId)

    query.get().addOnSuccessListener { querySnapshot ->
        if (!querySnapshot.isEmpty) {
            // El correo electrónico del nuevo miembro existe en tu sistema
            // Ahora, puedes agregar al nuevo miembro al equipo

            // Primero, obtén la referencia al documento del equipo
            val teamRef = db.collection("Equipos").document(teamId)

            // Luego, actualiza el campo de matriz "members" del equipo con el nuevo miembro
            teamRef.update("members", FieldValue.arrayUnion(memberId))
                .addOnSuccessListener {
                    // El nuevo miembro se agregó exitosamente al equipo
                    // Actualiza la vista del equipo para mostrar al nuevo miembro
                    // Puedes usar un estado mutable en Jetpack Compose para mantener la lista de miembros del equipo
                    // y agregar el nuevo miembro al estado mutable
                }
                .addOnFailureListener { e ->
                    // Hubo un error al agregar el nuevo miembro al equipo
                }
        } else {
            // El correo electrónico del nuevo miembro no existe en tu sistema
            // Puedes mostrar un mensaje de error al usuario
        }
    }
}
