package com.example.contrupro3.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contrupro3.R
import com.example.contrupro3.modelos.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.compose.material.icons.filled.Visibility
import com.google.firebase.auth.FirebaseAuthUserCollisionException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPage(navController: NavController) {

    val registrationSuccessState = remember { mutableStateOf(false) }
    val authRepository = AuthRepository(Firebase.auth)
    val auth: FirebaseAuth = Firebase.auth
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phonenumber by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Selecciona un rol...") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isRepeatPasswordVisible by remember { mutableStateOf(false) }
    val snackbarVisibleState = remember { mutableStateOf(false) }
    val snackbarMessageState = remember { mutableStateOf("") }
    val showMessageState = remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopCenter)
                .offset(y = 20.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
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
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
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
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellido") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
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
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
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
                value = phonenumber,
                onValueChange = { newValue -> phonenumber = newValue },
                label = { Text("Numero de telefono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
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
            //mi menu desplegable
            RoleSelector { newRole ->
                role = newRole
            }
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { newValue -> password = newValue },
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
                    textColor = Color.Black,
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
                value = repeatPassword,
                onValueChange = { newValue -> repeatPassword = newValue },
                label = { Text("Repetir contraseña") },
                visualTransformation = if (isRepeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    IconButton(onClick = { isRepeatPasswordVisible = !isRepeatPasswordVisible }) {
                        Icon(
                            Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .shadow(5.dp)
                    .background(Color.White)
            )

            if (snackbarVisibleState.value) {
                Snackbar(
                    action = {
                        Button(onClick = { snackbarVisibleState.value = false }) {
                            Text("Aceptar")
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(snackbarMessageState.value)
                }
            }

            Button(
                onClick = {
                    val validationMessage = authRepository.validateInput(
                        name,
                        lastName,
                        email,
                        phonenumber,
                        role,
                        password,
                        repeatPassword
                    )
                    if (validationMessage.isEmpty()) {
                        authRepository.registerUser(
                            name,
                            lastName,
                            email,
                            phonenumber,
                            password,
                            role,
                            onFail = { errorMessage ->
                                if (errorMessage == "EMAIL_ALREADY_IN_USE") {
                                    snackbarMessageState.value = "El correo electrónico ya está en uso."
                                } else {
                                    snackbarMessageState.value = "La autenticación falló."
                                }
                                snackbarVisibleState.value = true
                                registrationSuccessState.value = false
                            },
                            onSuccess = {
                                registrationSuccessState.value = true
                            }
                        )
                    } else {
                        snackbarMessageState.value = validationMessage
                        snackbarVisibleState.value = true
                    }
                },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                colors = ButtonDefaults.buttonColors(myOrange, contentColor = Color.White),
                modifier = Modifier
                    .offset(y = 20.dp)
                    .fillMaxWidth(0.6f)
                    .padding(8.dp)
            ) {
                Text("Registrarse")
            }

            if (registrationSuccessState.value) {
                Snackbar(
                    action = {
                        Button(onClick = {
                            registrationSuccessState.value = false
                            navController.navigate("login_screen")
                        }) {
                            Text("Aceptar")
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Registro exitoso. Por favor, confirma tu correo electrónico.")
                }
            }
        }
    }
}

@Composable
fun RoleSelector(onRoleSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val roles = listOf(
        "Miembro de equipo",
        "Supervisor",
        "Contratista",
        "Arquitecto",
        "Administrador"
    ) // Los roles que quieras
    var selectedRole by remember { mutableStateOf("Selecciona un rol...") } // Placeholder

    Box(modifier = Modifier.fillMaxWidth(0.8f)) {
        TextButton(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(Color.Transparent, contentColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp)
                .height(60.dp)
                .background(Color.White)
        ) {
            Text(
                selectedRole,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 5.dp),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            roles.forEach { role ->
                DropdownMenuItem(onClick = {
                    selectedRole = role
                    expanded = false
                    onRoleSelected(role)
                }) {
                    Text(role)
                }
            }
        }
    }
}