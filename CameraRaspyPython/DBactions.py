import mysql.connector
def getIDUtilizatorFisier():
    fileIDUtil=open("utilizatorID.txt")
    idUtilizator=fileIDUtil.read()
    fileIDUtil.close()
    return idUtilizator

def introducereVideo(numeVideoIN):
    idUtil = getIDUtilizatorFisier()
    bazaDeDate = mysql.connector.connect(host="192.168.1.8", user="mobilUser", password="1507", database="cameraBord")
    cursorDB = bazaDeDate.cursor()
    cale="/"+idUtil
    cursorDB.execute("INSERT INTO videoUtilizator (numeVideo, idUtilizator, caleVideo) VALUES ('"+numeVideoIN+"','"+idUtil+"','"+cale+"')")
    bazaDeDate.commit()
    cursorDB.execute("SELECT * FROM videoUtilizator") #aici pentru testare
    bazaDeDate.disconnect()
