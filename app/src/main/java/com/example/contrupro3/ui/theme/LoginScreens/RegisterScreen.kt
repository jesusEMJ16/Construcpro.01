package com.example.contrupro3.ui.theme.LoginScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.contrupro3.R
import com.example.contrupro3.models.LoginModels.Register_ViewModel
import com.example.contrupro3.models.LoginModels.isPhoneNumberValid
import com.example.contrupro3.models.LoginModels.isValidEmail
import com.example.contrupro3.ui.theme.myBlue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

@Composable
fun RegisterPage(navController: NavController, Register_ViewModel: Register_ViewModel) {
    val registrationSuccessState = remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isRepeatPasswordVisible by remember { mutableStateOf(false) }
    val snackbarVisibleState = remember { mutableStateOf(false) }
    val snackbarMessage = remember { mutableStateOf("") }

    val name = Register_ViewModel.name.observeAsState("")
    val lastName = Register_ViewModel.lastName.observeAsState("")
    val email = Register_ViewModel.email.observeAsState("")
    val phoneNumber = Register_ViewModel.phoneNumber.observeAsState("")
    val password = Register_ViewModel.password.observeAsState("")
    val repeatPassword = Register_ViewModel.repeatPassword.observeAsState("")
    val isMailValid = Register_ViewModel.isMailValid.observeAsState(false)
    val isPhoneNumberValid = Register_ViewModel.isPhoneNumberValid.observeAsState(false)
    val enableRegisterButton = Register_ViewModel.enableRegisterButton.observeAsState(false)
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo_name),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.TopCenter)
                .offset(y = 20.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(horizontal = 5.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Nombre",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                if (name.value.isEmpty()) {
                    Text(
                        text = "(Requerido)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Light,
                            color = Color.Red,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier
                            .padding(horizontal = 7.dp)
                    )
                } else Text(
                    text = "(${name.value.length}/20)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Light,
                        color = if (name.value.length > 20 || name.value.isEmpty()) Color.Red else Color.Black,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier
                        .padding(horizontal = 7.dp)
                )
            }
            OutlinedTextField(
                value = name.value,
                onValueChange = {
                    Register_ViewModel.onRegisterFieldsChanged(
                        name = it,
                        lastName = lastName.value,
                        phoneNumber = phoneNumber.value,
                        email = email.value,
                        password = password.value,
                        repeatPassword = repeatPassword.value
                    )
                },
                placeholder = { Text(text = "Nombre del usuario") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    backgroundColor = Color(0x79D8D8D8),
                    focusedBorderColor = Color.Transparent,
                    cursorColor = myBlue,
                    disabledBorderColor = Color.Transparent,
                ),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(horizontal = 5.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Apellido",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                if (lastName.value.isEmpty()) {
                    Text(
                        text = "(Requerido)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Light,
                            color = Color.Red,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier
                            .padding(horizontal = 7.dp)
                    )
                } else {
                    Text(
                        text = "(${lastName.value.length}/20)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Light,
                            color = if (lastName.value.length > 20 || lastName.value.isEmpty()) Color.Red else Color.Black,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier
                            .padding(horizontal = 7.dp)
                    )
                }
            }
            OutlinedTextField(
                value = lastName.value,
                onValueChange = {
                    Register_ViewModel.onRegisterFieldsChanged(
                        name = name.value,
                        lastName = it,
                        phoneNumber = phoneNumber.value,
                        email = email.value,
                        password = password.value,
                        repeatPassword = repeatPassword.value
                    )
                },
                placeholder = { Text(text = "Apellido del usuario") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    backgroundColor = Color(0x79D8D8D8),
                    focusedBorderColor = Color.Transparent,
                    cursorColor = myBlue,
                    disabledBorderColor = Color.Transparent,
                ),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(horizontal = 5.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Email",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                if (email.value.isEmpty()) {
                    Text(
                        text = "(Requerido)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Light,
                            color = Color.Red,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier
                            .padding(horizontal = 7.dp)
                    )
                } else if(!isValidEmail(email.value)) Text(
                    text = "(Email no válido)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Light,
                        color = Color.Red,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier
                        .padding(horizontal = 7.dp)
                )
            }
            OutlinedTextField(
                value = email.value,
                onValueChange = {
                    Register_ViewModel.onRegisterFieldsChanged(
                        name = name.value,
                        lastName = lastName.value,
                        phoneNumber = phoneNumber.value,
                        email = it,
                        password = password.value,
                        repeatPassword = repeatPassword.value
                    )
                },
                placeholder = { Text(text = "Tu Correo electrónico") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    backgroundColor = Color(0x79D8D8D8),
                    focusedBorderColor = Color.Transparent,
                    cursorColor = myBlue,
                    disabledBorderColor = Color.Transparent,
                ),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(horizontal = 5.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Numero de telefono",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                if (phoneNumber.value.isEmpty()) {
                    Text(
                        text = "(Requerido)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Light,
                            color = Color.Red,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier
                            .padding(horizontal = 7.dp)
                    )
                } else if(!isPhoneNumberValid(phoneNumber.value)) Text(
                    text = "(Numero no válido)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Light,
                        color = Color.Red,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier
                        .padding(horizontal = 7.dp)
                )
            }
            OutlinedTextField(
                value = phoneNumber.value,
                onValueChange = {
                    Register_ViewModel.onRegisterFieldsChanged(
                        name = name.value,
                        lastName = lastName.value,
                        phoneNumber = it,
                        email = email.value,
                        password = password.value,
                        repeatPassword = repeatPassword.value
                    )
                },
                placeholder = { Text(text = "Numero de telefono") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    backgroundColor = Color(0x79D8D8D8),
                    focusedBorderColor = Color.Transparent,
                    cursorColor = myBlue,
                    disabledBorderColor = Color.Transparent,
                ),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(horizontal = 5.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Contraseña",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                if (password.value.isEmpty()) {
                    Text(
                        text = "(Requerido)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Light,
                            color = Color.Red,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier
                            .padding(horizontal = 7.dp)
                    )
                } else Text(
                    text = "(${password.value.length}/30)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Light,
                        color = if (password.value.length > 30 || password.value.length < 6) Color.Red else Color.Black,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier
                        .padding(horizontal = 7.dp)
                )
            }
            OutlinedTextField(
                value = password.value,
                onValueChange = {
                    Register_ViewModel.onRegisterFieldsChanged(
                        name = name.value,
                        lastName = lastName.value,
                        phoneNumber = phoneNumber.value,
                        email = email.value,
                        password = it,
                        repeatPassword = repeatPassword.value
                    )
                },
                placeholder = { Text(text = "Contraseña del usuario") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    backgroundColor = Color(0x79D8D8D8),
                    focusedBorderColor = Color.Transparent,
                    cursorColor = myBlue,
                    disabledBorderColor = Color.Transparent,
                ),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        if(isPasswordVisible) Icon(
                            Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        ) else Icon(
                            Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(horizontal = 5.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Repetir Contraseña",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                if (repeatPassword.value.isEmpty()) {
                    Text(
                        text = "(Requerido)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Light,
                            color = Color.Red,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier
                            .padding(horizontal = 7.dp)
                    )
                } else if(repeatPassword.value != password.value) Text(
                    text = "(Contraseña desigual)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Light,
                        color = Color.Red,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier
                        .padding(horizontal = 7.dp)
                )
            }
            OutlinedTextField(
                value = repeatPassword.value,
                onValueChange = {
                    Register_ViewModel.onRegisterFieldsChanged(
                        name = name.value,
                        lastName = lastName.value,
                        phoneNumber = phoneNumber.value,
                        email = email.value,
                        password = password.value,
                        repeatPassword = it
                    )
                },
                placeholder = { Text(text = "Repetir Contraseña") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    backgroundColor = Color(0x79D8D8D8),
                    focusedBorderColor = Color.Transparent,
                    cursorColor = myBlue,
                    disabledBorderColor = Color.Transparent,
                ),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (isRepeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isRepeatPasswordVisible = !isRepeatPasswordVisible }) {
                        if(isRepeatPasswordVisible) Icon(
                            Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        ) else Icon(
                            Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
            )

            LaunchedEffect(snackbarVisibleState.value) {
                if (snackbarVisibleState.value) {
                    delay(1500)
                    snackbarVisibleState.value = false

                    if (snackbarMessage.value == "Se ha registrado correctamente.") {
                        navController.navigate("login_screen")
                    }
                }
            }

            if (snackbarVisibleState.value) Snackbar(
                action = {},
                modifier = Modifier
                    .padding(16.dp)
                    .zIndex(10f)
            ) { Text("${snackbarMessage.value}") }

            Button(
                onClick = {
                    Register_ViewModel.changeEnableRegisterButton(false)
                    RegisterUser(
                        name.value,
                        lastName.value,
                        email.value,
                        phoneNumber.value,
                        password.value,
                        {
                            snackbarMessage.value = "Se ha registrado correctamente."
                            snackbarVisibleState.value = true
                        },
                        { errorMessage ->
                            if (errorMessage === "EMAIL_ALREADY_IN_USE") {
                                snackbarMessage.value = "El correo electronico ya esta en uso."
                                snackbarVisibleState.value = true
                            } else if (errorMessage === "EMAIL_VERIFICATION_FAILED") {
                                snackbarMessage.value =
                                    "Ocurrio un problema al registrarte, intente de nuevo mas tarde."
                                snackbarVisibleState.value = true
                            }
                        }
                    )
                },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                colors = ButtonDefaults.buttonColors(myBlue, contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(60.dp)
                    .padding(8.dp),
                enabled = enableRegisterButton.value
            ) {
                Text("Registrarse")
            }
        }
    }
}

fun RegisterUser(
    name: String,
    lastName: String,
    email: String,
    phoneNumber: String,
    password: String,
    onSucces: () -> Unit,
    onFail: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val capitalizedFirstName = name.capitalize()
                val capitalizedLastName = lastName.capitalize()

                val userData = hashMapOf(
                    "name" to capitalizedFirstName,
                    "lastName" to capitalizedLastName,
                    "email" to email,
                    "phoneNumber" to phoneNumber
                )

                if (user != null) {
                    val firestore = FirebaseFirestore.getInstance()

                    firestore.collection("Users")
                        .document(user.uid)
                        .set(userData)
                        .addOnSuccessListener {
                            user.sendEmailVerification().addOnCompleteListener { emailTask ->
                                if (emailTask.isSuccessful) {
                                    onSucces()
                                } else {
                                    onFail("EMAIL_VERIFICATION_FAILED")
                                }
                            }
                        }
                        .addOnFailureListener { exception: Exception ->
                            onFail("FIRESTORE_ERROR")
                        }
                }
            } else {
                val exception = task.exception
                if (exception is FirebaseAuthUserCollisionException) {
                    onFail("EMAIL_ALREADY_IN_USE")
                } else {
                }
            }
        }
}
