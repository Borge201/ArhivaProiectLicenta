<%@ page language="java" contentType="text/html; charset=UTF-8"
   import="java.lang.*,java.math.*,java.sql.*, java.io.*, java.util.*,video.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<title>Video</title>
	<jsp:useBean id="javaVideo" scope="session" class="video.Video"/>
    <jsp:setProperty name="javaVideo" property="*"/>
	</head>
	<p align="center">
            <a href="userHome.jsp"><b>Home</b></a>
            <br/>
        </p>
	<body>
        <%
            String s = request.getParameter("cheieprimara");
        	ResultSet rezultat;
        	javaVideo.connect();
        	rezultat=javaVideo.getVideoName(s);
        	rezultat.next();
        	String numeVideo=rezultat.getString("numeVideo");
        	String caleVideo=rezultat.getString("idUtilizator");
        	javaVideo.disconnect();
        	String videoString="/home/george/serverJava/"+caleVideo+"/"+numeVideo+".mp4";
        %>
        <video width="1280" height="720" controls>
		<source src="<%=videoString %>" type="video/webm">
		</video>
    </body>
</html>