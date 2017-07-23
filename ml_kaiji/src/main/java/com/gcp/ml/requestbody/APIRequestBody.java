package com.gcp.ml.requestbody;

import java.util.List;

public class APIRequestBody {
	
	List<Integer> myDeck;
	List<Integer> emDeck;
	int count;
	int myWin;
	int emWin;
	public List<Integer> getMyDeck() {
		return myDeck;
	}
	public void setMyDeck(List<Integer> myDeck) {
		this.myDeck = myDeck;
	}
	public List<Integer> getEmDeck() {
		return emDeck;
	}
	public void setEmDeck(List<Integer> emDeck) {
		this.emDeck = emDeck;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getMyWin() {
		return myWin;
	}
	public void setMyWin(int myWin) {
		this.myWin = myWin;
	}
	public int getEmWin() {
		return emWin;
	}
	public void setEmWin(int emWin) {
		this.emWin = emWin;
	}
	
	

}
