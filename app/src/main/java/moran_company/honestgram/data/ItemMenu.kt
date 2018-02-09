package moran_company.honestgram.data


class ItemMenu(val idTitleString: Int, val idImageDrawable: Int, val menuType: MENU_TYPE) {

    enum class MENU_TYPE {
        LOGIN, PROFILE, MAIN, MAP, PRODUCTS, INFRASTRUCTURE,
        EVENTS, BUSINESS_MEETINGS, ABOUT, CITIES_AND_HOODS, CHATS, NONE,SHARE,
        ADD_PRODUCT, CHAT_DETAIL
    }


}
