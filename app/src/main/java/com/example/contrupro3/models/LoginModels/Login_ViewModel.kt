package com.example.contrupro3.models.LoginModels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Login_ViewModel: ViewModel() {
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email
    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password
    private val _enabledLoginButton = MutableLiveData<Boolean>()
    val enabledLoginButton: LiveData<Boolean> = _enabledLoginButton
    private val _isMailValid = MutableLiveData<Boolean>()
    val isMailValid: LiveData<Boolean> = _isMailValid

    fun onFieldsChanged(email: String, password: String) {
        _email.value = email
        _password.value = password

        _isMailValid.value = isValidEmail(email)
        if(isValidEmail(email) && password.length >= 6) _enabledLoginButton.value = true
    }
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return email.matches(emailRegex)
    }
}