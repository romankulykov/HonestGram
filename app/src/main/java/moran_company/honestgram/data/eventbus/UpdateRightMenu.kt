package moran_company.honestgram.data.eventbus

/**
 * Created by roman on 14.02.2018.
 */
class UpdateRightMenu {
    internal var type: Int? = null

    constructor (type: Int?) {
        this.type = type
    }

    fun getType(): Int? {
        return type
    }

    fun setType(type: Int?) {
        this.type = type
    }
}