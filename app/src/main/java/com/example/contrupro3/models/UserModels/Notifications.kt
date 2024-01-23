package com.example.contrupro3.models.UserModels

import android.os.Parcel
import android.os.Parcelable
import com.example.contrupro3.models.TeamsModels.Teams

data class ActionButton(
    val text: String? = null,
    val action: String? = null,
    var clicked: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(action)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ActionButton> {
        override fun createFromParcel(parcel: Parcel): ActionButton {
            return ActionButton(parcel)
        }

        override fun newArray(size: Int): Array<ActionButton?> {
            return arrayOfNulls(size)
        }
    }
}

data class IconModel(
    val icon: String? = null,
    val color: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(icon)
        parcel.writeString(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ActionButton> {
        override fun createFromParcel(parcel: Parcel): ActionButton {
            return ActionButton(parcel)
        }

        override fun newArray(size: Int): Array<ActionButton?> {
            return arrayOfNulls(size)
        }
    }
}

data class NotificationModel(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val status: String? = "unread",
    val sendAt: String? = "",
    val actionButton: ActionButton? = null,
    val additionalInfo: Map<String, Any>? = null,
    val icon: IconModel? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(ActionButton::class.java.classLoader),
        readMap(parcel),
        parcel.readParcelable(IconModel::class.java.classLoader)
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(status)
        parcel.writeString(sendAt)
        parcel.writeParcelable(actionButton, flags)
        writeMap(parcel, additionalInfo)
        parcel.writeParcelable(icon, flags)
    }
    private fun writeMap(parcel: Parcel, map: Map<String, Any>?) {
        if (map != null) {
            parcel.writeInt(map.size)
            for ((key, value) in map) {
                parcel.writeString(key)
                parcel.writeValue(value)
            }
        } else {
            parcel.writeInt(-1)
        }
    }
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationModel> {
        override fun createFromParcel(parcel: Parcel): NotificationModel {
            return NotificationModel(parcel)
        }

        override fun newArray(size: Int): Array<NotificationModel?> {
            return arrayOfNulls(size)
        }
        private fun readMap(parcel: Parcel): Map<String, Any>? {
            val size = parcel.readInt()
            return if (size >= 0) {
                mutableMapOf<String, Any>().apply {
                    repeat(size) {
                        val key = parcel.readString() ?: ""
                        val value = parcel.readValue(Any::class.java.classLoader)
                        put(key, value!!)
                    }
                }
            } else {
                null
            }
        }
    }
}