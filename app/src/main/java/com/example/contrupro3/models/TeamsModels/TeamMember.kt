package com.example.contrupro3.models.TeamsModels

import android.os.Parcel
import android.os.Parcelable

data class TeamMember(
    val id: String? = null,
    val name: String? = "",
    val lastName: String? = "",
    val email: String? = "",
    val role: String? = "",
    val phoneNumber: String? = "",
    val inviteStatus: String? = ""

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(lastName)
        parcel.writeString(email)
        parcel.writeString(role)
        parcel.writeString(phoneNumber)
        parcel.writeString(inviteStatus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Teams> {
        override fun createFromParcel(parcel: Parcel): Teams {
            return Teams(parcel)
        }

        override fun newArray(size: Int): Array<Teams?> {
            return arrayOfNulls(size)
        }
    }
}