package com.example.contrupro3.ui.theme.Budgets_purchasesScreens.PresupuestoModels

data class Compra(
    val id: Int,
    val nombre: String,
    val fecha: String,
    val proveedor: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val precioTotal: Double
)