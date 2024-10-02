package com.example.mobilevideostream
import com.example.mobilevideostream.DataBaseActions
import java.sql.ResultSet


fun main(args: Array<String>){
    val actiuniDB= DataBaseActions()
    actiuniDB.connect()
    val rezultat:ResultSet?=actiuniDB.interogareTest()
    while(rezultat?.next() == true){
        println(rezultat.getString("passUtil"))
        println(actiuniDB.hashPass("1234",rezultat.getString("passSalt")))
        println(rezultat.getString("passUtil").equals(actiuniDB.hashPass("1234",rezultat.getString("passSalt"))))
    }
    println(actiuniDB.generateSalt())
    actiuniDB.disconnect()
}
