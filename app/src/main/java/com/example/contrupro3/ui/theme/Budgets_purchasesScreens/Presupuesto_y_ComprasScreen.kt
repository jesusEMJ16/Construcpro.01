package com.example.contrupro3.ui.theme.Budgets_purchasesScreens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssuredWorkload
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RealEstateAgent
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.BudgetModels.SuministrosViewModel
import com.example.contrupro3.models.ProjectsModels.Project
import com.example.contrupro3.ui.theme.Menu.HamburgueerMenu
import com.example.contrupro3.ui.theme.lightblue
import com.example.contrupro3.ui.theme.myBlue
import com.example.contrupro3.ui.theme.myOrangehigh

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Presupuesto_y_Compras(navController: NavController, authRepository: AuthRepository, userId: String) {
    // Variable para almacenar la selección actual (Presupuestos o Compras)
    var currentSelection by remember { mutableStateOf("Presupuestos") }
    var isProjectDialogOpen by remember { mutableStateOf(false) }
    var selectedProject by remember { mutableStateOf<Project?>(null) }
    val projectsList = remember { mutableStateOf<List<Project>>(emptyList()) }

    authRepository.loadProjectsFromFirebase(projectsList)
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column {
            ProyectoSelectionHeader(projectsList.value!!, authRepository) { projectSel -> selectedProject = projectSel }

            if(selectedProject !== null) {
                TabsRow(currentSelection) { selection -> currentSelection = selection }
                Box(modifier = Modifier.weight(1f)) {
                    when (currentSelection) {
                        "Resumen del Presupuesto" -> ResumenScreen()
                        "Gestión de Compras" -> ComprasScreen(authRepository, userId, selectedProject!!)
                        "Gestión de Suministros y Materiales" -> SuministrosScreen(SuministrosViewModel(), authRepository)
                        "Provedores" -> ProveedoresScreen()
                        "Especificaciones Técnicas" -> EspecificacionesScreen()
                        "Criterios de Cuantificación" -> CriteriosScreen()
                        "Historial y Reportes" -> HistorialScreen()
                    }
                }
            }
        }
    }

    HamburgueerMenu(navController = navController, authRepository = authRepository)
}
@Composable
fun ProyectoSelectionHeader(projectsList: List<Project>, authRepository: AuthRepository, SendProjectSelected: (Project) -> Unit) {
    var isDialogVisible by remember { mutableStateOf(false) }
    var selectedProject by remember { mutableStateOf<Project?>(null) }

    if(selectedProject !== null) SendProjectSelected(selectedProject!!)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(color = Color.Transparent)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Presupuesto y compras",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = selectedProject?.projectName ?: "",
                    onValueChange = { newText ->
                        selectedProject?.projectName = newText
                    },
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        isDialogVisible = true
                                    }
                                }
                            }
                        },
                    label = { Text("Selecciona tu proyecto") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        backgroundColor = Color(0x79D8D8D8),
                        focusedBorderColor = Color(0x79A5A5A5),
                        cursorColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                    )
                )
            }
        }
    }

    if (isDialogVisible) {
        ProjectSearchDialog(
            projectsList,
            authRepository = authRepository,
            closeDialog = { isDialogVisible = false }
        ) { project -> selectedProject = project }
    }
}

@Composable
fun ProjectSearchDialog(
    projectsList: List<Project>,
    authRepository: AuthRepository,
    closeDialog: () -> Unit,
    onProjectSelected: (Project) -> Unit
) {
    Dialog(onDismissRequest = { closeDialog() }) {
        Card(
            modifier = Modifier.padding(10.dp),
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(if (projectsList.size > 0) 0.7f else 0.35f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Lista De Proyectos",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = myBlue
                )
                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = Color.Black, thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))
                if (projectsList.size > 0) {
                    Text(
                        text = "Estos son los proyectos creados actualmente",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                        textAlign = TextAlign.Center
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.7f),
                        userScrollEnabled = true
                    ) {
                        items(projectsList.sortedBy { it.projectName }) { proyect ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = Color.LightGray, thickness = 1.dp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .clickable {
                                        onProjectSelected(proyect!!)
                                        closeDialog()
                                    },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "${proyect.projectName}",
                                    modifier = Modifier.offset(x = 10.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { closeDialog() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = myOrangehigh,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .width(100.dp)
                                .offset(y = 20.dp)
                                .padding(start = 0.dp, top = 5.dp, end = 5.dp, bottom = 0.dp)
                        ) {
                            Text(
                                text = "Cerrar",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No hay proyectos creados",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun TabsRow(currentSelection: String, onTabSelected: (String) -> Unit) {
    val tabs = listOf(
        TabData("Resumen del Presupuesto", Icons.Default.RealEstateAgent),
        TabData("Gestión de Compras", Icons.Default.ShoppingCart),
        TabData("Gestión de Suministros y Materiales",Icons.Default.AssuredWorkload),
        TabData("Provedores", Icons.Default.People),
        TabData("Especificaciones Técnicas", Icons.Default.Checklist),
        TabData("Criterios de Cuantificación", Icons.Default.Computer),
        TabData("Historial y Reportes", Icons.Default.History),
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tabs) { tabData ->
            TabItem(
                title = tabData.title,
                icon = tabData.icon,
                isSelected = currentSelection == tabData.title
            ) {
                onTabSelected(tabData.title)
            }
        }
    }
}
@Composable
fun TabItem(title: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) lightblue else myBlue
        )
        GradientText(title, isSelected)
    }
}
@Composable
fun GradientText(text: String, isSelected: Boolean) {
    Text(
        text = text,
        style = if (isSelected) {
            TextStyle(fontWeight = FontWeight.Bold) // Texto en negrita si está seleccionado
        } else {
            TextStyle(fontWeight = FontWeight.Normal) // Texto normal si no está seleccionado
        },
        color = if (isSelected) lightblue else myBlue
    )
}

data class TabData(val title: String, val icon: ImageVector)
