package com.md.mainpage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md.mainpage.R;
import com.md.network.api.Group;

import java.util.List;

public class CategoryDetailAdapter extends RecyclerView.Adapter<CategoryDetailAdapter.ViewHolder> {
    private static final int SUB_COLUM=3;//子Recyclerview的列数
    private Context mContext;
    private List<Group> groups;
    private CategorySubAdapter subAdapter;

    public CategoryDetailAdapter(Context mContext, List<Group> groups){
        this.mContext = mContext;
        this.groups=groups;
    }

    public void setGroups(List<Group> groups){
        this.groups=groups;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_category_detail,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Group curGroup = groups.get(position);
        holder.title.setText(curGroup.getName());
        RecyclerView.LayoutManager manager = new GridLayoutManager(mContext,SUB_COLUM);
        holder.recyclerView.setLayoutManager(manager);
        subAdapter = new CategorySubAdapter(mContext,curGroup.getAlbums());
        holder.recyclerView.setAdapter(subAdapter);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView recyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            recyclerView = itemView.findViewById(R.id.subRec);

        }
    }



}
