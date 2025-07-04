import com.beust.jcommander.JCommander;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        // НАВЕСИТЬ ТРАЙ КЕТЧ, ЕСЛИ ПРОБЛЕМА С АРГУМЕНТАМИ И ВАЛИДАЦИЕЙ
        CFTFilterArgs jArgs = new CFTFilterArgs();
        JCommander filterCmd = JCommander.newBuilder()
                .addObject(jArgs)
                .build();
        filterCmd.parse(args);

        System.out.println("Input files: " + jArgs.getInputFiles());
        System.out.println("Prefix: " + jArgs.getPrefix());
        System.out.println("Output path: " + jArgs.getOutput());
        System.out.println("Adding mode enabled: " + jArgs.addModeEnabled());
        System.out.println("Short stats enabled: " + jArgs.shortStatsModeEnabled());
        System.out.println("Full stats enabled: " + jArgs.fullStatsModeEnabled());


//        BufferedReader[] readers = new BufferedReader[args.length];
//
//        String fullIntegerFileName = jArgs.getPrefix() != null ? jArgs.getPrefix()+"integers.txt" : "integers.txt";
//        String fullFloatFileName = jArgs.getPrefix() != null ? jArgs.getPrefix()+"floats.txt" : "floats.txt";
//        String fullStringFileName = jArgs.getPrefix() != null ? jArgs.getPrefix()+"strings.txt" : "strings.txt";
//
//        try (
//                BufferedReader reader = Files.newBufferedReader(Paths.get(jArgs.getInputFiles().getFirst()));
//
//                // ЕСЛИ ПРОБЛЕМА С КАСТОМНЫМИ ИМЕНАМИ - ПЫТАЕМСЯ ПИСАТЬ В ДЕФОЛТ
//                FileWriter integerWriter = new FileWriter(  // можно ДОБАВЛЯТЬ в файл, применив в конструкторе append
//                        jArgs.getOutput()+fullIntegerFileName);
//
//                FileWriter floatWriter = new FileWriter(  // можно ДОБАВЛЯТЬ в файл, применив в конструкторе append
//                        jArgs.getOutput()+fullFloatFileName);
//
//                FileWriter stringWriter = new FileWriter(  // можно ДОБАВЛЯТЬ в файл, применив в конструкторе append
//                        jArgs.getOutput()+fullStringFileName)
//        ) {
//            FilterStats stats = new FilterStats();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // НУЖНО УЧЕСТЬ РЕЖИМ СТАТИСТИКИ. ЕСЛИ ЕЁ НЕТ ИЛИ КОРОТКАЯ - НЕ СЧИТАТЬ ЛИШНИЕ ВЕЩИ
//                // НУЖНО УЧЕСТЬ NFE/ARIPHM И ДРУГИЕ. ЕСЛИ ВЫЛЕТАЕТ - СКИПАЕМ ЭТУ СТРОКУ.
//                // НОРМАЛЬНЫЕ ЛИ РЕГУЛЯРКИ? ^$ нужны? хочу чтобы регулярки полность соответствовали парсингу BigInt/BigDec
//                if (line.matches("^[+-]?\\d+$")) { // целые
//                    BigInteger num = new BigInteger(line);
//                    if (stats.integerCount == 0) { // если это первый интегер
//                        stats.minInteger = num;
//                        stats.maxInteger = num;
//                        // сюда засунуть создание/открытие выходного файла
//                    } else {
//                        stats.minInteger = stats.minInteger.min(num);
//                        stats.maxInteger = stats.maxInteger.max(num);
//                    }
//                    stats.integerCount++;
//                    stats.sumInteger = stats.sumInteger.add(num);
//                    integerWriter.write(line);
//                    integerWriter.write(System.lineSeparator());
//                    integerWriter.flush();
//                } else if (line.matches("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][+-]?\\d+)?")) { // флоат
//                    BigDecimal num = new BigDecimal(line);
//                    if (stats.floatCount == 0) { // если это первый флоат
//                        stats.minFloat = num;
//                        stats.maxFloat = num;
//                        // сюда засунуть создание/открытие выходного файла
//                    } else {
//                        stats.minFloat = stats.minFloat.min(num);
//                        stats.maxFloat = stats.maxFloat.max(num);
//                    }
//                    stats.floatCount++;
//                    stats.sumFloat = stats.sumFloat.add(num);
//                    floatWriter.write(line);
//                    floatWriter.write(System.lineSeparator());
//                    floatWriter.flush();
//                } else { // строки
//                    if (stats.stringCount == 0) { // если это первая строка
//                        stats.shortestStringLength = line.length();
//                        stats.longestStringLength = line.length();
//                        // сюда засунуть создание/открытие выходного файла
//                    } else {
//                        if (line.length() < stats.shortestStringLength) {
//                            stats.shortestStringLength = line.length();
//                        }
//                        if (line.length() > stats.longestStringLength) {
//                            stats.longestStringLength = line.length();
//                        }
//                    }
//                    stats.stringCount++;
//                    stringWriter.write(line);
//                    stringWriter.write(System.lineSeparator());
//                    stringWriter.flush();
//
//                }
//            }
//
//            if (stats.integerCount != 0) {
//                stats.averageInteger = new BigDecimal(stats.sumInteger)
//                        .divide(new BigDecimal(stats.integerCount), RoundingMode.HALF_UP);
//            }
//            if (stats.floatCount != 0) {
//                stats.averageFloat = stats.sumFloat
//                        .divide(new BigDecimal(stats.floatCount), RoundingMode.HALF_UP);
//            }
//
//
//
//            if (jArgs.fullStatsModeEnabled()) {
//                System.out.println(stats.getFullStats());
//            } else if (jArgs.shortStatsModeEnabled()) {
//                System.out.println(stats.getShortStats());
//            }
//            System.out.println("Filter worked successfully");
//
//
//        } catch (IOException e) {
//            System.err.println("Ошибка при работе с файлами: " + e.getMessage());
//        }


    }
}