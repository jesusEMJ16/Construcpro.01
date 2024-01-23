import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.contrupro3.GetTimeAgoFromTimestamp
import com.example.contrupro3.R
import com.example.contrupro3.TimestampToDate
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.TasksModels.Cardview_Task_ViewModel
import com.example.contrupro3.models.TasksModels.TaskModel
import com.example.contrupro3.models.TeamsModels.Teams
import com.example.contrupro3.ui.theme.backgroundButtonColor
import com.example.contrupro3.ui.theme.myBlue
import com.example.contrupro3.ui.theme.myOrangehigh
import com.google.firebase.firestore.FirebaseFirestore
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardviewTaskScreen(
    navController: NavHostController,
    authRepository: AuthRepository,
    userID: String,
    projectId: String,
    taskId: String,
    Cardview_Task_viewmodel: Cardview_Task_ViewModel
) {
    val task = remember { mutableStateOf<TaskModel?>(null) }
    val taskLoadingStatus = remember { mutableStateOf("Loading") }
    authRepository.loadTask(
        userID,
        projectId,
        taskId,
        task,
        { status -> taskLoadingStatus.value = status }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (taskLoadingStatus.value === "Loaded") {
                        Column {
                            Text(
                                "${task.value?.name}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                task.value?.creatorName ?: "",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = backgroundButtonColor
                        )
                    }
                },
            )
        }
    ) {
        if (taskLoadingStatus.value === "Loaded") {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .offset(y = 50.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    InformationCard(
                        navController,
                        authRepository,
                        userID,
                        task,
                        projectId,
                        Cardview_Task_viewmodel
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    TeamsAdded(
                        authRepository,
                        userID,
                        task,
                        projectId,
                        Cardview_Task_viewmodel
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        } else if (taskLoadingStatus.value === "Loading") {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    color = backgroundButtonColor
                )
            }
        }
    }
}

/*@Composable
fun UsersAdded(
    authRepository: AuthRepository,
    userID: String,
    task: MutableState<TaskModel?>,
    projectId: String,
    Cardview_Task_viewmodel: Cardview_Task_ViewModel
) {
    val showInviteDialog = remember { mutableStateOf(false) }
    val teamsList = remember { mutableStateOf<List<Teams>>(emptyList()) }
    val showRemoveTeamDialog = remember { mutableStateOf(false) }
    val teamsSelectedToRemove by Cardview_Task_viewmodel
        .teamsSelectedToRemove.observeAsState(emptyList())
    authRepository.loadTeamsOfProject(teamsList, userID, projectId)

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Miembros Asignados",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = myBlue,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .background(Color(0x79D8D8D8))
            ) {
                items(teamsList.value.size) { index ->
                    val team = teamsList.value[index]
                    TeamCard(team, Cardview_Task_viewmodel)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (teamsSelectedToRemove.size > 0) {
                    Button(
                        onClick = { Cardview_Task_viewmodel.onSelectionToRemoveUpdated(emptyList()) },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = myOrangehigh
                        ),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Text(text = "Cancelar")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = { showRemoveTeamDialog.value = true },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = myOrangehigh
                        ),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Text(text = "Remover")
                    }
                } else {
                    Button(
                        onClick = { showInviteDialog.value = true },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = myOrangehigh
                        ),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Text(text = "Añadir")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserCard(
    user: UserModel,
    Cardview_Task_viewmodel: Cardview_Task_ViewModel
) {
    val usersSelectedToRemove by Cardview_Task_viewmodel
        .usersSelectedToRemove.observeAsState(emptyList())
    val isSelectedToRemove = remember(user, usersSelectedToRemove) {
        mutableStateOf(usersSelectedToRemove.contains(user))
    }

    Box(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onLongClick = {
                        if (isSelectedToRemove.value) {
                            val newList =
                                usersSelectedToRemove.filter { m -> m != user }
                            Cardview_Task_viewmodel.onSelectionToRemoveUsersUpdated(newList)
                        } else {
                            Cardview_Task_viewmodel.onSelectionToRemoveUsersUpdated(usersSelectedToRemove + user)
                        }
                    },
                    onClick = {
                        if (usersSelectedToRemove.size > 0) {
                            if (isSelectedToRemove.value) {
                                val newList =
                                    usersSelectedToRemove.filter { m -> m != user }
                                Cardview_Task_viewmodel.onSelectionToRemoveUsersUpdated(newList)
                            } else {
                                Cardview_Task_viewmodel.onSelectionToRemoveUsersUpdated(
                                    usersSelectedToRemove + user
                                )
                            }
                        }
                    }
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserIcon(user)
            UserInfo(user)
            UserIconIsSelected(isSelectedToRemove, Cardview_Task_viewmodel, user)
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
    }
}

@Composable
fun UserIconIsSelected(
    isSelectedToRemove: MutableState<Boolean>,
    Cardview_Task_viewmodel: Cardview_Task_ViewModel,
    user: UserModel
) {
    val usersSelectedToRemove by Cardview_Task_viewmodel
        .usersSelectedToRemove.observeAsState(emptyList())

    if(usersSelectedToRemove.size > 0) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .offset(x = 120.dp)
        ) {
            IconButton(onClick = {
                if (usersSelectedToRemove.size > 0) {
                    if (isSelectedToRemove.value) {
                        val newList =
                            usersSelectedToRemove.filter { m -> m != user }
                        Cardview_Task_viewmodel.onSelectionToRemoveUsersUpdated(newList)
                    } else {
                        Cardview_Task_viewmodel.onSelectionToRemoveUsersUpdated(
                            usersSelectedToRemove + user
                        )
                    }
                }
            }) {
                Icon(
                    if (isSelectedToRemove.value) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                    contentDescription = null,
                    tint = backgroundButtonColor
                )
            }
        }
    }
}

@Composable
fun UserInfo(user: UserModel) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "${user.name} ${user.lastName}",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "${user.email}",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 3.dp)
        )
    }
}

@Composable
fun UserIcon(user: UserModel) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .padding(end = 3.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_icon),
            contentDescription = "Icono del usuario",
            modifier = Modifier.offset(y = 3.dp)
        )
    }
}*/

@Composable
fun TeamsAdded(
    authRepository: AuthRepository,
    userID: String,
    task: MutableState<TaskModel?>,
    projectId: String,
    Cardview_Task_viewmodel: Cardview_Task_ViewModel
) {
    val showAddTeamsDialog = remember { mutableStateOf(false) }
    val teamsSelectedToAdd = remember { mutableStateOf<List<Teams>>(emptyList()) }
    val showRemoveTeamDialog = remember { mutableStateOf(false) }
    val teamsSelectedToRemove by Cardview_Task_viewmodel
        .teamsSelectedToRemove.observeAsState(emptyList())

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Equipos Asignados",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = myBlue,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .background(Color(0x79D8D8D8))
            ) {
                items(teamsSelectedToAdd.value.size) { index ->
                    val team = teamsSelectedToAdd.value[index]
                    TeamCard(team, Cardview_Task_viewmodel)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (teamsSelectedToRemove.size > 0) {
                    Button(
                        onClick = { Cardview_Task_viewmodel.onSelectionToRemoveUpdated(emptyList()) },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = myOrangehigh
                        ),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Text(text = "Cancelar")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = { showRemoveTeamDialog.value = true },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = myOrangehigh
                        ),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Text(text = "Remover")
                    }
                } else {
                    Button(
                        onClick = { showAddTeamsDialog.value = true },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = myOrangehigh
                        ),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Text(text = "Añadir")
                    }
                }
            }
        }
    }

    if (showAddTeamsDialog.value) TeamsSelection(
        userID = userID,
        authRepository = authRepository,
        onDismiss = { showAddTeamsDialog.value = false },
        projectId,
        onTeamsSelected = { list ->
            showAddTeamsDialog.value = false
            teamsSelectedToAdd.value = list
        }
    )
}

@Composable
private fun TeamsSelection(
    userID: String,
    authRepository: AuthRepository,
    onDismiss: () -> Unit,
    projectId: String,
    onTeamsSelected: (List<Teams>) -> Unit
) {
    val teamsList = remember { mutableStateOf<List<Teams>>(emptyList()) }
    var teamsLoadedStatus by remember { mutableStateOf("Loading") }
    val teamsSelectedToAdd = remember { mutableStateOf<List<Teams>>(emptyList()) }

    LaunchedEffect(userID) {
        authRepository.loadTeamsOfProject(
            teamsList,
            userID,
            projectId,
            { status -> teamsLoadedStatus = status })
    }

    Dialog(onDismissRequest = {
        onTeamsSelected(teamsSelectedToAdd.value)
    }) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight(0.6f),
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Asignar Equipos",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = myBlue
                )
                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = Color.Black, thickness = 1.dp)
                Spacer(modifier = Modifier.height(7.dp))
                if (teamsLoadedStatus == "Loaded") {
                    if (teamsList.value.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            items(teamsList.value.size) { index ->
                                val team = teamsList.value[index]

                                if (index == 0) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.TopCenter
                                    ) {
                                        Text(
                                            text = "Estos son todos los equipos administrados por ti.",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Light
                                            ),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth(0.7f)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .clickable {
                                            if(teamsSelectedToAdd.value.contains(team)) {
                                                val newList = teamsSelectedToAdd.value.filter { t -> t !== team }
                                                teamsSelectedToAdd.value = newList
                                            } else {
                                                teamsSelectedToAdd.value += team
                                            }
                                        },
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.teams_icon),
                                        contentDescription = "Icono del proyecto",
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(end = 4.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "${team.name}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Text(
                                            text = "${team.creatorName}",
                                            style = MaterialTheme.typography.labelMedium,
                                            modifier = Modifier.offset(x = 4.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(horizontal = 40.dp))
                                    Icon(
                                        if(teamsSelectedToAdd.value.contains(team)) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                                        contentDescription = null,
                                        tint = backgroundButtonColor
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "No hay equipos creados",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                            textAlign = TextAlign.Center
                        )
                    }
                } else if (teamsLoadedStatus == "Loading") {
                    Text(
                        text = "Cargando Equipos...",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                        textAlign = TextAlign.Center
                    )
                } else if (teamsLoadedStatus == "Failed") {
                    Text(
                        text = "Error al cargar los equipos...",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TeamCard(
    team: Teams,
    Cardview_Task_viewmodel: Cardview_Task_ViewModel
) {
    val teamsSelectedToRemove by Cardview_Task_viewmodel
        .teamsSelectedToRemove.observeAsState(emptyList())
    val isSelectedToRemove = remember(team, teamsSelectedToRemove) {
        mutableStateOf(teamsSelectedToRemove.contains(team))
    }

    Box(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onLongClick = {
                        if (isSelectedToRemove.value) {
                            val newList =
                                teamsSelectedToRemove.filter { m -> m != team }
                            Cardview_Task_viewmodel.onSelectionToRemoveUpdated(newList)
                        } else {
                            Cardview_Task_viewmodel.onSelectionToRemoveUpdated(teamsSelectedToRemove + team)
                        }
                    },
                    onClick = {
                        if (teamsSelectedToRemove.size > 0) {
                            if (isSelectedToRemove.value) {
                                val newList =
                                    teamsSelectedToRemove.filter { m -> m != team }
                                Cardview_Task_viewmodel.onSelectionToRemoveUpdated(newList)
                            } else {
                                Cardview_Task_viewmodel.onSelectionToRemoveUpdated(
                                    teamsSelectedToRemove + team
                                )
                            }
                        }
                    }
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TeamIcon(team)
            TeamInfo(team)
            TeamIconIsSelected(isSelectedToRemove, Cardview_Task_viewmodel, team)
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
    }
}

@Composable
fun TeamIconIsSelected(
    isSelectedToRemove: MutableState<Boolean>,
    Cardview_Task_viewmodel: Cardview_Task_ViewModel,
    team: Teams
) {
    val teamsSelectedToRemove by Cardview_Task_viewmodel
        .teamsSelectedToRemove.observeAsState(emptyList())

    if (teamsSelectedToRemove.size > 0) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .offset(x = 120.dp)
        ) {
            IconButton(onClick = {
                if (teamsSelectedToRemove.size > 0) {
                    if (isSelectedToRemove.value) {
                        val newList =
                            teamsSelectedToRemove.filter { m -> m != team }
                        Cardview_Task_viewmodel.onSelectionToRemoveUpdated(newList)
                    } else {
                        Cardview_Task_viewmodel.onSelectionToRemoveUpdated(
                            teamsSelectedToRemove + team
                        )
                    }
                }
            }) {
                Icon(
                    if (isSelectedToRemove.value) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                    contentDescription = null,
                    tint = backgroundButtonColor
                )
            }
        }
    }
}

@Composable
fun TeamInfo(team: Teams) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "${team.name}",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "${team.creatorName}",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 3.dp)
        )
    }
}

@Composable
fun TeamIcon(team: Teams) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .padding(end = 3.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.teams_icon),
            contentDescription = "Icono del equipo",
            modifier = Modifier.offset(y = 3.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun InformationCard(
    navController: NavHostController,
    authRepository: AuthRepository,
    userID: String,
    task: MutableState<TaskModel?>,
    projectId: String,
    Cardview_Task_viewmodel: Cardview_Task_ViewModel
) {
    val name = Cardview_Task_viewmodel.name.observeAsState("${task.value?.name}")
    val description =
        Cardview_Task_viewmodel.description.observeAsState("${task.value?.description}")
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val startDate =
        Cardview_Task_viewmodel.startDateTextField.observeAsState(task.value?.startDate)
    val endDate = Cardview_Task_viewmodel.endDateTextField.observeAsState(task.value?.endDate)
    val isStartDateDialog = remember { mutableStateOf(false) }
    val isEndDateDialog = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Información De La Tarea",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = myBlue,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Nombre de la tarea",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .offset(x = 5.dp)
                    .padding(5.dp)
            )
            OutlinedTextField(
                value = name.value,
                onValueChange = { Cardview_Task_viewmodel.onFieldsUpdated(it, description.value) },
                maxLines = 1,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    backgroundColor = Color(0x79D8D8D8),
                    focusedBorderColor = Color.Transparent,
                    cursorColor = myBlue,
                    disabledBorderColor = Color.Transparent,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        Cardview_Task_viewmodel.onFieldsUpdated("", description.value)
                    }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Borrar nombre",
                            tint = backgroundButtonColor
                        )
                    }
                }
            )
            if (name.value !== task.value?.name) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (name.value.length < 6) {
                        Text(
                            text = "* Requerido",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Light,
                                color = myBlue
                            ),
                            modifier = Modifier.offset(x = 5.dp)
                        )
                    } else Spacer(modifier = Modifier.width(0.dp))
                    Text(
                        text = "${name.value.length}/25",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Light,
                            color = if (name.value.length < 6 || name.value.length > 25) Color.Red else Color.Black
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Descripción de la tarea",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .offset(x = 5.dp)
                    .padding(5.dp)
            )
            OutlinedTextField(
                value = description.value,
                onValueChange = { Cardview_Task_viewmodel.onFieldsUpdated(name.value, it) },
                maxLines = 1,
                placeholder = {
                    if (task.value?.description != null && task.value?.description != null) {
                        Text("${description.value}")
                    } else {
                        Text("No hay descripción establecida.")
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    backgroundColor = Color(0x79D8D8D8),
                    focusedBorderColor = Color.Transparent,
                    cursorColor = myBlue,
                    disabledBorderColor = Color.Transparent,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        Cardview_Task_viewmodel.onFieldsUpdated(name.value, "")
                    }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Borrar descripción",
                            tint = backgroundButtonColor
                        )
                    }
                }
            )
            if (description.value !== task.value?.description) {
                Text(
                    text = "${description.value.length}/200",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light,
                        color = if (description.value.length > 200) Color.Red else Color.Black
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Fecha de inicio",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .offset(x = 5.dp)
                            .padding(5.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.extraLarge)
                            .fillMaxWidth(0.45f)
                            .height(40.dp)
                            .clickable { isStartDateDialog.value = true }
                            .background(Color(0x79D8D8D8)),
                    ) {
                        if (startDate.value != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(
                                    "${TimestampToDate(startDate.value)}",
                                    modifier = Modifier.offset(x = 20.dp)
                                )
                                IconButton(
                                    onClick = {
                                        Cardview_Task_viewmodel.onCalendarUpdated(
                                            null,
                                            endDate.value
                                        )
                                    },
                                    modifier = Modifier.offset(x = 7.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = null,
                                        tint = backgroundButtonColor
                                    )
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("-")
                            }
                        }
                    }
                    if (startDate.value != null) {
                        Text(
                            text = "${GetTimeAgoFromTimestamp(startDate.value)}",
                            modifier = Modifier.fillMaxWidth(0.45f),
                            style = MaterialTheme.typography.labelSmall.copy(
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        )
                    } else Text("")
                }
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Column {
                    Text(
                        text = "Fecha de finalizado",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .offset(x = 5.dp)
                            .padding(5.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.extraLarge)
                            .fillMaxWidth()
                            .height(40.dp)
                            .clickable { isEndDateDialog.value = true }
                            .background(Color(0x79D8D8D8)),
                    ) {
                        if (endDate.value != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    "${TimestampToDate(endDate.value)}",
                                    modifier = Modifier.offset(x = 25.dp)
                                )
                                IconButton(
                                    onClick = {
                                        Cardview_Task_viewmodel.onCalendarUpdated(
                                            startDate.value,
                                            null
                                        )
                                    },
                                    modifier = Modifier.offset(x = 15.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = null,
                                        tint = backgroundButtonColor
                                    )
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("-")
                            }
                        }
                    }
                    if (startDate.value != null) {
                        Text(
                            text = "${GetTimeAgoFromTimestamp(endDate.value)}",
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.labelSmall.copy(textAlign = TextAlign.Center)
                        )
                    } else Text("")
                }
            }
        }
    }

    if (isStartDateDialog.value) StartDateSelectDialog(
        Cardview_Task_viewmodel,
        startDate.value,
        endDate.value,
    ) { isStartDateDialog.value = false }
    if (isEndDateDialog.value) EndDateSelectDialog(
        Cardview_Task_viewmodel,
        startDate.value,
        endDate.value,
    ) { isEndDateDialog.value = false }

    if (name.value !== task.value?.name || description.value !== task.value?.description || startDate.value !== task.value?.startDate || endDate.value !== task.value?.endDate) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    keyboardController?.hide()
                    Cardview_Task_viewmodel.onFieldsUpdated(
                        "${task.value?.name}",
                        "${task.value?.description}"
                    )
                    Cardview_Task_viewmodel.onCalendarUpdated(
                        task.value?.startDate,
                        task.value?.endDate
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = myOrangehigh
                ),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = "Cancelar")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    keyboardController?.hide()
                    val db = FirebaseFirestore.getInstance()
                    val collection =
                        db.collection("Users")
                            .document(userID)
                            .collection("Projects")
                            .document(projectId)
                            .collection("Tasks")
                            .document(task.value?.id.toString())

                    if (name.value !== task.value?.name) {
                        val newProject = TaskModel(
                            id = task.value?.id,
                            name = name.value,
                            description = task.value?.description,
                            creatorName = task.value?.creatorName,
                            creatorUID = task.value?.creatorUID,
                            startDate = task.value?.startDate,
                            endDate = task.value?.endDate
                        )
                        task.value = newProject
                        collection.update("name", name.value)
                    }
                    if (description.value !== task.value?.description) {
                        val newProject = TaskModel(
                            id = task.value?.id,
                            name = task.value?.name,
                            description = description.value,
                            creatorName = task.value?.creatorName,
                            creatorUID = task.value?.creatorUID,
                            startDate = task.value?.startDate,
                            endDate = task.value?.endDate
                        )
                        task.value = newProject
                        collection.update("description", description.value)
                    }
                    if (startDate.value !== task.value?.startDate) {
                        /*SendNotificationToProjectMembers(
                            title = "Se ha establecido una fecha de inicio",
                            description = "Se estableció la fecha ${TimestampToDate(startDate.value)} (${
                                GetTimeAgoFromTimestamp(startDate.value)
                            }) para el comienzo de la tarea '${task.value?.name}'.",
                            additionalInfo = mapOf(
                                "ownerId" to task.value?.creatorUID.toString(),
                                "projectId" to task.value?.id.toString()
                            ),
                            icon = IconModel(
                                "Priority High",
                                color = "Yellow"
                            )
                        )*/
                        val newProject = TaskModel(
                            id = task.value?.id,
                            name = task.value?.name,
                            description = task.value?.description,
                            creatorName = task.value?.creatorName,
                            creatorUID = task.value?.creatorUID,
                            startDate = startDate.value,
                            endDate = task.value?.endDate
                        )
                        task.value = newProject
                        collection.update("startDate", startDate.value)
                    }
                    if (endDate.value !== task.value?.endDate) {
                        /*SendNotificationToProjectMembers(
                            title = "Se ha establecido una fecha de finalización",
                            description = "Se estableció la fecha ${TimestampToDate(endDate.value)} (${
                                GetTimeAgoFromTimestamp(endDate.value)
                            }) para la finalización del proyecto '${project.value?.name}'.",
                            additionalInfo = mapOf(
                                "ownerId" to project.value?.creatorUID.toString(),
                                "projectId" to project.value?.id.toString()
                            ),
                            icon = IconModel(
                                "Priority High",
                                color = "Red"
                            )
                        )*/
                        val newProject = TaskModel(
                            id = task.value?.id,
                            name = task.value?.name,
                            description = task.value?.description,
                            creatorName = task.value?.creatorName,
                            creatorUID = task.value?.creatorUID,
                            startDate = task.value?.startDate,
                            endDate = endDate.value
                        )
                        task.value = newProject
                        collection.update("endDate", endDate.value)
                    }
                    Toast.makeText(
                        context,
                        "Información actualizada",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                enabled = name.value.length >= 6 && name.value.length <= 25 && description.value.length <= 200,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = myOrangehigh
                ),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = "Guardar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StartDateSelectDialog(
    Cardview_Task_viewmodel: Cardview_Task_ViewModel,
    startDateText: String?,
    endDateText: String?,
    onDismiss: () -> Unit
) {
    val timeBoundary = LocalDate.now().let { now ->
        if (endDateText != null) {
            val endDateMillis = endDateText?.toLongOrNull() ?: System.currentTimeMillis()
            val endDate = Instant.ofEpochMilli(endDateMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            now.minusYears(10)..endDate.minusDays(1)
        } else now.minusYears(10)..now.plusYears(99)
    }
    val showCalendar = remember { mutableStateOf(true) }
    if (!showCalendar.value) onDismiss()

    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        CalendarDialog(
            state = rememberUseCaseState(visible = showCalendar.value),
            config = CalendarConfig(
                yearSelection = true,
                monthSelection = true,
                boundary = timeBoundary,
                style = CalendarStyle.MONTH,
            ),
            selection = CalendarSelection.Date(
                onSelectDate = { selectedDate ->
                    val date =
                        Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    val timestamp = calendar.timeInMillis

                    onDismiss()
                    Cardview_Task_viewmodel.onCalendarUpdated(timestamp.toString(), endDateText)
                }
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EndDateSelectDialog(
    Cardview_Task_viewmodel: Cardview_Task_ViewModel,
    startDateText: String?,
    endDateText: String?,
    onDismiss: () -> Unit
) {
    val timeBoundary = LocalDate.now().let { now ->
        if (startDateText != null) {
            val startDateMillis = startDateText?.toLongOrNull() ?: System.currentTimeMillis()
            val startDate = Instant.ofEpochMilli(startDateMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            startDate.plusDays(1)..now.plusYears(99)
        } else now.minusYears(10)..now.plusYears(99)
    }
    val showCalendar = remember { mutableStateOf(true) }
    if (!showCalendar.value) onDismiss()

    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        CalendarDialog(
            state = rememberUseCaseState(visible = showCalendar.value),
            config = CalendarConfig(
                yearSelection = true,
                monthSelection = true,
                boundary = timeBoundary,
                style = CalendarStyle.MONTH,
            ),
            selection = CalendarSelection.Date(
                onSelectDate = { selectedDate ->
                    val date =
                        Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    val timestamp = calendar.timeInMillis

                    onDismiss()
                    Cardview_Task_viewmodel.onCalendarUpdated(startDateText, timestamp.toString())
                }
            )
        )
    }
}