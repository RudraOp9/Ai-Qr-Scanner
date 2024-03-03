package com.leo.qrscanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.mlkit.vision.barcode.common.Barcode
import com.leo.qrscanner.workers.Helpers.makeToast
import com.leo.qrscanner.workers.recycler.ShowDataAdapter

class showData : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnPrimary: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_data)


        recyclerView = findViewById(R.id.recyclerView)
        btnPrimary = findViewById(R.id.btnPrimary)


        // ShareData sd = new ShareData();
        // String text = i.getStringExtra("text");
        // String btnText = i.getStringExtra("btnText");
        //  int btnType = i.getIntExtra("type",0);

        val type = intent.getIntExtra("type", 0)

        val styledRawString = intent.getStringArrayListExtra("styledRaw") as ArrayList<String>
        val styledString = intent.getStringArrayListExtra("styled") as ArrayList<String>

        btnPrimary.text = styledString[styledString.size - 1]
        styledString.removeAt(styledString.size - 1)

        recyclerView.adapter = ShowDataAdapter(styledString, styledRawString, this)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnPrimary.setOnClickListener {
            buttonAction(type)
            makeToast(this, "check")
        }


        //  textView.setText(barcode.getRawValue());
        //  button.setText(btnText);
        /* button.setOnClickListener(View.OnClickListener {
             //  buttonAction(btnType);
         })*/
    }

    private fun buttonAction(btnType: Int) {
        when (btnType) {
            Barcode.TYPE_EMAIL -> {
                btnPrimary.text = "Send Email"
            }

            Barcode.TYPE_PHONE -> {
                btnPrimary.text = "Send Email"
            }

            Barcode.TYPE_WIFI -> {
                btnPrimary.text = "Send Email"
            }

            Barcode.TYPE_URL -> {
                btnPrimary.text = "Send Email"
            }

            Barcode.TYPE_CONTACT_INFO -> {
                btnPrimary.text = "Send Email"
            }

            Barcode.TYPE_CALENDAR_EVENT -> {
                btnPrimary.text = "Send Email"
            }

            Barcode.TYPE_GEO -> {
                btnPrimary.text = "Send Email"
            }

            Barcode.TYPE_DRIVER_LICENSE -> {
                btnPrimary.text = "Send Email"
            }

            Barcode.TYPE_SMS -> {
                btnPrimary.text = "Send Email"
            }

            Barcode.TYPE_ISBN -> {
                btnPrimary.text = "Send Email"
            }

            Barcode.TYPE_PRODUCT -> {
                btnPrimary.text = "Send Email"
            }

            Barcode.TYPE_TEXT -> {
                btnPrimary.text = "Send Email"
            }

            else -> {
                btnPrimary.text = "Send Email"
            }
        }
    }
}