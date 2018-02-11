package moran_company.honestgram.data

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.clustering.ClusterItem
import java.io.Serializable


/**
 * Created by roman on 12.01.2018.
 */
/*@Entity(tableName = "users"*//*,
        foreignKeys = arrayOf(ForeignKey(entity = CustomLocation::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("location_id")))*//*)*/
@IgnoreExtraProperties
class Users (
        //@PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var city: String? = null,
        var district: String? = null,
        var nickname: String? = null,
        var login: String? = null,
        var password: String? = null,
        var photoName: String? = null,
        var photoURL: String? = null,
        var token: String? = null,
        //@TypeConverters(LocationConverter::class)
        //@Ignore
        var location: CustomLocation? = null
) : Serializable, Parcelable, ClusterItem {



    //@Ignore
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

    //@Ignore
    override fun getPosition(): LatLng? {
        return LatLng(location!!.latitude.toDouble(), location!!.longitude.toDouble())
    }

    //@TypeConverters(ArrayListCustomLocationConverter::class)
    //@Ignore
    var locations: ArrayList<CustomLocation>? = null
    //@TypeConverters(ArrayListGoodsConverter::class)
    //@Ignore
    var cart: ArrayList<Goods>? = null
    //@Ignore
    var statusId: Long? = null

    constructor() : this(0,"","","","","","","","",null)

    //@Ignore
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
            cart: ArrayList<Goods>?,
            statusId: Long?) : this(id, city, district, nickname, login, password, photoName, photoURL, token, location) {
        this.locations = locations
        this.cart = cart
        this.statusId = statusId
    }


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

    class LocationConverter {
        //@TypeConverter
        fun fromCustomLocation(value: String?): CustomLocation? {
            return if (value == null) null else Gson().fromJson(value, CustomLocation::class.java)
        }

        //@TypeConverter
        fun customLocationToGson(location: CustomLocation?): String? {
            return Gson().toJson(location)
        }
    }

    class ArrayListCustomLocationConverter {
        //@TypeConverter
        fun fromCustomLocation(value: String?): ArrayList<CustomLocation>? {
            val gson = Gson()
            val type = object : TypeToken<List<CustomLocation>>() {

            }.getType()
            return gson.fromJson(value, type)
            //return if (value == null) null else Gson().fromJson(value, ArrayList<Any>::class.java)
        }

        //@TypeConverter
        fun customLocationToGson(location: ArrayList<CustomLocation>?): String? {
            return Gson().toJson(location)
        }
    }
    class ArrayListGoodsConverter {
        //@TypeConverter
        fun fromGoods(value: String?): ArrayList<Goods>? {
            val gson = Gson()
            val type = object : TypeToken<List<Goods>>() {

            }.getType()
            return gson.fromJson(value, type)
            //return if (value == null) null else Gson().fromJson(value, ArrayList<Any>::class.java)
        }

        //@TypeConverter
        fun customGoodsToGson(location: ArrayList<Goods>?): String? {
            return Gson().toJson(location)
        }
    }
}