package com.gcp.dataobject;

import java.util.ArrayList;
import java.util.List;

public class ListRecvFormat<T> {

	int list_size = 0;
	int total = 0; 
	int page = 0;
	List<T> list = null;
	
	public ListRecvFormat()
	{
		list = new ArrayList<T>();
	}
	
	
	public int getList_size() {
		return list_size;
	}
	public void setList_size(int list_size) {
		this.list_size = list_size;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public void add(T t)
	{
		list.add(t);
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
}
