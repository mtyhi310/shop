package com.example.demo.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.ProductDto;

@Mapper
public interface ProductMapper {
    public ArrayList<ProductDto> productList(String pcode,String str,int index);
    // 대,중,소분류의 name을 가져오는 메소드
    public String getDaeName(String code);
    public String getJungName(String code, String daecode);
    public String getSoName(String code, String daejung);
    public int getChong(String pcode);
    public ProductDto productContent(String pcode);
    public void jjimOk(String pcode,String userid);
    public void jjimDel(String pcode,String userid);
    public int isJjim(String pcode, String userid);
    public void addCart(String pcode,int su,String userid);
    public boolean isCart(String pcode , String userid);
    public void upCart(String pcode,String userid,int su);
}


