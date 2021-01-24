# Proiect Energy System Etapa 2

## Despre

Proiectare Orientata pe Obiecte, Seriile CA, CD
2020-2021

<https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa2>

Student: Hoisan Stefan Alexandru, grupa 333CA

## Rulare teste

Clasa Test#main
  * ruleaza solutia pe testele din checker/, comparand rezultatele cu cele de referinta
  * ruleaza checkstyle

Detalii despre teste: checker/README

Biblioteci necesare pentru implementare:
* Jackson Core 
* Jackson Databind 
* Jackson Annotations

## Implementare

### Entitati

Pachetul mappingclass contine toate entitatile necesare maparii json, atat pentru
citire, cat si pentru scriere.  Pachetul handler contine implementarea propriu-zisa pentru consumatori, distribuitori si producatori.  Pachetul strategies contine entitatile care implementeaza interfata Strategy in
diverse moduri

### Flow

Clasa care se ocupa de intreg flowul temei este Handler. Aceasta are urmatoarele
functionalitati:
1) Creeaza instantele pentru consumatori, distribuitori, producatori pornind de la input
2) Creeaza instantele necesare pentru Factory & Observer Patterns
3) Simuleaza runda initiala, abonand fiecare distribuitor la o lista de producatori, iar
apoi aplica procesul consumatori-distribuitori implementat in etapa1
4) La inceputul fiecarei luni se aplica modificarile pentru consumatori si distribuitori
5) In timpul lunii se aplica modificarile pentru producatori
6) La sfarsitul lunii, fiecare distribuitor abonat la un producator care a suferit o
modificare, isi reaplica strategia
7) Tot la sfarsitul fiecarei luni, se populeaza monthlyStats pentru toti producatorii
8) La sfarsitul simularii tuturor lunilor, este intors un mapping class pentru output

### Elemente de design OOP

Am abstractizat:
* Procesul de serializare/deserializare pentru fisierele json
* Aplicarea metodelor asupra consumatorilor, distribuitorilor  Am incapsulat:
* Entitatile Consumer, Distributor, Producer in clase bine definite, asupra carora
operez cand e necesar  Polimorfism:
* La crearea instantelor pentru strategii, in functie de tipul cu care se apeleaza

### Design patterns

Factory Pattern:
* Pentru procesul de billing dintre consumatori-distribuitori
* Pentru crearea strategiilor  Observer Pattern:
* Observerul meu este instanta clasei Handler, observable este instanta clasei
SubjectObservable -> folosit cu scopul actualizarii producatorilor si retinerii
lor intr-o clasa  Singleton Pattern:
* Instantierea clasei Factory (pentru Billing)  Strategy Pattern:
* Implementarea diferita a strategiilor distribuitorilor