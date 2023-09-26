package com.example.contrupro3.ui.theme


import androidx.compose.foundation.layout.*

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.OutlinedTextField
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.contrupro3.R
import com.example.contrupro3.modelos.AuthRepository
import com.example.contrupro3.modelos.Equipos
import com.example.contrupro3.modelos.Project
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.Locale
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun TeamCreationScreen(navController: NavController, authRepository: AuthRepository, userID: String) {
    var selectedFilter by remember { mutableStateOf("Nombre") }
    var isFilterAscending by remember { mutableStateOf(false) }
    var isFilterMenuOpen by remember { mutableStateOf(false) }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val loggedInUserUID: String by authRepository.getLoggedInUserUID().observeAsState("")
    val teamsList = remember { mutableStateOf(emptyList<Equipos>()) }
    val isAddTeamDialogOpen = remember { mutableStateOf(false) }
    val loggedInUserName: String by authRepository.getLoggedInUserName().observeAsState("")

    authRepository.loadEquiposFromFirebase(teamsList)
    val filteredEquipos = remember(teamsList, selectedFilter, isFilterAscending, searchQuery) {
        derivedStateOf {
            var filteredList = teamsList.value.filter { it.name?.contains(searchQuery, ignoreCase = true) == true }
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
                        text = "Proyectos",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Divider(color = Color.LightGray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(5.dp))
                    when (filteredEquipos.value.size) {
                        0 -> {
                            Text(
                                text = "No tienes equipos creados.",
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }

                        1 -> {
                            Text(
                                text = "Tienes 1 equipo creado.",
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }

                        else -> {
                            Text(
                                text = "Tienes ${filteredEquipos.value.size} equipos creados.",
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }
                    }
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(filteredEquipos.value) { equipo ->
                            EquipoCard(
                                team = equipo,
                                navController = navController,
                                authRepository = authRepository,
                                equiposList = teamsList,
                                userID = userID,
                            )
                            Spacer(Modifier.height(15.dp))
                        }
                    }
                }
            }
        }
    )

    HamburgueerMenu(navController = navController, authRepository = authRepository)
    if (isAddTeamDialogOpen.value) {
        Dialog(onDismissRequest = { isAddTeamDialogOpen.value = false }) {
            RegisterCardTeam(isAddTeamDialogOpen,loggedInUserName, loggedInUserUID, filteredEquipos.value)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EquipoCard(team: Equipos, navController: NavController,userID: String, authRepository: AuthRepository, equiposList: MutableState<List<Equipos>>) {
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
                        navController.navigate("cardteam_screen/${team.id}")
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
                        text = team.name.toString(),
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
                        text = team.creatorName.toString(),
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
                        text = team.members.size.toString(), // Cantidad de personas dentro del equipo
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
                                if (team.id != null) {
                                    // Eliminar el equipo
                                    deleteEquipo(userID, team.id)
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
fun RegisterCardTeam(
    isAddTeamDialogOpen: MutableState<Boolean>, loggedInUserName: String,
    loggedInUserUID: String,
    teams: List<Equipos>
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
                text = "Crear Equipo",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = myOrange
            )
            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = Color.Black, thickness = 1.dp)
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = name.value,
                onValueChange = {
                    name.value = it
                    if (teams.find {
                            it.name?.trim().equals(name.value.trim(), ignoreCase = true)
                        } != null) nameRepliqued.value = true else nameRepliqued.value = false
                },
                label = { Text(text = "Nombre del equipo") },
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
                    .fillMaxWidth(0.9f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if(name.value.length < 6) {
                    Text(
                        text = "* Requerido",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light, color = myOrange),
                        modifier = Modifier.offset(x = 20.dp)
                    )
                } else if(nameRepliqued.value === true) {
                    Text(
                        text = "* Nombre duplicado",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light, color = myOrange),
                        modifier = Modifier.offset(x = 20.dp)
                    )
                } else Text(text = " ")
                Text(
                    text = "${name.value.length}/30",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light, color = if(name.value.length > 30) myOrange else Color.Black),
                    modifier = Modifier
                        .offset(x = -20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                label = { Text(text = "Descripción del equipo") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    cursorColor = myOrange,
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
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light, color = if(description.value.length > 200) myOrange else Color.Black),
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
                    enabled = name.value.length >= 6 && name.value.length <= 30 && nameRepliqued.value === false && description.value.length <= 200,
                    onClick = {
                        scope.launch {
                            val newDoc = Equipos(
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
                                emptyList()
                            )

                            val db = FirebaseFirestore.getInstance()
                            val collectionReference = db.collection("Usuarios")
                                .document(loggedInUserUID)
                                .collection("Equipos")

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
                                            isAddTeamDialogOpen.value = false

                                            Toast.makeText(
                                                context,
                                                "Equipo creado correctamente",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
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
                    },
                    colors = ButtonDefaults.buttonColors(myOrange, contentColor = Color.White),
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

fun addMemberToTeam(teamId: String, memberId: String) {
    val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("Usuarios")
    val query = usersCollection.whereEqualTo("correo", memberId)

    query.get().addOnSuccessListener { querySnapshot ->
        if (!querySnapshot.isEmpty) {
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
