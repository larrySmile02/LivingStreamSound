package com.md.mainpage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.md.mainpage.R
import com.md.network.api.Album

class MainDailySupplyAdapter (var context: Context, var mData:List<Album>): RecyclerView.Adapter<MainDailySupplyAdapter.DailySupplyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailySupplyViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.adapter_main_daily,parent,false)
        return DailySupplyViewHolder(view)
    }

    override fun getItemCount(): Int {
       return mData.size
    }

    override fun onBindViewHolder(holder: DailySupplyViewHolder, position: Int) {
        var curBean = mData[position]
        holder.icon?.let { Glide.with(context).load(curBean.cover).into(it) }
        holder.title!!.text=curBean.name

    }

    //应该使用DiffUtil的，以后再加
    fun setData(list:List<Album>){
        mData = list
        notifyDataSetChanged()
    }

    class DailySupplyViewHolder : RecyclerView.ViewHolder {
        var title: TextView?=null
        var icon: ImageView?=null
        constructor(itemView: View):super(itemView){
            title = itemView.findViewById(R.id.tvDailyMain)
            icon = itemView.findViewById(R.id.ivDailyMain)
        }
    }


}