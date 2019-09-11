# ApplicazioniInternet
### Componenti: Flavio Lorenzo 246189, Marco Zudettich 251400, Simone Boscain 262626, Matteo Bucci 258282

Avvio con Docker:
* Installare docker
* Assicurarsi che il drive su cui e' presente questa cartella faccia parte degli shared drives (docker / Setting / Shared Drives)
* Fermare tutte le istanze di postgres, fakesmtpserver, angular e spring che stanno girando
* Compilare il jar del progetto Spring attraverso il comando: mvn -Dmaven.test.skip=true package
* Dentro la cartella principale (dove si trova docker-compose.yml)
    * docker-compose up                ----> fa partire i 4 docker in foreground (control-c per stoppare)
    * docker-compose up -d             ----> fa partire i 4 docker in background (docker compose stop per stoppare)

A questo punto si puo' accedere alle funzionalita' dell'applicazione navigando su http://localhost:4200.
E' possibile vedere degli screen delle diverse funzionalita' nella cartella ApplicazioniInternet\screenshots

In automatico si viene rediretti sulla pagina di login.
Per testare senza registrarsi e' possibile fare login con le credenziali: u2@example.it:password  (1_login.PNG)
Una volta effettuato l'accesso si presenta una pagina dove e' possibile selezionare la linea e la data.  (2_line_view.PNG e 3_line_view.PNG)
Si presti attenzione che attualmente le date disponibili sono 14/06/2019 - 15/06/2019.
Se la corsa non e' disponibile un messaggio viene visualizzato a schermo, altrimenti la corsa viene visualizzata.
Cliccando sugli utenti che si sono prenotati e' possibile cambiare il loro stato (presi/lasciati) alla fermata corrispondente. (4_u1_unpicked.PNG e 4_u1_picked.PNG)
Nel caso si volesse aggiungere un utente che non si e' prenotato basta cliccare nell'apposita lista in basso.
Un form viene presentato all'accompagnatore con richiesto dove l'utente e' stato preso o lasciato. In automatico l'utente
verra` prenotato alla fermata corrispondente. (5_adding_u4_to_stop1.PNG e 5_u4_added_to_stop1.PNG)

Per testare la registrazione bisogna compilare il form di registrazione (e' possibile accederci navigando su Register).  (6_register.PNG)
Una volta compilati i campi e fatto il submit, una mail viene inviata al server smtp di test, che la deposita all'interno della 
cartella /email, creata all'avvio di Docker. (6_smpt_server_mail_received.PNG e 6_mail_confirmation_link.PNG)
La mail contiene un url che permette di confermare l'account. In automatico lo user viene rediretto al login e puo` accedere.
