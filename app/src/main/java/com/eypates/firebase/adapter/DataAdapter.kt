package com.eypates.firebase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eypates.firebase.databinding.RecyclerRowBinding
import com.eypates.firebase.model.DataModel
import com.squareup.picasso.Picasso

class DataAdapter(private var dataList: List<DataModel>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerRowBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        Picasso.get().load(item.url).resize(400,500).into(holder.binding.rowImgShareImage)

        holder.binding.line = item
    }
}