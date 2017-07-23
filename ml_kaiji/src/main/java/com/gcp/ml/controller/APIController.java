package com.gcp.ml.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gcp.dataobject.DataFormat;
import com.gcp.ml.bot.MLBot;
import com.gcp.ml.requestbody.APIRequestBody;

@RestController
@RequestMapping("/api")
public class APIController {
	
	@Autowired
	MLBot bot;
	
	@RequestMapping("echoChoice")
	public Object echoChoice(@RequestBody APIRequestBody apiBody)
	{
		return apiBody;
	}
	
	@RequestMapping("/makeChoice")
	public Object makeChoice(@RequestBody APIRequestBody apiBody)
	{
		
		DataFormat dataFormat = new DataFormat();
		
		Map<String, Object> recv = new HashMap<String, Object>();
		
		
		int action = bot.makeChoice(apiBody.getMyDeck(), apiBody.getEmDeck(), apiBody.getMyWin(), apiBody.getEmWin(), apiBody.getCount());
		
		
		recv.put("action", action);
		
		dataFormat.setRecv(recv);
		if(action >= 0)
			dataFormat.setResult(100, "success");
		else
			dataFormat.setResult(-1, "fail");
		
		return dataFormat;
	}

}
