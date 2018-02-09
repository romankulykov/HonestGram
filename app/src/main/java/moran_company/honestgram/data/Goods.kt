package moran_company.honestgram.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

/**
 * Created by roman on 12.01.2018.
 */
@IgnoreExtraProperties
class Goods(var id : Long,var title : String,var price : Long,
            var url : String,var cityId : Long,var description : String,
            var ownerId : Long,var specialPrice : Long ) : Parcelable,Serializable {


    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readLong()) {
    }

    var urls : ArrayList<Urls>? = null

    constructor(urls : ArrayList<Urls>) : this(0,"",0,"",0,"",0,0){
        this.urls = urls
    }

    constructor() : this(0,"",0,"",0,"",0,0)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeLong(price)
        parcel.writeString(url)
        parcel.writeLong(cityId)
        parcel.writeString(description)
        parcel.writeLong(ownerId)
        parcel.writeLong(specialPrice)
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