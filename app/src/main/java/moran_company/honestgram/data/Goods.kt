package moran_company.honestgram.data

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import moran_company.honestgram.utility.ApiConstants
import java.io.Serializable

/**
 * Created by roman on 12.01.2018.
 */
const val PRODUCTS_TABLE = "products"
const val ROW_CITY_ID = "city_id"
const val ROW_PRICE = "price"
@IgnoreExtraProperties
@Entity(tableName = PRODUCTS_TABLE)
data class Goods(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var title: String? = null,
        @ColumnInfo(name = ROW_PRICE)
        var price: Long? = null,
        var url: String? = null,
        @ColumnInfo(name = ROW_CITY_ID)
        var cityId: Long? = null,
        var description: String? = null,
        var ownerId: Long? = null,
        var specialPrice: Long = 0) : Parcelable, Serializable {


    @TypeConverters(ArrayListUrlsConverter::class)
    var urls: ArrayList<Urls>? = null

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readValue(Long::class.java.classLoader) as Long) {

    }

    constructor(urls: ArrayList<Urls>) : this(0, "", 0, "", 0, "", 0, 0) {
        this.urls = urls
    }



    constructor() : this(0, "", 0, "", 0, "", 0, 0)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeValue(price)
        parcel.writeString(url)
        parcel.writeValue(cityId)
        parcel.writeString(description)
        parcel.writeValue(ownerId)
        parcel.writeValue(specialPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Goods(id=$id, title=$title, price=$price, url=$url, cityId=$cityId, description=$description, ownerId=$ownerId, specialPrice=$specialPrice, urls=$urls)"
    }

    companion object CREATOR : Parcelable.Creator<Goods> {
        override fun createFromParcel(parcel: Parcel): Goods {
            return Goods(parcel)
        }

        override fun newArray(size: Int): Array<Goods?> {
            return arrayOfNulls(size)
        }
    }

    class ArrayListUrlsConverter {
        @TypeConverter
        fun fromGoods(value: String?): ArrayList<Urls>? {
            val gson = Gson()
            val type = object : TypeToken<List<Urls>>(){}.getType()
            return gson.fromJson(value, type)
        }

        @TypeConverter
        fun customGoodsToGson(location: ArrayList<Urls>?): String? {
            return Gson().toJson(location)
        }
    }
}


@Dao
interface GoodsDao {
    @Query("select * from $PRODUCTS_TABLE")
    fun getAllTasks(): Flowable<List<Goods>>

    @Query("select * from $PRODUCTS_TABLE ORDER BY $ROW_CITY_ID desc")
    fun getFilteredByCityDesc(): Flowable<List<Goods>>

    @Query("select * from $PRODUCTS_TABLE ORDER BY $ROW_PRICE desc")
    fun getFilteredByPriceDesc(): Flowable<List<Goods>>

    @Query("select * from $PRODUCTS_TABLE where ownerId = :arg0")
    fun getProductByOwnerId(id: Long): Flowable<List<Goods>>

    @Query("SELECT COUNT(*) FROM $PRODUCTS_TABLE")
    fun hasData(): Flowable<Int>

    @Query("select * from $PRODUCTS_TABLE where id = :arg0")
    fun findUserById(id: Long): Goods

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(user: Goods)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(user: Goods)

    @Delete
    fun deleteTask(user: Goods)
}