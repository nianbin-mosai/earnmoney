package com.mdxx.qmmz.network.cache;

import java.io.Serializable;

/**
 * Created by Rays on 16/7/8.
 */
public class CacheEntity implements Serializable {
    public int statusCode;
    public String data;
    public String key;
    public boolean isAvail;
    public long updateTime;
    public long validity;
}
