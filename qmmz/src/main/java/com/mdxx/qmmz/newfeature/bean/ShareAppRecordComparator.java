package com.mdxx.qmmz.newfeature.bean;

import java.util.Comparator;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年11月23日 11:42
 * 邮箱：nianbin@mosainet.com
 */
public class ShareAppRecordComparator implements Comparator<ShareAppRecord> {
    @Override
    public int compare(ShareAppRecord t1, ShareAppRecord t2) {
        return (int) (t1.getShareTime()-t2.getShareTime());
    }
}
