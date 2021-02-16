package com.md.mainpage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.md.mainpage.R
import com.md.mainpage.model.bean.FakeCategoryBean

class CategoryAdapter(var context: Context,var mData:List<FakeCategoryBean>):RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        var view= LayoutInflater.from(context).inflate(R.layout.adapter_category,parent,false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
       return mData.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var curBean = mData[position]
        holder.icon!!.setImageDrawable(context.resources.getDrawable(curBean.icon))
        holder.title!!.text=curBean.title
    }

    //应该使用DiffUtil的，以后再改
    fun setData(list :List<FakeCategoryBean> ){
        mData=list
        notifyDataSetChanged()
    }

    class CategoryViewHolder : RecyclerView.ViewHolder {
        var title:TextView?=null
        var icon:ImageView?=null
        constructor(itemView: View):super(itemView){
            title=itemView.findViewById(R.id.tvCategory)
            icon=itemView.findViewById(R.id.ivCategory)
        }
    }

}