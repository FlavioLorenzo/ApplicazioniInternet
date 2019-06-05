# ApplicazioniInternet

1# Installare docker
2# Assicurarsi che il drive su cui c'e` questa cartella faccia parte degli shared drives (docker / Setting / Shared Drives)
3# Fermare tutte le istanze di postgres, fakesmtpserver, angular e spring che stanno girando
4# Dentro la cartella principale (dove si trova docker-compose.yml)
   docker-compose up                ----> fa partire i 4 docker in foreground (control-c per stoppare)
   docker-compose up -d             ----> fa partire i 4 docker in background (docker compose stop per stoppare)


