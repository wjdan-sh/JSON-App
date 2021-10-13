package com.example.jsonapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.jsonapp.APIClient
import com.example.jsonapp.APIInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var details: EURO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userinp = findViewById<View>(R.id.userinput) as EditText
        val convrt = findViewById<View>(R.id.btn) as Button
        val spinner = findViewById<View>(R.id.spr) as Spinner

        val cur = arrayListOf("inr", "usd", "aud", "sar", "cny", "jpy")

        var selected: Int = 0

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, cur
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    selected = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        convrt.setOnClickListener {

            var sel = userinp.text.toString()
            var currency: Double = sel.toDouble()

            getCurrency(onResult = {
                details = it

                when (selected) {
                    0 -> disp(calc(details?.eur?.inr?.toDouble(), currency));
                    1 -> disp(calc(details?.eur?.usd?.toDouble(), currency));
                    2 -> disp(calc(details?.eur?.aud?.toDouble(), currency));
                    3 -> disp(calc(details?.eur?.sar?.toDouble(), currency));
                    4 -> disp(calc(details?.eur?.cny?.toDouble(), currency));
                    5 -> disp(calc(details?.eur?.jpy?.toDouble(), currency));
                }
            })
        }

    }

    private fun disp(calc: Double) {

        val responseText = findViewById<View>(R.id.textView3) as TextView

        responseText.text = " result " + calc
    }

    private fun calc(i: Double?, sel: Double): Double {
        var s = 0.0
        if (i !== null) {
            s = (i * sel)
        }
        return s
    }

    private fun getCurrency(onResult: (EURO?) -> Unit) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getCurrency()?.enqueue(object : Callback<EURO> {
                override fun onResponse(
                    call: Call<EURO>,
                    response: Response<EURO>
                ) {
                    onResult(response.body())

                }

                override fun onFailure(call: Call<EURO>, t: Throwable) {
                    onResult(null)
                    Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show();
                }

            })
        }
    }
}