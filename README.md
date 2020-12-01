# Generator plików ics
## Api zwraca pliki ics zawierające wydarzenia z kalendarza WEEIA ze strony: http://www.weeia.p.lodz.pl/

Opis Api
## adres URL /ics/
### Metoda:GET
####     Parametr:year (String) - miesiąc dla którego mają być pobrane wydarzenia.
####     Parametr:month (String) - dla którego mają być pobrane wydarzenia.

# Przykładowe zapytania
####	http://localhost:8080/ics?year=2020&month=03
### Zwrócony plik we formacie ics: 
#### 102020calendar.ics
#### W odpowiedzi został zwróony plik ics z informcjami o wydarzeniach z marca 2020 roku

####	http://localhost:8080/ics?year=2019&month=01
### Zwrócony plik: 
#### 102020calendar.ics
#### W odpowiedzi został zwróony plik ics z informcjami o wydarzeniach z lutego 2019 roku
