package com.leo.qrscanner

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.barcode.common.Barcode
import com.leo.qrscanner.workers.dataType

class showData : AppCompatActivity() {
    var textView: EditText? = null
    var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_data)
        textView = findViewById(R.id.textView3)
        button = findViewById(R.id.btnPrimary)
        // ShareData sd = new ShareData();
        val i = intent
        // String text = i.getStringExtra("text");
        // String btnText = i.getStringExtra("btnText");
        //  int btnType = i.getIntExtra("type",0);
        val b = i.getBundleExtra("key")


        //  textView.setText(barcode.getRawValue());
        //  button.setText(btnText);
        /* button.setOnClickListener(View.OnClickListener {
             //  buttonAction(btnType);
         })*/
    }

    private fun buttonAction(btnType: Int) {
        when (btnType) {
            dataType.TYPE_EMAIL -> {}
            dataType.TYPE_PHONE -> {}
            Barcode.TYPE_WIFI -> {}
            Barcode.TYPE_URL -> {}
            Barcode.TYPE_CONTACT_INFO -> {}
            dataType.TYPE_CALENDAR_EVENT -> {}
            dataType.TYPE_GEO -> {}
            dataType.TYPE_DRIVER_LICENSE -> {}
            dataType.TYPE_SMS -> {}
            dataType.TYPE_ISBN -> {}
            dataType.TYPE_PRODUCT -> {}
            dataType.TYPE_TEXT -> {}
            else -> {}
        }
    }
}