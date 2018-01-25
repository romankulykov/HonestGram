package moran_company.honestgram.data

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.IgnoreExtraProperties
import com.google.maps.android.clustering.ClusterItem
import java.io.Serializable

/**
 * Created by roman on 12.01.2018.
 */
@IgnoreExtraProperties
class Users constructor(
        var id: Long,
        var city: String,
        var district: String,
        var nickname: String,
        var login: String,
        var password: String,
        var photoName: String,
        var photoURL: String,
        var token: String?,
        var location: CustomLocation?
) : Serializable, Parcelable, ClusterItem {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(CustomLocation::class.java.classLoader)) {
    }

    override fun getPosition(): LatLng? {
        return LatLng(location!!.latitude.toDouble(), location!!.longitude.toDouble())
    }

    var locations: ArrayList<CustomLocation>? = null
    var cart: ArrayList<Goods>? = null

    constructor(
            id: Long,
            city: String,
            district: String,
            nickname: String,
            login: String,
            password: String,
            photoName: String,
            photoURL: String,
            token: String?,
            location: CustomLocation?,
            locations: ArrayList<CustomLocation>?,
            cart: ArrayList<Goods>?) : this(id, city, district, nickname, login, password, photoName, photoURL, token, location) {
        this.locations = locations
        this.cart = cart
    }


    constructor() : this(0, "", "", "", "", "", "", "", "", CustomLocation(0.0f, 0.0f))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(city)
        parcel.writeString(district)
        parcel.writeString(nickname)
        parcel.writeString(login)
        parcel.writeString(password)
        parcel.writeString(photoName)
        parcel.writeString(photoURL)
        parcel.writeString(token)
        parcel.writeParcelable(location, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Users(id=$id, city='$city', district='$district', nickname='$nickname', login='$login', password='$password', photoName='$photoName', photoURL='$photoURL', token=$token, location=$location)"
    }

    companion object CREATOR : Parcelable.Creator<Users> {
        override fun createFromParcel(parcel: Parcel): Users {
            return Users(parcel)
        }

        override fun newArray(size: Int): Array<Users?> {
            return arrayOfNulls(size)
        }
    }


}