package moran_company.honestgram.data


class ItemMenu(val idTitleString: Int, val idImageDrawable: Int, val menuType: MENU_TYPE) {

    var title : String ? = null
    var productId : Long? = null

    constructor(title : String,productId : Long,menuType: MENU_TYPE) : this(0,0,menuType){
        this.title = title
        this.productId = productId
    }

    enum class MENU_TYPE {
        LOGIN, PROFILE, MAIN, MAP, PRODUCTS, INFRASTRUCTURE,
        EVENTS, BUSINESS_MEETINGS, ABOUT, CITIES_AND_HOODS, CHATS, NONE,SHARE,
        TRACKING,
        ADD_PRODUCT, CHAT_DETAIL,FILTER_BY_CITY,FILTER_BY_PRICE,LIST_OF_GOODS,
        SHIPPED_ORDERS
    }


}
