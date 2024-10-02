<%@ page language="java" contentType="text/html; charset=UTF-8"
   import="java.lang.*,java.math.*,java.sql.*, java.io.*, java.util.*,video.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<title>Browse Video</title>
	<jsp:useBean id="javaVideo" scope="session" class="video.Video"/>
    <jsp:setProperty name="javaVideo" property="*"/>
	</head>
	<body>
	<p align="left"><a href="home.html"><b>Home</b></a>
		<h1 align="left">Inregistrari Video:</h1>
		<form action="userViewVideo.jsp" method="post">
			<table border="1" align ="left">
				<tr>
					<td><b>Mark:</b></td>
					<td><b>NumeVideo:</b></td>
				</tr>
				<%
				Integer userID=null;
			    Cookie[] cookies = request.getCookies();
			    if(cookies!=null){
			  	  for(Cookie cookie:cookies){
			  		  if(cookie.getName().equals("userID")) {userID=java.lang.Integer.parseInt(cookie.getValue());}
			  	
			  			  
			  	  }
			    }
				javaVideo.connect();
				System.out.println(userID);
				ResultSet rezultat = javaVideo.seeTableVideo(userID);
				long id;
				while (rezultat.next()){
				id=rezultat.getInt("idVideo");
					
					%>
					<tr>
						<td><input type="radio" name="cheieprimara" value="<%= id%>"/></td>
						<td><%= rezultat.getString("numeVideo") %></td>
						<%
				}
				rezultat.close();
				javaVideo.disconnect();
						%>
					</tr>
					</table>
					<p align="left"><input type="submit" value="Vizualizati Video">
					</p>
					</form>
		
</body>
</html>