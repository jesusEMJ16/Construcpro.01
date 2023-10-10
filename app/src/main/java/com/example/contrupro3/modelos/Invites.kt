package com.example.contrupro3.modelos

import android.os.Parcel
import android.os.Parcelable

data class ModelInvites(
    val id: String? = null,
    val userName: String? = "",
    val userEmail: String? = "",
    val userPhotoUrl: String? = "",
    val status: String? = ""

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userName)
        parcel.writeString(userEmail)
        parcel.writeString(userPhotoUrl)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelInvites> {
        override fun createFromParcel(parcel: Parcel): ModelInvites {
            return ModelInvites(parcel)
        }

        override fun newArray(size: Int): Array<ModelInvites?> {
            return arrayOfNulls(size)
        }
    }
}