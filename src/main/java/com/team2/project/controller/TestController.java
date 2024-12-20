package com.team2.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping()
public class TestController {

	@RequestMapping(value="/join")
	public ModelAndView join() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("join");
		return mav;
	}
	
	@RequestMapping(value="/findID")
	public ModelAndView findID() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("findID");
		return mav;
	}
	@RequestMapping(value="/findPW")
	public ModelAndView findPW() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("findPW");
		return mav;
	}
	@RequestMapping(value="/phonechk")
	public ModelAndView phonenumchk() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("phonechk");
		return mav;
	}
	
}
