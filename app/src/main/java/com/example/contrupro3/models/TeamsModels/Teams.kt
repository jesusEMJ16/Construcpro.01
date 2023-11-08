package com.example.contrupro3.models.TeamsModels

import android.os.Parcel
import android.os.Parcelable

data class Teams(
    val id: String? = null,
    val name: String? = "",
    val creatorName: String? = "",
    val creatorUID: String? = "",
    val description: String? = "",
    val members: List<String>? = emptyList(),
    val projectsLinked: List<String>? = emptyList()

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList() ?: emptyList(),
        parcel.createStringArrayList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(creatorName)
        parcel.writeString(creatorUID)
        parcel.writeString(description)
        parcel.writeStringList(members)
        parcel.writeStringList(projectsLinked)
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