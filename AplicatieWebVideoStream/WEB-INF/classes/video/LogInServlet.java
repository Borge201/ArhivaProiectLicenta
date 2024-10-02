package video;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class LogInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String userEmail="";
    private String userPass="";
    Connection con=null;
    String error;
    public void initiate() throws ServletException{
    	try {
    		 Class.forName("com.mysql.cj.jdbc.Driver");
    		 con = DriverManager.getConnection("jdbc:mysql://192.168.1.8:3306/cameraBord", "mobilUser", "1507");
    	}catch (ClassNotFoundException |SQLException e) {
    		e.printStackTrace();
    	}
    }
    public void disconnect() throws SQLException{
        try{
            if(con!=null){
                con.close();
            }
        }catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
    }
    public ResultSet getUserDetails(String email) throws SQLException,Exception{
		ResultSet rezultat=null;
			if(con!=null) {try {
				
				String interogare = ("SELECT * FROM utilizatori where emailUtilizator='"+ email + "';");
			    Statement statement=con.createStatement();
			    rezultat=statement.executeQuery(interogare);
			
			}catch (SQLException sqle){
			    error="exceptieSQL: REALIZARE NEREUSITA, ceva nu merge!!!";
			    throw new SQLException(error);
			}
			catch(Exception e){
				error="A aparut o exceptie";
				throw new Exception(error);
			}}
			else {
			    error="Exceptie: conexiunea cu baza de date a fost pierduta.";
			    throw new Exception(error);
			}
			return rezultat;
	}
    public ResultSet checkUser(String email) throws SQLException,Exception{
		ResultSet rezultat=null;
		if(con!=null) {try {
			
			String interogare = ("SELECT COUNT(*) AS exista FROM utilizatori where emailUtilizator='"+ email + "';");
            Statement statement=con.createStatement();
            rezultat=statement.executeQuery(interogare);
		
		}catch (SQLException sqle){
            error="exceptieSQL: REALIZARE NEREUSITA, ceva nu merge!!!";
            throw new SQLException(error);
		}catch(Exception e) {
			error="exceptie: REALIZARE NEREUSITA, ceva nu merge!!!";
            throw new Exception(error);
		}
		}
		else {
            error="Exceptie: conexiunea cu baza de date a fost pierduta.";
            throw new Exception(error);
        }
		return rezultat;
	}
    public ResultSet getSavedSalt(String email) throws SQLException,Exception {
		ResultSet rezultatSare=null;
		if(con!=null) {try {
			
			String interogare = ("SELECT passSalt  FROM utilizatori where emailUtilizator='"+ email + "';");
            Statement statement=con.createStatement();
            rezultatSare=statement.executeQuery(interogare);
		
		}catch (SQLException sqle){
            error="exceptieSQL: REALIZARE NEREUSITA, ceva nu merge!!!";
            throw new SQLException(error);
}
		}else {
            error="Exceptie: conexiunea cu baza de date a fost pierduta.";
            throw new Exception(error);
        }
		return rezultatSare;
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	    
		String utilizator = request.getParameter("Email");
		String parola=request.getParameter("passw");
		Integer utilizatorID=-1;
		Integer userExists=0;
		ResultSet rezultatExistentaUtilizator=null;
		initiate();
		
		try {
			rezultatExistentaUtilizator = checkUser(utilizator);
			rezultatExistentaUtilizator.next();
			userExists = rezultatExistentaUtilizator.getInt("exista");
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResultSet rezultatInterogare=null;
		if(userExists==1) {
			
			ResultSet rezultatSareUtilizator=null;
			initiate();
			try {
				rezultatSareUtilizator=getSavedSalt(utilizator);
				rezultatSareUtilizator.next();
			
			String sare=new String();
				sare = rezultatSareUtilizator.getString("passSalt");
				parola = Video.hashPass(parola,sare);
				
			
				rezultatInterogare=getUserDetails(utilizator);
				rezultatInterogare.next();
				userEmail=rezultatInterogare.getString("eMailUtilizator");
				userPass=rezultatInterogare.getString("passUtil");}
			catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		if(userEmail.equals(utilizator) && userPass.equals(parola) )
		{
			try {
				utilizatorID=rezultatInterogare.getInt("idUtilizator");
				disconnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Cookie loginCookieID= new Cookie("userID",utilizatorID.toString());
			loginCookieID.setMaxAge(24*60*60);//expira in 24 ore
			response.addCookie(loginCookieID);
			response.sendRedirect("userLogInSucces.jsp");
			
		}else {
			try {
				disconnect();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/userLogIn.jsp");
			PrintWriter out=response.getWriter();
			out.println("<font color=red>Email-ul sau parola sunt incorecte !</font>");
			rd.include(request, response);
		}
		}else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/userLogIn.jsp");
			PrintWriter out=response.getWriter();
			out.println("<font color=red>Email-ul sau parola sunt incorecte sau utilizatorul nu exista!</font>");
			rd.include(request, response);
		}
		
		
	}

}
