package com.example.contrupro3.ui.theme.UserProfileScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contrupro3.R
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.ui.theme.Menu.HamburgueerMenu
import com.example.contrupro3.ui.theme.myBlue
import com.example.contrupro3.ui.theme.mywhie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilePage(navController: NavController, authRepository: AuthRepository) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mywhie),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.user_icon),
                contentDescription = "User Profile Image",
                modifier = Modifier
                    .size(100.dp)
                    .shadow(20.dp, shape = CircleShape)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Username",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Involucrado en 5 proyectos y 15 tareas",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = TextFieldValue(),
                onValueChange = {},
                label = { Text("Nombre") },
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
                value = TextFieldValue(),
                onValueChange = {},
                label = { Text("Apellido") },
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
                value = TextFieldValue(),
                onValueChange = {},
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
                value = TextFieldValue(),
                onValueChange = {},
                label = { Text("Numero de telefono") },
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

            //aqui va mi selector

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = TextFieldValue(),
                onValueChange = {},
                label = { Text("Contraseña vieja") },
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
                value = TextFieldValue(),
                onValueChange = {},
                label = { Text("Contraseña nueva") },
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

            Button(
                onClick = {},
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                colors = ButtonDefaults.buttonColors(myBlue, contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(8.dp)
            ) {
                Text("Actualizar perfil")
            }
        }
        HamburgueerMenu(navController, authRepository)
    }
}
