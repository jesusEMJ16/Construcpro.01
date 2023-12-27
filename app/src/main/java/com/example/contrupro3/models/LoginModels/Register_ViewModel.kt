package com.example.contrupro3.models.LoginModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Register_ViewModel: ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name
    private val _lastName = MutableLiveData<String>()
    val lastName: LiveData<String> = _lastName
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email
    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber
    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password
    private val _repeatPassword = MutableLiveData<String>()
    val repeatPassword: LiveData<String> = _repeatPassword
    private val _enableRegisterButton = MutableLiveData<Boolean>()
    val enableRegisterButton: LiveData<Boolean> = _enableRegisterButton
    private val _isEmailValid = MutableLiveData<Boolean>()
    val isMailValid: LiveData<Boolean> = _isEmailValid
    private val _isPhoneNumberValid = MutableLiveData<Boolean>()
    val isPhoneNumberValid: LiveData<Boolean> = _isPhoneNumberValid

    fun onRegisterFieldsChanged(
        name: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String,
        repeatPassword: String
    ) {
        _name.value = name
        _lastName.value = lastName
        _email.value = email
        _phoneNumber.value = phoneNumber
        _password.value = password
        _repeatPassword.value = repeatPassword
        _isEmailValid.value = isValidEmail(email)
        _isPhoneNumberValid.value = isPhoneNumberValid(phoneNumber)

        if(name.isNotEmpty() && name.length <= 20 &&
            lastName.isNotEmpty() && lastName.length <= 20 &&
            isValidEmail(email) &&
            password.length >= 6 && password.length <= 30 &&
            repeatPassword == password) _enableRegisterButton.value = true else _enableRegisterButton.value = false
    }

    fun changeEnableRegisterButton(state: Boolean) {
        _enableRegisterButton.value = state
    }
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
    return email.matches(emailRegex)
}
fun isPhoneNumberValid(phoneNumber: String): Boolean {
    val phoneRegex = Regex("^\\+?[1-9]\\d{0,3}[-.\\s]?\\(?\\d{1,}\\)?[-.\\s]?\\d{1,}[-.\\s]?\\d{1,}\$")
    return phoneRegex.matches(phoneNumber)
}