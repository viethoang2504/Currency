package com.example.currency

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinner1: Spinner = findViewById(R.id.spinner)
        val spinner2: Spinner = findViewById(R.id.spinner2)
        val currencySymbol1: TextView = findViewById(R.id.textView3)
        val currencySymbol2: TextView = findViewById(R.id.textView4)
        val value1: TextInputEditText = findViewById(R.id.editText1)
        val value2: TextInputEditText = findViewById(R.id.editText2)
        value1.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        value2.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL

        val rule: TextView = findViewById(R.id.textView2)
        var currentFocus: Int = 1

        val items: Array<String> = arrayOf(
            "United States - Dollar", "United Kingdom - Pound", "Vietnam - Dong", "Japan - Yen",
            "Thailand - Baht"
        )

        val currencySymbols = mapOf(
            "United States - Dollar" to "$",
            "United Kingdom - Pound" to "£",
            "Vietnam - Dong" to "₫",
            "Japan - Yen" to "¥",
            "Thailand - Baht" to "฿"
        )

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            items
        )

        val exchangeRates = mapOf(
            "United States - Dollar" to 1.00,           // Base currency
            "United Kingdom - Pound" to 0.77,          // GBP to USD rate
            "Vietnam - Dong" to 25355.00,       // VND to USD rate
            "Japan - Yen" to 152.00,         // JPY to USD rate
            "Thailand - Baht" to 33.65           // THB to USD rate
        )

        spinner1.run {
            adapter = arrayAdapter
            setSelection(2)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    currencySymbol1.text = currencySymbols[items[p2]]
                    val ratio = (exchangeRates[spinner2.selectedItem.toString()]
                        ?: 1.0) / (exchangeRates[spinner1.selectedItem.toString()] ?: 1.0)
                    rule.text = buildString {
                        append("1 ")
                        append(currencySymbols[spinner1.selectedItem.toString()])
                        append(" = ")
                        append(String.format("%.2f",ratio))
                        append(" ")
                        append(currencySymbols[spinner2.selectedItem.toString()])
                    };
                    if (value1.isFocused) {
                        convertCurrency(
                            value1,
                            spinner1,
                            spinner2,
                            exchangeRates,
                            value2
                        )
                    } else {
                        convertCurrency(
                            value2,
                            spinner2,
                            spinner1,
                            exchangeRates,
                            value1
                        )
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }

        spinner2.run {
            adapter = arrayAdapter
            setSelection(0)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    currencySymbol2.text = currencySymbols[items[p2]]
                    val ratio = (exchangeRates[spinner2.selectedItem.toString()]
                        ?: 1.0) / (exchangeRates[spinner1.selectedItem.toString()] ?: 1.0)
                    rule.text = buildString {
                        append("1 ")
                        append(currencySymbols[spinner1.selectedItem.toString()])
                        append(" = ")
                        append(String.format("%.2f",ratio))
                        append(" ")
                        append(currencySymbols[spinner2.selectedItem.toString()])
                    };
                    if (value1.isFocused) {
                        convertCurrency(
                            value1,
                            spinner1,
                            spinner2,
                            exchangeRates,
                            value2
                        )
                    } else {
                        convertCurrency(
                            value2,
                            spinner2,
                            spinner1,
                            exchangeRates,
                            value1
                        )
                    }

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }

        value1.setOnClickListener {
            value1.setTypeface(null, Typeface.BOLD)
            value2.setTypeface(null, Typeface.NORMAL)
        }

        value2.setOnClickListener {
            value1.setTypeface(null, Typeface.NORMAL)
            value2.setTypeface(null, Typeface.BOLD)
        }

        value1.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                currentFocus = 1
                value1.setTypeface(null, Typeface.BOLD)
                value2.setTypeface(null, Typeface.NORMAL)
            }
        }

        value2.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                currentFocus = 2
                value2.setTypeface(null, Typeface.BOLD)
                value1.setTypeface(null, Typeface.NORMAL)
            }
        }


        value1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (currentFocus == 1) {
                    convertCurrency(
                        value1,
                        spinner1,
                        spinner2,
                        exchangeRates,
                        value2
                    )
                }

            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has been changed
            }
        })

        value2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (currentFocus == 2) {
                    convertCurrency(
                        value2,
                        spinner2,
                        spinner1,
                        exchangeRates,
                        value1
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has been changed
            }
        })
    }

    private fun convertCurrency(
        amountEditText: TextInputEditText,
        fromCurrencySpinner: Spinner,
        toCurrencySpinner: Spinner,
        exchangeRates: Map<String, Double>,
        resultEditText: TextInputEditText
    ) {
        val amountStr = amountEditText.text.toString().replace(",", ".")
        if (amountStr.isNotEmpty()) {
            val amount = amountStr.toDouble()
            val fromCurrency = fromCurrencySpinner.selectedItem.toString();
            val toCurrency = toCurrencySpinner.selectedItem.toString()
            val fromRate = exchangeRates[fromCurrency] ?: 1.0
            val toRate = exchangeRates[toCurrency] ?: 1.0
            val convertedAmount = (amount / fromRate) * toRate
            resultEditText.setText(String.format("%.2f", convertedAmount))
        }

    }
}