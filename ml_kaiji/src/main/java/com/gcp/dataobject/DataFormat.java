package com.gcp.dataobject;

public class DataFormat {
	
	ResultFormat result = new ResultFormat();
	Object recv = null;
	
	public ResultFormat getResult() {
		return result;
	}
	
	public void setResult(int code, String msg)
	{
		result.setCode(code);
		result.setMsg(msg);
	}
	public void setResult(ResultFormat result) {
		this.result = result;
	}
	public Object getRecv() {
		return recv;
	}
	public void setRecv(Object recv) {
		this.recv = recv;
	}
	

}
