package com.example.contrupro3.ui.theme.Budgets_purchasesScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.contrupro3.ui.theme.Budgets_purchasesScreens.PresupuestoModels.Compra

@Composable
fun ComprasScreen() {

    val listaDeCompras = listOf(
        Compra(1, "01/01/2023", "Proveedor A", 10, 100.0, 1000.0),
        Compra(2, "02/01/2023", "Proveedor B", 5, 200.0, 1000.0),
        // ... otros objetos Compra
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Gestión de Compras", style = MaterialTheme.typography.bodySmall)

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(listaDeCompras) { compra ->
                CompraItem(compra)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { /* Navegar al historial de transacciones */ }) {
                Text("Historial de Transacciones")
            }
            Button(onClick = { /* Mostrar diálogo para agregar nueva compra */ }) {
                Text("Agregar Compra")
            }
        }
    }
}

@Composable
fun CompraItem(compra: Compra) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = compra.fecha)
        Text(text = compra.proveedor)
        Text(text = "${compra.cantidad}")
        Text(text = "${compra.precioUnitario}")
        Text(text = "${compra.precioTotal}")
        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", /* Acción para editar */)
        Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", /* Acción para eliminar */)
    }
}
