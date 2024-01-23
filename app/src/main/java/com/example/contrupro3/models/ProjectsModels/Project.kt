package com.example.contrupro3.models.ProjectsModels

import android.os.Parcel
import android.os.Parcelable

data class CountersModel(
    val members: Int = 0,
    val teams: Int = 0,
    val documents: Int = 0,
    val tasks: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(members)
        parcel.writeInt(teams)
        parcel.writeInt(documents)
        parcel.writeInt(tasks)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CountersModel> {
        override fun createFromParcel(parcel: Parcel): CountersModel {
            return CountersModel(parcel)
        }

        override fun newArray(size: Int): Array<CountersModel?> {
            return arrayOfNulls(size)
        }
    }
}

data class ProjectModel(
    val id: String? = null,
    var name: String? = null,
    val creatorName: String? = null,
    val creatorUID: String? = null,
    var description: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val counters: CountersModel? = CountersModel(),
    val createdAt: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(CountersModel::class.java.classLoader),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(creatorName)
        parcel.writeString(creatorUID)
        parcel.writeString(description)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeParcelable(counters, flags)
        parcel.writeString(createdAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProjectModel> {
        override fun createFromParcel(parcel: Parcel): ProjectModel {
            return ProjectModel(parcel)
        }

        override fun newArray(size: Int): Array<ProjectModel?> {
            return arrayOfNulls(size)
        }
    }
}