package com.example.contrupro3.ui.theme.Budgets_purchasesScreens

import android.util.Log
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
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RealEstateAgent
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Support
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.contrupro3.modelos.AuthRepository
import com.example.contrupro3.modelos.Project
import com.example.contrupro3.ui.theme.HamburgueerMenu
import com.example.contrupro3.ui.theme.myOrange
import com.google.android.material.tabs.TabItem

@Composable
fun Presupuesto_y_Compras(navController: NavController, authRepository: AuthRepository) {
    // Variable para almacenar la selección actual (Presupuestos o Compras)
    var currentSelection by remember { mutableStateOf("Presupuestos") }
    var isProjectDialogOpen by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column {
            ProyectoSelectionHeader(authRepository)
            // Barra de navegación/tabulación

            TabsRow(currentSelection) { selection ->
                currentSelection = selection
            }
            // Contenido principal basado en la selección
            Box(modifier = Modifier.weight(1f)) {
                when (currentSelection) {
                    "Resumen del Presupuesto" -> ResumenScreen()
                    "Gestión de Compras" -> ComprasScreen()
                    "Gestión de Suministros y Materiales" -> SuministrosScreen()
                    "Provedores" -> ProveedoresScreen()
                    "Especificaciones Técnicas" -> EspecificacionesScreen()
                    "Criterios de Cuantificación" -> CriteriosScreen()
                    "Historial y Reportes" -> HistorialScreen()
                }
            }
        }
    }
    HamburgueerMenu(navController = navController, authRepository = authRepository)
}
@Composable
fun ProyectoSelectionHeader(authRepository: AuthRepository) {
    var isDialogVisible by remember { mutableStateOf(false) }
    var selectedProject by remember { mutableStateOf("") }

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
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = selectedProject,
                    onValueChange = { newText ->
                        selectedProject = newText
                    },
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        Log.d("MiApp", "TextField clicked!")
                                        isDialogVisible = true
                                    }
                                }
                            }
                        },
                    label = { Text("Selecciona tu proyecto") }
                )
            }
        }
    }

    if (isDialogVisible) {
        Log.d("MiApp", "Rendering dialog!")
        ProjectSearchDialog(
            authRepository = authRepository,
            isDialogOpen = isDialogVisible,
            closeDialog = {
                isDialogVisible = false
                Log.d("MiApp", "Dialog dismissed!")
            },
            onProjectSelected = { projectId, projectName ->
                selectedProject = projectName
                Log.d("MiApp", "Project selected: $projectName with ID: $projectId")
            }
        )
    }
}

@Composable
fun ProjectSearchDialog(
    authRepository: AuthRepository,
    isDialogOpen: Boolean,
    closeDialog: () -> Unit,
    onProjectSelected: (String, String) -> Unit
) {
    val projectsList = remember { mutableStateOf(emptyList<Project>()) }
    var selectedProjectId by remember { mutableStateOf<String?>(null) }
    var selectedProjectName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        authRepository.loadProjectsFromFirebase(projectsList)
    }

    if (isDialogOpen) {
        Dialog(onDismissRequest = closeDialog) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Añadir Proyectos",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(projectsList.value.sortedBy { it.projectName }) { project ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedProjectId = project.id.toString()
                                        selectedProjectName = project.projectName
                                    },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "${project.projectName}")
                                RadioButton(
                                    selected = selectedProjectId == project.id.toString(),
                                    onClick = {
                                        selectedProjectId = project.id.toString()
                                        selectedProjectName = project.projectName
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = closeDialog,
                        ) {
                            Text(text = "Cancelar")
                        }
                        Button(
                            onClick = {
                                if (selectedProjectId != null && selectedProjectName != null) {
                                    onProjectSelected(selectedProjectId!!, selectedProjectName!!)
                                    closeDialog()
                                }
                            },
                            enabled = selectedProjectId != null,
                        ) {
                            Text(text = "Seleccionar")
                        }
                    }
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
        TabData("Gestión de Suministros y Materiales", Icons.Default.Support),
        TabData("Provedores", Icons.Default.Support),
        TabData("Especificaciones Técnicas", Icons.Default.Support),
        TabData("Criterios de Cuantificación", Icons.Default.Support),
        TabData("Historial y Reportes", Icons.Default.Support),
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
            tint = if (isSelected) myOrange else Color.Black
        )
        GradientText(title, isSelected)
    }
}
@Composable
fun GradientText(text: String, isSelected: Boolean) {
    Text(
        text = text,
        color = if (isSelected) myOrange else Color.Black
    )
}

data class TabData(val title: String, val icon: ImageVector)
