package moran_company.honestgram.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

/**
 * Created by roman on 12.01.2018.
 */
@IgnoreExtraProperties
class Goods(var id : Long,var title : String,var price : Long,var url : String,var cityId : Long ) : Parcelable,Serializable {

    constructor() : this(0,"",0,"",0)

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeLong(price)
        parcel.writeString(url)
        parcel.writeLong(cityId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Goods> {
        override fun createFromParcel(parcel: Parcel): Goods {
            return Goods(parcel)
        }

        override fun newArray(size: Int): Array<Goods?> {
            return arrayOfNulls(size)
        }
    }


}