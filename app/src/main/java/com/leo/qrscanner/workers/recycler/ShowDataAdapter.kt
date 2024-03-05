package com.leo.qrscanner.workers.recycler

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.leo.qrscanner.R
import com.leo.qrscanner.workers.recycler.ShowDataAdapter.myholder

class ShowDataAdapter(
    private val items: ArrayList<String>, val itemCopy: ArrayList<String>,
    private val context: Context
) : RecyclerView.Adapter<myholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myholder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_show_data, parent, false)
        return myholder(view)
    }

    override fun onBindViewHolder(holder: myholder, position: Int) {
        val currentItem: String = items[position]

        val styleData: TextView = holder.itemView.findViewById(R.id.appCompatTextView)
        val styleDataCopy: MaterialButton = holder.itemView.findViewById(R.id.materialButton1)

        styleData.text = currentItem
        styleDataCopy.setOnClickListener {
            Snackbar.make(it, "copied", 3000).show()
            context.copyToClipboard(itemCopy[position])
        }
        /*
                val tvNumbersInText: TextView = holder.itemView.findViewById(R.id.tvNumbersInText)
                tvNumbersInText.text = currentItem.text2*/

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class myholder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun Context.copyToClipboard(text: CharSequence) {
        val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
        clipboard?.setPrimaryClip(ClipData.newPlainText("", text))
    }
}


