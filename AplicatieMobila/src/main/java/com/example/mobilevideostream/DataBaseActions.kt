package com.example.mobilevideostream
import java.io.Serializable
import java.sql.*
import java.util.Random
import java.security.MessageDigest
class DataBaseActions() : Serializable  { // clasa DataBaseActions care mosteneste serializable
    private var conn: Connection?=null // ? intoarce null daca e null si valoarea daca nu e
    // parantezele de langa numele clasei impune un constructor gol
    fun connect(){
    try {
        Class.forName("com.mysql.jdbc.Driver").newInstance()
        conn=DriverManager.getConnection("jdbc:mysql://192.168.1.8:3306/cameraBord", "mobilUser", "1507")
    }catch (ex: SQLException){
        ex.printStackTrace()
    }catch(ex: Exception){
        ex.printStackTrace()
    }
    }
    fun disconnect(){
    try {
        conn?.close()  // echivalentul unui if null else daca nu e null
    }
    catch(ex: SQLException){
        ex.printStackTrace()
    }
    }

    fun checkUser(eMail: String):ResultSet?{
        var stmt: Statement?= null
        var resultset: ResultSet? = null
        try {stmt=conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT COUNT(*) AS exista FROM cameraBord.utilizatori where emailUtilizator='"+ eMail + "';")
            //asta ar trebui sa ne intoarca daca exista un user sau nu
        }catch(ex: SQLException){
            ex.printStackTrace()
        }
    return resultset
    }

    fun createNewUser(eMail: String, passwd: String, sare: String ){
        var stmt: Statement?= null
        try {
            stmt=conn!!.createStatement()
            stmt!!.executeUpdate("INSERT INTO cameraBord.utilizatori (emailUtilizator, passUtil, passSalt) values ('"+ eMail +"','"+ passwd +"','" + sare+ "');")
        }catch(ex: SQLException){
            ex.printStackTrace()
        }
    }
    fun createNewUserPass(eMail: String, passwd: String, sare: String ){
        var stmt: Statement?= null
        try {
            stmt=conn!!.createStatement()
            stmt!!.executeUpdate("UPDATE cameraBord.utilizatori (passUtil, passSalt) values ('"+ passwd +"','" + sare+ "') where emailUtilizator='"+ eMail +"';")
        }catch(ex: SQLException){
            ex.printStackTrace()
        }
    }
    fun getVideoNames(idVideo: String):ResultSet?{
        var stmt: Statement?= null
        var resultset: ResultSet? = null
        try {stmt=conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT numeVideo,idUtilizator from cameraBord.videoUtilizator where idVideo='"+idVideo+"';")

        }catch(ex: SQLException){
            ex.printStackTrace()
        }
        return resultset
    }
    fun getVideoTable(idUser: String):ResultSet?{
        var stmt: Statement?= null
        var resultset: ResultSet? = null
        try {stmt=conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT idVideo,numeVideo from cameraBord.videoutilizator where idUtilizator='"+idUser+"';")

        }catch(ex: SQLException){
            ex.printStackTrace()
        }
        return resultset
    }
    fun generateSalt():String{
        var rng: Random = Random()
        val caractere: String="abcdefghijklmonpqrstuvwxyz1234567890!@#$%^&*()_+-=[]{}|?<>;:ABCDEFGHIJKLMOPQRSTUVWXYZ"
        var salt:CharArray= CharArray(8)
        for (i in 0 until 8){
            salt[i]=caractere[(rng.nextInt(caractere.length))]
        }
        return salt.concatToString() //ar trebui sa ne intoarca un sir generat aleator cum trebuie
    }

    fun hashPass(passwd: String, sare:String?):String {
        val parola: String = passwd + sare
        var digestParola = MessageDigest.getInstance("SHA-256")
        var hash= digestParola.digest(parola.toByteArray(Charsets.UTF_8))
        val r = StringBuilder()
        for ( i in 0 until hash.size){
            val hexaDeci: String = Integer.toHexString(0xff and hash[i].toInt())
            if(hexaDeci.length == 1) {
                r.append('0')}
                // adaugat pentru a reprezenta valorie de tip 05
                // (default reprezentate ca 5)
                // ca 05 din nou

            r.append(hexaDeci)
        }

        return r.toString()
    }
    fun getUserDetails(eMail: String):ResultSet?{
        var stmt: Statement?= null
        var resultset: ResultSet? = null
        try {stmt=conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT * from cameraBord.utilizatori where emailUtilizator='"+ eMail + "';")
        }catch(ex: SQLException){
            ex.printStackTrace()
        }
    return resultset
    }
    fun interogareTest():ResultSet?{
        var stmt: Statement?= null
        var resultset: ResultSet? = null
        try {stmt=conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT passUtil,passSalt from cameraBord.utilizatori;")

        }catch(ex: SQLException){
            ex.printStackTrace()
        }
        return resultset
    }

}