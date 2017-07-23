package com.gcp.dataobject;

/**
 * 
 * @author 3F8VJ32
 * api의 성공 실패 정보를 담는 class
 */
public class ResultFormat {
	
	int code = 0;
	String msg = "";
	
	public ResultFormat()
	{
	}
	
	public ResultFormat(int code, String msg)
	{
		setCode(code);
		setMsg(msg);
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
