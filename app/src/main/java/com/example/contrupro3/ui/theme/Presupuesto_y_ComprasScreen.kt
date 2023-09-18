package com.example.contrupro3.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contrupro3.modelos.AuthRepository

@Composable
fun Presupuesto_y_Compras(navController : NavController, authRepository : AuthRepository) {
    var currentView by remember { mutableStateOf("default") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Presupuesto y Compras",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { currentView = "presupuesto" },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (currentView == "presupuesto") Color.White else Color.Black
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .background(if (currentView == "presupuesto") Color.Gray else Color.Transparent)
                        .border(BorderStroke(0.dp, Color.Transparent)),
                    shape = RectangleShape
                ) {
                    Text("Presupuesto")
                }

                Divider(
                    color = Color.LightGray, modifier = Modifier
                        .height(40.dp)
                        .width(1.dp)
                        .align(Alignment.CenterVertically)
                )

                Button(
                    onClick = { currentView = "compras" },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (currentView == "compras") Color.White else Color.Black
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .background(if (currentView == "compras") Color.Gray else Color.Transparent)
                        .border(BorderStroke(0.dp, Color.Transparent)),
                    shape = RectangleShape
                ) {
                    Text("Compras")
                }
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(5.dp))

            when(currentView) {
                "presupuesto" -> Presupuesto()
                "compras" -> Compras()
                else -> { /* Mostrar vista por defecto o nada */ }
            }
        }
        HamburgueerMenu(navController = navController, authRepository = authRepository)
    }
}

@Composable
fun Presupuesto() {
    Box(modifier = Modifier
        .background(Color.White)){
        Text(text = "Presupuesto")
    }
}

@Composable
fun Compras() {
    Box(modifier = Modifier
        .background(Color.White)){
        Text(text = "Compras")
    }
}
