package moran_company.honestgram.data

import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.Gson
import com.google.maps.android.clustering.ClusterItem

/**
 * Created by roman on 16.02.2018.
 */
@IgnoreExtraProperties
open class ItemUserOrder(
        @PrimaryKey(autoGenerate = true)
        open var id: Long,
        open var photoName: String? = null,
        open var photoURL: String? = null,
        @TypeConverters(LocationConverter::class)
        open var location: CustomLocation
) : ClusterItem {

    constructor() : this(0,"","",CustomLocation())

    //fun getLatLng(): LatLng

    override fun getPosition(): LatLng = location.getLatLng()

//    fun getNamePhoto() : String?
//
//    fun getUrl() : String?
//
//    fun getTitle() : String?
//
//    fun getLocationsList() : ArrayList<CustomLocation>?
//
//    fun getPlacePhoto() : String ?

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

}