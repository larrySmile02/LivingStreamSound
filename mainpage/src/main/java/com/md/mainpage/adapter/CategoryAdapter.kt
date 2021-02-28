package com.md.mainpage.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.md.mainpage.R
import com.md.mainpage.model.bean.FakeCategoryBean
import com.md.mainpage.ui.CategoryDetailActivity
import com.md.mainpage.ui.MainCategoryActivity
import com.md.network.api.Category
import com.md.network.api.ICategory
import com.md.network.api.LocalCategory

/**
 * @author liyue
 * created 2021/2/16
 * desc 首页面分类部分adapter
 */
class CategoryAdapter(var context: Context,var mData:List<ICategory>):RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        var view= LayoutInflater.from(context).inflate(R.layout.adapter_category,parent,false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
       return mData.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var curBean = mData[position] as LocalCategory
//        holder.icon?.let { Glide.with(context).load(curBean.logo).into(it) }
        holder.icon!!.setImageDrawable(context.resources.getDrawable(curBean.localRes))
        holder.title!!.text=curBean.name
        holder.root!!.setOnClickListener {
            if (position==mData.size-1)
                context.startActivity(Intent(context as AppCompatActivity,MainCategoryActivity::class.java))
            else
                CategoryDetailActivity.gotoCategoryDetail(context as AppCompatActivity,curBean.name,curBean.id)
        }
    }

    //应该使用DiffUtil的，以后再改
    fun setData(list: ArrayList<Category>){
        mData=list
        notifyDataSetChanged()
    }

    class CategoryViewHolder : RecyclerView.ViewHolder {
        var title:TextView?=null
        var icon:ImageView?=null
        var root:LinearLayout?=null
        constructor(itemView: View):super(itemView){
            title=itemView.findViewById(R.id.tvCategory)
            icon=itemView.findViewById(R.id.ivCategory)
            root=itemView.findViewById(R.id.rootCategoryMore)
        }
    }

}