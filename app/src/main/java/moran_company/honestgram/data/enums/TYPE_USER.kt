package moran_company.honestgram.data.enums

/**
 * Created by roman on 14.02.2018.
 */
enum class TYPE_USER(var id:Int){
    ADMIN(1),MODERATOR(2),SELLER(3),BUYER(4);

    companion object{
        fun getTypeUserById(id: Int) = when(id){
            1 -> ADMIN
            2 -> MODERATOR
            3 -> SELLER
            else -> BUYER
        }
    }
}