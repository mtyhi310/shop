package com.example.demo.service;

import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface ProductService {
    public String productList(HttpServletRequest request,Model model);
    public String productContent(HttpServletRequest request,Model model,HttpSession session);
    public String jjimOk(HttpServletRequest request,HttpSession session);
    public String addCart(HttpServletRequest request,HttpSession session,HttpServletResponse response);
    
}
