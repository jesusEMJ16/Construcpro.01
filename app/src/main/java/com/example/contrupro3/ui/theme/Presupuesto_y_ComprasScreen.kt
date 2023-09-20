package com.example.contrupro3.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.contrupro3.modelos.AuthRepository
@Composable
fun Presupuesto_y_Compras(navController: NavController, authRepository: AuthRepository) {
    // Variable para almacenar la selección actual (Presupuestos o Compras)
    var currentSelection by remember { mutableStateOf("Presupuestos") }
    var isProjectDialogOpen by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column {
            ProyectoSelectionHeader()
            // Barra de navegación/tabulación
            TabsRow(currentSelection) { selection ->
                currentSelection = selection
            }
            // Contenido principal basado en la selección
            Box(modifier = Modifier.weight(1f)) {
                when (currentSelection) {
                    "Presupuestos" -> PresupuestosContent()
                    "Compras" -> ComprasContent()
                }
            }
        }
    }
    HamburgueerMenu(navController = navController, authRepository = authRepository)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectoSelectionHeader() {
    var isDialogVisible by remember { mutableStateOf(false) }
    var selectedProject by remember { mutableStateOf("") }
    val proyectosMenu1 = listOf("Proyecto 1A", "Proyecto 2A", "Proyecto 3A")
    val proyectosMenu2 = listOf("Proyecto 1B", "Proyecto 2B", "Proyecto 3B")
    var isMenu1Expanded by remember { mutableStateOf(false) }
    var isMenu2Expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(color = Color.Transparent)
    ) {
        Button(onClick = { isMenu1Expanded = true }) {
            Text("Mostrar Menú 1")
        }

        DropdownMenu(
            expanded = isMenu1Expanded,
            onDismissRequest = { isMenu1Expanded = false }
        ) {
            proyectosMenu1.forEach { proyecto ->
                DropdownMenuItem(onClick = {
                    // TODO: Acción al seleccionar el proyecto
                    selectedProject = proyecto
                    isMenu1Expanded = false
                }) {
                    Text(proyecto)
                }
            }
        }

        //Segund  o DropdownMenu
        Button(onClick = { isMenu2Expanded = true }) {
            Text("Mostrar Menú 2")
        }

        DropdownMenu(
            expanded = isMenu2Expanded,
            onDismissRequest = { isMenu2Expanded = false }
        ) {
            proyectosMenu2.forEach { proyecto ->
                DropdownMenuItem(onClick = {
                    // TODO: Acción al seleccionar el proyecto
                    selectedProject = proyecto
                    isMenu2Expanded = false
                }) {
                    Text(proyecto)
                }
            }
        }
    }
}

@Composable
fun SelectProjectDialog(isDialogOpen: Boolean, closeDialog: () -> Unit) {
    if (isDialogOpen) {
        Dialog(onDismissRequest = closeDialog) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
            ) {
                // Tu contenido aquí
            }
        }
    }
}

@Composable
fun TabsRow(currentSelection: String, onTabSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TabItem(title = "Presupuestos", isSelected = currentSelection == "Presupuestos") {
            onTabSelected("Presupuestos")
        }
        TabItem(title = "Compras", isSelected = currentSelection == "Compras") {
            onTabSelected("Compras")
        }
    }
}

@Composable
fun TabItem(title: String, isSelected: Boolean, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(
            text = title,
            color = if (isSelected) myOrange else Color.Black
        )
    }
}

@Composable
fun PresupuestosContent() {
    // Contenido detallado para la sección de Presupuestos
    // Aquí puedes agregar listas, gráficos, formularios, etc.
}

@Composable
fun ComprasContent() {
    // Contenido detallado para la sección de Compras
    // Aquí puedes agregar listas, gráficos, formularios, etc.
}