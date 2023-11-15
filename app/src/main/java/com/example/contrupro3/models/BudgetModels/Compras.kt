package com.example.contrupro3.models.BudgetModels

import android.os.Parcel
import android.os.Parcelable
import com.example.contrupro3.models.DocumentsModels.DocumentModel

data class PurchasesModel(
    val id: String? = null,
    val name: String? = "",
    val date: String? = "",
    val supplier: String? = "",
    val quantity: Int? = null,
    val priceUnit: Double? = null,
    val totalPrice: Double? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble()
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(date)
        parcel.writeString(supplier)
        parcel.writeInt(quantity?: 0)
        parcel.writeDouble(priceUnit?: 0.0)
        parcel.writeDouble(totalPrice?: 0.0)
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