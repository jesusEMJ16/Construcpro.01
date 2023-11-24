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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.LoginModels.Login_ViewModel
import com.example.contrupro3.ui.theme.myBlue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun LoginPage(
    navController: NavController,
    authRepository: AuthRepository,
    LoginViewModel: Login_ViewModel
) {
    val email = LoginViewModel.email.observeAsState("")
    val password = LoginViewModel.password.observeAsState("")
    val enabledLoginButton = LoginViewModel.enabledLoginButton.observeAsState(false)
    val isMailValid = LoginViewModel.isMailValid.observeAsState(false)
    val focusManager = LocalFocusManager.current

    var isPasswordVisible by remember { mutableStateOf(false) }
    val snackbarVisibleState = remember { mutableStateOf(false) }
    val snackbarMessageState = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopCenter)
                .offset(y = 35.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_name),
                contentDescription = "App Logo",
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 100.dp)
        ) {
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
                } else if (!isMailValid.value) {
                    Text(
                        text = "(Email no valido)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Light,
                            color = Color.Red,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier
                            .padding(horizontal = 7.dp)
                    )
                }
            }
            androidx.compose.material.OutlinedTextField(
                value = email.value,
                onValueChange = {
                    LoginViewModel.onFieldsChanged(it, password.value)
                },
                placeholder = { Text(text = "Tu Correo electrónico") },
                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
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
            Spacer(modifier = Modifier.height(20.dp))
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
                if(password.value.isEmpty()) {
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
                } else if(password.value.length < 6) Text(
                    text = "(Demasiado pequeña)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Light,
                        color = Color.Red,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier
                        .padding(horizontal = 7.dp)
                )
            }
            androidx.compose.material.OutlinedTextField(
                value = password.value,
                onValueChange = {
                    LoginViewModel.onFieldsChanged(email.value, it)
                },
                placeholder = { Text(text = "Tu Contraseña registrada") },
                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
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
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        if(isPasswordVisible) {
                            Icon(
                                Icons.Default.Visibility,
                                contentDescription = "Toggle password visibility"
                            )
                        } else Icon(
                            Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
            )

            LaunchedEffect(snackbarVisibleState.value) {
                delay(2500)
                snackbarVisibleState.value = false
            }

            Button(
                onClick = {
                    SignInUser(
                        email.value,
                        password.value,
                        {
                            val userID = authRepository.getCurrentUser()?.uid
                            if (userID != null) {
                                navController.navigate("projects_screen/$userID") {
                                    popUpTo("login_screen") { inclusive = true }
                                }
                            }
                        },
                        { errorMessage ->
                            when (errorMessage) {
                                "USER_NOT_FOUND" -> {
                                    snackbarMessageState.value =
                                        "No se pudo encontrar el email ingresado."
                                    snackbarVisibleState.value = true
                                }

                                "PASSWORD_INVALID" -> {
                                    snackbarMessageState.value =
                                        "La contraseña no coincide con nuestros registros."
                                    snackbarVisibleState.value = true
                                }

                                "UNKNOWN_ERROR" -> {
                                    snackbarMessageState.value =
                                        "Ha ocurrido un problema. Porfavor, intentelo de nuevo."
                                    snackbarVisibleState.value = true
                                }

                                "FAILED_TO_GET_USER_DOCUMENT" -> {
                                    snackbarMessageState.value =
                                        "No fue posible obtener los datos del usuario. Porfavor, intentelo de nuevo mas tarde."
                                    snackbarVisibleState.value = true
                                }
                            }
                        }
                    )
                },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                colors = ButtonDefaults.buttonColors(myBlue, contentColor = Color.White),
                modifier = Modifier
                    .offset(y = 30.dp)
                    .fillMaxWidth(0.6f)
                    .padding(8.dp),
                enabled = enabledLoginButton.value
            ) {
                Text("Ingresar")
            }

            Button(
                onClick = {
                    navController.navigate("register_screen")
                },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                colors = ButtonDefaults.buttonColors(myBlue, contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(8.dp)
                    .offset(y = 15.dp)
            ) {
                Text("Registrarse")
            }
            TextButton(
                onClick = {
                },
                colors = ButtonDefaults.buttonColors(Color.Transparent, contentColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(8.dp)
            ) {
                Text("Olvidé mi contraseña")
            }
        }

        if (snackbarVisibleState.value) {
            Snackbar(
                modifier = Modifier
                    .padding(8.dp)
                    .zIndex(10f),
                action = {}
            ) { Text(snackbarMessageState.value) }
        }
    }
}

fun SignInUser(email: String, password: String, onSuccess: () -> Unit, onFailed: (String) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val firebase = FirebaseFirestore.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val docRef = firebase
                    .collection("Users")
                    .document(auth.currentUser?.uid ?: "")

                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            onSuccess()
                        } else {
                            onFailed("FAILED_TO_GET_USER_DOCUMENT")
                        }
                    }.addOnFailureListener {
                        onFailed(it.message ?: "UNKNOWN_ERROR")
                    }
            } else {
                val exception = task.exception
                if (exception is FirebaseAuthInvalidUserException) {
                    onFailed("USER_NOT_FOUND")
                } else if (exception is FirebaseAuthInvalidCredentialsException) {
                    onFailed("PASSWORD_INVALID")
                } else {
                    onFailed("UNKNOWN_ERROR")
                }
            }
        }
}
