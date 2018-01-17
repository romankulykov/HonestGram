package moran_company.honestgram.fragments.main

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import moran_company.honestgram.base_mvp.BasePresenterImpl
import moran_company.honestgram.data.PreferencesData
import moran_company.honestgram.data.Users
import moran_company.honestgram.utility.Utility
import java.util.HashMap
import java.util.LinkedHashMap

/**
 * Created by roman on 12.01.2018.
 */
class MainPresenter : BasePresenterImpl<MainMvp.View>,MainMvp.Presenter{
    override fun initUsers() {
        mUsersReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var users: MutableList<Users> = ArrayList()
                for (postSnapshot in dataSnapshot.children) {
                    //postSnapshot.ref.setValue(words)
                    var messageMap: Map<String, Users> = LinkedHashMap<String, Users>()
                    messageMap = (postSnapshot.value as HashMap<String, Users>?)!!
                    users.add(Utility.toUser(messageMap))
                }
                PreferencesData.saveUsers(users as ArrayList<Users>)
            }
        })
    }

    constructor(view : MainMvp.View) : super(view)



}