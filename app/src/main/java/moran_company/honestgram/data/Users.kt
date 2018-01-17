package moran_company.honestgram.data

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

/**
 * Created by roman on 12.01.2018.
 */
@IgnoreExtraProperties
class Users(var id: Long,
            var city: String,
            var district: String,
            var nickname: String,
            var login: String,
            var password:String,
            var photoName : String,
            var photoURL : String,
            var token : String?
            ):Serializable {




}