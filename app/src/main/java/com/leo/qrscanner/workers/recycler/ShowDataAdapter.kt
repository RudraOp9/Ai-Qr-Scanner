package com.leo.qrscanner.workers.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.leo.qrscanner.R
import com.leo.qrscanner.workers.recycler.ShowDataAdapter.myholder

class ShowDataAdapter(private val items: List<showDataModel>) : RecyclerView.Adapter<myholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myholder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_show_data, parent, false)
        return myholder(view)
    }

    override fun onBindViewHolder(holder: myholder, position: Int) {
        val currentItem: showDataModel = items[position]

        val styleData: TextView = holder.itemView.findViewById(R.id.appCompatTextView)
        styleData.text = currentItem.styleData
        holder.itemView.setOnClickListener {
            Snackbar.make(it, position, 3000).show()
        }
        /*
                val tvNumbersInText: TextView = holder.itemView.findViewById(R.id.tvNumbersInText)
                tvNumbersInText.text = currentItem.text2*/

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class myholder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}
