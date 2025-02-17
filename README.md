# TaskFlow

## Descrizione del Progetto
TaskFlow è un'applicazione web progettata per ottimizzare la gestione dei flussi di lavoro collaborativi all'interno di team di qualsiasi dimensione. Il tool consente agli utenti di pianificare, monitorare e gestire le attività in modo efficiente, migliorando la comunicazione e la produttività.

## Funzionalità Principali
- **Gestione delle Organizzazioni**: Creazione e amministrazione di organizzazioni, con ruoli differenziati tra utenti e proprietari.
- **Gestione dei Progetti**: Possibilità di creare, modificare e cancellare progetti all'interno di un'organizzazione.
- **Gestione delle Attività**: Creazione e assegnazione di task con campi personalizzabili per adattarsi a diverse esigenze.
- **Notifiche e Scadenze**: Reminder automatici tramite email per le attività con date di scadenza.
- **Sicurezza e Autenticazione**: Gestione degli utenti con login sicuro e ruoli differenziati.

## Stack Tecnologico
### Backend
- **Linguaggio**: Java
- **Framework**: Spring (Spring Boot, Spring Security, Spring Data MongoDB)
- **Database**: MongoDB (NoSQL)
- **Servizi Aggiuntivi**: MapStruct per il mapping degli oggetti, Spring Mail per l'invio di notifiche via email.

### Frontend
- **Framework**: Angular
- **UI Framework**: Angular Material (Material Design 3)

## Architettura
TaskFlow segue un'architettura RESTful, con una netta separazione tra frontend e backend, garantendo scalabilità e flessibilità.

- **Database MongoDB**: Archivia dati in formato BSON, offrendo flessibilità nella gestione dei campi personalizzati per le attività.
- **Spring Boot**: Implementa il backend con API REST per la gestione delle organizzazioni, progetti e attività.
- **Angular**: Frontend dinamico e reattivo per una user experience intuitiva.

L'applicazione sarà accessibile su `http://localhost:4200/`.

## Contributi
Sviluppato da:
- **Tommaso Botarelli**
- **Alberto Giovannoni**
- **Daniele Morganti**

Relazione del Progetto: [Visualizza il PDF](swam.pdf)
