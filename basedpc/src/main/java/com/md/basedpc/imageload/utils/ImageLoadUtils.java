package com.md.basedpc.imageload.utils;

import android.text.TextUtils;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.md.basedpc.application.AppManager;

/**
 * @author wanggq
 * @description:
 * @date :2020-01-06 13:45
 */
public class ImageLoadUtils {

    /**
     * @param img
     * @param url
     * @param defaultImage
     */
    public static void disPlayCircle(ImageView img, String url, int defaultImage) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .error(defaultImage)
                .placeholder(defaultImage);
        if (TextUtils.isEmpty(url)) url = "";
        RequestManager requestManager = Glide.with(AppManager.getAppManager().getAppContext());
        requestManager.load(url).apply(requestOptions).into(img);
    }


}