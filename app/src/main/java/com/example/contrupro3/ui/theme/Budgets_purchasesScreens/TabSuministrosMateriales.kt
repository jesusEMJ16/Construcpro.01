package com.example.contrupro3.ui.theme.Budgets_purchasesScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.BudgetModels.Materialmodel
import com.example.contrupro3.models.BudgetModels.SuministrosViewModel
import com.example.contrupro3.ui.theme.myBlue
import com.example.contrupro3.ui.theme.myOrangehigh


@Composable
fun SuministrosScreen(
    viewModel: SuministrosViewModel,
    authRepository: AuthRepository,
    onOpenSelectProject: () -> Unit
) {

    // Suponiendo que tienes un ViewModel que maneja la lógica de negocio
    val listaMateriales by viewModel.listaMateriales.observeAsState(initial = emptyList())
    val showDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ListaMateriales(materiales = listaMateriales)
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .offset(x = 3.dp, y = -7.dp),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                onClick = { onOpenSelectProject() },
                containerColor = myOrangehigh
            ) {
                androidx.compose.material.Icon(
                    Icons.Default.FolderOpen,
                    contentDescription = "Seleccionar Proyecto",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            FloatingActionButton(
                onClick = { showDialog.value = true },
                containerColor = myBlue,
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar lista",
                    tint = Color.White
                )
            }
            if (showDialog.value) {
                ListaDialog(onDismissRequest = { showDialog.value = false }, viewModel)
            }
        }
    }
}

@Composable
fun AgregarMaterialForm(onAgregarMaterial: (Materialmodel) -> Unit) {
    // Variables para almacenar el estado de los inputs del formulario
    var id: String by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var unidadDeMedida by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var costoEstimado by remember { mutableStateOf("") }
    var costoReal by remember { mutableStateOf("") }
    var proveedor by remember { mutableStateOf("") }
    var estadoPedido by remember { mutableStateOf("") }

    Column {
        Spacer(modifier = Modifier.height(5.dp))
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del Material") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = cantidad,
            onValueChange = { cantidad = it },
            label = { Text("Cantidad") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = unidadDeMedida,
            onValueChange = { unidadDeMedida = it },
            label = { Text("Unidad de medida") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoria") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = costoEstimado,
            onValueChange = { costoEstimado = it },
            label = { Text("Costo estimado") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = proveedor,
            onValueChange = { proveedor = it },
            label = { Text("Proveedor") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

@Composable
fun ListaMateriales(materiales: List<Materialmodel>) {
    LazyColumn {
        items(materiales) { material ->
            MaterialItem(material)
        }
    }
}

@Composable
fun MaterialItem(material: Materialmodel) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${material.nombre}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Descripción: ${material.descripcion}")
            Text(text = "Cantidad: ${material.descripcion}")
            Text(text = "Unidad de medida: ${material.descripcion}")
            Text(text = "Categoria: ${material.descripcion}")
            Text(text = "Costo estimado: ${material.descripcion}")
            Text(text = "Costo real: ${material.descripcion}")
            Text(text = "Proveedor: ${material.descripcion}")
        }
    }
}


@Composable
fun ListaDialog(onDismissRequest: () -> Unit, viewModel: SuministrosViewModel) {

    var id: String by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var unidadDeMedida by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var costoEstimado by remember { mutableStateOf("") }
    var costoReal by remember { mutableStateOf("") }
    var proveedor by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    "Añadir lista de materiales necesarios",
                    style = TextStyle(
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)
                )
            },
            text = {
                AgregarMaterialForm(onAgregarMaterial = { material ->
                    viewModel.agregarMaterial(
                        Materialmodel(
                            id,
                            nombre,
                            descripcion,
                            cantidad,
                            unidadDeMedida,
                            categoria,
                            costoEstimado,
                            costoReal,
                            proveedor
                        )
                    )
                })
            },

            confirmButton = {
                androidx.compose.material.Button(
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = myBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                androidx.compose.material.Button(
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = myBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
