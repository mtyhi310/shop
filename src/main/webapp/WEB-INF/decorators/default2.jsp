<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 <style>
   @font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
   }
   body {
      margin:0px;
   }
   #ads {
      width:100%;
      height:40px;
      background:purple;
   }
   #ads #first {
      width:1100px;
      height:40px;
      margin:auto;
      font-family:'GmarketSansMedium';
   }
   #ads #first #left {
      width:1000px;
      height:40px;
      line-height:40px;
      float:left;
      text-align:center;
      color:white;
   }
   #ads #first #right {
      width:100px;
      height:40px;
      line-height:40px;
      float:left;
      text-align:right;
      color:white;
   }
   header {
      width:1100px;
      height:70px;
      line-height:70px;
      margin:auto;
      font-family:'GmarketSansMedium';
   }
   header a {
     text-decoration:none;
     color:black;
   }
   header #logo {
      width:300px;
      height:70px;
      line-height:70px;
      float:left;
   }
   header #search {
      width:500px;
      height:70px;
      float:left;
   }
   header #search #searchForm {  /* 둘러싸고 있는 div태그 */
      width:300px;
      height:40px;
      line-height:40px;
      border:1px solid purple; 
      margin-top:15px;
      border-radius:10px;
   }
   header #search #searchForm #searchTxt { /* type="text" */
      width:220px;
      margin-left:14px;
      border:none;
      outline:none;
   }
   header #search #searchForm #xx {  /* x표시 그림 */
      visibility:hidden;
      cursor:pointer;
   }
   header #member {
      width:300px;
      height:70px;
      line-height:70px;
      float:left;
      text-align:right;
   }
   
   nav {
      width:1100px;
      height:50px;
      line-height:50px;
      margin:auto;
      font-family:'GmarketSansMedium';
   }
   nav #cate {  /* 카테고리메뉴 */
      position:relative;
      display:block;
      width:90px;
      height:58px;
   }
   nav #daeMenu {  /* ul */
      position:absolute;
      left:-6px;
      top:50px;
      padding-left:0px;
      width:100px;
      height:210px;
      background:white;
      display:none;
   }
   nav #daeMenu > li {
      list-style-type:none;
      width:100px;
      height:30px;
      line-height:30px;
      text-align:center;
      background:white;
      cursor:pointer;
      position:relative;
   }
   nav #daeMenu > li .jungMenu {
      position:absolute;
      left:100px;
      top:0px;
      padding-left:0px;
      background:white;
      }
   nav #daeMenu > li .jungMenu > li {
      list-style-type:none;
      width:100px;
      height:30px;
   }
   nav > #mainMenu {  
      padding-left:0px;
   }
   nav > #mainMenu > li {
      list-style-type:none;
      display:inline-block;
      width:170px;
      height:50px;
      line-height:50px;
      text-align:center;
    
   }
    
   nav > #mainMenu > li:first-child {
      width:220px;
      text-align:left;
   }
    
   footer {
      width:1100px;
      height:150px;
      margin:auto;
    }
 </style>
 <script>
   function xCheck(val)
   {
	  if(val.length==0)
      {
		  document.getElementById("xx").style.visibility="hidden";
      }		
	  else
	  {
		  document.getElementById("xx").style.visibility="visible";
	  }	  
   }
   
   function clearTxt()
   {
	   document.getElementById("searchTxt").value="";
	   xCheck("");
   }
   
   // dae테이블에서 대분류메뉴읽어오기
   mchk=0;
   function viewMenu()
   {
	     document.getElementById("daeMenu").style.display="block";
	       
	     if(mchk==0)
	     {  
		   var chk=new XMLHttpRequest();
		   chk.onload=function()
		   {
			   //alert(chk.responseText);
			   var daeAll=JSON.parse(chk.responseText);
			   var str="";
			   for(dae of daeAll)
			   {
				   str=str+"<li onmouseover='jungView("+dae.code+")' onmouseout='jungHide("+dae.code+")'> "+dae.name+" <ul class='jungMenu'></ul></li>";
			   }	  
			   document.getElementById("daeMenu").innerHTML=str;
 
		   }
		   chk.open("get","../main/getDae");
		   chk.send();
		   mchk++;
	     }
   }
   function hideMenu()
   {
 	   document.getElementById("daeMenu").style.display="none";
   }
  
   function jungView(daecode)
   {
	   document.getElementsByClassName("jungMenu")[daecode-1].style.display="block";
	   var chk=new XMLHttpRequest();
	   var str="";
	   chk.onload=function()
	   {
		  //alert(chk.responseText);
		  var jungAll=JSON.parse(chk.responseText);
		  for(jung of jungAll)
		  {
			  str=str+"<li onmouseover='soView("+(daecode+jung.code)+")' onmouseout='soHide()'> "+jung.name+" </li>";
		  }
		  document.getElementsByClassName("jungMenu")[daecode-1].innerHTML=str;
	   }
	   chk.open("get","../main/getJung?daecode="+daecode);
	   chk.send();
  
   }
   function jungHide(daecode)
   {
	   document.getElementsByClassName("jungMenu")[daecode-1].style.display="none";
   }
   
   function soView()
   {
	   
   }
   function soHide()
   {
	   
   }
   function viewSrc()
   {
	   document.getElementById("src").innerText=document.getElementsByTagName("html")[0].innerHTML;
   }
 </script>
 
 <sitemesh:write property="head"/>
 
</head>
<body> <!-- main/index.jsp -->
 <input type="button" value="소스보기" onclick="viewSrc()">
 <div id="src"></div>
   <div id="ads">
     <div id="first">
       <div id="left"> 회원가입하고 상품 첫 주문시 100만원 드립니다. </div>
       <div id="right"> X </div>
     </div>
   </div>
   <header>
     <div id="logo"> 
      <a href="../main/index">
       <img src="../static/main/logo.png" width="150" valign="middle">
      </a> 
     </div>
     <div id="search"> 
      <div id="searchForm">
       <input type="text" name="search" onkeyup="xCheck(this.value)" id="searchTxt" placeholder="검색어를 입력하세요">
       <img src="../static/main/x.png" valign="middle" id="xx" onclick="clearTxt()">
       <img src="../static/main/s.png" valign="middle"> 
      </div>
     </div>
     <div id="member">
      <c:if test="${userid == null}">
       <a href="../member/member"> 회원가입 </a> | 
       <a href="../login/login"> 로그인 </a> |
      </c:if>
      <c:if test="${userid != null}">
       ${name}님 | 
       나의메뉴 |
       <a href="../login/logout"> 로그아웃 </a> | 
      </c:if>      
        고객센터 
     </div>
   </header> <!-- 로고, 상품검색, 회원가입,로그인등등 -->
   <nav>
     <ul id="mainMenu">
       <li> 
         <span id="cate" onmouseover="viewMenu()" onmouseout="hideMenu()">
            <img src="../static/main/3.png" valign="middle"> 카테고리 
            <ul id="daeMenu"></ul>
         </span> 
         
       </li>
       <li> 신상품 </li>
       <li> 특가상품 </li>
       <li> 베스트 </li>
       <li> 이벤트 </li>
       <li> 일일특가 </li>
     </ul>
   </nav> <!-- 메뉴 -->   
    
   <sitemesh:write property="body"/>
    
   <footer>
     <img src="../static/main/footer.png">
   </footer> <!-- 쇼핑몰관련정보 -->
   
</body>
</html>
 