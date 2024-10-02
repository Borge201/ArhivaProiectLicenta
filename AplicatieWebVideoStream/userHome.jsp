<%@ page language="java" contentType="text/html; charset=UTF-8"
   import="java.lang.*,java.math.*,java.sql.*, java.io.*, java.util.*,video.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<title>Home</title>
	<jsp:useBean id="javaVideo" scope="session" class="video.Video"/>
    <jsp:setProperty name="javaVideo" property="*"/>
	</head>
	<%
	String userID=null;
    Cookie[] cookies = request.getCookies();
    if(cookies!=null){
  	  for(Cookie cookie:cookies){
  		  if(cookie.getName().equals("userID")) {userID=cookie.getValue();}
  	
  			  
  	  }
  	  if(userID==null)
  	{%>
	  <script>(function goToUserHome(){
    	
       	location.replace("userLogIn.jsp")
       })();</script>
	  <%}
  	  }
  	String streamString="/home/goerge/serverJava/"+userID+"/"+userID+"stream.mjpg";
	%>
	<body>
      <p>USER HOME</p>
      <script>
      function  deleteCookieAndRedirect(){
    	  nume="userID"
    	  document.cookie = nume+ '=' +'; expires=Thu, 01 jan 1970 00:00:01 GMT';
    	  location.replace("userLogIn.jsp") 
    	  
      }
      </script>
      <button onclick="deleteCookieAndRedirect()" >LogOut</button>
      <p align="center"><a href="userViewVideoServer.jsp"><b>Inregistrari Video</b></a>
      <h1>CameraStream</h1>
 		
	  <img src="<%= streamString %>" width="640" height="480" />
	</body>
</html>