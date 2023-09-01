package com.example.contrupro3.modelos

import android.os.Parcel
import android.os.Parcelable

data class Project(
    val id: String? = null, // Este es el projectID que necesitas para la función deleteProject
    val projectName: String = "",
    val creatorName: String = "", // Visible on the card
    val creatorUID: String = "", // Used for association in the database
    val descripcion: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val numParticipants: Int = 0,
    val numTask: Int = 0,
    val numDocuments: Int = 0,
    val estado: String = ""

) : Parcelable {
    constructor(
        readString: String,
        readString1: String,
        readString2: String,
        readString3: String,
        readString4: String,
        readInt: Int,
        readInt1: Int,
        readInt2: Int,
        readString5: String
    ) : this("", "", "","", "", "", "",0, 0, 0, "")

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(projectName)
        parcel.writeString(creatorName)
        parcel.writeString(creatorUID)
        parcel.writeString(descripcion)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeInt(numParticipants)
        parcel.writeInt(numTask)
        parcel.writeInt(numDocuments)
        parcel.writeString(estado)
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