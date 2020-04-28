# Retirement strategies, charts

This repository showcases how to use the xcharts library in a Kotlin-Gradle project to create images containing charts.
Charts present
(1) changes in purchasing power of given goods like cash (PLZ/PLN, USD), gold and real estates in Poland, and
(2) to what extend term deposits, government bonds, and hypothetical ETF mWIG40 protect the holder against inflation, also in Poland.
Additional description in polish is added as it might concern polish readers.

## Strategie emerytalne, wykresy 

Poniższe wykresy prezentują zmianę siły nabywczej dóbr takich jak gotówka (PLZ/PLN, USD), złoto i nieruchomości, a także w jakim stopniu lokaty terminowe, obligacje skarbu państwa i hipoteczny ETF mWIG40 chronią posiadacza przed inflacją.
Mogą one stanowić pomoc przy tworzeniu strategii emerytalnej przez osobę mieszkająca w Polsce i zarabiającą w polskim złotym.

### Zmiany siły nabywczej

#### PLN (PLZ)
![Siła nabywcza, PLN (PLZ), 1950 - 2019](charts/pp_pln_plz_total_1950_2019_logY.png)

Złoty polski w okresie od początku 1950 do końca 2019 roku stracił ponad 99,991% swojej pierwotnej wartości.

#### USD
![Siła nabywcza, USD, 1985 - 2019](charts/pp_usd_total_1985_2019.png)

Dolar amerykański w okresie od 1984 do 2019 roku stracił 65,9% swojej pierwotnej wartości.

#### Złoto
![Siła nabywcza, Złoto, 1985 - 2019](charts/pp_gold_total_1985_2019.png)

Złoto w okresie od 1984 do 2019 roku zyskało 36.37% swojej pierwotnej wartości.

#### Nieruchomości
![Siła nabywcza, mieszkanie, 1966 - 2019](charts/pp_flat_total_1966_2019.png)

Nieruchomości w okresie od 1965 do 2019 roku zyskały 211,97% swojej pierwotnej wartości.

### Ochrona przed inflacją

#### Indeks giełdowy najlepiej odwzorowujący cały rynek (i.e. WIG)
![WIG / WIG20 / mWIG40 / sWIG80](charts/wig_compare_1998_2020.png)

W okresie od 1998 do 2019 roku suma odległości pomiędzy znormalizowanymi kursami zamknięcia w ostatnim dniu każdego miesiąca WIGu, a indeksami WIG20, mWIG40 i sWIG80 jest najmniejsza dla mWIG40 - najlepiej odwzorowywał on ruch całego rynku.
Na tej podstawie bazą hipotetycznego ETFa, który zostanie porównany do „inwestowania” w lokaty i obligacje skarbu państwa będzie mWIG40.

#### Lokaty vs obligacje skarbowe vs mWIG40 ETF
![Lokaty vs obligacje skarbowe vs mWIG40 ETF](charts/investment_strategies_compare_2005_2020.png)

Wykres porównuje 3 strategie inwestycyjne w okresie od początku 2005 do początku 2020 roku.
Pierwsza, „lokata” - zakłada, że w pierwszym miesiącu 2005 roku inwestor założył roczną lokatę o oprocentowaniu równym średniemu podanemu przez NBP, po roku zapłacił podatek, a kapitał wraz z odsetkami zainwestował ponownie w kolejną lokatę na tych samych warunkach.
Druga, „obligacje skarbowe” – zakłada, że w pierwszym miesiącu 2005 roku zakupił dziesięcioletnie obligacje skarbu państwa, w 2015 roku zapłacił podatek, a kapitał wraz z odsetkami zainwestował w kolejne obligacje dziesięcioletnie.
Dla większej przejrzystości wyniki inwestycji w każdym roku (nie tylko w 2015) są obniżone o wartość podatku od zysków kapitałowych.
Trzecia, „ETF” – zakłada, że po cenie zamknięcia w ostatnim dniu notowań w 2004 roku inwestor zakupił ETF na mWIG40.
Wykres przedstawia wartość przy sprzedaży ETFa w kolejnych latach po cenie zamknięcia w ostatnim dniu notowań danego roku i uwzględniając podatek od zysków kapitałowych (o ile osiągnął zysk).
Na początku 2020 strategia „lokata” przyniosła - 55.08%, „obligacje skarbowe” - 78.66%, „ETF” - 101,97% zysku, przy całkowitej inflacji wynoszącej 33.29%.


### Uwagi
1. Dość krótki okres czasu za który dane są dostępne, może utrudniać wnioskowanie na ich podstawie.
2. Ciche założenie, że koszyk inflacyjny w sposób obiektywny wyraża wartość jest dyskusyjne.
3. Dostęp do dóbr takich jak dolar amerykański czy złoto w okresie Polski Ludowej był utrudniony, a udostępnione dane nie odzwierciedlają cen „nieoficjalnych”.
4. Dane na podstawie których postały wyliczenia (a następnie wykresy) mogą zawierać błędy.
5. Wyliczania mogą zawierać błędy.