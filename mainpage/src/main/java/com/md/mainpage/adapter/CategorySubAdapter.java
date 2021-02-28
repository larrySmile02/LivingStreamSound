package com.md.mainpage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.md.mainpage.R;
import com.md.network.api.Album;

import java.util.List;

/**
 * @author liyue
 * created 2021/2/28
 * desc 分类详情页里展示专辑的小item
 * 遗留问题：Glide加载没有考虑网络出错的情况，后续有时间再优化
 */
public class CategorySubAdapter extends RecyclerView.Adapter<CategorySubAdapter.SubViewHolder> {
    private Context mContext;
    private List<Album> albumList;

    public CategorySubAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_category_sub, parent, false);
        return new SubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubViewHolder holder, int position) {
        Album curAlbum = albumList.get(position);
        holder.title.setText(curAlbum.getName());
        Glide.with(mContext)
                .load(curAlbum.getCover())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))//用这行设置圆角似乎高要写死，否则高度好像变成了match_parent
                .into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    class SubViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView title;

        public SubViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_cover);
            title = itemView.findViewById(R.id.iv_title);
        }
    }
}
