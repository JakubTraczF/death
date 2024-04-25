
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class DeathCauseStatisticsList {

    List<DeathCauseStatistics> stats;

    public void repopulate(Path path){ // Metoda repopulate, która służy do ponownego wypełnienia listy stats na podstawie danych z pliku CSV.
        try( // Rozpoczęcie bloku try-with-resources, który automatycznie zamyka zasoby po jego zakończeniu.
             Stream<String> fileLines = Files.lines(path); // Inicjalizacja strumienia fileLines z liniami odczytanymi z pliku o ścieżce path.
        ) {
            stats = fileLines.skip(2) // Pominięcie pierwszych dwóch linii, które mogą zawierać nagłówki lub inne dane.
                    .map(DeathCauseStatistics::fromCsvLine) // Mapowanie każdej linii na obiekt DeathCauseStatistics za pomocą metody fromCsvLine.
                    .toList(); // Konwersja przetworzonego strumienia na listę i przypisanie jej do pola stats.

        } catch (IOException e) { // Obsługa wyjątku IOException, który może wystąpić podczas operacji na pliku.
            throw new RuntimeException(e); // Rzucenie nowego wyjątku RuntimeException z przyczyną e (oryginalny wyjątek).
        }
    }


    public List<DeathCauseStatistics> mostDeadlyDiseases(int age, int n)
    {
        /*List<DeathCauseStatistics> results = new ArrayList<>();
        for(DeathCauseStatistics currentStat : stats){
            results.add(currentStat.getDeathsForAge(age));
        }*/
        List<DeathCauseStatistics> results = new ArrayList<>(stats);
        results.sort((disease1, disease2)->Integer.compare(
                disease1.getDeathsForAge(age).deathCount(),
                disease2.getDeathsForAge(age).deathCount()
        ));
        return results.reversed().subList(0, n);
    }

    public List<DeathCauseStatistics> getStats() {
        return stats;
    }
}
