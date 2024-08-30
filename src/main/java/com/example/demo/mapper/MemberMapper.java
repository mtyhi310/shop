package com.example.demo.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.MemberDto;
import com.example.demo.dto.ProductDto;

@Mapper
public interface MemberMapper {
    public String useridCheck(String userid);
    public void memberOk(MemberDto mdto);
    public HashMap getProduct(String pcode);
    public ArrayList<HashMap> getProductAll(String userid);
    public void cartDel(String pcode,String userid);
}
