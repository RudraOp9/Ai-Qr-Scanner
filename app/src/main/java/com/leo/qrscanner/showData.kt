package com.leo.qrscanner

import android.app.SearchManager
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import android.provider.ContactsContract
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.google.mlkit.vision.barcode.common.Barcode
import com.leo.qrscanner.workers.Helpers.makeSnack
import com.leo.qrscanner.workers.Helpers.makeToast
import com.leo.qrscanner.workers.recycler.ShowDataAdapter
import java.util.Calendar


class showData : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnPrimary: MaterialButton
    private lateinit var shareRaw: MaterialButton
    private lateinit var viewRaw: MaterialButton
    private lateinit var styledString: ArrayList<String>
    private lateinit var styledRawString: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_data)


        recyclerView = findViewById(R.id.recyclerView)
        btnPrimary = findViewById(R.id.btnPrimary)
        shareRaw = findViewById(R.id.shareRaw)
        viewRaw = findViewById(R.id.viewRaw)


        // ShareData sd = new ShareData();
        // String text = i.getStringExtra("text");
        // String btnText = i.getStringExtra("btnText");
        //  int btnType = i.getIntExtra("type",0);

        val type = intent.getIntExtra("type", 0)

        styledRawString = intent.getStringArrayListExtra("styledRaw") as ArrayList<String>
        styledString = intent.getStringArrayListExtra("styled") as ArrayList<String>

        btnPrimary.text = styledString[styledString.size - 1]
        styledString.removeAt(styledString.size - 1)

        recyclerView.adapter = ShowDataAdapter(styledString, styledRawString, this)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnPrimary.setOnClickListener {
            buttonAction(type, it)
            makeToast(this, "check")
        }

        shareRaw.setOnClickListener {
            val intent: Intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            var x: String = ""
            styledString.onEach { x += (it) }
            intent.putExtra(Intent.EXTRA_TEXT, x)
            startActivity(Intent.createChooser(intent, "share to"))

        }
        viewRaw.setOnClickListener {
            val alertDialog: AlertDialog
            alertDialog =
                MaterialAlertDialogBuilder(this).setView(R.layout.custom_show_raw).create()

            alertDialog.setCanceledOnTouchOutside(true)
            alertDialog.show()
            var showRaw: MaterialTextView? = alertDialog.findViewById(R.id.showRaw)
            if (showRaw != null) {
                showRaw.text = getIntent().getStringExtra("raw")
            }

        }


        //  textView.setText(barcode.getRawValue());
        //  button.setText(btnText);
        /* button.setOnClickListener(View.OnClickListener {
             //  buttonAction(btnType);
         })*/
    }


    private fun buttonAction(btnType: Int, view: View) {
        val i: Intent
        when (btnType) {
            Barcode.TYPE_EMAIL -> {
                i = Intent(Intent.ACTION_MAIN)
                i.type = Intent.CATEGORY_APP_EMAIL
                i.putExtra(Intent.EXTRA_EMAIL, styledString[2])
                i.putExtra(Intent.EXTRA_SUBJECT, styledString[3])
                i.putExtra(Intent.EXTRA_TEXT, styledString[4])
            }



            Barcode.TYPE_WIFI -> {
                // var wifiSugg =
                //  WifiNetworkSuggestion.Builder().setSsid(styledString[2])
                val ad = AlertDialog.Builder(this@showData)
                    .setTitle("Add Wifi")
                    .setMessage(
                        "Network : ${styledRawString[1]}\n" +
                                "Security:  ${styledRawString[3]} \n" +
                                "Password : ${styledRawString[2]}\n\n" +
                                "Please add this network manually in your WiFi settings."
                    )
                    .setCancelable(true)
                    .setPositiveButton("Add Wifi") { dialog: DialogInterface, _: Int ->
                        startActivity(Intent(android.provider.Settings.ACTION_WIFI_SETTINGS))

                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }

                ad.create().show()
            }

            Barcode.TYPE_URL -> {
                if (styledRawString[1].startsWith("http") || styledRawString[1].startsWith("https")) {
                    // Open as URL
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(styledRawString[1]))
                    startActivity(browserIntent)
                } else {
                    // Open as search
                    val searchIntent = Intent(Intent.ACTION_WEB_SEARCH)
                    searchIntent.putExtra(SearchManager.QUERY, styledRawString[1])
                    startActivity(searchIntent)
                }

            }

            Barcode.TYPE_CONTACT_INFO -> {
                val intent = Intent(ContactsContract.Intents.Insert.ACTION)
                intent.type = (ContactsContract.RawContacts.CONTENT_TYPE)
                intent.putExtra(ContactsContract.Intents.Insert.NAME, styledRawString[1])
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, styledRawString[2])
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, styledRawString[3])
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, styledRawString[4])
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, styledRawString[5])
                intent.putExtra(ContactsContract.Intents.Insert.DATA, styledRawString[7])
                startActivity(intent)

            }

            Barcode.TYPE_CALENDAR_EVENT -> {
                //val intent = Intent(Intent.ACTION_VIEW)
                val beginTime = Calendar.getInstance()
                val endTime = Calendar.getInstance()
                beginTime.timeInMillis = styledRawString[1].toLong()
                endTime.timeInMillis = styledRawString[2].toLong()

                // beginTime[2012, 0, 19, 7 30


                val intent: Intent = Intent(Intent.ACTION_INSERT)
                    .setData(Events.CONTENT_URI)
                    .putExtra(
                        CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis
                    )
                    .putExtra(
                        CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis
                    )
                    .putExtra(Events.TITLE, "")
                    .putExtra(Events.DESCRIPTION, "Group class")
                    .putExtra(Events.EVENT_LOCATION, styledRawString[3])
                    .putExtra(Events.STATUS, styledRawString[4])
                    .putExtra(Events.DESCRIPTION, styledRawString[5])
                    .putExtra(Events.ORGANIZER, styledRawString[6])


                //.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
                startActivity(intent)
// You can add additional extras like guests, reminders, etc.


            }

            Barcode.TYPE_GEO -> {
                val mapIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?q=" + styledRawString[1] + "," + styledRawString[2])
                )
                startActivity(mapIntent)
            }

            Barcode.TYPE_DRIVER_LICENSE -> {
                makeSnack(view, 3000, "coming soooon")
            }

            Barcode.TYPE_SMS -> {

                val i1 = Intent(Intent.ACTION_DIAL, "tel:${styledRawString[2]}".toUri())

                //  i.putExtra("tel:",styledRawString[2])
                startActivity(i1)

                /*   i = Intent(Intent.CATEGORY_APP_MAPS)
                   i.type = Intent.
                   i.putExtra(Intent.EXTRA_PHONE_NUMBER,styledRawString[2])
                   i.putExtra(Intent.EXTRA_TEXT,styledRawString[1])
                   Intent.createChooser(i , "open in")
                   startActivity(i)*/

            }

            Barcode.TYPE_PHONE -> {

                val i1 = Intent(Intent.ACTION_DIAL, "tel:${styledRawString[1]}".toUri())
                startActivity(i1)

                /* i = Intent(Intent.ACTION_MAIN)
                 i.type = Intent.EXTRA_PHONE_NUMBER
                 i.putExtra(Intent.EXTRA_PHONE_NUMBER,styledRawString[1])*/
            }

            Barcode.TYPE_ISBN -> {
                val searchIntent = Intent(Intent.ACTION_WEB_SEARCH)
                searchIntent.putExtra(SearchManager.QUERY, styledRawString[1])
                startActivity(searchIntent)
            }

            Barcode.TYPE_PRODUCT -> {
                val searchIntent = Intent(Intent.ACTION_WEB_SEARCH)
                searchIntent.putExtra(SearchManager.QUERY, styledRawString[1])
                startActivity(searchIntent)
            }

            Barcode.TYPE_TEXT -> {
                val searchIntent = Intent(Intent.ACTION_WEB_SEARCH)
                searchIntent.putExtra(SearchManager.QUERY, styledRawString[1])
                startActivity(searchIntent)
            }

            else -> {
                btnPrimary.text = "Send Email"
                TODO() // FIXME: before url
            }
        }
    }
}