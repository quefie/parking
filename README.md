### Projekt aplikacji webowej do zarządzania parkomatem.

----------

### Aplikacja pozwala na podstawowe funkcjonalności:
  - Dla kierowcy:
      - Start parkometru (naliczania opłaty)  [ POST ] [ /driver/start ]
      - Sprawdzenie kwoty do zapłacenia  [ GET ] [ /driver/{id}/cost ]
      - Stop parkometru [ PUT ] [ /driver/{id}/stop ]
      - Sprawdzenie waluty [ GET ] [ /driver/{id}/currency ]
      
  - Dla obsługi parkomatu
      - Sprawdzenie czy kierowca ma aktywny biler [ GET ] [ /driver/{id}/check ]
      
  - Dla właściciela parkomatu
      - Sprawdzenie zysku za dany dzień [ GET ] [/day/{year}/{month}/{day}/profit ]
      - Sprawdzenie waluty [ GET ] [ /day/{year}/{month}/{day}/currency ]
      
----------      
### TESTS: 
  - [![CircleCI](https://circleci.com/gh/quefie/parking.svg?style=svg)](https://circleci.com/gh/quefie/parking)

----------
### Użyte technologie:

  - Java,
  - Spring MVC,
  - Hibernate,
  - H2 Database,
  - Thye,
  - Project Lombok,
  - jUnit,
  - Mockito,
  - Maven,
  - REST.
