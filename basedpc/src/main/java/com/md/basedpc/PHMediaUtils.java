package com.md.basedpc;

import android.media.MediaMetadataRetriever;

/**
 * 媒体资源工具类
 * create by 朱大可 on 2021年01月28日10:29:28
 */
public class PHMediaUtils {

    /**
     * 根据媒体资源获取资源的时长（音视频）
     * @param mediaPath 资源本地路径
     * @return
     */
    public static int getLocalMediaPathDuration(String mediaPath) {
        int duration;
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mediaPath);
            duration = Integer.parseInt(mmr.extractMetadata
                    (MediaMetadataRetriever.METADATA_KEY_DURATION));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return duration;
    }
}
