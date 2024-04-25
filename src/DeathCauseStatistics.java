import java.util.Arrays; // Importuje klasę Arrays z pakietu java.util, która zawiera metody do operacji na tablicach.
import java.util.Map; // Importuje klasę Map z pakietu java.util, która reprezentuje mapowanie kluczy na wartości.
import java.util.function.Function; // Importuje klasę Function z pakietu java.util.function, która reprezentuje funkcję, która przyjmuje jeden argument i zwraca wynik.

public class DeathCauseStatistics { // Deklaracja klasy DeathCauseStatistics.

    private String icd10; // Deklaracja pola przechowującego kod ICD-10.
    private int totalDeaths; // Deklaracja pola przechowującego całkowitą liczbę zgonów.
    private int[] stats; // Deklaracja pola przechowującego statystyki zgonów w różnych przedziałach wiekowych.

    public record AgeBracketDeaths(Integer young, Integer old, Integer deathCount){ // Deklaracja rekordu AgeBracketDeaths reprezentującego statystyki zgonów w danym przedziale wiekowym.
        @Override
        public String toString() { // Przesłonięcie metody toString().
            return "AgeBracketDeaths{" + // Zwraca tekstową reprezentację obiektu.
                    "young=" + young + // Dodaje liczbę zgonów w młodszym przedziale wiekowym.
                    ", old=" + old + // Dodaje liczbę zgonów w starszym przedziale wiekowym.
                    ", deathCount=" + deathCount + // Dodaje całkowitą liczbę zgonów w danym przedziale wiekowym.
                    '}';
        }
    }

    public String getIcd10() { // Metoda zwracająca kod ICD-10.
        return icd10; // Zwraca kod ICD-10.
    }

    public static DeathCauseStatistics fromCsvLine(String line) { // Metoda statyczna tworząca obiekt klasy DeathCauseStatistics na podstawie linii z pliku CSV.
        Function<String, Integer> parseIntFromCsv = s -> s.equals("-") ? 0 : Integer.parseInt(s); // Funkcja pomocnicza parsująca liczbę z formatu CSV.
        DeathCauseStatistics result = new DeathCauseStatistics(); // Inicjalizacja nowego obiektu klasy DeathCauseStatistics.
        String[] split = line.split("[ \t]+"); // Podział linii na części przy użyciu spacji lub tabulatorów jako separatorów.
        result.icd10 = split[0]; // Przypisanie kodu ICD-10 z pierwszej części.
        result.stats = new int[20]; // Inicjalizacja tablicy statystyk zgonów na 20 elementów.
        String[] stats = split[1].split(","); // Podział drugiej części na poszczególne statystyki, oddzielone przecinkami.
        result.totalDeaths = parseIntFromCsv.apply(stats[1]); // Parsowanie całkowitej liczby zgonów.
        for (int i = 0; i < 20; i++) // Iteracja po 20 statystykach zgonów.
            result.stats[i] = parseIntFromCsv.apply(stats[i + 2]); // Parsowanie kolejnych statystyk zgonów.
        return result; // Zwraca utworzony obiekt.
    }

    @Override
    public String toString() { // Przesłonięcie metody toString().
        return "DeathCauseStatistics{" + // Zwraca tekstową reprezentację obiektu.
                "icd10='" + icd10 + '\'' + // Dodaje kod ICD-10.
                ", totalDeaths=" + totalDeaths + // Dodaje całkowitą liczbę zgonów.
                ", stats=" + Arrays.toString(stats) + // Dodaje tablicę statystyk zgonów.
                '}';
    }

    public AgeBracketDeaths getDeathsForAge(Integer age){ // Metoda zwracająca statystyki zgonów dla określonego przedziału wiekowego.
        int index = age >= 100 ? stats.length - 1 : age / 5; // Określenie indeksu dla danego wieku w tablicy statystyk.
        return new AgeBracketDeaths( // Zwraca obiekt AgeBracketDeaths z odpowiednimi wartościami.
                index*5, // Dolny zakres przedziału wiekowego.
                index == stats.length-1 ? null : index*5+4, // Górny zakres przedziału wiekowego, lub null jeśli to ostatni przedział.
                stats[index] // Całkowita liczba zgonów w danym przedziale wiekowym.
        );
    }
}
