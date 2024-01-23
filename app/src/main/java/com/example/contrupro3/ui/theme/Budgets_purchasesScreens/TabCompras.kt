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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderOpen
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.contrupro3.models.BudgetModels.Purchases.PurchasesModel
import com.example.contrupro3.models.BudgetModels.Purchases.Purchases_ViewModel
import com.example.contrupro3.models.ProjectsModels.ProjectModel
import com.example.contrupro3.ui.theme.backgroundButtonColor
import com.example.contrupro3.ui.theme.contentButtonColor
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnrememberedMutableState")
@Composable
fun ComprasScreen(
    authRepository: AuthRepository,
    userId: String,
    selectedProject: MutableState<ProjectModel?>,
    onOpenSelectProject: () -> Unit
) {
    val Purchases_ViewModel = Purchases_ViewModel()
    val purchasesList = remember { mutableStateOf<List<PurchasesModel>>(emptyList()) }
    authRepository.loadPurchasesFromFirebase(selectedProject.value?.id.toString(), purchasesList)
    val selectedRows = remember { mutableStateMapOf<String, Boolean>() }
    val showDeleteDialog = Purchases_ViewModel.showDeleteDialog.observeAsState(false)
    val showAddDialog = Purchases_ViewModel.showDialog.observeAsState(false)
    val currentSelectedPurchasesToDelete = Purchases_ViewModel
        .currentSelectedPurchasesToDelete.observeAsState(emptyList())

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
                            Purchases_ViewModel
                        )
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        CustomPurchasesFloatingActionButton(
            userId,
            purchasesList,
            selectedProject,
            selectedRows,
            Purchases_ViewModel
        ) { onOpenSelectProject() }

        if (showAddDialog.value) {
            CompraDialog(
                userId,
                selectedProject.value?.id,
                Purchases_ViewModel
            )
        }
        if (showDeleteDialog.value) {
            DeletePurchasesDialog(
                userId,
                selectedProject.value?.id.toString(),
                Purchases_ViewModel,
                purchasesList,
                currentSelectedPurchasesToDelete.value
            ) {
                selectedRows.clear()
                Purchases_ViewModel.onSelectedPurchasesToDeleteUpdated(emptyList())
                Purchases_ViewModel.onDialogsUpdated(false, false)
            }
        }
    }
}


@Composable
fun CustomPurchasesFloatingActionButton(
    userId: String,
    purchasesList: MutableState<List<PurchasesModel>>,
    selectedProject: MutableState<ProjectModel?>,
    selectedRows: SnapshotStateMap<String, Boolean>,
    Purchases_ViewModel: Purchases_ViewModel,
    onOpenSelectProject: () -> Unit
) {
    val currentSelectedPurchasesToDelete = Purchases_ViewModel
        .currentSelectedPurchasesToDelete.observeAsState(emptyList())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .offset(x = 3.dp, y = -7.dp),
        horizontalArrangement = Arrangement.End
    ) {
        if (currentSelectedPurchasesToDelete.value.size > 0) {
            FloatingActionButton(
                onClick = {
                    Purchases_ViewModel.onDialogsUpdated(false, false)
                    Purchases_ViewModel.onSelectedPurchasesToDeleteUpdated(emptyList())
                    selectedRows.clear()
                },
                containerColor = backgroundButtonColor,
                contentColor = contentButtonColor
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Cancelar",
                    tint = contentButtonColor
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            FloatingActionButton(
                onClick = { Purchases_ViewModel.onDialogsUpdated(true, false) },
                containerColor = backgroundButtonColor,
                contentColor = contentButtonColor
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar Compras",
                    tint = contentButtonColor
                )
            }
        } else {
            FloatingActionButton(
                onClick = { onOpenSelectProject() },
                containerColor = backgroundButtonColor,
                contentColor = contentButtonColor
            ) {
                Icon(
                    Icons.Default.FolderOpen,
                    contentDescription = "Seleccionar Proyecto",
                    tint = contentButtonColor
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            FloatingActionButton(
                onClick = { Purchases_ViewModel.onDialogsUpdated(false, true) },
                containerColor = backgroundButtonColor,
                contentColor = contentButtonColor
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar Compra",
                    tint = contentButtonColor
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeletePurchasesDialog(
    userId: String,
    projectId: String,
    Purchases_ViewModel: Purchases_ViewModel,
    purchasesList: MutableState<List<PurchasesModel>>,
    currentSelectedPurchasesToDelete: List<PurchasesModel>,
    onDismiss: () -> Unit
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
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = backgroundButtonColor,
                            contentColor = contentButtonColor
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Cancelar", color = contentButtonColor)
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                    Button(
                        onClick = {
                            DeletePurchasesFromDatabase(
                                userId,
                                projectId,
                                currentContext,
                                currentSelectedPurchasesToDelete.toList()
                            ) {
                                purchasesList.value = purchasesList.value.filter { c ->
                                    !currentSelectedPurchasesToDelete.contains(c)
                                }
                                onDismiss()
                                Purchases_ViewModel.onSelectedPurchasesToDeleteUpdated(emptyList())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = backgroundButtonColor,
                            contentColor = contentButtonColor
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Aceptar", color = contentButtonColor)
                    }
                }
            }
        }
    }
}

fun DeletePurchasesFromDatabase(
    userId: String,
    projectId: String,
    currentContext: Context,
    currentSelectedPurchasesToDelete: List<PurchasesModel>,
    onDeletedPurchases: () -> Unit
) {
    val firebase = FirebaseFirestore.getInstance()
    val collection =
        firebase.collection("Users")
            .document(userId)
            .collection("Projects")
            .document(projectId)
            .collection("Purchases")

    for (purchase in currentSelectedPurchasesToDelete) {
        collection.document(purchase.id!!).delete()
    }

    onDeletedPurchases()
    Toast.makeText(
        currentContext,
        "Compras eliminadas",
        Toast.LENGTH_SHORT
    ).show()
}

@Composable
fun CompraDialog(userId: String, projectId: String?, Purchases_ViewModel: Purchases_ViewModel) {
    val producto = remember { mutableStateOf("") }
    val fechaCompra = remember { mutableStateOf("") }
    val proveedor = remember { mutableStateOf("") }
    val cantidad = remember { mutableStateOf("") }
    val precioUnitario = remember { mutableStateOf("") }
    val currentContext = LocalContext.current

    Dialog(onDismissRequest = { Purchases_ViewModel.onDialogsUpdated(false, false) }) {
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
                        onClick = { Purchases_ViewModel.onDialogsUpdated(false, false) },
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
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                    Button(
                        onClick = {
                            val firebase = FirebaseFirestore.getInstance()
                            val collection = firebase
                                .collection("Users")
                                .document(userId)
                                .collection("Projects")
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
                                    Purchases_ViewModel.onDialogsUpdated(false, false)
                                    Toast.makeText(
                                        currentContext,
                                        "Compra agregada",
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
                        enabled = producto.value.length > 0 && cantidad.value != null && precioUnitario.value != null
                    ) {
                        Text(
                            text = "Agregar",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
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
    Purchases_ViewModel: Purchases_ViewModel
) {
    val isSelected = selectedRows[compra.id!!] ?: false
    val backgroundColor = if (isSelected) Color(0x328A8A8A) else Color.Transparent
    val currentSelectedPurchasesToDelete = Purchases_ViewModel
        .currentSelectedPurchasesToDelete.observeAsState(emptyList())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = {
                    if (!currentSelectedPurchasesToDelete.value.contains(compra)) {
                        Purchases_ViewModel.onSelectedPurchasesToDeleteUpdated(
                            currentSelectedPurchasesToDelete.value.plus(compra)
                        )
                    } else {
                        val newList = currentSelectedPurchasesToDelete.value.filter { c ->
                            !currentSelectedPurchasesToDelete.value.contains(c)
                        }
                        Purchases_ViewModel.onSelectedPurchasesToDeleteUpdated(newList)
                    }
                    selectedRows[compra.id!!] = !isSelected
                },
                onLongClick = {
                    if (!currentSelectedPurchasesToDelete.value.contains(compra)) {
                        Purchases_ViewModel.onSelectedPurchasesToDeleteUpdated(
                            currentSelectedPurchasesToDelete.value.plus(compra)
                        )
                    } else {
                        val newList = currentSelectedPurchasesToDelete.value.filter { c ->
                            !currentSelectedPurchasesToDelete.value.contains(c)
                        }
                        Purchases_ViewModel.onSelectedPurchasesToDeleteUpdated(newList)
                    }
                    selectedRows[compra.id!!] = !isSelected
                }
            )
            .background(backgroundColor)
    ) {
        if(currentSelectedPurchasesToDelete.value.size > 0 && isSelected) {
            IconButton(
                modifier = Modifier.size(25.dp),
                onClick = { /*TODO*/ }
            ) {
                Icon(Icons.Default.CheckBox, contentDescription = null, tint = backgroundButtonColor)
            }
        }
        if(currentSelectedPurchasesToDelete.value.size > 0 && !isSelected) {
            IconButton(
                modifier = Modifier.size(25.dp),
                onClick = { /*TODO*/ }
            ) {
                Icon(Icons.Default.CheckBoxOutlineBlank, contentDescription = null, tint = backgroundButtonColor)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = compra.name.toString(),
                modifier = Modifier.width(150.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = if (compra.date == null) "-" else "${compra.date}",
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = if (compra.supplier == null) "-" else "${compra.supplier}",
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
