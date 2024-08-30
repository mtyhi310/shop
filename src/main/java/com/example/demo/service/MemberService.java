package com.example.demo.service;

import org.springframework.ui.Model;

import com.example.demo.dto.MemberDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface MemberService {
    public String member();
    public String useridCheck(String userid);
    public String memberOk(MemberDto mdto);
    public String cartView(HttpSession session, HttpServletRequest request,Model model);
    public String cartDel(HttpServletRequest request,HttpSession session,HttpServletResponse response);
}
