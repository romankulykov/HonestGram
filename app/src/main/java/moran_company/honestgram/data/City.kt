package moran_company.honestgram.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

/**
 * Created by roman on 23.01.2018.
 */
@IgnoreExtraProperties
class City constructor(var id : Long, var city : String) : Parcelable,Serializable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString()) {
    }

    constructor() : this(0,"")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(city)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<City> {
        override fun createFromParcel(parcel: Parcel): City {
            return City(parcel)
        }

        override fun newArray(size: Int): Array<City?> {
            return arrayOfNulls(size)
        }
    }

}