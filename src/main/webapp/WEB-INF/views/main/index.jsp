<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
 
   article {
      width:1500px;
      height:350px;
      margin:auto;
      background:pink;
      overflow:hidden;
      }
   article #inner {
      width:10000px;
   }
   main {
      width:1100px;
      margin:auto;
   }
   main #p1 {
      width:1100px;
      height:400px;
      margin:auto;
      background:#abcdef;
   }
   main #p2 {
      width:1100px;
      height:400px;
      margin:auto;
      background:#ab18ef;
   }
   main #p3 {
      width:1100px;
      height:400px;
      margin:auto;
      background:#abcd12;
   }
   main #p4 {
      width:1100px;
      height:400px;
      margin:auto;
      background:#abcdef;
   }
  
 </style>
 <script src="https://code.jquery.com/jquery-latest.js"></script>
 <script>
   $(function()
   {
	    setInterval(function()
	    {
	    	 $("#inner").animate(
	    	 {
	    		 marginLeft:"-1500px"
	    	 },2000,function()
	    	        {
	    		        $("#inner").css("margin-left","0px");
	    		        $(".mimg").eq(0).insertAfter($(".mimg").eq(5));
	    	        });
	    },4000);
   });
 </script>
</head>
<body>
   <article id="outer">
     <div id="inner">
       <img class="mimg" src="../static/main/s2.png" width="1500" height="350"><img class="mimg" src="../static/main/s3.png" width="1500" height="350"><img class="mimg" src="../static/main/s4.png" width="1500" height="350"><img class="mimg" src="../static/main/s5.png" width="1500" height="350"><img class="mimg" src="../static/main/s6.png" width="1500" height="350"><img class="mimg" src="../static/main/s7.png" width="1500" height="350">
     </div>
   </article> <!-- 그림 스크롤 -->
   <main>
    <section id="p1"></section>
    <section id="p2"></section>
    <section id="p3"></section>
    <section id="p4"></section>
   </main> <!-- 전체 내용 -->
 </body>
 </html>


