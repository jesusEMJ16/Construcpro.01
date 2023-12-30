package com.example.contrupro3.ui.theme.Budgets_purchasesScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.models.ProjectsModels.Project
import com.example.contrupro3.ui.theme.myOrangehigh

@Composable
fun EspecificacionesScreen(
    authRepository: AuthRepository,
    userId: String,
    selectedProject: MutableState<Project?>,
    onOpenSelectProject: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Especificaciones Técnicas", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .offset(x = 3.dp, y = -7.dp),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                onClick = { onOpenSelectProject() },
                containerColor = myOrangehigh
            ) {
                Icon(
                    Icons.Default.FolderOpen,
                    contentDescription = "Seleccionar Proyecto",
                    tint = Color.White
                )
            }
        }
    }
}