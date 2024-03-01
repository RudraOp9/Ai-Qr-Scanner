package com.leo.qrscanner.workers

import android.os.Parcel
import android.os.Parcelable
import com.google.mlkit.vision.barcode.common.Barcode


class ShareData : Parcelable {
    var barcode: Barcode? = null

    constructor(barcode: Barcode?) {
        this.barcode = barcode
    }

    constructor()


    protected constructor(`in`: Parcel?)

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {}


    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ShareData?> = object : Parcelable.Creator<ShareData?> {
            override fun createFromParcel(`in`: Parcel): ShareData {
                return ShareData(`in`)
            }

            override fun newArray(size: Int): Array<ShareData?> {
                return arrayOfNulls(size)
            }
        }
    }
}
