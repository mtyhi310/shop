package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.util.WebUtils;

import com.example.demo.dto.MemberDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.mapper.MemberMapper;
import com.example.demo.util.MyUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
@Qualifier("ms2")
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberMapper mapper;

	@Override
	public String member() 
	{		 
		return "/member/member";
	}

	@Override
	public String useridCheck(String userid)
	{
		return mapper.useridCheck(userid); 
	}

	@Override
	public String memberOk(MemberDto mdto) 
	{
		mapper.memberOk(mdto);
		return "../login/login";
	}

	@Override
	public String cartView(HttpSession session, HttpServletRequest request, Model model) 
	{
	 
		// 로그인의 유무에 따라 테이블, 쿠키변수
		ArrayList<HashMap> pMapAll=null;
		if(session.getAttribute("userid")==null)
		{
			 // 쿠키변수에서 읽은 후 상품코드를 분리하여 가져온다..
			//  상품코드1-수량/상품코드2-수량/상품코드3-수량/
			 Cookie cookie=WebUtils.getCookie(request, "pcode"); 
			 if(cookie!=null && !cookie.getValue().equals(""))
			 {
				 String[] pcodes=cookie.getValue().split("/");
				 
				 pMapAll=new ArrayList<HashMap>();
				 //for(int i=0;i<pcodes.length;i++) // 모든 요소를 다 실행해야 된다.
				 for(int i=pcodes.length-1;i>=0;i--)
				 {
					 String pcode=pcodes[i].substring(0,12);
					 int su=Integer.parseInt( pcodes[i].substring(13) );
					 
					 
					 HashMap map=mapper.getProduct(pcode);
									 
					 // ArrayList에 추가하기전에 장바구니의 수량을 전달
					 // 로그인 한 경우 csu변수에 저장=> 여기도 변경
					 // map.put("su", su);
					 map.put("csu", su);
					 map.put("days", "0");
					 pMapAll.add(map);
				 }
			 }
		}
		else
		{
			 // cart테이블에서 읽어서 가져온다
			String userid=session.getAttribute("userid").toString();
			pMapAll=mapper.getProductAll(userid);
			//System.out.println(pMapAll.get(0).get("csu"));
			// csu필드의 값을 변경 => plist.get(0).put("csu","100")
			//System.out.println( String.valueOf( pMapAll.get(0).get("csu") ));
			//System.out.println( pMapAll.get(0).get("csu"));
			int a=Integer.parseInt(String.valueOf(pMapAll.get(0).get("csu")));
			//System.out.println(a);
			
	     }
		
		// null일때 실행하면 오류가 발생
		if(pMapAll != null)
		{
			// 1. 모든 상품의 구매금액 , 모든 상품의 적립금 , 모든 상품의 배송비
			int halinpriceTot=0, jukpriceTot=0, baepriceTot=0;
			
			for(int i=0;i<pMapAll.size();i++)
			{
				HashMap map=pMapAll.get(i);
				// pMapAll에서 필요한 내용 변경하여 저장하기
				// 1. 배송예정
				LocalDate today=LocalDate.now();
				int baeday=Integer.parseInt(map.get("baeday").toString());
				LocalDate xday=today.plusDays(baeday);
				
				String yoil=MyUtil.getYoil(xday);
				String baeEx=null;
				if(baeday==1)
				{
					baeEx="내일("+yoil+") 도착예정";
				}
				else if(baeday==2)
				     {
					     baeEx="모레("+yoil+") 도착예정";
				     }
				     else
				     {
				    	 int m=xday.getMonthValue();
				    	 int d=xday.getDayOfMonth();
				    	 baeEx=m+"/"+d+"("+yoil+") 도착예정";
				     }
				 
				// ArrayList<HashMap>에 baeEx넣어주기
				pMapAll.get(i).put("baeEx", baeEx);
				
				// 2. 상품금액(할인율이 적용된 금액)
				int price=Integer.parseInt(map.get("price").toString());
				int halin=Integer.parseInt(map.get("halin").toString());
				int su=Integer.parseInt(map.get("csu").toString());
				int halinprice=(int)( price-(price*halin/100.0) )*su; // 하나의 상품구매 금액
			    pMapAll.get(i).put("halinprice", halinprice);
			    
			    
			    
				// 3. 적립금
				int juk=Integer.parseInt(map.get("juk").toString());
				int jukprice=(int)(price*juk/100.0)*su;               // 하나의 상품구매 적립금
				pMapAll.get(i).put("jukprice", jukprice);
				
				
				
				
				// 쿠키변수에서 온 list에서는 days변수가 없다 => 위에서 강제로 넣어준다..
				int days=Integer.parseInt( map.get("days").toString() );
				if(days<=1)
				{
					// 2-1. 모든 상품의 구매금액을 누적
				    halinpriceTot=halinpriceTot+halinprice;
				    // 3-1 모든 상품의 적립금액을 누적
					jukpriceTot=jukpriceTot+jukprice;
					// 4 모든 상품의 배송비를 누적
					int baeprice=Integer.parseInt(map.get("baeprice").toString());
					baepriceTot=baepriceTot+baeprice;
				}
				
			} // for의 끝
			// 뷰에 모든 상품의 구매금액,적립금,배송비를 전달
			model.addAttribute("halinpriceTot",halinpriceTot);
			model.addAttribute("jukpriceTot",jukpriceTot);
			model.addAttribute("baepriceTot",baepriceTot);
			
		} // 장바구니에 상품이 있다면의 if끝
		  
		model.addAttribute("pMapAll",pMapAll);
		return "/member/cartView";
	}

	 

	@Override
	public String cartDel(HttpServletRequest request, HttpSession session, HttpServletResponse response) 
	{
		String[] pcodes=request.getParameter("pcode").split("/");
		String userid=session.getAttribute("userid").toString();
		
		for(int i=0;i<pcodes.length;i++)
		{
			if(userid==null)
			{
				// 쿠키변수에 삭제할 pcode를 삭제
				Cookie cookie=WebUtils.getCookie(request, "pcode");
				if(cookie !=null)
					{
					String newPcode쿸
					}
					}
				
			else
			{
				// cart테이블에서 pcode에 해당하는 레코드를 삭제
				mapper.cartDel(pcodes[i],userid);
			}
		}
		
		return "redirect:/member/cartView";
	}
}








