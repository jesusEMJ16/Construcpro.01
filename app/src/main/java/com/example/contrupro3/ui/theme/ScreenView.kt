package com.example.contrupro3.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.contrupro3.R
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.navigation.NavHostController
import com.example.contrupro3.modelos.AuthRepository
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun SplashScreen(navController: NavHostController,authRepository: AuthRepository) {
    Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo_con_nombre), // reemplace esto con el recurso de su logo
                contentDescription = "Logo",
                modifier = Modifier.padding(bottom = 100.dp)
                    .size(250.dp)

            )
            BouncingDots()
        }
    }
    LaunchedEffect(Unit) {
        delay(1000) // retraso de 1 segundos
        if (authRepository.isEmailVerified()) {
            val userID = authRepository.getCurrentUser()?.uid // Obtén el userID
            val projectID = "project_id" // Deberías obtener el projectID de alguna manera
            navController.navigate("project_screen/$userID/$projectID"){
                popUpTo("splash_screen") { inclusive = true }
            }
    } else {
            navController.navigate("login_screen"){
                popUpTo("splash_screen") { inclusive = true }
            }
        }
    }
}

@Composable
fun BouncingDots() {
    val infiniteTransition = rememberInfiniteTransition()
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 2000, easing = LinearEasing)
        ), label = ""
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (i in 0 until 5) {
            val offset = i * (2 * Math.PI.toFloat() / 10)
            val dotSize = abs(sin(time + offset)) * 8.dp.value + 8.dp.value
            val shadowSize = (20.dp.value - dotSize) // Ajustar la elevación de la sombra basándose en el tamaño del punto
            val verticalOffset = dotSize / 10 // Ajustar la posición vertical basándose en el tamaño del punto
            Box(
                modifier = Modifier
                    .size(dotSize.dp)
                    .offset(y = verticalOffset.dp) // Mover los puntos hacia arriba y hacia abajo
                    .shadow(
                        elevation = shadowSize.dp,
                        shape = CircleShape
                    ) // Ajustar la elevación de la sombra
                    .clip(CircleShape)
                    .background(Color(0xFFFFA500)) // Color naranja
            )
        }
    }
}