package moran_company.honestgram.activities.registration

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import moran_company.honestgram.activities.login.LoginMvp
import moran_company.honestgram.base_mvp.BasePresenterImpl
import moran_company.honestgram.data.Users
import moran_company.honestgram.utility.Utility
import java.util.*
import moran_company.honestgram.R

/**
 * Created by roman on 13.01.2018.
 */
class RegistrationPresenter : BasePresenterImpl<RegistrationMvp.View>, RegistrationMvp.Presenter {

    private var lastId: Long? = 0

    constructor(view: RegistrationMvp.View) : super(view) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                // Get Post object and use the values to update the UI
                var usersIds: MutableList<Long> = ArrayList()
                if (dataSnapshot != null && dataSnapshot.value != null) {

                    for (postSnapshot in dataSnapshot.children) {
                        var messageMap: Map<String, Users> = LinkedHashMap<String, Users>()
                        messageMap = (postSnapshot.value as HashMap<String, Users>?)!!
                        usersIds.add(Utility.toUser(messageMap).id)
                    }
                    if (!usersIds.isEmpty())lastId = Collections.max(usersIds)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        }
        mUsersReference?.addValueEventListener(postListener)
    }


    override fun registration(nickname: String, login: String, password: String) {
        //mUsersReference.orderByChild("id").equalTo(1).
        mUsersReference.orderByChild("login")?.equalTo(login)?.
                addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            mView.showToast(R.string.already_exist)
                        } else {
                            var token : String? = FirebaseInstanceId.getInstance().getToken()
                            var user = Users(lastId!!.inc(), "Kyiv", "Pecherskiy", nickname, login, password, "", "",token)
                            mUsersReference?.push()?.setValue(user)
                            mView.successRegistration(user)
                        }

                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
    }
}