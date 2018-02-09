package moran_company.honestgram.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

/**
 * Created by roman on 14.01.2018.
 */
@IgnoreExtraProperties
class Dialogs constructor(var dialog_id : Long,
              var message : String,
              var message_id : Long,
              var timestamp : Long,
              var user_id: Long,
              var url : String?) : Parcelable,Serializable {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readString()) {
    }

    constructor() : this(-1,"",0,0,0,"")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dialog_id)
        parcel.writeString(message)
        parcel.writeLong(message_id)
        parcel.writeLong(timestamp)
        parcel.writeLong(user_id)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dialogs> {
        override fun createFromParcel(parcel: Parcel): Dialogs {
            return Dialogs(parcel)
        }

        override fun newArray(size: Int): Array<Dialogs?> {
            return arrayOfNulls(size)
        }
    }

}