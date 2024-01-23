package com.example.contrupro3.ui.theme.Budgets_purchasesScreens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssuredWorkload
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RealEstateAgent
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contrupro3.ProjectSelection
import com.example.contrupro3.R
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.BudgetModels.SuministrosViewModel
import com.example.contrupro3.models.ProjectsModels.ProjectModel
import com.example.contrupro3.ui.theme.Menu.HamburgueerMenu
import com.example.contrupro3.ui.theme.backgroundButtonColor
import com.example.contrupro3.ui.theme.contentButtonColor
import com.example.contrupro3.ui.theme.myBlue

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Presupuesto_y_Compras(
    navController: NavController,
    authRepository: AuthRepository,
    userId: String
) {
    // Variable para almacenar la selección actual (Presupuestos o Compras)
    val currentSelection = remember { mutableStateOf("Budget Summary") }
    val selectedProject = remember { mutableStateOf<ProjectModel?>(null) }
    val openSelectProjectsDialog = remember { mutableStateOf(true) }
    val showFloatingButtons = remember { mutableStateOf(true) }

    Scaffold(
        floatingActionButton = {
            if(showFloatingButtons.value) {
                CompositionLocalProvider(
                    LocalContentColor provides colorResource(id = R.color.white)
                ) {
                    FloatingActionButton(
                        onClick = { openSelectProjectsDialog.value = true },
                        containerColor = backgroundButtonColor,
                        contentColor = contentButtonColor
                    ) {
                        Icon(
                            Icons.Default.FolderOpen,
                            contentDescription = "Seleccionar Proyecto"
                        )
                    }
                }
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Presupuestos y Compras",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    )
                    if (selectedProject.value !== null) Text(
                        text = "(${selectedProject.value?.name})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    if (selectedProject.value == null) {
                        Text(
                            text = "No se ha seleccionado un proyecto.",
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    } else {
                        if (selectedProject.value != null) {
                            TabsRow(currentSelection.value) { selection -> currentSelection.value = selection }
                            Box(modifier = Modifier.weight(1f)) {
                                when (currentSelection.value) {
                                    "Budget Summary" -> { // Resumen del presupuesto
                                        showFloatingButtons.value = false
                                        ResumenScreen(
                                            authRepository,
                                            userId,
                                            selectedProject
                                        ) { openSelectProjectsDialog.value = true }
                                    }
                                    "Purchasing Management" -> { // Gestion de compras
                                        showFloatingButtons.value = false
                                        ComprasScreen(
                                            authRepository,
                                            userId,
                                            selectedProject,
                                        ) { openSelectProjectsDialog.value = true }
                                    }
                                    "Supply and Materials Management" -> { // Gestion de suministros y materiales
                                        showFloatingButtons.value = false
                                        SuministrosScreen(
                                            SuministrosViewModel(),
                                            authRepository
                                        ) { openSelectProjectsDialog.value = true }
                                    }
                                    "Suppliers" -> { // Proveedores
                                        showFloatingButtons.value = false
                                        ProveedoresScreen(
                                            authRepository,
                                            userId,
                                            selectedProject
                                        ) { openSelectProjectsDialog.value = true }
                                    }
                                    "Technical Specifications" -> { // Especificaciones tecnicas
                                        showFloatingButtons.value = false
                                        EspecificacionesScreen(
                                            authRepository,
                                            userId,
                                            selectedProject
                                        ) { openSelectProjectsDialog.value = true }
                                    }
                                    "Quantification Criteria" -> { // Criterios de cuantificacion
                                        showFloatingButtons.value = false
                                        CriteriosScreen(
                                            authRepository,
                                            userId,
                                            selectedProject
                                        ) { openSelectProjectsDialog.value }
                                    }
                                    "History and Reports" -> { // Historial y reportes
                                        showFloatingButtons.value = false
                                        HistorialScreen(
                                            authRepository,
                                            userId,
                                            selectedProject
                                        ) { openSelectProjectsDialog.value = true }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )

    if (openSelectProjectsDialog.value) ProjectSelection(
        userID = userId,
        authRepository = authRepository,
        onDismiss = { openSelectProjectsDialog.value = false },
        onProjectSelected = { p -> selectedProject.value = p }
    )
    HamburgueerMenu(navController = navController, authRepository = authRepository)
}

@Composable
fun TabsRow(currentSelection: String, onTabSelected: (String) -> Unit) {
    val tabs = listOf(
        TabData("Resumen del Presupuesto", "Budget Summary", Icons.Default.RealEstateAgent),
        TabData("Gestión de Compras", "Purchasing Management", Icons.Default.ShoppingCart),
        TabData("Gestión de Suministros y Materiales", "Supply and Materials Management", Icons.Default.AssuredWorkload),
        TabData("Provedores", "Suppliers", Icons.Default.People),
        TabData("Especificaciones Técnicas", "Technical Specifications", Icons.Default.Checklist),
        TabData("Criterios de Cuantificación", "Quantification Criteria", Icons.Default.Computer),
        TabData("Historial y Reportes", "History and Reports", Icons.Default.History),
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
                isSelected = currentSelection == tabData.titleID
            ) {
                onTabSelected(tabData.titleID)
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
            tint = if (isSelected) backgroundButtonColor else myBlue
        )
        GradientText(title, isSelected)
    }
}

@Composable
fun GradientText(text: String, isSelected: Boolean) {
    Text(
        text = text,
        style = if (isSelected) {
            TextStyle(fontWeight = FontWeight.Black, textDecoration = TextDecoration.Underline) // Texto en negrita si está seleccionado
        } else {
            TextStyle(fontWeight = FontWeight.Normal) // Texto normal si no está seleccionado
        },
        color = if (isSelected) backgroundButtonColor else myBlue
    )
}

data class TabData(
    val title: String,
    val titleID: String,
    val icon: ImageVector
)
