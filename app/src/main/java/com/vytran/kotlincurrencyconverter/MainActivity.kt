package com.vytran.kotlincurrencyconverter

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


    fun get(view: View) {
        val downloadData = Download()

        try {
            val url = "http://data.fixer.io/api/latest?access_key=5d839479b42e9158d3ff0b18c85ce7eb"
            //val chosenBase = editText.text.toString()
            downloadData.execute(url)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

    }

    inner class Download: AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {
            var result = ""
            var url: URL
            val httpURLConnection : HttpURLConnection

            try {
                url = URL(p0[0])
                httpURLConnection = url.openConnection() as HttpURLConnection
                val inputStream = httpURLConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream)

                var data = inputStreamReader.read()

                while (data > 0) {
                    var character = data.toChar()
                    result += character

                    data = inputStreamReader.read()
                }

                return result
            }
            catch (e:Exception) {
                e.printStackTrace()
                return result
            }

            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {
                val jSONObject = JSONObject(result)
                println(jSONObject)
                val base = jSONObject.getString("base")
                println(base)
                val date = jSONObject.getString("date")
                println(date)
                val rates = jSONObject.getString("rates")
                println(rates)

                val newJsonObject = JSONObject(rates)
                /*val chf = newJsonObject.getString("CHF")
                println("CHF " + chf)
                val czk = newJsonObject.getString("CZK")
                println("CZK " + czk)
                val usd = newJsonObject.getString("USD")
                println("USD: " + usd)*/

                val getFromCurrency = fromText.text.toString().toUpperCase()
                val fromCurrency = newJsonObject.getString(getFromCurrency).toDouble()
                println(getFromCurrency + " " + fromCurrency.toString())

                val getToCurrency = toText.text.toString().toUpperCase()
                val toCurrency = newJsonObject.getString(getToCurrency).toDouble()
                println(getToCurrency + " " + toCurrency.toString())

                val amount = amountText.text.toString().toDouble()
                println("Amount " + amount.toString())

                val elapse = amount/fromCurrency
                val output = elapse*toCurrency
                println("Result "+ output.toString())

                resultView.text = amount.toString() + " " + getFromCurrency + " = " + "%.2f".format(output) + " " + getToCurrency

            }
            catch (e: Exception) {
                e.printStackTrace()
                resultView.text = "Invalid currency or Invalid amount"
            }

        }
    }

}
