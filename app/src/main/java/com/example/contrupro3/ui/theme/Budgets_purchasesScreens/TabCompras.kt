package com.example.contrupro3.ui.theme.Budgets_purchasesScreens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.BudgetModels.PurchasesModel
import com.example.contrupro3.models.ProjectsModels.Project
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnrememberedMutableState")
@Composable
fun ComprasScreen(
    authRepository: AuthRepository,
    userId: String,
    selectedProject: Project?
) {
    val purchasesList = remember { mutableStateOf<List<PurchasesModel>>(emptyList()) }
    authRepository.loadPurchasesFromFirebase(selectedProject?.id.toString(), purchasesList)
    val showDialog = remember { mutableStateOf(false) }
    val selectedRows = remember { mutableStateMapOf<String, Boolean>() }
    val currentSelectedPurchasesToDelete = remember { mutableStateListOf<PurchasesModel>() }
    val showDeleteDialog = remember { mutableStateOf(false) }

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
                    purchasesList.value.forEach { compra ->
                        CompraItem(
                            compra,
                            selectedRows,
                            currentSelectedPurchasesToDelete,
                            { purchase -> currentSelectedPurchasesToDelete.add(purchase) }
                        ) { purchase -> currentSelectedPurchasesToDelete.remove(purchase) }
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
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
            if (currentSelectedPurchasesToDelete.size > 0) {
                FloatingActionButton(
                    onClick = {
                        showDeleteDialog.value = false
                        currentSelectedPurchasesToDelete.clear()
                        selectedRows.clear()
                    },
                    containerColor = Color(0xFF233B40),
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cancelar",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                FloatingActionButton(
                    onClick = { showDeleteDialog.value = true },
                    containerColor = Color(0xFF233B40),
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar Compras",
                        tint = Color.White
                    )
                }
            } else {
                FloatingActionButton(
                    onClick = { showDialog.value = true },
                    containerColor = Color(0xFF233B40),
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Agregar Compra",
                        tint = Color.White
                    )
                }
            }

            if (showDialog.value) {
                CompraDialog(
                    userId,
                    selectedProject?.id,
                    onDismissRequest = { showDialog.value = false })
            }
            if (showDeleteDialog.value) {
                DeletePurchasesDialog(
                    currentSelectedPurchasesToDelete,
                    userId,
                    selectedProject?.id.toString(),
                    {
                        showDeleteDialog.value = false
                        currentSelectedPurchasesToDelete.clear()
                        selectedRows.clear()
                    }) {
                    purchasesList.value = purchasesList.value.filter {
                        !currentSelectedPurchasesToDelete.contains(it)
                    }
                    showDeleteDialog.value = false
                    currentSelectedPurchasesToDelete.clear()
                    selectedRows.clear()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeletePurchasesDialog(
    currentSelectedPurchasesToDelete: SnapshotStateList<PurchasesModel>,
    userId: String,
    projectId: String,
    onDismiss: () -> Unit,
    onDeletedPurchases: () -> Unit
) {
    val currentContext = LocalContext.current

    AlertDialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.shadow(4.dp, clip = true)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                    Text(
                        text = "Advertencia",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF233B40)
                    )
                }
                if (currentSelectedPurchasesToDelete.size === 1) {
                    Text(text = "Se va a eliminar 1 compra de la lista. ¿Esta Seguro?")
                } else Text(text = "Se van a eliminar ${currentSelectedPurchasesToDelete.size} compras de la lista. ¿Esta Seguro?")
                Spacer(Modifier.padding(horizontal = 30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { onDismiss() }) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                    Button(onClick = {
                        DeletePurchasesFromDatabase(
                            userId,
                            projectId,
                            currentSelectedPurchasesToDelete,
                            currentContext,
                        ) { onDeletedPurchases() }
                    }) {
                        Text("Aceptar")
                    }
                }
            }
        }
    }
}

fun DeletePurchasesFromDatabase(
    userId: String,
    projectId: String,
    currentSelectedPurchasesToDelete: SnapshotStateList<PurchasesModel>,
    currentContext: Context,
    onDeletedPurchases: () -> Unit
) {
    val firebase = FirebaseFirestore.getInstance()
    val collection =
        firebase.collection("Usuarios")
            .document(userId)
            .collection("Proyectos")
            .document(projectId)
            .collection("Purchases")

    for (purchase in currentSelectedPurchasesToDelete) {
        collection.document(purchase.id!!).delete()
    }

    onDeletedPurchases()
    Toast.makeText(
        currentContext,
        "Compras eliminadas correctamente",
        Toast.LENGTH_LONG
    ).show()
}

@Composable
fun CompraDialog(userId: String, projectId: String?, onDismissRequest: () -> Unit) {
    val producto = remember { mutableStateOf("") }
    val fechaCompra = remember { mutableStateOf("") }
    val proveedor = remember { mutableStateOf("") }
    val cantidad = remember { mutableStateOf("") }
    val precioUnitario = remember { mutableStateOf("") }
    val currentContext = LocalContext.current

    Dialog(onDismissRequest = onDismissRequest) {
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Añadir Compra",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF233B40)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = Color.Black, thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Rellena los campos para agregar tu compra",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                    textAlign = TextAlign.Center
                )
                addcompra(
                    producto,
                    fechaCompra,
                    proveedor,
                    cantidad,
                    precioUnitario,
                    { product -> producto.value = product },
                    { date -> fechaCompra.value = date },
                    { supplier -> proveedor.value = supplier },
                    { quiantity -> cantidad.value = quiantity }
                ) { unitPrice -> precioUnitario.value = unitPrice }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onDismissRequest() },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF233B40),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.padding(
                            start = 0.dp,
                            top = 5.dp,
                            end = 5.dp,
                            bottom = 0.dp
                        )
                    ) {
                        Text(
                            text = "Cancelar",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Button(
                        onClick = {
                            val firebase = FirebaseFirestore.getInstance()
                            val collection = firebase
                                .collection("Usuarios")
                                .document(userId)
                                .collection("Proyectos")
                                .document(projectId!!)
                                .collection("Purchases")

                            val newPurchase = PurchasesModel(
                                name = producto.value,
                                date = null,
                                supplier = proveedor.value,
                                quantity = cantidad.value.toInt(),
                                priceUnit = precioUnitario.value.toDouble(),
                                totalPrice = precioUnitario.value.toDouble() * cantidad.value.toInt()
                            )

                            collection.add(newPurchase).addOnSuccessListener {
                                val doc = newPurchase.copy(id = it.id)
                                collection.document(it.id).set(doc).addOnSuccessListener {
                                    onDismissRequest()
                                    Toast.makeText(
                                        currentContext,
                                        "Compra agregada correctamente",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF233B40),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.padding(
                            start = 0.dp,
                            top = 5.dp,
                            end = 5.dp,
                            bottom = 0.dp
                        ),
                        enabled = producto.value.length > 0
                    ) {
                        Text(
                            text = "Agregar",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun addcompra(
    producto: MutableState<String>,
    fechaCompra: MutableState<String>,
    proveedor: MutableState<String>,
    cantidad: MutableState<String>,
    precioUnitario: MutableState<String>,
    productChanged: (String) -> Unit,
    dateChanged: (String) -> Unit,
    supplierChanged: (String) -> Unit,
    quiantityChanged: (String) -> Unit,
    unitPriceChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 15.dp)
    ) {
        TextField(
            value = producto.value,
            onValueChange = { productChanged(it) },
            label = { Text("Nombre del producto") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            maxLines = 1,
            singleLine = true
        )
        TextField(
            value = proveedor.value,
            onValueChange = { supplierChanged(it) },
            label = { Text("Nombre del proveedor") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            maxLines = 1,
            singleLine = true
        )
        TextField(
            value = cantidad.value,
            onValueChange = { quiantityChanged(it) },
            label = { Text("Cantidad") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        TextField(
            value = precioUnitario.value,
            onValueChange = { unitPriceChanged(it) },
            label = { Text("Precio Unitario") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal
            )
        )
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
        Text(
            text = "Producto",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(150.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Fecha de compra",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(100.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Proveedor",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(150.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Cantidad",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(100.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Precio Unitario",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(120.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Precio Total",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(120.dp),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompraItem(
    compra: PurchasesModel,
    selectedRows: SnapshotStateMap<String, Boolean>,
    currentSelectedPurchasesToDelete: SnapshotStateList<PurchasesModel>,
    addSelection: (PurchasesModel) -> Unit,
    removeSelection: (PurchasesModel) -> Unit
) {
    val isSelected = selectedRows[compra.id!!] ?: false
    val backgroundColor = if (isSelected) Color.LightGray else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    if (currentSelectedPurchasesToDelete.contains(compra))
                        removeSelection(compra) else addSelection(compra)
                    selectedRows[compra.id!!] = !isSelected
                }
            )
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Divider(color = Color.LightGray, thickness = 1.dp)
            Text(
                text = compra.name.toString(),
                modifier = Modifier.width(150.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = compra.date.toString(),
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = if (compra.supplier?.length === 0) "-" else "${compra.supplier}",
                modifier = Modifier.width(150.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "${compra.quantity}",
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "${compra.priceUnit}",
                modifier = Modifier.width(120.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "${compra.totalPrice}",
                modifier = Modifier.width(120.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
