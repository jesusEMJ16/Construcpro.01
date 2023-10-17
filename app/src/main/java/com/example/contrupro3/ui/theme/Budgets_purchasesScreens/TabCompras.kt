package com.example.contrupro3.ui.theme.Budgets_purchasesScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.contrupro3.ui.theme.Budgets_purchasesScreens.PresupuestoModels.Compra
import androidx.compose.runtime.*
import androidx.compose.ui.layout.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.example.contrupro3.ui.theme.myBlue

@Composable
fun ComprasScreen() {
    val listaDeCompras = listOf(
        Compra(1,"asdasdadasdadadadsCemento", "01/01/2023", "Proveedor A", 100000, 100.0, 1000.0),
        Compra(2,"Hierro", "02/01/2023", "Proveedor B", 5, 200.0, 1000.0),
    )

    val selectedRows = remember { mutableStateMapOf<Int, Boolean>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Gestión de Compras", style = MaterialTheme.typography.bodySmall)

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(top = 8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(rememberScrollState())
            ) {
                Column {
                    // Encabezados
                    HeaderRow()
                    listaDeCompras.forEach { compra ->
                        CompraItem(compra,selectedRows)
                        Divider(modifier = Modifier.padding(vertical = 4.dp))  // Separador entre ítems
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                onClick = { },
                containerColor = myBlue,
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar Compra",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun HeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Producto", fontWeight = FontWeight.Bold, modifier = Modifier.width(150.dp), textAlign = TextAlign.Center)
        Text(text = "Fecha de compra", fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp), textAlign = TextAlign.Center)
        Text(text = "Proveedor", fontWeight = FontWeight.Bold, modifier = Modifier.width(150.dp), textAlign = TextAlign.Center)
        Text(text = "Cantidad", fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp), textAlign = TextAlign.Center)
        Text(text = "Precio Unitario", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp), textAlign = TextAlign.Center)
        Text(text = "Precio Total", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp), textAlign = TextAlign.Center)
    }
}

@Composable
fun CompraItem(compra: Compra, selectedRows: SnapshotStateMap<Int, Boolean>) {
    val isSelected = selectedRows[compra.id] ?: false
    val backgroundColor = if (isSelected) Color.LightGray else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(backgroundColor)
            .clickable {
                selectedRows[compra.id] = !isSelected
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Divider(color = Color.LightGray, thickness = 1.dp)
            Text(
                text = compra.nombre,
                modifier = Modifier.width(150.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = compra.fecha,
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = compra.proveedor,
                modifier = Modifier.width(150.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "${compra.cantidad}",
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "${compra.precioUnitario}",
                modifier = Modifier.width(120.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "${compra.precioTotal}",
                modifier = Modifier.width(120.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview
fun aspreviewas(){
    ComprasScreen()
}
