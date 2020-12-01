# Generator plików ics
## Api zwraca pliki ics zawierające wydarzenia z kalendarza WEEIA ze strony: http://www.weeia.p.lodz.pl/

Przykładowe zapytania
Wymagane parametry
### miesiąc(month) - miesiąc dla którego mają być pobrane wydarzenia.
#### rok(year) dla którego mają być pobrane wydarzenia.

## adres URL /ics/
### Metoda:GET
###	Przykład:
####     Parametr:year (String)
####     Parametr:month (String)
####	http://localhost:8080/ics?year=2020&month=03

### Zwrócony plik: 
102020calendar.ics
Zawierający wszystkie wydarzenia z wyżej wymienonego miesiąca i roku.
