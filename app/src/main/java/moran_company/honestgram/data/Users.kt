package moran_company.honestgram.data

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Flowable
import moran_company.honestgram.data.enums.TYPE_USER
import java.io.Serializable


/**
 * Created by roman on 12.01.2018.
 */
const val USER_TABLE = "users"
const val ROW_ID = "id"

@Entity(tableName = USER_TABLE/*,
        foreignKeys = arrayOf(ForeignKey(entity = CustomLocation::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("location_id")))*/)
@IgnoreExtraProperties
data class Users(
        var city: String? = null,
        var district: String? = null,
        var nickname: String? = null,
        var login: String? = null,
        var password: String? = null,
        var token: String? = null
) : Serializable, Parcelable, ItemUserOrder() {


    fun getLocationsList(): ArrayList<CustomLocation>? = locations

    @TypeConverters(ArrayListCustomLocationConverter::class)
    @Ignore
    var locations: ArrayList<CustomLocation>? = null
    @TypeConverters(ArrayListGoodsConverter::class)
    @Ignore
    var cart: String? = null
    //@Ignore
    var statusId: Long? = 0

    override var id: Long
        get() = super.id
        set(value) {
            super.id = value
        }

    override var location: CustomLocation
        get() = super.location
        set(value) {
            super.location = value
        }

    override var photoName: String?
        get() = super.photoName
        set(value) {
            super.photoName = value
        }

    override var photoURL: String?
        get() = super.photoURL
        set(value) {
            super.photoURL = value
        }

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
        cart = parcel.readString()
        statusId = parcel.readValue(Long::class.java.classLoader) as? Long
    }

    constructor(id: Long,
                city: String,
                district: String,
                nickname: String,
                login: String,
                password: String,
                photoName: String,
                photoURL: String,
                token: String?,
                location : CustomLocation
    ) : this() {
        this.id = id
        this.city = city
        this.district = district
        this.nickname = nickname
        this.login = login
        this.password = password
        this.photoName = photoName
        this.photoURL = photoURL
        this.token = token
        this.location = location
    }

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
            location: CustomLocation,
            locations: ArrayList<CustomLocation>?,
            cart: String?,
            statusId: Long?) : this(id, city, district, nickname, login, password, photoName, photoURL, token, location) {
        this.id = id
        this.locations = locations
        this.cart = cart
        this.statusId = statusId
    }

    open fun getCartList(): ArrayList<Goods>? {
        if (this.cart != null) {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Goods>>() {

            }.getType()
            return gson.fromJson(this.cart, type)
        } else return null
    }

    fun getTypeUser(): TYPE_USER? = TYPE_USER.getTypeUserById(statusId!!.toInt())

    open fun setCartList(goods: ArrayList<Goods>) {
        this.cart = Gson().toJson(location)
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
        parcel.writeString(cart)
        parcel.writeValue(statusId)
    }

    override fun describeContents(): Int {
        return 0
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


class LocationConverter {
    @TypeConverter
    fun fromCustomLocation(value: String?): CustomLocation? {
        return if (value == null) null else Gson().fromJson(value, CustomLocation::class.java)
    }

    @TypeConverter
    fun customLocationToGson(location: CustomLocation?): String? {
        return Gson().toJson(location)
    }
}

class ArrayListCustomLocationConverter {
    @TypeConverter
    fun fromCustomLocation(value: String?): ArrayList<CustomLocation>? {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<CustomLocation>>() {

        }.getType()
        return gson.fromJson(value, type)
        //return if (value == null) null else Gson().fromJson(value, ArrayList<Any>::class.java)
    }

    @TypeConverter
    fun customLocationToGson(location: ArrayList<CustomLocation>?): String? {
        return Gson().toJson(location)
    }
}

class ArrayListGoodsConverter {
    @TypeConverter
    fun fromGoods(value: String?): ArrayList<Goods>? {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Goods>>() {}.getType()
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun customGoodsToGson(location: ArrayList<Goods>?): String? {
        return Gson().toJson(location)
    }
}


@Dao
interface UsersDao {
    @Query("select * from $USER_TABLE")
    fun getAllTasks(): Flowable<List<Users>>

    @Query("SELECT COUNT(*) FROM $USER_TABLE")
    fun hasData(): Flowable<Int>

    @Query("select * from $USER_TABLE where id = :arg0")
    fun findUserById(id: Long): Users

    @Insert(onConflict = REPLACE)
    fun insertTask(user: Users)

    @Update(onConflict = REPLACE)
    fun updateTask(user: Users)

    @Delete
    fun deleteTask(user: Users)
}