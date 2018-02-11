package moran_company.honestgram.data

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

/**
 * Created by roman on 18.01.2018.
 */
@IgnoreExtraProperties
class CustomLocation constructor(
        var longitude: Float,
        var latitude: Float) : Parcelable, Serializable {
    constructor(parcel: Parcel) : this(
            parcel.readFloat(),
            parcel.readFloat()) {
    }
    var id: Long = 0
    var time: Long = 0

    constructor(id: Long?, time: Long?, longitude: Float?, latitude: Float?) : this(longitude!!, latitude!!) {
        this.id = id!!
        this.time = time!!
    }


    open fun getLatLng(): LatLng {
        return LatLng(latitude.toDouble(), longitude.toDouble())
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(longitude)
        parcel.writeFloat(latitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "CustomLocation(longitude=$longitude, latitude=$latitude)"
    }

    companion object CREATOR : Parcelable.Creator<CustomLocation> {
        override fun createFromParcel(parcel: Parcel): CustomLocation {
            return CustomLocation(parcel)
        }

        override fun newArray(size: Int): Array<CustomLocation?> {
            return arrayOfNulls(size)
        }
    }


    constructor() : this(0f, 0f)

}