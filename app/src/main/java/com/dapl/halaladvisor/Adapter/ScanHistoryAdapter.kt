package com.dapl.halaladvisor.Adapter

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dapl.halaladvisor.Model.ScanHistoryItem
import com.dapl.halaladvisor.R
import com.dapl.halaladvisor.productscan.ResultActivity

class ScanHistoryAdapter(private var items: List<ScanHistoryItem>) :
    RecyclerView.Adapter<ScanHistoryAdapter.ViewHolder>() {

    /** Replaces the visible rows (used by the local search filter). */
    fun updateItems(newItems: List<ScanHistoryItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgStatus: ImageView = view.findViewById(R.id.imgStatus)
        val txtProductName: TextView = view.findViewById(R.id.txtProductName)
        val txtBrandName: TextView = view.findViewById(R.id.txtBrandName)
        val txtIngredientsInfo: TextView = view.findViewById(R.id.txtIngredientsInfo)
        val txtStatusBadge: TextView = view.findViewById(R.id.txtStatusBadge)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val txtTimeSub: TextView = view.findViewById(R.id.txtTimeSub)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scan_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Open the full result for this scan when the row is tapped.
        holder.itemView.setOnClickListener { view ->
            val barcode = item.barcode
            if (barcode.isBlank()) {
                Toast.makeText(view.context, "Barcode not available for this scan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(view.context, ResultActivity::class.java)
            intent.putExtra("barcode", barcode)
            view.context.startActivity(intent)
        }

        holder.txtProductName.text = item.productName
        holder.txtBrandName.text = item.brandName
        holder.txtIngredientsInfo.text = item.ingredientsSummary
        holder.txtStatusBadge.text = item.status
        holder.txtTime.text = item.dateLabel
        holder.txtTimeSub.text = item.timeLabel

        when (item.status.uppercase()) {
            "HALAL" -> {
                holder.imgStatus.setImageResource(R.drawable.ic_new_shield)
                holder.imgStatus.setBackgroundResource(R.drawable.bg_status_icon_halal)
                holder.imgStatus.imageTintList = ColorStateList.valueOf(Color.parseColor("#1F7A37"))
                holder.txtStatusBadge.setBackgroundResource(R.drawable.bg_pill_halal)
                holder.txtStatusBadge.setTextColor(Color.parseColor("#1F7A37"))
                holder.txtIngredientsInfo.setTextColor(Color.parseColor("#2EA84F"))
            }
            "HARAM" -> {
                holder.imgStatus.setImageResource(R.drawable.ic_block)
                holder.imgStatus.setBackgroundResource(R.drawable.bg_status_icon_haram)
                holder.imgStatus.imageTintList = ColorStateList.valueOf(Color.parseColor("#E53935"))
                holder.txtStatusBadge.setBackgroundResource(R.drawable.bg_pill_haram)
                holder.txtStatusBadge.setTextColor(Color.parseColor("#C62828"))
                holder.txtIngredientsInfo.setTextColor(Color.parseColor("#D32F2F"))
            }
            else -> { // MUSHBOOH
                holder.imgStatus.setImageResource(R.drawable.ic_doubtful)
                holder.imgStatus.setBackgroundResource(R.drawable.bg_status_icon_mushbooh)
                holder.imgStatus.imageTintList = ColorStateList.valueOf(Color.parseColor("#B58A00"))
                holder.txtStatusBadge.setBackgroundResource(R.drawable.bg_pill_mushbooh)
                holder.txtStatusBadge.setTextColor(Color.parseColor("#7A5E00"))
                holder.txtIngredientsInfo.setTextColor(Color.parseColor("#A07A00"))
            }
        }
    }

    override fun getItemCount() = items.size
}