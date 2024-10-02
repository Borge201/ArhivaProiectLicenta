<%@ page language="java" contentType="text/html; charset=UTF-8"
   import="java.lang.*,java.math.*,java.sql.*, java.io.*, java.util.*,video.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<title>Log-In succes</title>
	<jsp:useBean id="javaVideo" scope="session" class="video.Video"/>
    <jsp:setProperty name="javaVideo" property="*"/>
	</head>
	<body>
      <%
      String userID=null;
      Cookie[] cookies = request.getCookies();
      if(cookies!=null){
    	  for(Cookie cookie:cookies){
    		  if(cookie.getName().equals("userID")) {userID=cookie.getValue();
    		  response.sendRedirect("userHome.jsp");}
    	  }
   
      }
      
       if(userID == null) response.sendRedirect("userLogIn.jsp");
      
      %>
	</body>
</html>