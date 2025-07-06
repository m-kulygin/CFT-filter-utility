import com.beust.jcommander.JCommander;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // НАВЕСИТЬ ТРАЙ КЕТЧ, ЕСЛИ ПРОБЛЕМА С АРГУМЕНТАМИ И ВАЛИДАЦИЕЙ
        CFTFilterArgs jArgs = new CFTFilterArgs();
        JCommander filterCmd = JCommander.newBuilder()
                .addObject(jArgs)
                .build();
        try {
            // разобраться с дефолтными значениями, если не удалось распарсить
            filterCmd.parse(args);
        } catch (Exception e) {}

        System.out.println();
        System.out.println("Input files: " + jArgs.getInputFiles());
        System.out.println("Prefix: " + jArgs.getPrefix());
        System.out.println("Output path: " + jArgs.getOutput());
        System.out.println("Adding mode enabled: " + jArgs.addModeEnabled()); // +
        System.out.println("Short stats enabled: " + jArgs.shortStatsModeEnabled()); // +
        System.out.println("Full stats enabled: " + jArgs.fullStatsModeEnabled()); // +
        System.out.println();

        String fullIntegerFileName = jArgs.getPrefix() != null ? jArgs.getPrefix() + "integers.txt" : "integers.txt";
        String fullFloatFileName = jArgs.getPrefix() != null ? jArgs.getPrefix() + "floats.txt" : "floats.txt";
        String fullStringFileName = jArgs.getPrefix() != null ? jArgs.getPrefix() + "strings.txt" : "strings.txt";
        String actualAbsolutePath = new File(jArgs.getOutput()).getAbsolutePath() + File.separator;
        fullIntegerFileName = actualAbsolutePath + fullIntegerFileName;
        fullFloatFileName = actualAbsolutePath + fullFloatFileName;
        fullStringFileName = actualAbsolutePath + fullStringFileName;
        // ?????? писать в дефолт????


        List<String> validInputFiles = new ArrayList<>();
        for (String name : jArgs.getInputFiles()) {
            File file = new File(name);
            if ((file.exists()) && (file.isFile()) && (file.canRead())) {
                validInputFiles.add(name);
            } else {
                System.out.println("Input file " + name + " not found");
            }
        }
        if (validInputFiles.isEmpty()) {
            System.out.println("No valid input files found. Terminating utility work. " +
                    "Please specify at least one valid input file.");
            return;
        }

        FilterStats stats = new FilterStats();

        // ВРОДЕ БЫ на этом этапе у нас корректные префикс и выходной путь. надо разобраться,
        // существуют или нет такие выходные файлы, и можем ли мы в них писать.
        // ЕСЛИ ПРОБЛЕМА С КАСТОМНЫМИ ИМЕНАМИ - ПЫТАЕМСЯ ПИСАТЬ В ДЕФОЛТ
//        FileWriter integerWriter = new FileWriter(  // можно ДОБАВЛЯТЬ в файл, применив в конструкторе append
//                fullIntegerFileName);
//        FileWriter floatWriter = new FileWriter(  // можно ДОБАВЛЯТЬ в файл, применив в конструкторе append
//                fullFloatFileName);
//        FileWriter stringWriter = new FileWriter(  // можно ДОБАВЛЯТЬ в файл, применив в конструкторе append
//                fullStringFileName)


        try (
                OutputIntegerWriter integerWriter = new OutputIntegerWriter(fullIntegerFileName, jArgs.addModeEnabled());
                OutputFloatWriter floatWriter = new OutputFloatWriter(fullFloatFileName, jArgs.addModeEnabled());
                OutputStringWriter stringWriter = new OutputStringWriter(fullStringFileName, jArgs.addModeEnabled());
        ) {
            for (String inputFile : validInputFiles) {
                try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // НУЖНО УЧЕСТЬ NFE/ARIPHM И ДРУГИЕ. ЕСЛИ ВЫЛЕТАЕТ - СКИПАЕМ ТОЛЬКО ТЕКУЩУЮ СТРОКУ.
                        if (line.matches("[+-]?\\d+")) { // целые
                            BigInteger num = new BigInteger(line);
                            if (jArgs.fullStatsModeEnabled())
                                stats.updateIntegerFullStats(num);
                            try {
                                integerWriter.writeIntegerToFile(line);
                                stats.integerCount++;
                            } catch (IOException e) {
                                System.out.println("Error while writing to file: " + inputFile);
                            }

                        } else if (line.matches("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][+-]?\\d+)?")) { // флоат
                            BigDecimal num = new BigDecimal(line);
                            if (jArgs.fullStatsModeEnabled())
                                stats.updateFloatFullStats(num);
                            try {
                                floatWriter.writeFloatToFile(line);
                                stats.floatCount++;
                            } catch (IOException e) {
                                System.out.println("Error while writing to file: " + inputFile);
                            }

                        } else { // строки
                            if (jArgs.fullStatsModeEnabled())
                                stats.updateStringFullStats(line);
                            try {
                                stringWriter.writeStringToFile(line);
                                stats.stringCount++;
                            } catch (IOException e) {
                                System.out.println("Error while writing to file: " + inputFile);
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error while reading input file: " + inputFile);
                }
            }
        } catch (Exception e) {
            System.err.println("Error while writing to output: " + e.getMessage());
        } finally {
            if (jArgs.fullStatsModeEnabled()) {
                stats.countIntegerAverage();
                stats.countFloatAverage();
                System.out.println(stats.getFullStats());
            } else if (jArgs.shortStatsModeEnabled()) {
                System.out.println(stats.getShortStats());
            }
        }
        System.out.println("Filter worked successfully");

    }


}