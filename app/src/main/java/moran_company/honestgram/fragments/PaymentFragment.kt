package moran_company.honestgram.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.cloudipsp.android.*
import moran_company.honestgram.R

/**
 * Created by roman on 11.01.2018.
 */
class PaymentFragment {
   /* private val MERCHANT_ID = 1396424

    private var editAmount: EditText? = null
    private var spinnerCcy: Spinner? = null
    private var editEmail: EditText? = null
    private var editDescription: EditText? = null
    private var cardInput: CardInputView? = null
    private var webView: CloudipspWebView? = null

    private var cloudipsp: Cloudipsp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        findViewById<View>(R.id.btn_amount).setOnClickListener(this)
        editAmount = findViewById<View>(R.id.edit_amount) as EditText
        spinnerCcy = findViewById<View>(R.id.spinner_ccy) as Spinner
        editEmail = findViewById<View>(R.id.edit_email) as EditText
        editDescription = findViewById<View>(R.id.edit_description) as EditText
        cardInput = findViewById<View>(R.id.card_input) as CardInputView
        cardInput?.isHelpedNeeded = BuildConfig.DEBUG
        cardInput?.setCompletionListener {
            Toast.makeText(this, "onCompleted", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.btn_pay).setOnClickListener(this)

        webView = findViewById<View>(R.id.web_view) as CloudipspWebView
        cloudipsp = Cloudipsp(MERCHANT_ID, webView)

        spinnerCcy?.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Currency.values())
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_amount -> fillTest()
            R.id.btn_pay -> processPay()
        }
    }

    private fun fillTest() {
        editAmount?.setText("1")
        editEmail?.setText("test@test.com")
        editDescription?.setText("test payment")
    }

    private fun processPay() {
        editAmount?.error = null
        editEmail?.error = null
        editDescription?.error = null

        val amount: Int
        try {
            amount = Integer.valueOf(editAmount?.text.toString())
        } catch (e: Exception) {
            editAmount?.error = getString(R.string.e_invalid_amount)
            return
        }

        val email = editEmail?.text.toString()
        val description = editDescription?.text.toString()
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail?.error = getString(R.string.e_invalid_email)
        } else if (TextUtils.isEmpty(description)) {
            editDescription?.error = getString(R.string.e_invalid_description)
        } else {
            val card = cardInput?.confirm(object : CardInputView.ConfirmationErrorHandler {
                override fun onCardInputErrorClear(view: CardInputView, editText: EditText) {}

                override fun onCardInputErrorCatched(view: CardInputView, editText: EditText, error: String) {}
            })

            if (card != null) {
                val currency = spinnerCcy!!.selectedItem as Currency
                val order = Order(amount, currency, "vb_" + System.currentTimeMillis(), description, email)
                order.setLang(Order.Lang.ru)

                cloudipsp?.pay(card, order, object : Cloudipsp.PayCallback {
                    override fun onPaidProcessed(receipt: Receipt) {
                        Toast.makeText(this@MainActivity, "Paid " + receipt.status.name + "\nPaymentId:" + receipt.paymentId, Toast.LENGTH_LONG).show()
                    }

                    override fun onPaidFailure(e: Cloudipsp.Exception) {
                        if (e is Cloudipsp.Exception.Failure) {

                            Toast.makeText(this@MainActivity, "Failure\nErrorCode: " +
                                    e.errorCode + "\nMessage: " + e.message + "\nRequestId: " + e.requestId, Toast.LENGTH_LONG).show()
                        } else if (e is Cloudipsp.Exception.NetworkSecurity) {
                            Toast.makeText(this@MainActivity, "Network security error: " + e.message, Toast.LENGTH_LONG).show()
                        } else if (e is Cloudipsp.Exception.ServerInternalError) {
                            Toast.makeText(this@MainActivity, "Internal server error: " + e.message, Toast.LENGTH_LONG).show()
                        } else if (e is Cloudipsp.Exception.NetworkAccess) {
                            Toast.makeText(this@MainActivity, "Network error", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@MainActivity, "Payment Failed", Toast.LENGTH_LONG).show()
                        }
                        e.printStackTrace()
                    }
                })
            }
        }
    }

    override fun onBackPressed() {
        if (webView!!.waitingForConfirm()) {
            webView!!.skipConfirm()
        } else {
            super.onBackPressed()
        }
    }*/
}