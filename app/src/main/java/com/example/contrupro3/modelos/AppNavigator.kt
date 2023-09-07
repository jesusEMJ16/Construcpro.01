package com.example.contrupro3.modelos

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.contrupro3.ui.theme.CardViewTeam
import com.example.contrupro3.ui.theme.CardView_Documents
import com.example.contrupro3.ui.theme.Cardview
import com.example.contrupro3.ui.theme.DocumentsScreen
import com.example.contrupro3.ui.theme.LoginPage
import com.example.contrupro3.ui.theme.ProjectView
import com.example.contrupro3.ui.theme.RegisterPage
import com.example.contrupro3.ui.theme.SplashScreen
import com.example.contrupro3.ui.theme.TeamCreationScreen
import com.example.contrupro3.ui.theme.UserProfilePage
import com.google.firebase.auth.FirebaseAuth


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigator(auth: FirebaseAuth, navController: NavHostController, authRepository: AuthRepository) {

    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") {
            SplashScreen(navController = navController, authRepository = authRepository)
        }
        composable("login_screen") {
            LoginPage(navController = navController, authRepository = authRepository)
        }
        composable("register_screen") {
            RegisterPage(navController = navController)
        }
        composable("user_screen") {
            UserProfilePage(navController = navController)
        }
        composable("project_screen/{userID}/{projectID}") { backStackEntry ->
            val arguments = backStackEntry.arguments
            val userID = arguments?.getString("userID")
            val projectID = arguments?.getString("projectID")
            if (userID != null && projectID != null) {
                ProjectView(navController = navController, authRepository = authRepository, userID)
            } else {
                // Manejar el caso en que los parámetros sean null
            }
        }
        composable("card_screen/{projectName}/{creatorName}") { backStackEntry ->
            val projectName = backStackEntry.arguments?.getString("projectName") ?: ""
            val creatorName = backStackEntry.arguments?.getString("creatorName") ?: ""
            Cardview(navController = navController, projectName = projectName, creatorName = creatorName)
        }
        composable("team_screen/{userID}") { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")
            if (userID != null) {
                TeamCreationScreen(navController, authRepository, userID)
            }
        }
        composable("cardteam_screen/{equipoID}") { backStackEntry ->
            val equipoID = backStackEntry.arguments?.getString("equipoID") ?: ""
            CardViewTeam(navController = navController, authRepository = authRepository, equipoID)
        }
        composable("documents_screen/{userID}") { backStackEntry ->
            val arguments = backStackEntry.arguments
            val userID = arguments?.getString("userID")
            if (userID != null) {
                DocumentsScreen(navController = navController, authRepository = authRepository, userID)
            }
        }
        composable("cardDocument_screen/{userId}/{documentId}") { backStackEntry ->
            val arguments = backStackEntry.arguments
            val userID = arguments?.getString("userId")
            val documentId = arguments?.getString("documentId")
            if (userID != null && documentId != null) {
                CardView_Documents(navController = navController, authRepository = authRepository, userID, documentId)
            }
        }

        // Definir otras rutas aquí...
    }
}