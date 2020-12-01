# Generator plików ics
## Api zwraca pliki ics zawierające wydarzenia z kalendarza WEEIA ze strony: http://www.weeia.p.lodz.pl/

Przykładowe zapytania
Wymagane parametry
	miesiąc - miesiąc dla którego mają być pobrane wydarzenia URL Wymagany: month=[string]
	rok rok dla którego mają być pobrane wydarzenia -   URL Params Required: year=[string]
## adres URL /ics/
### Metoda:GET
###	Przykład:
####	http://localhost:8080/ics?year=2020&month=03

### Zwrócony plik: 
102020calendar.ics
Zawierający wszystkie wydarzenia z wyżej wymienonego miesiąca i roku.
