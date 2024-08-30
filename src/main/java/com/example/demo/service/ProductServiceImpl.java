package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.util.WebUtils;

import com.example.demo.dto.ProductDto;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.util.MyUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
@Qualifier("ps")
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper mapper;

	@Override
	public String productList(HttpServletRequest request, Model model) 
	{
		String pcode=request.getParameter("pcode");
		
		// pcode를 이용하여  화면에 HOME-대분류-중분류-소분류 형태의 텍스트를 만들어서
		// 사용자에게 알려준다.
		
		
		/*
		  String pos="HOME - ";
		if(pcode.length()==3)  // p01
		{
			String code=pcode.substring(1);
			pos=pos+mapper.getDaeName(code); // HOME-대분류
		}
		else if(pcode.length()==5) // p0101
		     {
			     String daecode=pcode.substring(1,3);
			     pos=pos+mapper.getDaeName(daecode); // HOME-대분류
			     
			     String code=pcode.substring(3);
			     pos=pos+" - "+mapper.getJungName(code, daecode); // HOME-대분류-중분류
			     
		     }
		     else if(pcode.length()==7)  // p010102
		          {
		    	      String daecode=pcode.substring(1,3);
		    	      pos=pos+mapper.getDaeName(daecode);  // HOME-대분류
		    	      
		    	      String daejung=pcode.substring(1,5);
		    	      String jungcode=pcode.substring(3,5);
		    	      String code=pcode.substring(5); // 소분류코드
		    	      pos=pos+" - "+mapper.getJungName(jungcode, daecode); // HOME-대분류-중분류
		    	      
		    	      pos=pos+" - "+mapper.getSoName(code, daejung); // HOME-대분류-중분류-소분류
		          }
		*/
		String pos="HOME";
		String[] poses= {null,null,null}; // 대,중,소분류코드를 넣는 배열
		for(int i=0;i<poses.length;i++)
		{
			
			try
			{   // 대,중,소를 자른다 => 오류발생 => for문 종료
				poses[i]=pcode.substring(i*2+1,i*2+3);  //  p010101
			    if(i==0)  // p01
			    {
				    pos=pos+" ▷ "+mapper.getDaeName(poses[0]);
			    }
			    else if(i==1)
			         {
				         pos=pos+" ▷ "+mapper.getJungName(poses[1], poses[0]);
			         }
			         else if(i==2)
			              {
			    	          pos=pos+" ▷ "+mapper.getSoName(poses[2],poses[0]+poses[1]);
			              }
				
			}
			catch(Exception e)
			{
				break;
			}
		}
		
        model.addAttribute("pos",pos);			
		
        // 조회순서가 넘어올 때
        // 1 => order by pansu desc
        // 2 => order by price desc
        // 3 => order by price asc
        // 4 => order by star desc
        // 5 => order by writeday desc
        
        String order="1";
        if(request.getParameter("order")!=null)
          order=request.getParameter("order");
        
        String str=null;
        switch(order)
        {
           case "1": str=" pansu desc"; break;
           case "2": str=" price desc"; break;
           case "3": str=" price asc"; break;
           case "4": str=" star desc"; break;
           case "5": str=" writeday desc"; break;
        }
        
        // 사용자가 원하는 페이지의 인덱스값 구하기
        int page=1;
        if(request.getParameter("page")!=null)
        	page=Integer.parseInt(request.getParameter("page"));
        
        int index=(page-1)*20;
        
        
        
        
        ArrayList<ProductDto> plist=mapper.productList(pcode,str,index);
        
		//ArrayList<ProductDto> plist=mapper.productList(pcode);
		// index값으로 정렬되어 있다 => 배열처럼 정렬되어 있다.
		// 할인후 상품금액, 적립금액, 배송일관련 처리하여 dto에저장
		for(int i=0;i<plist.size();i++)
		{
			ProductDto pdto=plist.get(i);
			// 할인후 상품금액 => 상품금액-(상품금액*(할인율/100))
			int halinPrice=(int)(pdto.getPrice()-( pdto.getPrice()*( pdto.getHalin()/100.0) ));
			// 적립금액 => 상품금액*(적립률/100)
			int jukPrice=(int)(pdto.getPrice()*(pdto.getJuk()/100.0));
			
			// 배송예정일 =>  내일(요일) 배송예정  ,  모레(요일) 배송예정 ,  월/일(요일) 배송예정
			// 오늘기준으로 배송예정일의 날짜객체를 생성
			LocalDate today=LocalDate.now(); // 오늘날짜 객체생성
			// 오늘 기준 몇일 후의 날짜 객체
			LocalDate xday=today.plusDays(pdto.getBaeday());
			String yoil=MyUtil.getYoil(xday);
		
			String baeEx=null;
			if(pdto.getBaeday()==1)
			{
				baeEx="내일("+yoil+") 도착예정";  // 내일(화) 도착예정
			}
			else if(pdto.getBaeday()==2)
			     {
				     baeEx="모레("+yoil+") 도착예정"; // 모레(수) 도착예정
			     }
			     else
			     {
			    	 int m=xday.getMonthValue();
			    	 int d=xday.getDayOfMonth();
			    	 baeEx=m+"/"+d+"("+yoil+") 도착예정";
			     }
				
			
			// ArrayList내부에 있는 DTO에 새로운 값을 저장
			plist.get(i).setHalinPrice(halinPrice);
			plist.get(i).setJukPrice(jukPrice);
			plist.get(i).setBaeEx(baeEx);
		}
		
		// productList에 페이지를 링크하기 위해 필요한 값을 전달하기
        // pstart, pend, chong, page
		int pstart,pend,chong;
		
		pstart=page/10;
		if(page%10==0)
			pstart=pstart-1;
		
		pstart=pstart*10+1;
		
		pend=pstart+9;
		
		// 총페이지 상품별로 정해진다.
		chong=mapper.getChong(pcode);
		
		// pend는 총페이지보다 크면 안된다
		if(pend>chong)
			pend=chong;
		
		model.addAttribute("page",page);
		model.addAttribute("pstart",pstart);
		model.addAttribute("pend",pend);
		model.addAttribute("chong" ,chong);
		
		model.addAttribute("plist",plist);
		model.addAttribute("pcode",pcode);
		model.addAttribute("order",order);
		return "product/productList";
	}

	@Override
	public String productContent(HttpServletRequest request,
			Model model,
			HttpSession session) 
	{
		String pcode=request.getParameter("pcode");
		
		ProductDto pdto=mapper.productContent(pcode);

		// 할인후 상품금액 => 상품금액-(상품금액*(할인율/100))
		int halinPrice=(int)(pdto.getPrice()-( pdto.getPrice()*( pdto.getHalin()/100.0) ));
		// 적립금액 => 상품금액*(적립률/100)
		int jukPrice=(int)(pdto.getPrice()*(pdto.getJuk()/100.0));
		
		// 배송예정일 =>  내일(요일) 배송예정  ,  모레(요일) 배송예정 ,  월/일(요일) 배송예정
		// 오늘기준으로 배송예정일의 날짜객체를 생성
		LocalDate today=LocalDate.now(); // 오늘날짜 객체생성
		// 오늘 기준 몇일 후의 날짜 객체
		LocalDate xday=today.plusDays(pdto.getBaeday());
		String yoil=MyUtil.getYoil(xday);
	
		String baeEx=null;
		if(pdto.getBaeday()==1)
		{
			baeEx="내일("+yoil+") 도착예정";  // 내일(화) 도착예정
		}
		else if(pdto.getBaeday()==2)
		     {
			     baeEx="모레("+yoil+") 도착예정"; // 모레(수) 도착예정
		     }
		     else
		     {
		    	 int m=xday.getMonthValue();
		    	 int d=xday.getDayOfMonth();
		    	 baeEx=m+"/"+d+"("+yoil+") 도착예정";
		     }
			
		
		// ArrayList내부에 있는 DTO에 새로운 값을 저장
		pdto.setHalinPrice(halinPrice);
		pdto.setJukPrice(jukPrice);
		pdto.setBaeEx(baeEx);
		
		model.addAttribute("pdto",pdto);
		
		// 현재상품이 현재 접속한 사용자의 찜테이블에 있는지 확인
		String jImg;
		if(session.getAttribute("userid")!=null)
		{
			String userid=session.getAttribute("userid").toString();
			int ch=mapper.isJjim(pcode, userid); // ch값  0 or 1
			
			if(ch==0)
				 jImg="jjim1.png";
			else
				 jImg="jjim2.png";
	
		}
		else
			jImg="jjim1.png";
				model.addAttribute("jImg",jImg);
		
		
		return "/product/productContent";
	}

	@Override
	public String jjimOk(HttpServletRequest request, HttpSession session) 
	{
		String pcode=request.getParameter("pcode");
		String fname=request.getParameter("fname");
		if(session.getAttribute("userid")==null)
		{
			return "0"; 
		}
		else
		{
			String userid=session.getAttribute("userid").toString();
			if(fname.equals("jjim1.png"))
			   mapper.jjimOk(pcode,userid);
			else
			   mapper.jjimDel(pcode,userid);
			
			return "1";
		} 
	}

	@Override
	public String addCart(HttpServletRequest request
			, HttpSession session
			,HttpServletResponse response) 
	{
		String pcode=request.getParameter("pcode");
		int su=Integer.parseInt(request.getParameter("su"));
		
		try
		{
			if(session.getAttribute("userid")==null)
			{
				// 로그인을 하지 않아도 장바구니 처리를 실행
				// 이전의 쿠키변수 pcode를 읽어오기
				Cookie cookie=WebUtils.getCookie(request, "pcode");
				String newPro=pcode+"-"+su+"/";
				
				String newPcode=null;
				if(cookie==null)
				{
					newPcode=newPro;
				}
				else
				{
					String getPcode=cookie.getValue(); //"p01010103005-1/p01050103012-12/p01030209003-1/"
					String[] pcodes=getPcode.split("/");
					
					int chk=-1;
					for(int i=0;i<pcodes.length;i++)
					{
						if(pcodes[i].indexOf(pcode) != -1) // 존재한다
							chk=i;  // chk변수에는 존재하는 코드의 배열인덱스값을 가진다..
					}
					
					if(chk != -1)
					{
						// 있다면 => 수량만 변경
						int num=Integer.parseInt(pcodes[chk].substring(13));  // p01010103005-1
						num=num+su; // 기존의 수량 + 추가되는 수량
						pcodes[chk]=pcodes[chk].substring(0,13)+num; // "p01010103005-" + num
						
						String imsi="";
						for(int i=0;i<pcodes.length;i++)
						{
							imsi=imsi+pcodes[i]+"/";
						}
						newPcode=imsi;
					}
					else
					{
						// 없다면 => 새로 추가
						newPcode=getPcode+newPro;
					}									
				}
				
				System.out.println(newPcode);
				
				// newPcode를 새로운 쿠키객체로 생성
				Cookie newCookie=new Cookie("pcode",newPcode);
				newCookie.setMaxAge(3600);
				newCookie.setPath("/");
				response.addCookie(newCookie);
				
			}
			else
			{
				String userid=session.getAttribute("userid").toString();
				
				if(mapper.isCart(pcode,userid)) //(지금장바구니에 담은 상품이 cart테이블에 있냐)
				{
					// 있다면 => 수량만 update
					mapper.upCart(pcode,userid,su);
				}
				else
				{
					// 없다면 => insert
					mapper.addCart(pcode,su,userid);
				}
				
				
			}
			return "0";
		}
		catch(Exception e)
		{
			return "1";
		}
		
	}
}














