import java.io.IOException; // Importuje klasę IOException z pakietu java.io, która obsługuje wyjątki związane z operacjami wejścia/wyjścia.
import java.nio.file.Files; // Importuje klasę Files z pakietu java.nio.file, która zawiera metody do operacji na plikach.
import java.nio.file.Path; // Importuje klasę Path z pakietu java.nio.file, która reprezentuje ścieżkę pliku lub katalogu.
import java.util.HashMap; // Importuje klasę HashMap z pakietu java.util, która implementuje interfejs Map w oparciu o tablicę haszową.
import java.util.Map; // Importuje interfejs Map z pakietu java.util, który reprezentuje mapowanie kluczy na wartości.
import java.util.stream.Stream; // Importuje klasę Stream z pakietu java.util.stream, która udostępnia sekwencję elementów z różnych źródeł i wspiera różne rodzaje operacji na danych sekwencyjnych i równoległych.

public class ICDCodeTabularOptimizedForTime implements ICDCodeTabular { // Deklaracja klasy ICDCodeTabularOptimizedForTime implementującej interfejs ICDCodeTabular.

    private Map<String, String> codeDescriptionMap = new HashMap<>(); // Inicjalizacja prywatnego pola codeDescriptionMap jako obiektu HashMap przechowującego kody i opisy.

    public ICDCodeTabularOptimizedForTime(Path path){ // Deklaracja konstruktora przyjmującego obiekt typu Path.
        try( // Rozpoczęcie bloku try-with-resources, który automatycznie zamyka zasoby po jego zakończeniu.
             Stream<String> lines = Files.lines(path); // Inicjalizacja strumienia linii z pliku o ścieżce path.
        ){
            lines.skip(88) // Pominięcie pierwszych 88 linii w strumieniu.
                    .map(String::trim) // Usunięcie białych znaków z każdej linii.
                    .filter(s -> s.matches("[A-Z][0-9]{2}.*")) // Filtrowanie linii pasujących do wzorca [A-Z][0-9]{2}.*, czyli zaczynających się wielką literą, a następnie dwiema cyframi.
                    .map(s -> s.split(" ", 2)) // Podział każdej linii na maksymalnie 2 części, oddzielone spacją.
                    .forEach(strings -> // Iteracja przez każdy podział linii.
                            codeDescriptionMap.put(strings[0], strings[1]) // Dodanie klucza i wartości do mapy.
                    );
        }
        catch(IOException e){ // Obsługa wyjątku IOException.
            throw new RuntimeException(); // Rzucenie nowego wyjątku RuntimeException w przypadku wystąpienia błędu wejścia/wyjścia.
        }
    }

    @Override
    public String getDescription(String code) { // Implementacja metody getDescription z interfejsu ICDCodeTabular.
        return codeDescriptionMap.getOrDefault(code, "?"); // Zwraca opis kodu z mapy lub "?" jeśli klucz nie istnieje.
    }
}
