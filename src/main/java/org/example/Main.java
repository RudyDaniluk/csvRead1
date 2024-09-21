package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        String csvFile = "plik.csv";
        String outputCsvFile = "sorted_data.csv";
        String line;
        int startLine = 10;
        int endLine = 31;
        String[][] data = new String[endLine - startLine + 1][];
        String[] header = {"Nazwisko", "Imię", "Czas udziału w spotkaniu", "Wydane zaświadczenie"};

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            int currentLine = 0;
            int arrayIndex = 0;
            while ((line = br.readLine()) != null) {
                currentLine++;
                if (currentLine >= startLine && currentLine <= endLine) {
                    String[] values = line.split("\t");

                    // Usuń tekst w nawiasach
                    String nameWithoutBrackets = values[0].replaceAll("\\s*\\([^)]*\\)", "").trim();

                    // Rozdziel imię i nazwisko
                    String[] nameParts = nameWithoutBrackets.split(" ", 2);
                    String firstName = nameParts.length > 0 ? nameParts[0] : "Brak";
                    String lastName = nameParts.length > 1 ? nameParts[1] : "Brak";

                    // Pobierz wartość czasu udziału w spotkaniu
                    String participationTime = values.length > 3 ? values[3] : "Brak";

                    // Sprawdź, czy czas udziału w spotkaniu przekracza godzinę
                    boolean issuedCertificate = false;
                    if (participationTime.contains("godz.")) {
                        int hours = Integer.parseInt(participationTime.split(" ")[0]);
                        if (hours > 1) {
                            issuedCertificate = true;
                        }
                    }

                    // Dodaj dane do tablicy
                    String certificateStatus = issuedCertificate ? "Tak" : "Nie";
                    data[arrayIndex++] = new String[]{lastName, firstName, participationTime, certificateStatus};
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Dodaj nagłówek jako pierwszy wiersz
        String[][] fullData = new String[data.length + 1][];
        fullData[0] = header;
        System.arraycopy(data, 0, fullData, 1, data.length);

        // Sortuj dane według nazwiska (pierwsza kolumna)
        Arrays.sort(fullData, 1, fullData.length, Comparator.comparing(row -> row[0]));

        // Sprawdź dane przed usunięciem ostatniego rekordu
        System.out.println("Dane przed usunięciem ostatniego rekordu:");
        for (String[] row : fullData) {
            System.out.println(Arrays.toString(row));
        }

        // Usuń ostatni wiersz (jeśli nie jest pusty)
        if (fullData.length > 1) {
            String[][] trimmedData = Arrays.copyOf(fullData, fullData.length - 1);
            fullData = trimmedData;
        }

        // Sprawdź dane po usunięciu ostatniego rekordu
        System.out.println("Dane po usunięciu ostatniego rekordu:");
        for (String[] row : fullData) {
            System.out.println(Arrays.toString(row));
        }

        // Zapisz posortowane dane do pliku CSV
        try (FileWriter writer = new FileWriter(outputCsvFile)) {
            for (String[] row : fullData) {
                writer.write(String.join(",", row) + "\n");
            }
            System.out.println("Plik CSV został wygenerowany: " + outputCsvFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



