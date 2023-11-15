package com.example.contrupro3.models.TeamsModels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contrupro3.models.ProjectsModels.Project

class TeamScreen_ViewModel : ViewModel() {
    /* ===== [ TeamScreen_ViewModel - Filter Projects ] ===== */
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
    private val _teamsSelectedToRemove = MutableLiveData<List<Teams>>()
    val teamsSelectedToRemove: LiveData<List<Teams>> = _teamsSelectedToRemove
    private val _showDeleteTeamsDialog = MutableLiveData<Boolean>()
    val showDeleteTeamsDialog: LiveData<Boolean> = _showDeleteTeamsDialog

    fun onRemoveTeamsChanged(
        teamsSelectedToRemove: List<Teams>,
        showDeleteTeamsDialog: Boolean
    ) {
        _teamsSelectedToRemove.value = teamsSelectedToRemove
        _showDeleteTeamsDialog.value = showDeleteTeamsDialog
    }
}