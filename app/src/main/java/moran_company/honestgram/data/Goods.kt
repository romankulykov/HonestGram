package moran_company.honestgram.data

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by roman on 12.01.2018.
 */
@IgnoreExtraProperties
class Goods {

    var nickname: String? = null

    constructor()

    constructor(nickname: String) : this()

}