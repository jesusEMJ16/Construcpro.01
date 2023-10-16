package com.example.contrupro3.modelos

import android.os.Parcel
import android.os.Parcelable

data class UserModel(
    val name: String? = null,
    val lastName: String? = "",
    val email: String? = "",
    val phoneNumber: String? = "",
    val role: String? = ""

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(lastName)
        parcel.writeString(email)
        parcel.writeString(phoneNumber)
        parcel.writeString(role)
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