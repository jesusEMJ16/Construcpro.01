package com.example.contrupro3.ui.theme.ProjectsScreens

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.contrupro3.GetTimeAgoFromTimestamp
import com.example.contrupro3.SendNotificationToProjectMembers
import com.example.contrupro3.TimestampToDate
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.ProjectsModels.ProjectCard_ViewModel
import com.example.contrupro3.models.ProjectsModels.ProjectModel
import com.example.contrupro3.models.UserModels.IconModel
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
fun CardviewProjectsScreen(
    navController: NavHostController,
    authRepository: AuthRepository,
    userID: String,
    projectId: String,
    ProjectCard_ViewModel: ProjectCard_ViewModel
) {
    val project = remember { mutableStateOf<ProjectModel?>(null) }
    val projectLoadingStatus = remember { mutableStateOf("Loading") }
    authRepository.loadProject(
        projectId,
        project,
        { status -> projectLoadingStatus.value = status })

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (projectLoadingStatus.value === "Loaded") {
                        Column {
                            Text(
                                "${project.value?.name}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                project.value?.creatorName ?: "",
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
        if (projectLoadingStatus.value === "Loaded") {
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
                        project,
                        ProjectCard_ViewModel
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TasksCard(userID, project, ProjectCard_ViewModel)
                }
            }
        } else if (projectLoadingStatus.value === "Loading") {
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

@Composable
private fun TasksCard(
    userID: String,
    project: MutableState<ProjectModel?>,
    ProjectCard_ViewModel: ProjectCard_ViewModel
) {
    val isSectionOpen = remember { mutableStateOf(false) }

    TasksTitleSection(isSectionOpen)
}

@Composable
private fun TasksTitleSection(isSectionOpen: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isSectionOpen.value = !isSectionOpen.value
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            if (isSectionOpen.value) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = null,
            tint = myBlue
        )
        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        Text(
            text = "Tareas Del Proyecto",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = myBlue
        )
        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        Icon(
            if (isSectionOpen.value) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = null,
            tint = myBlue
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
    project: MutableState<ProjectModel?>,
    ProjectCard_ViewModel: ProjectCard_ViewModel
) {
    val name = ProjectCard_ViewModel.projectName.observeAsState("${project.value?.name}")
    val description =
        ProjectCard_ViewModel.projectDescription.observeAsState("${project.value?.description}")
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val startDate =
        ProjectCard_ViewModel.startDateTextField.observeAsState(project.value?.startDate)
    val endDate = ProjectCard_ViewModel.endDateTextField.observeAsState(project.value?.endDate)
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
                text = "Información Del Proyecto",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = myBlue,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Nombre del proyecto",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .offset(x = 5.dp)
                    .padding(5.dp)
            )
            OutlinedTextField(
                value = name.value,
                onValueChange = { ProjectCard_ViewModel.onFieldsUpdated(it, description.value) },
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
                        ProjectCard_ViewModel.onFieldsUpdated("", description.value)
                    }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Borrar nombre",
                            tint = backgroundButtonColor
                        )
                    }
                }
            )
            if (name.value !== project.value?.name) {
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
                text = "Descripción del proyecto",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .offset(x = 5.dp)
                    .padding(5.dp)
            )
            OutlinedTextField(
                value = description.value,
                onValueChange = { ProjectCard_ViewModel.onFieldsUpdated(name.value, it) },
                maxLines = 1,
                placeholder = {
                    if (project.value?.description != null && project.value?.description != null) {
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
                        ProjectCard_ViewModel.onFieldsUpdated(name.value, "")
                    }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Borrar descripción",
                            tint = backgroundButtonColor
                        )
                    }
                }
            )
            if (description.value !== project.value?.description) {
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
                                        ProjectCard_ViewModel.onCalendarUpdated(
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
                                        ProjectCard_ViewModel.onCalendarUpdated(
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
        ProjectCard_ViewModel = ProjectCard_ViewModel,
        startDateText = startDate.value,
        endDateText = endDate.value,
    ) { isStartDateDialog.value = false }
    if (isEndDateDialog.value) EndDateSelectDialog(
        ProjectCard_ViewModel = ProjectCard_ViewModel,
        startDateText = startDate.value,
        endDateText = endDate.value,
    ) { isEndDateDialog.value = false }

    if (name.value !== project.value?.name || description.value !== project.value?.description || startDate.value !== project.value?.startDate || endDate.value !== project.value?.endDate) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    keyboardController?.hide()
                    ProjectCard_ViewModel.onFieldsUpdated(
                        "${project.value?.name}",
                        "${project.value?.description}"
                    )
                    ProjectCard_ViewModel.onCalendarUpdated(
                        project.value?.startDate,
                        project.value?.endDate
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
                            .document(project.value?.id.toString())

                    if (name.value !== project.value?.name) {
                        val newProject = ProjectModel(
                            id = project.value?.id,
                            name = name.value,
                            description = project.value?.description,
                            creatorName = project.value?.creatorName,
                            creatorUID = project.value?.creatorUID,
                            startDate = project.value?.startDate,
                            endDate = project.value?.endDate
                        )
                        project.value = newProject
                        collection.update("name", name.value)
                    }
                    if (description.value !== project.value?.description) {
                        val newProject = ProjectModel(
                            id = project.value?.id,
                            name = project.value?.name,
                            description = description.value,
                            creatorName = project.value?.creatorName,
                            creatorUID = project.value?.creatorUID,
                            startDate = project.value?.startDate,
                            endDate = project.value?.endDate
                        )
                        project.value = newProject
                        collection.update("description", description.value)
                    }
                    if (startDate.value !== project.value?.startDate) {
                        SendNotificationToProjectMembers(
                            title = "Se ha establecido una fecha de inicio",
                            description = "Se estableció la fecha ${TimestampToDate(startDate.value)} (${
                                GetTimeAgoFromTimestamp(startDate.value)
                            }) para el comienzo del proyecto '${project.value?.name}'.",
                            additionalInfo = mapOf(
                                "ownerId" to project.value?.creatorUID.toString(),
                                "projectId" to project.value?.id.toString()
                            ),
                            icon = IconModel(
                                "Priority High",
                                color = "Yellow"
                            )
                        )
                        val newProject = ProjectModel(
                            id = project.value?.id,
                            name = project.value?.name,
                            description = project.value?.description,
                            creatorName = project.value?.creatorName,
                            creatorUID = project.value?.creatorUID,
                            startDate = startDate.value,
                            endDate = project.value?.endDate
                        )
                        project.value = newProject
                        collection.update("startDate", startDate.value)
                    }
                    if (endDate.value !== project.value?.endDate) {
                        SendNotificationToProjectMembers(
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
                        )
                        val newProject = ProjectModel(
                            id = project.value?.id,
                            name = project.value?.name,
                            description = project.value?.description,
                            creatorName = project.value?.creatorName,
                            creatorUID = project.value?.creatorUID,
                            startDate = project.value?.startDate,
                            endDate = endDate.value
                        )
                        project.value = newProject
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
    ProjectCard_ViewModel: ProjectCard_ViewModel,
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
                    ProjectCard_ViewModel.onCalendarUpdated(timestamp.toString(), endDateText)
                }
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EndDateSelectDialog(
    ProjectCard_ViewModel: ProjectCard_ViewModel,
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
                    ProjectCard_ViewModel.onCalendarUpdated(startDateText, timestamp.toString())
                }
            )
        )
    }
}