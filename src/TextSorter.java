import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class TextSorter {

    public static void main(String[] args) {
        if (args.length < 4) {
            printUsage();
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];
        String criterion = args[2];
        int wordIndex = args.length > 3 ? Integer.parseInt(args[3]) : -1;

        try {
            List<String> lines = Files.readAllLines(Paths.get(inputFile));
            Map<String, Long> lineCountMap = lines.stream()
                    .collect(Collectors.groupingBy(line -> line, Collectors.counting()));

            List<String> sortedLines = new ArrayList<>(lineCountMap.keySet());

            switch (criterion) {
                case "alphabetical":
                    sortedLines.sort(Comparator.naturalOrder());
                    break;
                case "length":
                    sortedLines.sort(Comparator.comparingInt(String::length));
                    break;
                case "word":
                    sortedLines.sort(Comparator.comparing(line -> getWordAtIndex(line, wordIndex)));
                    break;
                default:
                    System.out.println("Invalid sorting criterion.");
                    printUsage();
                    return;
            }

            List<String> resultLines = sortedLines.stream()
                    .map(line -> line + " " + lineCountMap.get(line))
                    .collect(Collectors.toList());

            Files.write(Paths.get(outputFile), resultLines);
            System.out.println("Sorting complete. Check the output file: " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getWordAtIndex(String line, int index) {
        String[] words = line.split("\\s+");
        return (index > 0 && index <= words.length) ? words[index - 1] : "";
    }

    private static void printUsage() {
        System.out.println("Использование: java TextSorter <inputFile> <outputFile> <criterion> <wordIndex>");
        System.out.println("Критерий:");
        System.out.println("  alphabetical - Сортировка в алфавитном порядке");
        System.out.println("  length       - Сортивка по длине строки");
        System.out.println("  word         - Сортировка по n-длине (укажите индекс слова в качестве 4-го аргумента)");
    }
}