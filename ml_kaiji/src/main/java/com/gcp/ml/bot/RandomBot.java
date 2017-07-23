package com.gcp.ml.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class RandomBot implements Bot {

	@Override
	public int makeChoice(List<Integer> myDeck, List<Integer> emDeck, int myWin, int emWin, int count) {
		// TODO Auto-generated method stub
		
		Random rand = new Random();
		
		List<Integer> tempRand = new ArrayList<Integer>();
		
		for (int i = 0 ; i < myDeck.size() ; i++)
		{
			// 보유 카드가  0보다 많으면
			if(myDeck.get(i) >= 0)
				tempRand.add(i);
		}
		//카드가 없으면 걍 리턴해부리자!!!
		if(tempRand.size() == 0)
			return -1;
		
		return tempRand.get( rand.nextInt(tempRand.size()));
	}

}
