package com.henriquetavolaro.newreidofifa.ui.models

import android.os.Parcel
import android.os.Parcelable

data class Games(

    val id: String = "",
    val player1Id: String = "",
    val player2Id: String="",
    val resultP1: String="",
    val resultP2: String="",
    val date: String="",
    val players: String=""

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(player1Id)
        parcel.writeString(player2Id)
        parcel.writeString(resultP1)
        parcel.writeString(resultP2)
        parcel.writeString(date)
        parcel.writeString(players)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Games> {
        override fun createFromParcel(parcel: Parcel): Games {
            return Games(parcel)
        }

        override fun newArray(size: Int): Array<Games?> {
            return arrayOfNulls(size)
        }
    }
}
