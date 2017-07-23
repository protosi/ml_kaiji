package com.gcp.dataobject;

import java.util.List;

public class ListDataFormat <T> extends DataFormat {
	
	ListRecvFormat<T> recv= null;
	
	public ListDataFormat()
	{
		super();
		
		recv = new ListRecvFormat<T>();
		
	}
	
	public void add(T t)
	{
		recv.add(t);
	}
	public ListRecvFormat<T> getRecv()
	{
		return recv;
	}
	
	public void setPage(int page)
	{
		recv.setPage(page);
	}
	
	public void setTotal(int total)
	{
		recv.setTotal(total);
	}
	
	public void setList_size(int list_size)
	{
		recv.setList_size(list_size);
	}
	
	public void setList(List<T> list)
	{
		recv.setList(list);
	}
}
