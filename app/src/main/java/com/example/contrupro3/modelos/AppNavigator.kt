package com.example.contrupro3.modelos

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.contrupro3.ui.theme.Budgets_purchasesScreens.Presupuesto_y_Compras
import com.example.contrupro3.ui.theme.DocumentsScreens.CardViewDocumentsScreen
import com.example.contrupro3.ui.theme.DocumentsScreens.DocumentsScreen
import com.example.contrupro3.ui.theme.LoginPage
import com.example.contrupro3.ui.theme.ProjectsScreens.CardviewProjectsScreen
import com.example.contrupro3.ui.theme.ProjectsScreens.ProjectView
import com.example.contrupro3.ui.theme.RegisterPage
import com.example.contrupro3.ui.theme.SplashScreen
import com.example.contrupro3.ui.theme.TeamsScreens.CardViewTeamsScreen
import com.example.contrupro3.ui.theme.TeamsScreens.CardviewTeam_ViewModel
import com.example.contrupro3.ui.theme.TeamsScreens.TeamsScreen
import com.example.contrupro3.ui.theme.UserProfilePage
import com.google.firebase.auth.FirebaseAuth


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigator(
    auth: FirebaseAuth,
    navController: NavHostController,
    authRepository: AuthRepository,
) {

    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") {
            SplashScreen(navController, authRepository)
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
        composable("projects_screen/{userID}") { backStackEntry ->
            val arguments = backStackEntry.arguments
            val userID = arguments?.getString("userID")
            if (userID != null) {
                ProjectView(navController = navController, authRepository = authRepository, userID)
            }
        }
        composable("cardview_projects_screen/{userId}/{projectId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            CardviewProjectsScreen(navController, authRepository, userId, projectId)
        }
        composable("teams_screen/{userID}") { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")
            if (userID != null) {
                TeamsScreen(navController, authRepository, userID)
            }
        }
        composable("cardview_teams_screen/{userId}/{teamId}") { backStackEntry ->
            val teamId = backStackEntry.arguments?.getString("teamId") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            CardViewTeamsScreen(navController, authRepository, userId, teamId, CardviewTeam_ViewModel())
        }
        composable("documents_screen/{userID}") { backStackEntry ->
            val arguments = backStackEntry.arguments
            val userID = arguments?.getString("userID")
            if (userID != null) {
                DocumentsScreen(navController, authRepository, userID)
            }
        }
        composable("cardview_documents_screen/{userId}/{documentId}") { backStackEntry ->
            val arguments = backStackEntry.arguments
            val userID = arguments?.getString("userId")
            val documentId = arguments?.getString("documentId")
            if (userID != null && documentId != null) {
                CardViewDocumentsScreen(navController, authRepository, userID, documentId)
            }
        }
        composable("presucom_screen") { backStackEntry ->
            Presupuesto_y_Compras(navController, authRepository)
        }
        // Definir otras rutas aquí...
    }
}