package serverJava;

import java.io.*;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.*;
import java.util.Scanner;
import java.lang.*;



public class ServerJavaVideo extends Thread{
	private Scanner scanTCPMessage; // citeste text
	private Socket socket; // socket-ul in sine
	
	int portVideoTransfer=15720;
	public ServerJavaVideo(Socket conexiune) throws IOException{
		this.socket=conexiune;  //Obtine socket-ul
		this.scanTCPMessage=new Scanner(socket.getInputStream());//posibil sa ia toata comunicatia
	}
	
	public void run() {
		String mesajUserIDnVideoName;
		String userIDprimit;
		String filePath="/home/george/serverJava";
		try {
			System.out.println("Client conectat");
			while(true) {
				
					if(scanTCPMessage.hasNextLine()) {
						
					mesajUserIDnVideoName=scanTCPMessage.nextLine();
					System.out.println(mesajUserIDnVideoName);
					if(mesajUserIDnVideoName.contains("/20")==true) {
					DataInputStream dataIn=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
					String [] mesaj=mesajUserIDnVideoName.split("/"); //mesaj concatenat si despartit la /
					String userID=mesaj[0]; // ID-ul utilizatorului
					//System.out.println(mesaj[0]);
					//System.out.println(mesaj[1].length());
					//System.out.println(mesaj[1]);
					String numeFisier=mesaj[1].substring(0, 20); //ne ia doar stringul cu nume.mp4
					//System.out.println(numeFisier.length());
					//System.out.println(numeFisier);
					numeFisier=numeFisier.trim();
					File director = new File(filePath+"/"+mesaj[0]);
					if(!director.exists()) {
						System.out.println("director creat");
						director.mkdir();
					}
					else {
						System.out.println("director existent");
					}
					String fileNamePath=filePath+"/"+mesaj[0]+"/"+numeFisier;
					System.out.println(fileNamePath);
					File newFilePath=new File(fileNamePath);
					if(newFilePath.createNewFile()) {
						System.out.println(newFilePath.getName()+" uite ca exista");
					}
					else 
					{
						System.out.println("Eroare nu s-a putur crea fisierul");
					}
					fileNamePath=fileNamePath.trim();
					FileOutputStream fisierVideo= new FileOutputStream (newFilePath);
					byte [] buffer = new byte[1024];
					int bytesCititi;
					while((bytesCititi= dataIn.read(buffer))!=-1) {
						fisierVideo.write(buffer,0,bytesCititi);
						
					}
					fisierVideo.close();
					System.out.println("FisierPrimit");
					}
					
					}
					else {
						break; //incheie conexiunea daca nu transmitem live sau transmitem fisier
					}
			socket.close(); //gata am luat fisierul si apoi inchidem conexiunea
			
		}
	}catch (IOException ex) {
		ex.printStackTrace();
	}
	}
	
	
	public static void main(String[] args) throws IOException{
		
		ServerSocket socketTCP = new ServerSocket(15720); //creare socket srever pt video transfer si ce o mai fi
		while(true) {
			Socket conexiuneVideoTCP=socketTCP.accept();
			ServerJavaVideo server=new ServerJavaVideo(conexiuneVideoTCP);
			server.start();
		}
		
		
	}

}
