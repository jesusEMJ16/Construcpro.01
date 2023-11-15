package com.example.contrupro3.models.BudgetModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.filament.Material

data class Materialmodel(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val cantidadNecesaria: String,
    val unidadMedida: String,
    val categoria: String,
    val costoEstimado: String,
    val costoReal: String,
    val proveedor: String,
)

enum class EstadoPedido {
    PENDIENTE, PEDIDO, RECIBIDO
}

// ViewModel para manejar la lógica de negocio
class SuministrosViewModel : ViewModel() {
    // LiveData para la lista de materiales
    private val _listaMateriales = MutableLiveData<List<Materialmodel>>()
    val listaMateriales: LiveData<List<Materialmodel>> = _listaMateriales

    fun agregarMaterial(material: Materialmodel) {
        // Lógica para agregar el material a la base de datos y actualizar la lista
        // ...
    }
}
