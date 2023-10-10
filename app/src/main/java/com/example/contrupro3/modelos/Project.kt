package com.example.contrupro3.modelos

import android.os.Parcel
import android.os.Parcelable

data class Project(
    val id: String? = null,
    var projectName: String = "",
    val creatorName: String = "",
    val creatorUID: String = "",
    var description: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val documents: List<String> = emptyList(),
    val tasks: List<String> = emptyList(),
    val teams: List<String> = emptyList(),
    val members: List<String> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList() ?: emptyList(),
        parcel.createStringArrayList() ?: emptyList(),
        parcel.createStringArrayList() ?: emptyList(),
        parcel.createStringArrayList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(projectName)
        parcel.writeString(creatorName)
        parcel.writeString(creatorUID)
        parcel.writeString(description)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeStringList(documents)
        parcel.writeStringList(tasks)
        parcel.writeStringList(teams)
        parcel.writeStringList(members)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Project> {
        override fun createFromParcel(parcel: Parcel): Project {
            return Project(parcel)
        }

        override fun newArray(size: Int): Array<Project?> {
            return arrayOfNulls(size)
        }
    }
}