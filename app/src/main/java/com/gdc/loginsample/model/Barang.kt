package com.gdc.loginsample.model

import android.os.Parcel
import android.os.Parcelable

data class Barang(
    var nama: String? = "",
    var merk: String? = "",
    var harga: String? = "",
    var key: String? = ""): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nama)
        parcel.writeString(merk)
        parcel.writeString(harga)
        parcel.writeString(key)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Barang> {
        override fun createFromParcel(parcel: Parcel): Barang {
            return Barang(parcel)
        }

        override fun newArray(size: Int): Array<Barang?> {
            return arrayOfNulls(size)
        }
    }
}