package com.example.contrupro3

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.navigation.compose.rememberNavController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import com.example.contrupro3.modelos.AppNavigator
import com.example.contrupro3.modelos.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
