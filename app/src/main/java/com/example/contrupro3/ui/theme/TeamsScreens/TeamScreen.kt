package com.example.contrupro3.ui.theme.TeamsScreens


import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.contrupro3.R
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.TeamsModels.TeamScreen_ViewModel
import com.example.contrupro3.models.TeamsModels.Teams
import com.example.contrupro3.ui.theme.Menu.HamburgueerMenu
import com.example.contrupro3.ui.theme.myBlue
import com.example.contrupro3.ui.theme.myOrangehigh
import com.example.contrupro3.ui.theme.myOrangelow
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun TeamsScreen(
    navController: NavController,
    authRepository: AuthRepository,
    userID: String,
    TeamScreen_ViewModel: TeamScreen_ViewModel
) {
    val loggedInUserUID: String by authRepository.getLoggedInUserUID().observeAsState("")
    val loggedInUserName: String by authRepository.getLoggedInUserName().observeAsState("")
    val teamsSelectedToRemove =
        TeamScreen_ViewModel.teamsSelectedToRemove.observeAsState(emptyList())
    val showTeamDeleteDialog = TeamScreen_ViewModel.showDeleteTeamsDialog.observeAsState(false)
    val teamsList = remember { mutableStateOf<List<Teams>>(emptyList()) }
    val projectId = remember { mutableStateOf("") }
    val isAddTeamDialogOpen = remember { mutableStateOf(false) }

    authRepository.loadEquiposFromFirebase(teamsList, projectId.value)
    val filteredTeams = FilterTeams(teamsList, TeamScreen_ViewModel)


    Scaffold(
        floatingActionButton = {
            CompositionLocalProvider(
                LocalContentColor provides colorResource(id = R.color.white)
            ) {
                if (teamsSelectedToRemove.value.size > 0) {
                    Row {
                        FloatingActionButton(
                            onClick = {
                                TeamScreen_ViewModel.onRemoveTeamsChanged(
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
                            onClick = { TeamScreen_ViewModel.onRemoveTeamsChanged(teamsSelectedToRemove.value, true) },
                            containerColor = myOrangehigh
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar"
                            )
                        }
                    }
                } else {
                    FiltersDropdowMenu(TeamScreen_ViewModel) { isAddTeamDialogOpen.value = true }
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
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Spacer(modifier = Modifier.height(5.dp))
                    when (filteredTeams.size) {
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
                                text = "Tienes ${filteredTeams.size} equipos creados.",
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }
                    }
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(filteredTeams.size) { index ->
                            val team = filteredTeams[index]
                            EquipoCard(
                                team,
                                navController,
                                userID,
                                authRepository,
                                teamsList,
                                TeamScreen_ViewModel
                            )
                            Spacer(Modifier.height(15.dp))
                        }
                    }
                }
            }
        }
    )

    if(showTeamDeleteDialog.value) RemoveTeamsSelected(userID, projectId, TeamScreen_ViewModel)
    HamburgueerMenu(navController = navController, authRepository = authRepository)
    if (isAddTeamDialogOpen.value) {
        Dialog(onDismissRequest = { isAddTeamDialogOpen.value = false }) {
            RegisterCardTeam(
                isAddTeamDialogOpen,
                loggedInUserName,
                loggedInUserUID,
                filteredTeams
            )
        }
    }
}

@Composable
fun FilterTeams(
    teamList: MutableState<List<Teams>>,
    teamViewModel: TeamScreen_ViewModel
): List<Teams> {
    val filterSelected = teamViewModel.filterSelected.observeAsState("Fecha de inicio")
    val isFilterAscending = teamViewModel.isFilterAscending.observeAsState(false)
    val searchQuery = teamViewModel.searchQuery.observeAsState("")

    val filteredTeams =
        remember(teamList, filterSelected, isFilterAscending, searchQuery) {
            derivedStateOf {
                var filteredList = teamList.value.filter { team ->
                    team.name.toString().contains(
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

                    /*"Fecha de inicio" -> {
                        filteredList = if (isFilterAscending.value) {
                            filteredList.sortedBy { it. }
                        } else {
                            filteredList.sortedByDescending { it.startDate }
                        }
                    }*/
                    /*"Fecha de finalización" -> {
                        filteredList = if (isFilterAscending.value) {
                            filteredList.sortedBy { it.endDate }
                        } else {
                            filteredList.sortedByDescending { it.endDate }
                        }
                    }*/
                }
                filteredList
            }
        }
    return filteredTeams.value
}

@Composable
fun FiltersDropdowMenu(TeamScreen_ViewModel: TeamScreen_ViewModel, openAddTeams: () -> Unit) {
    val isFilterMenuOpen = TeamScreen_ViewModel.isFilterMenuOpen.observeAsState(false)
    val isSearchExpanded = TeamScreen_ViewModel.isSearchExpanded.observeAsState(false)
    val isFilterAscending = TeamScreen_ViewModel.isFilterAscending.observeAsState(false)
    val filterSelected = TeamScreen_ViewModel.filterSelected.observeAsState("Fecha de inicio")

    Row {
        FloatingActionButton(
            onClick = {  },
            containerColor = myOrangehigh
        ) {
            Icon(
                Icons.Default.FolderOpen,
                contentDescription = "Seleccionar Proyecto"
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        FloatingActionButton(
            onClick = {
                TeamScreen_ViewModel.onFilterSelectionChanged(
                    true,
                    isSearchExpanded.value,
                    "Fecha de inicio",
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
                TeamScreen_ViewModel.onFilterSelectionChanged(
                    false,
                    isSearchExpanded.value,
                    "Fecha de inicio",
                    isFilterAscending.value
                )
            }
        ) {
            DropdownMenuItem(onClick = {
                TeamScreen_ViewModel.onFilterSelectionChanged(
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
                TeamScreen_ViewModel.onFilterSelectionChanged(
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
                TeamScreen_ViewModel.onFilterSelectionChanged(
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
            onClick = { openAddTeams() },
            containerColor = myOrangehigh
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Crear Equipo"
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EquipoCard(
    team: Teams,
    navController: NavController,
    userID: String,
    authRepository: AuthRepository,
    teamList: MutableState<List<Teams>>,
    TeamScreen_ViewModel: TeamScreen_ViewModel
) {
    val teamsSelectedToRemove = TeamScreen_ViewModel.teamsSelectedToRemove.observeAsState(emptyList())
    val showDeleteTeamsDialog = TeamScreen_ViewModel.showDeleteTeamsDialog.observeAsState(false)

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
                        if (teamsSelectedToRemove.value.size > 0) {
                            if (!teamsSelectedToRemove.value.contains(team)) {
                                val newList = teamsSelectedToRemove.value + team
                                TeamScreen_ViewModel.onRemoveTeamsChanged(
                                    newList,
                                    showDeleteTeamsDialog.value
                                )
                            } else TeamScreen_ViewModel.onRemoveTeamsChanged(
                                teamsSelectedToRemove.value.filter { p -> p !== team },
                                showDeleteTeamsDialog.value
                            )
                        } else navController.navigate("cardview_teams_screen/${userID}/${team.id}")
                    },
                    onLongClick = {
                        if (!teamsSelectedToRemove.value.contains(team)) {
                            val newList = teamsSelectedToRemove.value + team
                            TeamScreen_ViewModel.onRemoveTeamsChanged(
                                newList,
                                showDeleteTeamsDialog.value
                            )
                        } else TeamScreen_ViewModel.onRemoveTeamsChanged(
                            teamsSelectedToRemove.value.filter { p -> p !== team },
                            showDeleteTeamsDialog.value
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
                            text = team.name.toString(),
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    if (teamsSelectedToRemove.value.contains(team)) {
                        Box {
                            Icon(
                                Icons.Default.CheckBox,
                                contentDescription = null,
                                tint = myBlue,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    if (teamsSelectedToRemove.value.size > 0 && !teamsSelectedToRemove.value.contains(team)) {
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
                        text = team.members?.size.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun RemoveTeamsSelected(
    userID: String,
    projectId: MutableState<String>,
    TeamScreen_ViewModel: TeamScreen_ViewModel
) {
    val teamsSelectedToRemove = TeamScreen_ViewModel.teamsSelectedToRemove.observeAsState(emptyList())
    val context = LocalContext.current

    if (teamsSelectedToRemove.value.size > 0) {
        AlertDialog(
            onDismissRequest = { TeamScreen_ViewModel.onRemoveTeamsChanged(teamsSelectedToRemove.value, false) },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            TeamScreen_ViewModel.onRemoveTeamsChanged(emptyList(), false)
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
                                db.collection("Usuarios")
                                    .document(userID)
                                    .collection("Proyectos")
                                    .document(projectId.value)
                                    .collection("Teams")

                            for (team in teamsSelectedToRemove.value) {
                                collectionRef.document("${team.id}").delete()
                            }
                            TeamScreen_ViewModel.onRemoveTeamsChanged(emptyList(), false)
                            Toast.makeText(
                                context,
                                "Los Equipos fueron removidos correctamente",
                                Toast.LENGTH_LONG
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
                    text = "Los equipos no se podran volver a recuperar. ¿Esta seguro de esto?",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light)
                )
            }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterCardTeam(
    isAddTeamDialogOpen: MutableState<Boolean>, loggedInUserName: String,
    loggedInUserUID: String,
    teams: List<Teams>
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
                color = myBlue
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
                    text = "${name.value.length}/30",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light,
                        color = if (name.value.length > 30) myBlue else Color.Black
                    ),
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
                    color = if (description.value.length > 200) myBlue else Color.Black
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
                    enabled = name.value.length >= 6 && name.value.length <= 30 && nameRepliqued.value === false && description.value.length <= 200,
                    onClick = {
                        scope.launch {
                            val newDoc = Teams(
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
                    colors = ButtonDefaults.buttonColors(myBlue, contentColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Guardar")
                }

                Button(
                    onClick = {
                        isAddTeamDialogOpen.value = false
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

fun addMemberToTeam(teamId: String, memberId: String) {
    val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("Usuarios")
    val query = usersCollection.whereEqualTo("correo", memberId)

    query.get().addOnSuccessListener { querySnapshot ->
        if (!querySnapshot.isEmpty) {
            val teamRef = db.collection("Equipos").document(teamId)
            teamRef.update("members", FieldValue.arrayUnion(memberId))
        }
    }
}

class TeamViewModel : ViewModel() {
    val teamsList = mutableStateOf(emptyList<Teams>())
    val teamsSelectedToRemove = mutableStateListOf<String>()
    val showTeamRemoveDialog = mutableStateOf(false)
}
