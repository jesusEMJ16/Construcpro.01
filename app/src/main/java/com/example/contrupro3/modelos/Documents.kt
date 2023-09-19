package com.example.contrupro3.modelos

import android.os.Parcel
import android.os.Parcelable

data class DocumentModel(
    val id: String? = null,
    val name: String? = "",
    val creatorName: String? = "",
    val creatorUID: String? = "",
    val description: String? = "",
    val fileRef: String? = "",
    val previewRef: String? = "",
    val teamsLinked: List<String> = emptyList(),
    val usersLinked: List<String> = emptyList()

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
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
        parcel.writeString(fileRef)
        parcel.writeString(previewRef)
        parcel.writeStringList(teamsLinked)
        parcel.writeStringList(usersLinked)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DocumentModel> {
        override fun createFromParcel(parcel: Parcel): DocumentModel {
            return DocumentModel(parcel)
        }

        override fun newArray(size: Int): Array<DocumentModel?> {
            return arrayOfNulls(size)
        }
    }
}