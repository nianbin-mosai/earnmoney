package com.mdxx.qmmz.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


/**
 * 用户本地缓存数据
 * 
 * @author Rays 2016年1月7日
 * 
 */
public class UserPF {
	private static final String appConfig = "appConfig";
	private static final String isFirstRunning = "isFirstRunning";
	private static final String phone = "phone";
	private static final String password = "password";
	private static final String logId = "logId";
    private static final UserPF instance = new UserPF();

    private SharedPreferences sharedPreferences;

    private UserPF() {
	}
	public void init(Context context){
		sharedPreferences = context.getSharedPreferences(appConfig,Context.MODE_PRIVATE);
	}
	public static UserPF getInstance() {
		return instance;
	}


	public void putString(String key, String value) {
		sharedPreferences.edit().putString(key, value).commit();
	}
	public String getString(String key, String defValue) {
		return sharedPreferences.getString(key, defValue);
	}
	
	public void putBoolean(String key, boolean value) {
		sharedPreferences.edit().putBoolean(key, value).commit();
	}
	public boolean getBoolean(String key, boolean defValue) {
		return sharedPreferences.getBoolean(key, defValue);
	}
	
	public void putFloat(String key, float value) {
		sharedPreferences.edit().putFloat(key, value).commit();
	}
	public float getFloat(String key, float defValue) {
		return sharedPreferences.getFloat(key, defValue);
	}
	
	public void putInt(String key, int value) {
		sharedPreferences.edit().putInt(key, value).commit();
	}

	public int getInt(String key, int defValue) {
		return sharedPreferences.getInt(key, defValue);
	}
	
	public void putLong(String key, long value) {
		sharedPreferences.edit().putLong(key, value).commit();
	}
	public long getLong(String key, long defValue) {
		return sharedPreferences.getLong(key, defValue);
	}
	
	public void setFirstRunning(boolean isFirstRunning){
		putBoolean(this.isFirstRunning,isFirstRunning);
	}
	public boolean isFirstRunning(){
		return getBoolean(isFirstRunning,false);
	}
	public void setPhone(String phone){
		putString(this.phone,phone);
	}
	public String getPhone(){
		return getString(this.phone,"");
	}
	public void setPassword(String password){
		putString(this.password,password);
	}
	public String getPassword(){
		return getString(this.password,"");
	}
	public void setLogId(String logId){
		putString(this.logId,logId);
	}
	public String getLogId(){
		return getString(this.logId,"");
	}

	public boolean isLogin(){
		return !TextUtils.isEmpty(getPhone()) && !TextUtils.isEmpty(getPassword());
	}
	public void logout(){
		setPhone("");
		setPassword("");
	}
}