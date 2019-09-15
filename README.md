# ApplicazioniInternet
### Componenti: 
Flavio Lorenzo 246189, Marco Zudettich 251400, Simone Boscain 262626, Matteo Bucci 258282

###Avvio con Docker:
* Installare docker
* Assicurarsi che il drive su cui e' presente questa cartella faccia parte degli shared drives (docker / Setting / Shared Drives)
* Fermare tutte le istanze di postgres, fakesmtpserver, angular e spring che stanno girando.
* Assicurarsi che le porte 4200 e 8080 siano libere su localhost.
* Compilare il jar del progetto Spring attraverso il comando: mvn -Dmaven.test.skip=true package
* Dentro la cartella principale (dove si trova docker-compose.yml)
    * docker-compose up                ----> fa partire i 4 docker in foreground (control-c per stoppare)
    * docker-compose up -d             ----> fa partire i 4 docker in background (docker compose stop per stoppare)

### Test delle funzionalita'
A questo punto si puo' accedere alle funzionalita' dell'applicazione navigando su http://localhost:4200.

In automatico si viene rediretti sulla pagina di login.
Sono presenti 4 ruoli all'interno del sistema:

* SYS_ADMIN   u1@example.it:password
* ADMIN       u2@example.it:password
* ESCORT      u3@example.it:password
* USER        u4@example.it:password

**REGISTRAZIONE**.
Per poter testare la funzionalita' di registrazione occorre fare il login come system administrator (u1@example.it:password).
Navigando su "Manage Users" e' possibile creare un nuovo utente.
Una volta compilati i campi e fatto il submit, una mail viene inviata al server smtp di test, che la deposita all'interno della 
cartella /email, creata all'avvio di Docker nella cartella root del progetto.
Nella mail e' presente un link nella forma http://localhost:4200/register/{random-UUID}, a cui lo user deve accedere per completare la registrazione.

**AGGIUNTA BAMBINI**.
Ogni utente puo' aggiungere i propri figli navigando alla tab "Children".
Una lista dei propri figli e' mostrata a schermo. E' inoltre possibile inserire un numero di telefono di riferimento diverso per ogni figlio.
E' possibile rimuovere un proprio figlio dalla lista cliccando sull'apposito pulsante.

**PRENOTARE UNA CORSA**.
E' possibile prenotare una corsa per un proprio figlio navigando sulla tab "Reservation".
Una volta selezionato il figlio, il giorno e la linea, si puo' prenotare cliccando sull'apposito bottone.

**DARE DISPONIBILITA' COME ACCOMPAGNATORE**.
Per dare la propria disponibilita' come accompagnatore e' neccesario avere come ruolo almeno quello di ESCORT.
Per aggiungere una disponibilita' si deve navigare sulla tab "Shift Definition". Da li' e' possibile cercare le corse per giorno e linea e dare la propria disponibilita' premendo
il bottone apposito.

**ASSEGNARE IL TURNO**.
Solo gli amministratori di una linea possono affidare il turno ad una scorta per quella linea.
Per assegnare un turno e' necessario navigare sulla tab "Shift Consolidation".
Per ogni linea amministrata, l'amministratore puo' assegnare il turno ad un accompagnatore che ha dato la sua disponibilita'.
L'accompagnatore ricevera' una notifica visibile nella tab dei messaggi.
Seguendo il link fornito l'accompagnatore dovra' confermare la presa visione del turno.
A quel punto l'amministratore di linea puo' chiudere quella corsa confermando l'assegnazione.

**ESEGUIRE LA CORSA**.
Quando e' il momento, la scorta assegnata puo' aprire e gestire la corsa nella tab "Attendance".
E' possibile confermare la presa in custodia dei bambini prenotati alle varie fermate cliccando sul nome del bambino stesso.
Facendo click e' possibile cambiare lo stato del bambino (preso/lasciato) alla fermata corrispondente.
Nel caso si volesse aggiungere un utente che non si e' prenotato basta cliccare nell'apposita lista in basso.
Un form viene presentato all'accompagnatore con richiesto dove l'utente e' stato preso o lasciato.
In automatico l'utente verra' prenotato alla fermata corrispondente.
Dopo ogni fermata l'accompagnatore puo' segnalare il suo passaggio alla fermata stessa cliccando sul bottone "Close stop".
Dalla pagina "Attendance" e' inoltre possibile esportare la corsa corrente come file JSON o csv.

**GESTIONE DELLE LINEE**.
Ogni amministratore puo' cedere la gestione delle proprie linee ad altri amministratori e/o accompagnatori.
La cessione puo' essere fatta nella tab "Users" cliccando su un utente non genitore.
Navigando nel popup aperto e' possibile selezionare la/e linea/e che si vogliono cedere all'utente selezionato.
Se l'utente che riceve una linea da amministrare non e' ancora amministratore lo diventa.
Se l'utente che cede una linea non ha piu' linee amministrate dopo l'operazione diventa accompagnatore.
L'amministratore di sistema non e' soggetto a questo vincolo.
