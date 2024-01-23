package com.example.contrupro3.models.ProjectsModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProjectCard_ViewModel: ViewModel() {
    /* ===== [ ProjectsScreen - Register Fields ] ===== */
    private val _projectName = MutableLiveData<String>()
    val projectName: LiveData<String> = _projectName
    private val _projectDescription = MutableLiveData<String>()
    val projectDescription: LiveData<String> = _projectDescription
    private val _startDateTextField = MutableLiveData<String?>()
    val startDateTextField: LiveData<String?> = _startDateTextField
    private val _endDateTextField = MutableLiveData<String?>()
    val endDateTextField: LiveData<String?> = _endDateTextField

    fun onFieldsUpdated(
        projectName: String,
        projectDescription: String,
    ) {
        _projectName.value = projectName
        _projectDescription.value = projectDescription
    }

    fun onCalendarUpdated(startDateText: String?, endDateText: String?) {
        _startDateTextField.value = startDateText
        _endDateTextField.value = endDateText
    }
}