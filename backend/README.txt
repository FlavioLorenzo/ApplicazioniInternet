Note sulla versione:
- Per avviare il programma è consigliabile utilizzare il programma FakeSMTPserver (http://nilhcem.com/FakeSMTP/index.html),
collegandosi alla porta 1234
- La richiesta REST relativa al recupero della password non funziona tramite form. Seppur tale problema sarebbe risolubile
sovrascrivendo il comportamento del form, per il momento ci siamo limitati a permettere l'invio di una post request
di tipo "application/json" al REST controller. Inviando pertanto la stessa richiesta utilizzando postman il risultato è
corretto.