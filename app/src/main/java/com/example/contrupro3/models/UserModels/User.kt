package com.example.contrupro3.models.UserModels

import android.os.Parcel
import android.os.Parcelable
import com.example.contrupro3.models.TeamsModels.Teams

data class UserModel(
    val id: String? = null,
    val name: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val emailToLowerCase: String? = null,
    val phoneNumber: String? = null,
    val role: String? = null

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
        parcel.writeString(emailToLowerCase)
        parcel.writeString(phoneNumber)
        parcel.writeString(role)
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