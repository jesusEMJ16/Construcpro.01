package com.example.contrupro3

import android.os.Build
import android.os.Bundle
import androidx.navigation.compose.rememberNavController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.contrupro3.models.Navigaton.AppNavigator
import com.example.contrupro3.models.AuthRepository
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val authRepository by lazy { AuthRepository(auth) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ContruPro3)
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            AppNavigator(auth, navController, authRepository)
        }
    }
}

