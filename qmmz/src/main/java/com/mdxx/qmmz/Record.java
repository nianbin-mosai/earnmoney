package com.mdxx.qmmz;

public class Record {
	private String tpye;
	private String time;
	private String money;
	private String haoma;
	
	public Record(String tpye, String time, String money, String haoma) {
		this.tpye = tpye;
		this.time = time;
		this.money = money;
		this.haoma = haoma;
	}
	public String getTpye() {
		if(tpye.equals("1")){
			return "支付宝";
		}else if(tpye.equals("2")){
			return "手机号";
		}else if(tpye.equals("3")){
			return "Q币";
		}else{
			return "";
		}
	}
	public String getTime() {
		return time;
	}
	public String getMoney() {
		return money;
	}
	public String getHaoma() {
		return haoma;
	}
	
}
