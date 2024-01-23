package com.example.contrupro3.models

import CardviewTaskScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.contrupro3.models.DocumentsModels.DocumentScreen_ViewModel
import com.example.contrupro3.models.LoginModels.Login_ViewModel
import com.example.contrupro3.models.LoginModels.Register_ViewModel
import com.example.contrupro3.models.ProjectsModels.ProjectCard_ViewModel
import com.example.contrupro3.models.ProjectsModels.ProjectsScreen_ViewModel
import com.example.contrupro3.models.TasksModels.Cardview_Task_ViewModel
import com.example.contrupro3.models.TasksModels.TasksScreen_ViewModel
import com.example.contrupro3.models.TeamsModels.TeamCard_ViewModel
import com.example.contrupro3.models.TeamsModels.TeamScreen_ViewModel
import com.example.contrupro3.ui.theme.Budgets_purchasesScreens.Presupuesto_y_Compras
import com.example.contrupro3.ui.theme.DocumentsScreens.CardViewDocumentsScreen
import com.example.contrupro3.ui.theme.DocumentsScreens.DocumentsScreen
import com.example.contrupro3.ui.theme.LoginScreens.LoginPage
import com.example.contrupro3.ui.theme.LoginScreens.RegisterPage
import com.example.contrupro3.ui.theme.ProjectsScreens.CardviewProjectsScreen
import com.example.contrupro3.ui.theme.ProjectsScreens.ProjectView
import com.example.contrupro3.ui.theme.SplashScreen.SplashScreen
import com.example.contrupro3.ui.theme.TasksScreen.TasksScreen
import com.example.contrupro3.ui.theme.TeamsScreens.CardViewTeamsScreen
import com.example.contrupro3.ui.theme.TeamsScreens.TeamsScreen
import com.example.contrupro3.ui.theme.UserProfileScreens.UserProfileView
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
            LoginPage(navController, authRepository, Login_ViewModel())
        }
        composable("register_screen") {
            RegisterPage(navController, Register_ViewModel())
        }
        composable("user_screen") {
            UserProfileView(navController = navController, authRepository)
        }
        // Projects Screen
        composable("projects_screen/{userID}") { backStackEntry ->
            val arguments = backStackEntry.arguments
            val userID = arguments?.getString("userID")
            if (userID != null) {
                ProjectView(navController, authRepository, userID, ProjectsScreen_ViewModel())
            }
        }
        // Project Cardview
        composable("cardview_projects_screen/{userId}/{projectId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            CardviewProjectsScreen(navController, authRepository, userId, projectId, ProjectCard_ViewModel())
        }
        // Tasks Screen
        composable("tasks_screen/{userID}") { backStackEntry ->
            val arguments = backStackEntry.arguments
            val userID = arguments?.getString("userID")
            if (userID != null) {
                TasksScreen(navController, authRepository, userID, TasksScreen_ViewModel())
            }
        }
        // Task Cardview
        composable("cardview_tasks_screen/{userID}/{projectId}/{taskId}") { backStackEntry ->
            val arguments = backStackEntry.arguments
            val userID = arguments?.getString("userID")
            val projectId = arguments?.getString("projectId")
            val taskId = arguments?.getString("taskId")
            if (userID != null && projectId != null && taskId != null) {
                CardviewTaskScreen(navController, authRepository, userID, projectId, taskId, Cardview_Task_ViewModel())
            }
        }
        // Teams Screen
        composable("teams_screen/{userID}") { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")
            if (userID != null) {
                TeamsScreen(navController, authRepository, userID, TeamScreen_ViewModel())
            }
        }
        // Team Cardview
        composable("cardview_teams_screen/{userId}/{teamId}/{projectId}") { backStackEntry ->
            val teamId = backStackEntry.arguments?.getString("teamId") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            CardViewTeamsScreen(
                navController,
                authRepository,
                userId,
                teamId,
                projectId,
                TeamCard_ViewModel()
            )
        }
        // Documents Screen
        composable("documents_screen/{userID}") { backStackEntry ->
            val arguments = backStackEntry.arguments
            val userID = arguments?.getString("userID")
            if (userID != null) {
                DocumentsScreen(navController, authRepository, userID, DocumentScreen_ViewModel())
            }
        }
        // Document Cardview
        composable("cardview_documents_screen/{userId}/{projectId}/{documentId}") { backStackEntry ->
            val arguments = backStackEntry.arguments
            val userID = arguments?.getString("userId")
            val projectId = arguments?.getString("projectId")
            val documentId = arguments?.getString("documentId")
            if (userID != null && projectId != null && documentId != null) {
                CardViewDocumentsScreen(
                    navController,
                    authRepository,
                    userID,
                    projectId,
                    documentId
                )
            }
        }
        composable("presucom_screen/{userId}") { backStackEntry ->
            val arguments = backStackEntry.arguments
            val userID = arguments?.getString("userId")
            if (userID != null) {
                Presupuesto_y_Compras(navController, authRepository, userID)
            }
        }
        // Definir otras rutas aquí...
    }
}