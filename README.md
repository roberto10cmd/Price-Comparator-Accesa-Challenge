# Price Comparator – Accesa Challenge

Aplicație web dezvoltată în Java & Spring Boot, care permite compararea prețurilor produselor alimentare între diferite magazine, optimizarea coșului de cumpărături și notificarea utilizatorilor când prețurile scad sub un prag dorit.

---
## Descriere
Am dezvoltat aceasta aplicatie cu scopul de a oferi o solutie practica, scalabila si extensibila pentru compararea preturilor intre magazine. De la inceput, am gandit arhitectura intr-un mod care sa permita tranzitia usoara spre un sistem bazat pe microservicii, pentru a sustine dezvoltari viitoare mai avansate.

Mi-a facut placere sa lucrez la acest proiect, deoarece functionalitatile sale sunt direct aplicabile in viata reala: optimizarea cumparaturilor, gestionarea reducerilor si alertelor de pret sunt lucruri pe care oricine le experimenteaza ca utilizator.

In plus, am adaugat un feature suplimentar – analiza valorilor nutritionale din cos – din convingerea ca un pret mic nu este suficient daca produsul nu este si sanatos. Astfel, aplicatia nu ofera doar cea mai ieftina varianta, ci si o perspectiva informata asupra calitatii nutritionale a produselor selectate.


---

## Funcționalități ( Cerinte ) 

-  Populare baza de date cu informatii din CSV uri
-  Optimizarea cosului pentru cel mai bun pret al fiecarui produs
-  Filtrare preturi pe produs in functie de magazin si reduceri aplicabile 
-  Trimitere alerte de pret trimise userelui cand un produs scade sub limita setata de el
-  Analiza nutritionala pentru produsele din coș: grăsimi, zahăr, proteine
-  Recomandari per produs (cel mai bun magazin + detalii preț per unitate)
-  Analiza valorilor nutritionale pe cosul de cumparaturi

---

## API Modules

- BasketController – gestioneaza cosurile de cumparaturi: creare, modificare, adaugare/stergere produse, optimizare preturi si analiza nutritionala.

- CsvReaderController – permite importul de produse, reduceri si informatii nutritionale din fisiere CSV.

- DiscountController – gestioneaza reducerile: adaugare, listare, stergere si extragerea celor mai bune oferte disponibile.

- PriceAlertController – gestioneaza alertele de pret: crearea alertelor si declansarea notificarilor in momentul in care un pret importat scade sub tinta definita de utilizator.

- PriceHistoryController – permite gestionarea istoricului de preturi: listare, creare, stergere si filtrare grafica dupa produs, magazin, categorie sau brand.

- ProductController – ofera operatii CRUD pentru produse si afiseaza istoricul de preturi asociat unui produs dat.

- StoreController – gestioneaza magazinele: adaugare, stergere, listare si afisarea produselor disponibile intr-un anumit magazin.

- UserController – gestioneaza utilizatorii aplicatiei: creare cont, listare utilizatori si stergere.

---


## Tehnologii

- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- H2 Database ( for development mode only )
- Lombok
- WebSocket (pentru notificări live)
- Maven

---

##  Cum rulezi aplicația

### 1. Cloneaza proiectul si ruleaza aplicatia
```bash
git clone https://github.com/roberto10cmd/Price-Comparator-Accesa-Challenge.git
cd Price-Comparator-Accesa-Challenge
./mvnw spring-boot:run



