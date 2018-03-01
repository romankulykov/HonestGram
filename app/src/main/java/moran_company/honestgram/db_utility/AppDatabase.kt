package moran_company.honestgram.db_utility

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import moran_company.honestgram.data.*
import android.arch.persistence.db.SupportSQLiteDatabase


/**
 * Created by roman on 09.02.2018.
 */
@Database(entities = arrayOf(Users::class, Orders::class, City::class, Goods::class, Chats::class), version = 3, exportSchema = false)
@TypeConverters(
        ItemUserOrder.LocationConverter::class,
        Goods.ArrayListUrlsConverter::class,
        DialogsConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UsersDao
    abstract val ordersDao: OrdersDao
    abstract val citiesDao: CitiesDao
    abstract val productsDao: GoodsDao
    abstract val chatsDao : ChatsDao

    companion object {

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL(
                        "CREATE TABLE $CHATS_TABLE (id INTEGER NOT NULL," +
                                " ownerId INTEGER NOT NULL," +
                                " productId INTEGER NOT NULL," +
                                " companionId INTEGER NOT NULL," +
                                " dialogs TEXT NOT NULL," +
                                " PRIMARY KEY(id))")
               /* // Copy the data
                database.execSQL(
                        "INSERT INTO users_new (userid, username, last_update) SELECT userid, username, last_update FROM users")
                // Remove the old table
                database.execSQL("DROP TABLE users")
                // Change the table name to the correct one
                database.execSQL("ALTER TABLE users_new RENAME TO users")*/
            }
        }
    }

}
