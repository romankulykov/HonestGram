package moran_company.honestgram.base_mvp


/**
 * Created by Kulykov Anton on 9/8/17.
 */

interface BaseMvp {

    interface View {

        fun onNetworkDisable()

        fun onUnauthorized()

        fun showToast(text: String)

        fun showToast(textId: Int)

        fun setProgressIndicator(active: Boolean)


    }

    interface Presenter {
        fun onDestroy()
    }

    interface InteractorFinishedListener {

        fun onError(text: String)

        fun onError(textId: Int)

        fun onError(throwable: Throwable)

        fun onNetworkDisable()

        fun onUnauthorized()

        fun onComplete()

        fun setProgressIndicator(active: Boolean)
    }
}

