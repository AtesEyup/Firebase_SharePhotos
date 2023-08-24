package com.eypates.firebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eypates.firebase.R
import com.eypates.firebase.model.DataModel
import com.squareup.picasso.Picasso

class DataAdapter(private var dataList: MutableList<DataModel>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val lblEmail: TextView = itemView.findViewById(R.id.row_LblShareEmail)
        val lblDate: TextView = itemView.findViewById(R.id.row_LblShareDate)
        val lblComment: TextView = itemView.findViewById(R.id.row_LblShareComment)
        val imgPhoto: ImageView = itemView.findViewById(R.id.row_ImgShareImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        holder.lblEmail.text = data.email
        holder.lblDate.text = data.dateTime.toString()
        holder.lblComment.text = data.comment
        Picasso.get().load(data.url).resize(320, 360).into(holder.imgPhoto)
    }
}