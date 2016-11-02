package com.mdxx.qmmz.common;

/**
 * 国际版工具类
 *
 * Created by linchaolong on 2016/9/8.
 */
public class GlobalUtils {

    /**
     * 手机号校验
     */
    public final static boolean isPhone(CharSequence phone) {
        if (phone == null) {
            return false;
        } else {
            if (phone.length() < 6 || phone.length() > 13) {
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(phone).matches();
            }
        }
    }

}
