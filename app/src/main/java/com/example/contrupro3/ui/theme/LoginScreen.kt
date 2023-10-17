package com.example.contrupro3.ui.theme

import androidx.compose.material.AlertDialog
import com.example.contrupro3.modelos.AuthRepository
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contrupro3.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavController, authRepository: AuthRepository) {
    var loginErrorState by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarVisibleState = remember { mutableStateOf(false) }
    val snackbarMessageState = remember { mutableStateOf("") }
    val loginSuccessState = remember { mutableStateOf(false) }
    val incorrectPasswordState = remember { mutableStateOf(false) }
    val emailNotFoundState = remember { mutableStateOf(false) }

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
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .shadow(5.dp)
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(), // Transformación visual para ocultar la contraseña
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ), // Configuración del teclado para el campo de contraseña
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }, // Icono para alternar la visibilidad de la contraseña
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .shadow(5.dp)
                    .background(Color.White)
            )

            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        loginErrorState = true
                        errorMessage = "Por favor, completa todos los campos."
                    } else {
                        authRepository.loginUser(email, password,
                            onSuccess = {
                                loginSuccessState.value = true
                            },
                            onFail = { errorMessage ->
                                when (errorMessage) {
                                    "PASSWORD_INVALID" -> {
                                        incorrectPasswordState.value = true
                                    }

                                    "EMAIL_INVALID" -> {
                                        emailNotFoundState.value = true
                                    }

                                    else -> {
                                        snackbarMessageState.value =
                                            "Inicio de sesión fallido. Verifica tus credenciales."
                                        snackbarVisibleState.value = true
                                    }
                                }
                            }
                        )
                    }
                },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                colors = ButtonDefaults.buttonColors(myBlue, contentColor = Color.White),
                modifier = Modifier
                    .offset(y = 30.dp)
                    .fillMaxWidth(0.6f)
                    .padding(8.dp)
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

        if (loginErrorState) {
            AlertDialog(
                onDismissRequest = {
                    loginErrorState = false
                },
                title = { Text("Error de inicio de sesión") },
                text = { Text(errorMessage) },
                confirmButton = {
                    Button(onClick = {
                        loginErrorState = false
                    }) {
                        Text("Cerrar")
                    }
                }
            )
        }
        if (emailNotFoundState.value) {
            AlertDialog(
                onDismissRequest = {
                    emailNotFoundState.value = false
                },
                title = { Text("Error de inicio de sesión") },
                text = { Text("Correo electrónico no encontrado. Por favor, verifica tu correo electrónico.") },
                confirmButton = {
                    Button(onClick = {
                        emailNotFoundState.value = false
                    }) {
                        Text("Cerrar")
                    }
                }
            )
        }
        if (incorrectPasswordState.value) {
            AlertDialog(
                onDismissRequest = {
                    incorrectPasswordState.value = false
                },
                title = { Text("Error de inicio de sesión") },
                text = { Text("Contraseña incorrecta. Por favor, verifica tu contraseña.") },
                confirmButton = {
                    Button(onClick = {
                        incorrectPasswordState.value = false
                    }) {
                        Text("Cerrar")
                    }
                }
            )
        }
        if (snackbarVisibleState.value) {
            Snackbar(
                modifier = Modifier.padding(8.dp),
                action = {
                    Button(onClick = { snackbarVisibleState.value = false }) {
                        Text("Cerrar")
                    }
                }
            ) {
                Text(snackbarMessageState.value)
            }
        }
        if (loginSuccessState.value) {
            val userID = authRepository.getCurrentUser()?.uid // Obtén el userID
            if (userID != null) { // Asegúrate de que el userID no sea nulo
                navController.navigate("projects_screen/$userID") {
                    popUpTo("login_screen") {
                        inclusive = true
                    } // Asegúrate de que esto coincida con la ruta definida en tu gráfico de navegación
                }
            } else {
                // Manejar el caso en que el userID sea nulo, quizás mostrando un error al usuario
            }
        }
    }
}