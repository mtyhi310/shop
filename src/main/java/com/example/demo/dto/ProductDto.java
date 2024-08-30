package com.example.demo.dto;

import lombok.Data;

@Data
public class ProductDto {
   private int id,price,halin,su,baeprice,baeday,juk,pansu,review;
   private double star;
   private String pcode, pimg, dimg, title, writeday;
   
   // 할인후 상품금액, 적립금액, 배송일관련
   private int halinPrice, jukPrice;
   private String baeEx;
}
