package com.gcp.ml.bot;

import java.util.List;

public interface Bot {
	
	public int makeChoice(List<Integer>myDeck, List<Integer>emDeck, int myWin, int emWin, int count);

}
