package com.example.demo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.DaeDto;
import com.example.demo.dto.JungDto;
import com.example.demo.dto.SoDto;
import com.example.demo.service.MainService;

import jakarta.servlet.http.HttpServletRequest;
 
@Controller
public class MainController {
   
	@Autowired
	@Qualifier("ms")
	private MainService service;
	
	@RequestMapping("/")
	public String home()
	{
		return "redirect:/main/index";
	}
	
	@RequestMapping("/main/index")
	public String index()
	{
		return service.index();
	}
	
	@RequestMapping("/main/getDae")
	public @ResponseBody ArrayList<DaeDto> getDae()
	{
		return service.getDae();
	}
	
	@RequestMapping("/main/getJung")
	public @ResponseBody ArrayList<JungDto> getJung(HttpServletRequest request)
	{
		return service.getJung(request);
	}
	
	@RequestMapping("/main/getSo")
	public @ResponseBody ArrayList<SoDto> getSo(HttpServletRequest request)
	{
		return service.getSo(request);
	}
	
	@RequestMapping("/main/category")
	public String category()
	{
		return "/main/category";
	}
	
	@RequestMapping("/main/spinner")
	public String spinner()
	{
		return "/main/spinner";
	}
	 
}






