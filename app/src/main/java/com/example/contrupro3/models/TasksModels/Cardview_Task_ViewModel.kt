package com.example.contrupro3.models.TasksModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contrupro3.models.TeamsModels.TeamMember
import com.example.contrupro3.models.TeamsModels.Teams
import com.example.contrupro3.models.UserModels.UserModel

class Cardview_Task_ViewModel : ViewModel() {
    /* ===== [ TaskScreen - Register Fields ] ===== */
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name
    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description
    private val _startDateTextField = MutableLiveData<String?>()
    val startDateTextField: LiveData<String?> = _startDateTextField
    private val _endDateTextField = MutableLiveData<String?>()
    val endDateTextField: LiveData<String?> = _endDateTextField

    fun onFieldsUpdated(
        name: String,
        description: String,
    ) {
        _name.value = name
        _description.value = description
    }

    fun onCalendarUpdated(startDateText: String?, endDateText: String?) {
        _startDateTextField.value = startDateText
        _endDateTextField.value = endDateText
    }

    /* ===== [ TaskScreen - TeamsSelectedToRemove ] ===== */
    private val _teamsSelectedToRemove = MutableLiveData<List<Teams>>()
    val teamsSelectedToRemove: LiveData<List<Teams>> = _teamsSelectedToRemove
    private val _usersSelectedToRemove = MutableLiveData<List<UserModel>>()
    val usersSelectedToRemove: LiveData<List<UserModel>> = _usersSelectedToRemove

    fun onSelectionToRemoveUpdated(teamsSelectedToRemove: List<Teams>) {
        _teamsSelectedToRemove.value = teamsSelectedToRemove
    }
    fun onSelectionToRemoveUsersUpdated(usersSelectedToRemove: List<UserModel>) {
        _usersSelectedToRemove.value = usersSelectedToRemove
    }
}