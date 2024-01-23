package com.example.contrupro3.models.BudgetModels.Purchases

import android.os.Parcel
import android.os.Parcelable
import com.example.contrupro3.models.DocumentsModels.DocumentModel

data class PurchasesModel(
    val id: String? = null,
    val name: String? = null,
    val date: String? = null,
    val supplier: String? = null,
    val quantity: Int? = null,
    val priceUnit: Double? = null,
    val totalPrice: Double? = null,
    val creatorName: String? = null,
    val creatorUID: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString()
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(date)
        parcel.writeString(supplier)
        parcel.writeInt(quantity?: 0)
        parcel.writeDouble(priceUnit?: 0.0)
        parcel.writeDouble(totalPrice?: 0.0)
        parcel.writeString(creatorName)
        parcel.writeString(creatorUID)
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