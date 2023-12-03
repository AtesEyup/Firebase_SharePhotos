package com.eypates.firebase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.eypates.firebase.databinding.RecyclerRowBinding
import com.eypates.firebase.model.DataModel

class DataAdapter(private var dataList: List<DataModel>, private var context: Context) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

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
        Glide
            .with(context)
            .load(item.url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.rowImgShareImage)

        holder.binding.line = item
    }
}