package moran_company.honestgram.activities.login

import android.text.TextUtils
import com.google.firebase.iid.FirebaseInstanceId
import moran_company.honestgram.base_mvp.BasePresenterImpl
import moran_company.honestgram.data.Users
import durdinapps.rxfirebase2.DataSnapshotMapper
import moran_company.honestgram.data.PreferencesData
import durdinapps.rxfirebase2.RxFirebaseDatabase
import moran_company.honestgram.R


/**
 * Created by roman on 12.01.2018.
 */
open class LoginPresenter : BasePresenterImpl<LoginMvp.View>, LoginMvp.Presenter {

    constructor(view: LoginMvp.View) : super(view) {


    }

    override fun signIn(login: String, password: String) {
        RxFirebaseDatabase.observeSingleValueEvent(mUsersReference, DataSnapshotMapper.listOf(Users::class.java))
                .toFlowable()
                .flatMapIterable({ it -> it })
                .filter({ user -> user.login == login && user.password == password })
                .toList().toFlowable()
                .subscribe({ user ->
                    if (!user.isEmpty()) {
                        PreferencesData.saveUser(user.get(0))
                        mView.successLogin(user.get(0))
                        setToken(user.get(0).id, FirebaseInstanceId.getInstance().getToken())
                    } else {
                        mView.showToast(R.string.not_found)
                    }
                    //Do something with yourpost
                }, { t -> mView.showToast(t.toString()) })

    }

    override fun registration(login: String, password: String) {

    }

    private fun setToken(id: Long, newToken: String?) {
        if (TextUtils.isEmpty(newToken)) {

        } else
            apiClient.getKeyById(id, mUsersReference)
                    .subscribe({ key ->
                        mUsersReference.child(key).child("token").setValue(newToken)
                    })
    }

    override fun forgotPassword(/*path : String*/) {//
        /* var path = "/data/user/0/moran_company.honestgram/cache/cropped355380787.jpg"
         var filePathUri = Uri.fromFile(File(path))
         val name1 = storageRef.child(path).getName()
         storageRef.child(name1).putFile(filePathUri)
                 .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->

                     val imageUploadInfo = ImageUploadInfo(path, taskSnapshot?.getDownloadUrl().toString())

                     // Getting image upload ID.
                     val ImageUploadId = mFirebaseDatabase?.push()?.getKey()

                     // Adding image upload id s child element into databaseReference.
                     mFirebaseDatabase?.child(ImageUploadId)?.setValue(imageUploadInfo)
                     var id = needUser?.id
                     mUsersReference?.orderByChild("login")?.equalTo(needUser?.login)?.
                             addListenerForSingleValueEvent(object : ValueEventListener {
                         override fun onDataChange(dataSnapshot: DataSnapshot) {
                             for (appleSnapshot in dataSnapshot.children) {
                                 needUser?.photoName = name1
                                 needUser?.photoURL = taskSnapshot?.getDownloadUrl().toString()
                                 appleSnapshot.ref.setValue(needUser)
                             }
                             Log.d(TAG, "Success deleted in web")
                         }

                         override fun onCancelled(databaseError: DatabaseError) {
                             Log.e(TAG, "onCancelled", databaseError.toException())
                         }
                     })


                 }
                 .addOnFailureListener(OnFailureListener {
                     exception ->  exception
                 })*/
    }

}