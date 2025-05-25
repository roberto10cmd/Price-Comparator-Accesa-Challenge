# Price Comparator – Accesa Challenge

Aplicație web dezvoltată în Java & Spring Boot, care permite compararea prețurilor produselor alimentare între diferite magazine, optimizarea coșului de cumpărături și notificarea utilizatorilor când prețurile scad sub un prag dorit.

---

## Funcționalități ( Cerinte ) 

-  Populare baza de date cu informatii din CSV uri
-  Optimizarea cosului pentru cel mai bun pret al fiecarui produs
-  Filtrare preturi pe produs in functie de magazin si reduceri aplicabile 
-  Trimitere alerte de pret trimise userelui cand un produs scade sub limita setata de el
-  Analiza nutritionala pentru produsele din coș: grăsimi, zahăr, proteine
-  Recomandari per produs (cel mai bun magazin + detalii preț per unitate)

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

### 1. Clonează proiectul
```bash
git clone https://github.com/roberto10cmd/Price-Comparator-Accesa-Challenge.git
cd Price-Comparator-Accesa-Challenge
