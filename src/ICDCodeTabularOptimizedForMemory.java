import java.io.IOException; // Importuje klasę IOException z pakietu java.io, która obsługuje wyjątki związane z operacjami wejścia/wyjścia.
import java.nio.file.Files; // Importuje klasę Files z pakietu java.nio.file, która zawiera metody do operacji na plikach.
import java.nio.file.Path; // Importuje klasę Path z pakietu java.nio.file, która reprezentuje ścieżkę pliku lub katalogu.
import java.util.stream.Stream; // Importuje klasę Stream z pakietu java.util.stream, która udostępnia sekwencję elementów z różnych źródeł i wspiera różne rodzaje operacji na danych sekwencyjnych i równoległych.

public class ICDCodeTabularOptimizedForMemory implements ICDCodeTabular { // Deklaracja klasy ICDCodeTabularOptimizedForMemory implementującej interfejs ICDCodeTabular.

    Path path; // Deklaracja pola przechowującego ścieżkę do pliku.

    public ICDCodeTabularOptimizedForMemory(Path path){ // Deklaracja konstruktora przyjmującego obiekt typu Path.
        this.path = path; // Przypisanie ścieżki do pola.
    }

    @Override
    public String getDescription(String code) { // Implementacja metody getDescription z interfejsu ICDCodeTabular.
        try( // Rozpoczęcie bloku try-with-resources, który automatycznie zamyka zasoby po jego zakończeniu.
             Stream<String> lines = Files.lines(path); // Inicjalizacja strumienia linii z pliku o ścieżce path.
        ){
            return lines.skip(88) // Pominięcie pierwszych 88 linii w strumieniu.
                    .map(String::trim) // Usunięcie białych znaków z każdej linii.
                    .filter(s -> s.matches("[A-Z][0-9]{2}.*")) // Filtrowanie linii pasujących do wzorca [A-Z][0-9]{2}.*, czyli zaczynających się wielką literą, a następnie dwiema cyframi.
                    .map(s -> s.split(" ", 2)) // Podział każdej linii na maksymalnie 2 części, oddzielone spacją.
                    .filter(strings -> strings[0].equals(code)) // Filtrowanie podziałów linii, które mają pierwszy element równy podanemu kodowi.
                    .findFirst() // Wybranie pierwszego pasującego podziału.
                    .map(strings -> strings[1]) // Wybranie drugiej części podziału, czyli opisu kodu.
                    .orElse("?"); // Jeśli nie znaleziono pasującego opisu, zwróć "?".
        }
        catch(IOException e){ // Obsługa wyjątku IOException.
            throw new RuntimeException(); // Rzucenie nowego wyjątku RuntimeException w przypadku wystąpienia błędu wejścia/wyjścia.
        }
    }
}
