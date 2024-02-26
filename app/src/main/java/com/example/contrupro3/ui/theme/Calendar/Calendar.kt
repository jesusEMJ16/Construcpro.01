package com.example.contrupro3.ui.theme.Calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contrupro3.models.AuthRepository
import com.example.contrupro3.ui.theme.mywhie
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import io.github.boguszpawlowski.composecalendar.StaticWeekCalendar


@Composable
fun Calendar(navController: NavController, AuthRepository: AuthRepository, userID : String) {



}

@Composable
fun Calendarp(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Center column contents horizontally
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Calendario",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            // Aquí incluiríamos el componente del calendario
            StaticCalendar()

            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(5.dp))

            CalendarInfoSection()
        }
        // Aquí incluiríamos la información relevante y los iconos debajo del calendario
    }
}
@Composable
fun CalendarInfoSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        InfoItem(text = "10 proyectos por finalizar", icon = Icons.Default.Work)
        InfoItem(text = "5 tareas a punto de acabar", icon = Icons.Default.List)
        InfoItem(text = "3 fechas de pago próximas", icon = Icons.Default.Payment)
        InfoItem(text = "1 junta esta semana", icon = Icons.Default.Event)
        // Puedes añadir más InfoItem aquí según sea necesario.
    }
}

@Composable
fun InfoItem(text: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null // Decorative icon, no description needed.
        )
        Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el icono y el texto.
        Text(text = text)
    }
}
@Composable
@Preview
fun CalendarPreview() {
    Calendarp()
}