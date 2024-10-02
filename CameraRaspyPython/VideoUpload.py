import socket
import DBactions
import time
from subprocess import Popen # creem subproces cu popen cu ffmpeg care apoi



#generareVideo si incarcarea acestuia
def createNumeVideo():
    numeVideoDataOra=str(time.strftime("%Y_%m_%d_%H_%M"))
    return numeVideoDataOra
def record2MinVideo(numeVideo): #createNumeVideo()
    procesVideoFile=Popen(['ffmpeg','-y','-f','v4l2','-framerate','30','-video_size'
                     ,'1280x720','-c:v','mjpeg','-i','/dev/video0','-an','-t','120','-b:v','4M',''+numeVideo+'.mp4'])
    procesVideoFile.wait()
    #return procesVideoFile

def incarcareVideoServer(numeVideo):
    # creeaza socket
    socketVideo = socket.socket()
    # conecteaza socket la adresa server cu port 15720
    # intai apeleaa introducerea datelor in DB
    numeVideoDB = numeVideo.removesuffix(".mp4")
    DBactions.introducereVideo(numeVideoDB)
    # apoi transmite prin socket
    # realizeaza socket cu port 15720
    socketVideo.connect(("localhost", 15720))
    # trimite IDutilizator pentru clasificarea video
    idUtil = DBactions.getIDUtilizatorFisier()
    idUtil = idUtil + '/' + numeVideo
    socketVideo.send(idUtil.encode())
    # citeste fisierul de incarcat ca forma de biti
    fisierDeIncarcat = open(numeVideo, "rb")

    # aici incarcam fisierul
    buffer = fisierDeIncarcat.read(1024)
    while (buffer): #cat timp se citesc date
        socketVideo.send(buffer) #trimite acele date
        buffer = fisierDeIncarcat.read(1024) #citeste urmatoarele date
    socketVideo.close() #inchide conexiunea socket
    fisierDeIncarcat.close() #opreste citirea fisierului
    # incepe transmisiune catre server