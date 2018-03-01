package moran_company.honestgram.utility

import kotlinx.coroutines.experimental.Deferred
import moran_company.honestgram.data.SenderNotification
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by roman on 20.02.2018.
 */
interface ApiCoroutineTest {


    @GET("api/planets/1/")
    fun listReposWithCoroutines(): Deferred<Any>

    @GET("api/peopcle/24/")
    fun listPeopleWithCoroutines(): Deferred<Any>

    @POST(FirebaseContants.API_URL_FCM)
    fun sendNotification(@Header("Authorization") authorization: String, @Body json: SenderNotification): Deferred<Any>


}