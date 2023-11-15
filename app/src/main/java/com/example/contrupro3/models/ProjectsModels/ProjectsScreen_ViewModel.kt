package com.example.contrupro3.models.ProjectsModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProjectsScreen_ViewModel : ViewModel() {
    /* ===== [ ProjectsScreen - Filter Projects ] ===== */
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

    /* ===== [ ProjectsScreen - Others ] ===== */
    private val _projectsSelectedToRemove = MutableLiveData<List<Project>>()
    val projectsSelectedToRemove: LiveData<List<Project>> = _projectsSelectedToRemove
    private val _showDeleteProyectsDialog = MutableLiveData<Boolean>()
    val showDeleteProyectsDialog: LiveData<Boolean> = _showDeleteProyectsDialog


    fun onRemoveProjectsChanged(
        projectsSelectedToRemove: List<Project>,
        showDeleteProjectsDialog: Boolean
    ) {
        _projectsSelectedToRemove.value = projectsSelectedToRemove
        _showDeleteProyectsDialog.value = showDeleteProjectsDialog
    }

    /* ===== [ ProjectsScreen - Register Fields ] ===== */
    private val _projectName = MutableLiveData<String>()
    val projectName: LiveData<String> = _projectName
    private val _projectDescription = MutableLiveData<String>()
    val projectDescription: LiveData<String> = _projectDescription
    private val _startDateTextField = MutableLiveData<String>()
    val startDateTextField: LiveData<String> = _startDateTextField
    private val _endDateTextField = MutableLiveData<String>()
    val endDateTextField: LiveData<String> = _endDateTextField

    fun onFieldsUpdated(
        projectName: String,
        projectDescription: String,
    ) {
        _projectName.value = projectName
        _projectDescription.value = projectDescription
    }

    fun onCalendarUpdated(startDateText: String, endDateText: String) {
        _startDateTextField.value = startDateText
        _endDateTextField.value = endDateText
    }
}