<%@ page language="java" contentType="text/html; charset=UTF-8"
   import="java.lang.*,java.math.*,java.sql.*, java.io.*, java.util.*,video.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<title>Forgot Password</title>
	<jsp:useBean id="javaVideo" scope="session" class="video.Video"/>
    <jsp:setProperty name="javaVideo" property="*"/>
	</head>
	<body>
        <%
        String eMail= request.getParameter("eMail");
        String password =request.getParameter("password");
        String buffer=request.getParameter("buffer");
        ResultSet rezultat;
        if (eMail!=null && password.equals(buffer))
        {javaVideo.connect();
        rezultat=javaVideo.checkUser(eMail);
        rezultat.next(); // pentru a ne obtine datele si nu padingul de tabel sau ce o mai fi
        if(rezultat.getInt("exista")==0){
        	javaVideo.disconnect();
        	
        %>
        
        <script>
        (function emailNefolosit(){
        	alert("Email-ul nu exista!")
        })();
        </script>
        
       <% 
       %>
       <script>
       (function goToUserHome(){
       	location.replace("userForgPass.jsp")
       })();
       </script>
       <% 
       // si asta
        }
        else{
        String sare=Video.generateSalt();
        String parolahasuita=Video.hashPass(password,sare);
        javaVideo.createNewUserPass(eMail,parolahasuita,sare);
        javaVideo.disconnect();
        %>
        <p>parola schimbata cu succes!</p>
        <a href="userLogIn.jsp">Catre pagina de LogIn</a>
        <%
        }
        }else {
        %>
        <h1> Va rugam sa folositi un e-mail si o parola.</h1>
        <form action="userForgPass.jsp" method="post">
            <table>
                <tr>
                    <td align="Right">E-mail:</td>
                    <td><input type="email" name="eMail" required /></td>
                </tr>
                <tr>
                    <td align="Right">Parola:</td>
                    <td><input type="password" name="password" requierd /></td>
                </tr>
                <tr>
                    <td align="Right">Parola:</td>
                    <td><input type="password" name="buffer" requierd /></td>
                </tr>
                
                
            </table>
            <button type="submit">Change Password</button>
            
        </form>
        <%
        }
        %>
	</body>
</html>