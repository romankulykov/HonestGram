package moran_company.honestgram.activities.login

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import moran_company.honestgram.base_mvp.BasePresenterImpl
import moran_company.honestgram.data.Users
import moran_company.honestgram.utility.Utility
import java.util.*
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import java.io.File
import java.io.FileInputStream
import android.widget.Toast
import moran_company.honestgram.data.ImageUploadInfo
import moran_company.honestgram.data.PreferencesData
import kotlin.collections.ArrayList
import durdinapps.rxfirebase2.RxFirebaseDatabase




/**
 * Created by roman on 12.01.2018.
 */
open class LoginPresenter : BasePresenterImpl<LoginMvp.View>, LoginMvp.Presenter {

    private var mPostListener: ValueEventListener? = null
    private var mFirebaseInstance: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var mFirebaseDatabase: DatabaseReference? = null

    private var storage = FirebaseStorage.getInstance()
    // Create a storage reference from our app
    var storageRef = storage.reference


    private var needUser: Users? = null


    constructor(view: LoginMvp.View) : super(view) {


    }

    override fun signIn(login: String, password: String) {


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
                var userFound : Users ? = null
                for (user in users) {
                    if (user.password == password && user.login == login) {
                        userFound = user
                        needUser = user
                    }
                }
                mView.successLogin(userFound)


            }
        })

    }

    override fun registration(login: String, password: String) {

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