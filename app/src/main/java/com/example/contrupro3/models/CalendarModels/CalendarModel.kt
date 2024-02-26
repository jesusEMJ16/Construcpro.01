package com.example.contrupro3.models.CalendarModels

import java.time.LocalDate

class CalendarModel {
}
data class Event(
    val id: Int,
    val title: String,
    val date: LocalDate
    // Puedes añadir más detalles aquí como descripción, color para la indicación, etc.
)