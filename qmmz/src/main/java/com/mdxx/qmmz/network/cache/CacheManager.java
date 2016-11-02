package com.mdxx.qmmz.network.cache;

import android.text.TextUtils;

/**
 * 缓存管理类
 * @author Rays 2015年12月24日
 *
 */
public class CacheManager {

	/**
	 * 获取缓存
	 * @param cacheKey
	 * @param lis
	 */
	public static void getCache(String cacheKey, OnGetCacheListener lis) {
		if (lis != null) {
			CacheEntity entity = DataCache.getInstance().query(cacheKey);
			if (entity != null && !TextUtils.isEmpty(entity.data)) {
                if (entity.isAvail) {
                    lis.onGetCacheSuccess(entity);
                } else {
                    lis.onGetCacheFail(true, entity);
                }
			} else {
				lis.onGetCacheFail(false, null);
			}

		}
	}
}
