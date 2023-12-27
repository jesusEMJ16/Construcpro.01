package com.example.contrupro3.models.DocumentsModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contrupro3.models.TeamsModels.Teams

class DocumentScreen_ViewModel : ViewModel() {
    /* ===== [ DocumentScreen - Filter Documents ] ===== */
    private val _filterSelected = MutableLiveData<String>()
    val filterSelected: LiveData<String> = _filterSelected
    private val _isFilterAscending = MutableLiveData<Boolean>()
    val isFilterAscending: LiveData<Boolean> = _isFilterAscending
    private val _isFilterMenuOpen = MutableLiveData<Boolean>()
    val isFilterMenuOpen: LiveData<Boolean> = _isFilterMenuOpen
    private val _isSearchExpanded = MutableLiveData<Boolean>()
    val isSearchExpanded: LiveData<Boolean> = _isSearchExpanded
    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    fun onFilterSelectionChanged(
        isFilterMenuOpen: Boolean,
        isSearchExpanded: Boolean,
        selectedFilter: String,
        isFilterAscending: Boolean
    ) {
        _isFilterMenuOpen.value = isFilterMenuOpen
        _isSearchExpanded.value = isSearchExpanded
        if (!isSearchExpanded) _searchQuery.value = ""
        _filterSelected.value = selectedFilter
        _isFilterAscending.value = isFilterAscending
    }

    /* ===== [ DocumentsScreen - Others ] ===== */
    private val _documentsSelectedToRemove = MutableLiveData<List<DocumentModel>>()
    val documentsSelectedToRemove: LiveData<List<DocumentModel>> = _documentsSelectedToRemove
    private val _showDeleteDocumentsDialog = MutableLiveData<Boolean>()
    val showDeleteDocumentsDialog: LiveData<Boolean> = _showDeleteDocumentsDialog

    fun onRemoveDocumentsChanged(
        documentsSelectedToRemove: List<DocumentModel>,
        showDeleteDocumentsDialog: Boolean
    ) {
        _documentsSelectedToRemove.value = documentsSelectedToRemove
        _showDeleteDocumentsDialog.value = showDeleteDocumentsDialog
    }
}