package com.app.novatech.model

import android.os.Parcel
import android.os.Parcelable

data class Tasks(
    val name: String,
    val description: String,
    val storyPoints: Int,
    val responsible: String,
    val status: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(storyPoints)
        parcel.writeString(status)
        parcel.writeString(responsible)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tasks> {
        override fun createFromParcel(parcel: Parcel): Tasks {
            return Tasks(parcel)
        }

        override fun newArray(size: Int): Array<Tasks?> {
            return arrayOfNulls(size)
        }
    }
}
