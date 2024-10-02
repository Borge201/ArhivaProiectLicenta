<%@ page language="java" contentType="text/html; charset=UTF-8"
   import="java.lang.*,java.math.*,java.sql.*, java.io.*, java.util.*,video.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<title>Log-In</title>
	<jsp:useBean id="javaVideo" scope="session" class="video.Video"/>
    <jsp:setProperty name="javaVideo" property="*"/>
	</head>
	<body>
	<%
	Cookie[] cookies = request.getCookies();
    if(cookies!=null){
  	  for(Cookie cookie:cookies){
  		  if(cookie.getName().equals("userID")) {response.sendRedirect("userLogInSucces.jsp");}  
  	  
  	  }
    }
	%>
	
	
      <form action="LogInServlet" method="post">
            <table>
                <tr>
                    <td align="Right">E-mail:</td>
                    <td><input type="text" name="Email" required /></td>
                </tr>
                <tr>
                    <td align="Right">Parola:</td>
                    <td><input type="password" name="passw" required /></td>
                </tr>
  
            </table>
            <button class="button" type="submit">Log in</button>
            
            <a href="userForgPass.jsp"><b>Forgot Password?</b></a>
        </form> 
	</body>
</html>