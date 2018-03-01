package moran_company.honestgram.data

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import io.reactivex.Flowable
import moran_company.honestgram.utility.ApiConstants
import java.io.Serializable

/**
 * Created by roman on 23.01.2018.
 */
const val CITY_TABLE = "cities"

@IgnoreExtraProperties
@Entity(tableName = CITY_TABLE)
data class City (
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        // TODO maybe this
        var city: String ) : Parcelable, Serializable {
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

@Dao
interface CitiesDao {
    @Query("select * from $CITY_TABLE")
    fun getAllTasks(): Flowable<List<City>>

    @Query("DELETE FROM $CITY_TABLE")
    fun delete()

    @Query("SELECT COUNT(*) FROM $CITY_TABLE")
    fun hasData(): Flowable<Int>

    @Query("select * from $CITY_TABLE where id = :arg0")
    fun findUserById(id: Long): City

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(user: City)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(user: City)

    @Delete
    fun deleteTask(user: City)
}