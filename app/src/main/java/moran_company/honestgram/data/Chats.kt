package moran_company.honestgram.data

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Flowable
import java.io.Serializable

/**
 * Created by roman on 26.01.2018.
 */
const val CHATS_TABLE = "chats"


@Entity(tableName = CHATS_TABLE)
class Chats(
        @PrimaryKey(autoGenerate = true)
        var id: Long,
        var ownerId: Long,
        var companionId: Long,
        @TypeConverters(DialogsConverter::class)
        var dialogs: List<Dialogs>)
    : Parcelable, Serializable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.createTypedArrayList(Dialogs)) {
    }

    var productId: Long = 0

    constructor(productId: Long, id: Long, ownerId: Long,
                companionId: Long, dialogs: List<Dialogs>) : this(id, ownerId, companionId, dialogs) {
        this.productId = productId
    }

    constructor() : this(0, 0, 0, ArrayList())

    constructor(id: Long) : this() {
        this.id = id
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(ownerId)
        parcel.writeLong(companionId)
        parcel.writeTypedList(dialogs)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Chats> {
        override fun createFromParcel(parcel: Parcel): Chats {
            return Chats(parcel)
        }

        override fun newArray(size: Int): Array<Chats?> {
            return arrayOfNulls(size)
        }
    }

}

class DialogsConverter {
    @TypeConverter
    fun fromDialogsConverter(value: String?): List<Dialogs>? {
        val gson = Gson()
        val type = object : TypeToken<List<Dialogs>>() {}.getType()
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun dialogsConverterToGson(dialogs: List<Dialogs>?): String? {
        return Gson().toJson(dialogs)
    }
}

@Dao
interface ChatsDao{
    @Query("select * from $CHATS_TABLE")
    fun getAllTasks(): Flowable<List<Chats>>

    @Query("SELECT COUNT(*) FROM $CHATS_TABLE")
    fun hasData(): Flowable<Int>

    @Query("select * from $CHATS_TABLE where id = :arg0")
    fun findUserById(id: Long): Chats

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(user: Chats)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(user: Chats)

    @Delete
    fun deleteTask(user: Chats)
}