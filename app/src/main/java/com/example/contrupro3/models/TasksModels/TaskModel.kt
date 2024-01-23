package com.example.contrupro3.models.TasksModels

import android.os.Parcel
import android.os.Parcelable

data class TaskModel(
    val id: String? = null,
    val name: String? = null,
    val creatorName: String? = null,
    val creatorUID: String? = null,
    val description: String? = null,
    val startDate: String? = null,
    val endDate: String? = null
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
        parcel.writeString(creatorName)
        parcel.writeString(creatorUID)
        parcel.writeString(description)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskModel> {
        override fun createFromParcel(parcel: Parcel): TaskModel {
            return TaskModel(parcel)
        }

        override fun newArray(size: Int): Array<TaskModel?> {
            return arrayOfNulls(size)
        }
    }
}