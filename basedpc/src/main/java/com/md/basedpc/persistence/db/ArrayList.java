package com.md.basedpc.persistence.db;
/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/6/16 17:46.
 */

public class ArrayList extends java.util.ArrayList<NameValuePairDB> {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean add(NameValuePairDB nameValuePair) {
        if (!StringUtils.isEmpty(nameValuePair.getValue())) {
            return super.add(nameValuePair);
        } else {
            return false;
        }
    }

    /**
     * 添加数据
     * @param key
     * @param value
     * @return
     */
    public boolean add(String key, String value) {
        return add(new NameValuePairDB(key, value));
    }

}