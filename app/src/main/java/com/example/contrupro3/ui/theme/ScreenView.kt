package com.example.contrupro3.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.contrupro3.R
import com.example.contrupro3.modelos.AuthRepository
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun SplashScreen(navController: NavHostController, authRepository: AuthRepository) {
    Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_name),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(300.dp)
                    //.padding(bottom = 100.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            BouncingDots()
        }
    }
    LaunchedEffect(Unit) {
        delay(2000)
        if (authRepository.isEmailVerified()) {
            val userID = authRepository.getCurrentUser()?.uid
            navController.navigate("projects_screen/$userID") {
                popUpTo("splash_screen") { inclusive = true }
            }
        } else {
            navController.navigate("login_screen") {
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
            val shadowSize = (20.dp.value - dotSize)
            val verticalOffset = dotSize / 10
            Box(
                modifier = Modifier
                    .size(dotSize.dp)
                    .offset(y = verticalOffset.dp)
                    .shadow(
                        elevation = shadowSize.dp,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .background(myBlue)
            )
        }
    }
}