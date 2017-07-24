package com.gcp.ml.bot;

import java.net.URL;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tensorflow.Graph;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;


@Component
public class MLBot implements Bot {
	SavedModelBundle bundle;
	
	@Value("${spring.profiles}")
	String profile;
	
	@Value("${ml.path}")
	String dir;
	
	public MLBot()
	{
		System.out.print(profile);
		System.out.print(dir);
		if(profile.equals("local"))
		{
			URL url = MLBot.class.getResource("/com/gcp/ml/bot/saved_model.pb");
	    	System.out.println(url.toString());
			bundle = SavedModelBundle.load(url.toString().replaceAll("file:/", "").replaceAll("saved_model.pb", ""), "serve");
		}
		else
		{
			bundle = SavedModelBundle.load(dir, "serve");
		}
	}
	

	@Override
	public int makeChoice(List<Integer> myDeck, List<Integer> emDeck, int myWin, int emWin, int count) {
		// TODO Auto-generated method stub
		
		// 1판은 무조건 래
		if(count ==0)
			return randomChoice(myDeck, emDeck, myWin, emWin, count);
		
		
		Graph graph = bundle.graph();
		float[][] data = {{0, 0, 0, 3, 3, 3, 3, 3, 3}};
		
		data[0][0] = count;
		data[0][1] = myWin;
		data[0][2] = emWin;
		
		for(int i = 0 ; i < myDeck.size() ; i++)
		{data[0][3+i] = myDeck.get(i);}
		
		for(int i = 0 ; i < emDeck.size() ; i++)
		{data[0][6+i] = emDeck.get(i);}
		
		Tensor t = Tensor.create(data);
        Session sess = bundle.session();
		
        List<Tensor> output =sess.runner().addTarget("main/output_y").feed("main/input_x", t).fetch("main/output_y").run();
        FloatBuffer fb = FloatBuffer.allocate(3) ;
        output.get(0).writeTo(fb);
		
        float max = 0;
        int maxId = -1;
        for(int i = 0 ; i < fb.limit() ; i++)
        {
        	System.out.println(fb.get(i));
        	if(fb.get(i) > max)
        	{
        		maxId = i;
        		max = fb.get(i);
        	}
        }
        
        if(maxId == -1)
        	return randomChoice(myDeck, emDeck, myWin, emWin, count);
        
        if(myDeck.get(maxId) <= 0)
        	return randomChoice(myDeck, emDeck, myWin, emWin, count);
        
        
		return maxId;
	}
	
	public int randomChoice(List<Integer> myDeck, List<Integer> emDeck, int myWin, int emWin, int count) 
	{
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
