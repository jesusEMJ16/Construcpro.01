package com.example.contrupro3.models.BudgetModels.Purchases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contrupro3.models.DocumentsModels.DocumentModel

class Purchases_ViewModel : ViewModel() {
    /* ===== [ Purchases - Fields ] ===== */
    private val _productName = MutableLiveData<String>()
    val productName: LiveData<String> = _productName
    private val _productDate = MutableLiveData<String>()
    val productDate: LiveData<String> = _productDate
    private val _productSupplier = MutableLiveData<String>()
    val productSupplier: LiveData<String> = _productSupplier
    private val _productQuantity = MutableLiveData<String>()
    val productQuantity: LiveData<String> = _productQuantity
    private val _productUnitPrice = MutableLiveData<String>()
    val productUnitPrice: LiveData<String> = _productUnitPrice
    private val _productTotalPrice = MutableLiveData<String>()
    val productTotalPrice: LiveData<String> = _productTotalPrice

    fun onFieldsUpdated(
        productName: String,
        productDate: String,
        productSupplier: String,
        productQuantity: String,
        productUnitPrice: String
    ) {
        _productName.value = productName
        _productDate.value = productDate
        _productSupplier.value = productSupplier
        _productQuantity.value = productQuantity
        _productUnitPrice.value = productUnitPrice

        if(productQuantity != null && productUnitPrice != null) _productTotalPrice.value = (productQuantity.toInt() * productUnitPrice.toLong()).toString()
    }

    /* ===== [ Purchases - Others ] ===== */
    private val _currentSelectedPurchasesToDelete = MutableLiveData<List<PurchasesModel>>()
    val currentSelectedPurchasesToDelete: LiveData<List<PurchasesModel>> = _currentSelectedPurchasesToDelete
    private val _showDeleteDialog = MutableLiveData<Boolean>()
    val showDeleteDialog: LiveData<Boolean> = _showDeleteDialog
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    fun onSelectedPurchasesToDeleteUpdated(selectionList: List<PurchasesModel>) {
        _currentSelectedPurchasesToDelete.value = selectionList
    }
    fun onDialogsUpdated(
        showDeleteDialog: Boolean,
        showAddPurchaseDialog: Boolean
    ) {
        _showDeleteDialog.value = showDeleteDialog
        _showDialog.value = showAddPurchaseDialog
    }
}