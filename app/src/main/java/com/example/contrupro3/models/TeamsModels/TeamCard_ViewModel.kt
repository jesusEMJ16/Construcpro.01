package com.example.contrupro3.models.TeamsModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TeamCard_ViewModel: ViewModel() {
    /* ===== [ TeamCard - Fields ] ===== */
    private val _teamName = MutableLiveData<String>()
    val teamName: LiveData<String> = _teamName
    private val _teamDescription = MutableLiveData<String>()
    val teamDescription: LiveData<String> = _teamDescription

    fun onFieldsUpdated(name: String, description: String) {
        _teamName.value = name
        _teamDescription.value = description
    }

    /* ===== [ TeamCard - MemberSelectedToRemove ] ===== */
    private val _membersSelectedToRemove = MutableLiveData<List<TeamMember>>()
    val membersSelectedToRemove: LiveData<List<TeamMember>> = _membersSelectedToRemove

    fun onSelectionToRemoveUpdated(membersSelectedToRemove: List<TeamMember>) {
        _membersSelectedToRemove.value = membersSelectedToRemove
    }
}