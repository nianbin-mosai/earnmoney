package com.mdxx.qmmz.newfeature.bean;

import org.litepal.crud.DataSupport;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年11月23日 11:37
 * 邮箱：nianbin@mosainet.com
 */
public class ShareAppRecord extends DataSupport{
    private long shareTime;
    private String phone;

    public long getShareTime() {
        return shareTime;
    }

    public void setShareTime(long shareTime) {
        this.shareTime = shareTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
