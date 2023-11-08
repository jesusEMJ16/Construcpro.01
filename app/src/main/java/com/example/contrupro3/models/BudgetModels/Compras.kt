package com.example.contrupro3.models.BudgetModels

data class Compra(
    val id: Int,
    val nombre: String,
    val fecha: String,
    val proveedor: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val precioTotal: Double
)