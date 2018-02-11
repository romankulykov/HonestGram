package moran_company.honestgram.data

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * Created by roman on 26.01.2018.
 */
class Chats (var id: Long, var ownerId: Long,
                        var companionId: Long, var dialogs: List<Dialogs>)
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

    constructor(id: Long):this(){
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