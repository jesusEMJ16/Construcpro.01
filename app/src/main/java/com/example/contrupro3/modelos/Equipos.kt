package com.example.contrupro3.modelos

import android.os.Parcel
import android.os.Parcelable

data class Equipos(
    val id: String? = null,
    val nombreEquipo: String? = "",
    val creatorName: String? = "",
    val creatorUID: String? = "",
    val descripcion: String? = "",
    val members: List<String> = emptyList() // Lista de miembros del equipo

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombreEquipo)
        parcel.writeString(creatorName)
        parcel.writeString(creatorUID)
        parcel.writeString(descripcion)
        parcel.writeStringList(members)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Equipos> {
        override fun createFromParcel(parcel: Parcel): Equipos {
            return Equipos(parcel)
        }

        override fun newArray(size: Int): Array<Equipos?> {
            return arrayOfNulls(size)
        }
    }
}