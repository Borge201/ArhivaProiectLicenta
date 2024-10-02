package video;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class Video implements Serializable{
	private static final long serialVersionUID = 1L;
	String error;
    Connection con;
	public void connect() throws Exception, SQLException{
	      try{
	    	  Class.forName("com.mysql.cj.jdbc.Driver");
	    	  con = DriverManager.getConnection("jdbc:mysql://192.168.1.8:3306/cameraBord", "mobilUser", "1507");
	          // user: "mobilUser" cu parola "1507"  la adresa  jdbc:mysql://192.168.1.8:3306/cameraBord pentru a putea sa il punem pe
	    	  //server altfel localhost cu root si 1507
	      } catch (SQLException cnfe){
	         error="SQLException: nu se poate conecta la baza de date";
	         throw new SQLException(error);
	      }catch(Exception e){
	          error="Exception: a aparut o eroare";
	          throw new Exception(error);
	      }
	    }
	public void disconnect() throws SQLException{
        try{
            if(con!=null){
                con.close();
            }
        }catch (SQLException sqle)
        {
            error="SQLException: nu se poate inchide conexiunea la baza de date";
            throw new SQLException(error);
        }
    }
	public void createNewUserPass(String eMail,String password, String sare) throws SQLException,Exception{
		if(con!=null) {try {
			
			Statement statement;
			statement=con.createStatement();
			statement.executeUpdate("UPDATE utilizatori SET passUtil='"+ password +"', passSalt='" + sare+ "' where emailUtilizator='"+ eMail +"';");
            
		
			
		}catch (SQLException sqle){
                error="exceptieSQL: REALIZARE NEREUSITA, ceva nu merge!!!";
                        throw new SQLException(error);
            }
        }
        else {
            error="Exceptie: conexiunea cu baza de date a fost pierduta.";
            throw new Exception(error);
        }
		
	}
	public void createNewUser(String eMail,String password,String sare) throws SQLException,Exception{
		if(con!=null) {try {
			
			Statement statement;
			statement=con.createStatement();
			statement.executeUpdate("INSERT INTO utilizatori (emailUtilizator, passUtil, passSalt) values ('"+ eMail +"','"+ password +"','" + sare+ "');");
            
		
			
		}catch (SQLException sqle){
                error="exceptieSQL: REALIZARE NEREUSITA, ceva nu merge!!!";
                        throw new SQLException(error);
            }
        }
        else {
            error="Exceptie: conexiunea cu baza de date a fost pierduta.";
            throw new Exception(error);
        }
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
}
		}else {
            error="Exceptie: conexiunea cu baza de date a fost pierduta.";
            throw new Exception(error);
        }
		return rezultat;
	}
	public ResultSet getVideoName(String idVideo) throws SQLException,Exception{
		ResultSet rezultat=null;
		if(con!=null) {try {
			
			String interogare = ("SELECT numeVideo,idUtilizator FROM videoUtilizator where idVideo='"+ idVideo + "';");
            Statement statement=con.createStatement();
            rezultat=statement.executeQuery(interogare);
		
		}catch (SQLException sqle){
            error="exceptieSQL: REALIZARE NEREUSITA, ceva nu merge!!!";
            throw new SQLException(error);
}
		}else {
            error="Exceptie: conexiunea cu baza de date a fost pierduta.";
            throw new Exception(error);
        }
		return rezultat;
	}
	public ResultSet seeTableVideo(int idUser) throws SQLException, Exception {
		ResultSet rs=null;
		try {
			String queryString=("SELECT idVideo,numeVideo FROM videoUtilizator WHERE idUtilizator='"+idUser+"';");
			Statement stmt= con.createStatement();
			rs= stmt.executeQuery(queryString);
		}catch (SQLException sqle) {
			error = "SQLException: interogarea nu a fost posibila";
			throw new SQLException(error);
			
		}catch(Exception e){
			error="A aparut o exceptie, ceva nu merge.";
			throw new Exception(error);
		}
	return rs;
	}
	public static String hashPass(String password,String sare) throws NoSuchAlgorithmException{
		password=password.concat(sare);
		MessageDigest digestparola = MessageDigest.getInstance("SHA-256");
		byte[] hash=digestparola.digest(password.getBytes(StandardCharsets.UTF_8));
		StringBuilder hashString = new StringBuilder();
		for (int i=0; i<hash.length;i++) {
			String hexadecimal=Integer.toHexString(0xff & hash[i]); 
			if(hexadecimal.length()==1)
				hashString.append('0');
			hashString.append(hexadecimal);
		}
		return hashString.toString();
	}
	public static String generateSalt() {
		Random rng=new Random();
		String caractere="abcdefghijklmonpqrstuvwxyz1234567890!@#$%^&*()_+-=[]{}|?<>;:ABCDEFGHIJKLMOPQRSTUVWXYZ"; // caracterele din care va fi compus stringul
		char[] salt= new char[8];
		for (int i=0; i<8; i++)
		{
			salt[i]=caractere.charAt(rng.nextInt(caractere.length()));
		}
		
		return new String(salt);
	}
	
}
