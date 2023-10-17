package com.example.contrupro3.ui.theme.TeamsScreens

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contrupro3.modelos.Equipos
import com.example.contrupro3.modelos.UserModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class CardviewTeam_ViewModel : ViewModel() {
    private val _team = MutableLiveData<Equipos?>()
    val team: LiveData<Equipos?> = _team

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _enableInviteButton = MutableLiveData<Boolean>()
    val enableInviteButton: LiveData<Boolean> = _enableInviteButton

    private val _saveNameAndDescriptionButtonEnable = MutableLiveData<Boolean>()
    val saveNameAndDescriptionButtonEnable: LiveData<Boolean> = _saveNameAndDescriptionButtonEnable

    fun onInfoChanged(name: String, description: String, team: Equipos?) {
        _name.value = name
        _team.value = team
        _description.value = description
        _saveNameAndDescriptionButtonEnable.value =
            isNameValid(name) || isDescriptionValid(description)
    }

    fun onAddDialogChanged(email: String) {
        _email.value = email
        _enableInviteButton.value = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isDescriptionValid(description: String): Boolean =
        description.trim() !== _team.value?.description.toString()
            .trim() && description.length <= 200

    private fun isNameValid(name: String): Boolean =
        name.trim() !== _team.value?.name.toString().trim() && name.length >= 6 && name.length <= 30
}