package moran_company.honestgram.data

import android.arch.persistence.room.*
import com.google.firebase.database.IgnoreExtraProperties
import io.reactivex.Flowable

/**
 * Created by roman on 15.02.2018.
 */
const val ORDERS_TABLE = "orders"
const val ORDER_ID = "order_id"
const val ORDER_PHOTO = "order_photoname"
const val ORDER_URL = "order_photourl"
const val ORDER_LOCATION = "order_location"

@IgnoreExtraProperties
@Entity(tableName = ORDERS_TABLE)
data class Orders(
        //var id: Long,
        var ownerId: Long,
        var customerId: Long,
        @Ignore
        var goods: List<Goods>? = null,
        var isShipped: Boolean? = null,
        var timestampShipped: Long? = null,
        @ColumnInfo(name = ORDER_ID)
        override var id: Long,
        @ColumnInfo(name = ORDER_PHOTO)
        override var photoName: String?,
        @ColumnInfo(name = ORDER_URL)
        override var photoURL: String?,
        @ColumnInfo(name = ORDER_LOCATION)
        override var location: CustomLocation
        //var photoName: String? = null,
        //var photoURL: String? = null,
        //var location: CustomLocation
) : /*ClusterItem,*/ ItemUserOrder(id, photoName, photoURL, location) {

    var photoPlace: String? = null

    constructor() : this(0,0,0,null,null,null,null,null,CustomLocation())

    constructor(id: Long,
                ownerId: Long,
                customerId: Long,
                goods: List<Goods>? = null,
                isShipped: Boolean? = null,
                timestampShipped: Long? = null,
                photoName: String? = null,
                photoURL: String? = null,
                location: CustomLocation) : this(ownerId, customerId, goods, isShipped, timestampShipped, id, photoName, photoURL, location) {
        this.id = id
        this.photoName = photoName
        this.photoURL = photoURL
        this.location = location
    }
}

@Dao
interface OrdersDao {
    @Query("select * from $ORDERS_TABLE")
    fun getAllTasks(): Flowable<List<Orders>>

    @Query("SELECT COUNT(*) FROM $ORDERS_TABLE")
    fun hasData(): Flowable<Int>

    @Query("select * from $ORDERS_TABLE where $ORDER_ID = :arg0")
    fun findUserById(id: Long): Orders

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(user: Orders)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(user: Orders)

    @Delete
    fun deleteTask(user: Orders)
}