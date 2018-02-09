package moran_company.honestgram.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

/**
 * Created by roman on 29.01.2018.
 */
@IgnoreExtraProperties
@org.parceler.Parcel(value = org.parceler.Parcel.Serialization.BEAN)
class Urls constructor(var url : String) : Parcelable,Serializable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    constructor() : this("")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Urls> {
        override fun createFromParcel(parcel: Parcel): Urls {
            return Urls(parcel)
        }

        override fun newArray(size: Int): Array<Urls?> {
            return arrayOfNulls(size)
        }
    }
}