package com.mdxx.qmmz.network.cache;

/**
 * Created by Rays on 16/7/8.
 */
public interface OnGetCacheListener {

    /**
     * 当成功获取缓存数据
     * @param cacheEntity 缓存数据
     */
    void onGetCacheSuccess(CacheEntity cacheEntity);

    /**
     * 当数据过期或者无缓存数据
     * @param hasCache 是否有缓存
     */
    void onGetCacheFail(boolean hasCache, CacheEntity cacheEntity);

}
