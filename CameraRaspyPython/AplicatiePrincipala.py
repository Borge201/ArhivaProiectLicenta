import os
from subprocess import Popen
import DBactions
import VideoUpload
import threading
from threading import Lock

semnalScriereUtilizator = False #efecti log in user din bluetooth ia user ID si atat
semnalInchidereProgram = False #exact ce zice
#semnalIncarcareInregistrare = False #trimite comanda de start inregistrare
semnalIncarcareInregistrare = True # DOAR DE TESTARE
#semnalOprireInregistrareIncarcare = False #trimite comanda de orpire inregistrare
semnalOprireInregistrareIncarcare=True #DOAR DE TESTARE
semnalStartStream = False #trimite comanda de start live
semnalStopStream = False #trimite comanda de stop live
semnalLiveStream = False #trimite comanda de livestream
semnalSEINREGISREAZA = False #flag de anuntare ca se inregistreaza
semnalSETRANSMITELIVE = False #flag de anuntare ca se transmite live
lock = Lock()
def functieIncarcareVideoThread(numeVideoDeIncarcat,lock):
    #incarca catre server in DB intai
    lock.acquire()
    VideoUpload.incarcareVideoServer(numeVideoDeIncarcat)
    lock.release()
    return 0
def functieInregistrareTHREAD(SemnalIncarcare, lock):
    lock.acquire()
    while (SemnalIncarcare is True):
        numeVideo = VideoUpload.createNumeVideo()
        numeVideoTrecut = numeVideo
        VideoUpload.record2MinVideo(numeVideo=numeVideo)
        if(semnalOprireInregistrareIncarcare is True):
            SemnalIncarcare=False
            lock.release()
        #aici incarci pe server ce s-a inregistrat

while(True):


    #threadStream
    if (semnalIncarcareInregistrare is not False and semnalLiveStream is False) and semnalSETRANSMITELIVE is False:
        threadIncarcareInregistrare = threading.Thread(target=functieInregistrareTHREAD, args=(True, lock),
                                                       daemon=False)
        semnalIncarcareInregistrare = False
        semnalSEINREGISREAZA = True
        threadIncarcareInregistrare.start()
    #din pacate daca camera e follosita de inregistrare video nu poate face stream
    #nu situ cum sa fac altfel asta
    #imi pare rau
    elif(semnalIncarcareInregistrare is False and semnalSEINREGISREAZA is True) and semnalOprireInregistrareIncarcare is True:
        #oprire inregistrare
        if(threadIncarcareInregistrare.is_alive() is True):
            threadIncarcareInregistrare.join()
            semnalSEINREGISREAZA = False #oprim inregistrarea
            semnalOprireInregistrareIncarcare = False #resetam semnalul de stop inregistrare
            # FA ALT THREADsi apeleazal cand se termina inregistrarea NU E NICIO PROBLEMA ZIC EU
            #creaza lista cu numele videourilor:
            lista_fisiere = os.listdir()
            #stergem din lista fisierele programului
            lista_fisiere.remove("AplicatiePrincipala.py")
            lista_fisiere.remove("DBactions.py")
            lista_fisiere.remove("VideoUpload.py")
            lista_fisiere.remove("utilizatorID.txt")
            lista_fisiere.remove("__pycache__")
            lista_fisiere.remove(".idea")
            #lista_fisiere.remove("pythonCameranDB_back-Git")
            #creaza un for loop in care vei crea un thread pentru transmisie si acesta va incarca pe server/inregistra in DB videourile
            for numeFisier in lista_fisiere:
                threadIncarcareServer = threading.Thread(target=functieIncarcareVideoThread, args=(numeFisier,lock))
                threadIncarcareServer.start()
                threadIncarcareServer.join()
            # gen trimite mesajul cu ID util si cu numele fisierului si apoi ia datele
            #for numeFisier in lista_fisiere:
             #   x=Popen(['rm',''+numeFisier+''])
    else:
        semnalOprireInregistrareIncarcare = False



    #incepe stream
    if (semnalStartStream is True and semnalLiveStream is False)and semnalSEINREGISREAZA is False:
        #incepe threadul
        semnalStartStream = False
        semnalLiveStream = True
        #aici threadul de streaming
    elif(semnalStartStream is False and semnalLiveStream is True) and semnalStopStream is True:
        semnalLiveStream = False #oprim live
        semnalStopStream = False #resetam semnalul de oprit live

    else:
        semnalStopStream = False
    #if semnalInchidereProgram is True:  #comentat pentru a putea folosi pe pc dezvoltare
        #semnalInchidereProgram = False
        #semnal de shutdown all threads
        #semnal de shutdown OS cu Popen