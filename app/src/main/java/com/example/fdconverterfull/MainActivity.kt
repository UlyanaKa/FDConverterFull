package com.example.fdconverterfull


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.net.URL
import android.widget.AdapterView
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    fun getRate(sum1: String, val1: String, val2: String) {
        val URL = "https://api.exchangeratesapi.io/latest?base=$val2&symbols=$val1"
        var okHttpClient: OkHttpClient = OkHttpClient()

        runOnUiThread {
            progressBar.visibility = View.VISIBLE
        }

        val request: Request = Request.Builder().url(URL).build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
            }

            override fun onResponse(call: Call?, response: Response?) {
                val json = response?.body()?.string()
                val txt = (JSONObject(json).getJSONObject("rates").get(val1)).toString()
                val rate: Double
                if (txt != "") {
                    rate = txt.toDouble()
                } else {
                    rate = 1.0
                }
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    var firstsum: EditText = findViewById(R.id.input)
                    var secondsum: TextView = findViewById(R.id.output)
                    if (sum1 != "") {
                        var sum1Double: Double
                        sum1Double = try {
                            sum1.toDouble()
                        } catch (e: NumberFormatException) {
                            1000.toDouble()
                        }
                        sum1Double = sum1Double / rate * 100
                        var sum1Int: Int = sum1Double.toInt()
                        var sum2Double = sum1Int.toDouble() / 100
                        secondsum.setText(sum2Double.toString())
                    } else {
                        secondsum.setText("")
                    }
                }
            }

        })

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getRate(s.toString(), val1.selectedItem.toString(), val2.selectedItem.toString())
            }
        })

        // Set an on item selected listener for spinner object
        val1.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                getRate(input.text.toString(), val1.selectedItem.toString(), val2.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
// Set an on item selected listener for spinner object
        val2.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                getRate(input.text.toString(), val1.selectedItem.toString(), val2.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
        // Create an ArrayAdapter
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.valutes, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        val1.adapter = adapter

        val adapter2 = ArrayAdapter.createFromResource(this,
            R.array.valutes2, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        val2.adapter = adapter2

            getRate("1000", val1.selectedItem.toString(), val2.selectedItem.toString())

    }
}