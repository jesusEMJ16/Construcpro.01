package com.example.contrupro3.models.TasksModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contrupro3.models.ProjectsModels.ProjectModel
import com.example.contrupro3.models.TeamsModels.TeamMember
import com.example.contrupro3.models.TeamsModels.Teams

class TasksScreen_ViewModel : ViewModel() {
    /* ===== [ TaskScreen_ViewModel - Filter Tasks ] ===== */
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



    /* ===== [ TasksScreen - Others ] ===== */
    private val _tasksSelectedToRemove = MutableLiveData<List<TaskModel>>()
    val tasksSelectedToRemove: LiveData<List<TaskModel>> = _tasksSelectedToRemove
    private val _showDeleteTasksDialog = MutableLiveData<Boolean>()
    val showDeleteTasksDialog: LiveData<Boolean> = _showDeleteTasksDialog
    private val _projectSelected = MutableLiveData<ProjectModel>()
    val projectSelected: LiveData<ProjectModel> = _projectSelected

    fun onProjectSaved(project: ProjectModel) {
        _projectSelected.value = project
    }

    fun onRemoveTasksChanged(
        tasksSelectedToRemove: List<TaskModel>,
        showDeleteTasksDialog: Boolean
    ) {
        _tasksSelectedToRemove.value = tasksSelectedToRemove
        _showDeleteTasksDialog.value = showDeleteTasksDialog
    }
}