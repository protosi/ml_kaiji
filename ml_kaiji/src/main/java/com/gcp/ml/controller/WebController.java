package com.gcp.ml.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web")
public class WebController {
	
	@Value("${server.context-path}")
	String context_path;
	
	@RequestMapping("index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView mav = new ModelAndView("index");
		mav.addObject("context_path", context_path);
		return mav;
	}

}
