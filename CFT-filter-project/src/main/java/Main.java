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
        filterCmd.parse(args);

        System.out.println();
        System.out.println("Input files: " + jArgs.getInputFiles());
        System.out.println("Prefix: " + jArgs.getPrefix());
        System.out.println("Output path: " + jArgs.getOutput());
        System.out.println("Adding mode enabled: " + jArgs.addModeEnabled());
        System.out.println("Short stats enabled: " + jArgs.shortStatsModeEnabled());
        System.out.println("Full stats enabled: " + jArgs.fullStatsModeEnabled());
        System.out.println();


        BufferedReader[] readers = new BufferedReader[args.length];

        String fullIntegerFileName = jArgs.getPrefix() != null ? jArgs.getPrefix()+"integers.txt" : "integers.txt";
        String fullFloatFileName = jArgs.getPrefix() != null ? jArgs.getPrefix()+"floats.txt" : "floats.txt";
        String fullStringFileName = jArgs.getPrefix() != null ? jArgs.getPrefix()+"strings.txt" : "strings.txt";
        fullIntegerFileName = jArgs.getOutput() != null ? jArgs.getOutput()+fullIntegerFileName : fullIntegerFileName;
        fullFloatFileName = jArgs.getOutput() != null ? jArgs.getOutput()+fullFloatFileName : fullFloatFileName;
        fullStringFileName = jArgs.getOutput() != null ? jArgs.getOutput()+fullStringFileName : fullStringFileName;

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
        FileWriter integerWriter = null;
        FileWriter floatWriter = null;
        FileWriter stringWriter = null;

        // ВРОДЕ БЫ на этом этапе у нас корректные префикс и выходной путь. надо разобраться,
        // существуют или нет такие выходные файлы, и можем ли мы в них писать.
        // ЕСЛИ ПРОБЛЕМА С КАСТОМНЫМИ ИМЕНАМИ - ПЫТАЕМСЯ ПИСАТЬ В ДЕФОЛТ
//        FileWriter integerWriter = new FileWriter(  // можно ДОБАВЛЯТЬ в файл, применив в конструкторе append
//                fullIntegerFileName);
//        FileWriter floatWriter = new FileWriter(  // можно ДОБАВЛЯТЬ в файл, применив в конструкторе append
//                fullFloatFileName);
//        FileWriter stringWriter = new FileWriter(  // можно ДОБАВЛЯТЬ в файл, применив в конструкторе append
//                fullStringFileName)

        for (String inputFile : validInputFiles) {
            try (
                    BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile))
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // НУЖНО УЧЕСТЬ NFE/ARIPHM И ДРУГИЕ. ЕСЛИ ВЫЛЕТАЕТ - СКИПАЕМ ЭТУ СТРОКУ.
                    // НОРМАЛЬНЫЕ ЛИ РЕГУЛЯРКИ? ^$ нужны? хочу чтобы регулярки полностью соответствовали парсингу BigInt/BigDec
                    if (line.matches("^[+-]?\\d+$")) { // целые
                        BigInteger num = new BigInteger(line);
                        stats.updateIntegerFullStats(num);
                        try {
                            if (stats.integerCount == 0) {
                                integerWriter = new FileWriter(fullIntegerFileName);
                            }
                            stats.integerCount++;
                            integerWriter.write(line);
                            integerWriter.write(System.lineSeparator());
                            integerWriter.flush();
                        } catch (IOException e) {
                            System.out.println("Error while writing to file: " + inputFile);
                        }

                    } else if (line.matches("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][+-]?\\d+)?")) { // флоат
                        BigDecimal num = new BigDecimal(line);
                        stats.updateFloatFullStats(num);
                        try {
                            if (stats.floatCount == 0) {
                                floatWriter = new FileWriter(fullFloatFileName);
                            }
                            stats.floatCount++;
                            floatWriter.write(line);
                            floatWriter.write(System.lineSeparator());
                            floatWriter.flush();
                        } catch (IOException e) {
                            System.out.println("Error while writing to file: " + inputFile);
                        }

                    } else { // строки
                        stats.updateStringFullStats(line);
                        try {
                            if (stats.stringCount == 0) {
                                stringWriter = new FileWriter(fullStringFileName);
                            }
                            stats.stringCount++;
                            stringWriter.write(line);
                            stringWriter.write(System.lineSeparator());
                            stringWriter.flush();
                        } catch (IOException e) {
                            System.out.println("Error while writing to file: " + inputFile);
                        }
                    }
                }

            } catch (IOException e) {
                System.err.println("Ошибка при работе с файлами: " + e.getMessage());
            }
        }

        try {
            if (integerWriter != null) {
                integerWriter.close();
            }
        } catch (IOException e) {
            System.out.println("Error while closing file: " + fullIntegerFileName);
        }

        try {
            if (floatWriter != null) {
                floatWriter.close();
            }
        } catch (IOException e) {
            System.out.println("Error while closing file: " + fullFloatFileName);
        }

        try {
            if (stringWriter != null) {
                stringWriter.close();
            }
        } catch (IOException e) {
            System.out.println("Error while closing file: " + fullStringFileName);
        }

        if (jArgs.fullStatsModeEnabled()) {
            stats.countIntegerAverage();
            stats.countFloatAverage();
            System.out.println(stats.getFullStats());
        } else if (jArgs.shortStatsModeEnabled()) {
            System.out.println(stats.getShortStats());
        }
        System.out.println("Filter worked successfully");

    }



}