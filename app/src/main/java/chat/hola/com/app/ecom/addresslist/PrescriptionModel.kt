package chat.hola.com.app.ecom.addresslist

import android.os.Parcel
import android.os.Parcelable
import com.kotlintestgradle.data.utils.DataConstants.EMPTY_STRING

data class PrescriptionModel(var localImagePath: String? = EMPTY_STRING,
                             var isLocalPath: Boolean,
                             var imageUrl: String? = EMPTY_STRING) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(localImagePath)
        parcel.writeByte(if (isLocalPath) 1 else 0)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PrescriptionModel> {
        override fun createFromParcel(parcel: Parcel): PrescriptionModel {
            return PrescriptionModel(parcel)
        }

        override fun newArray(size: Int): Array<PrescriptionModel?> {
            return arrayOfNulls(size)
        }
    }
}