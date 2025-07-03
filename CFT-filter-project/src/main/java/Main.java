import com.beust.jcommander.JCommander;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {


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


        BufferedReader[] readers = new BufferedReader[args.length];

        try (
                BufferedReader reader = Files.newBufferedReader(Paths.get(
                        "C:\\Users\\Maximus\\Desktop\\work\\SHIFT Java\\Task\\CFT-filter-utility\\files\\input\\input.txt"));
                FileWriter integerWriter = new FileWriter(  // можно ДОБАВЛЯТЬ в файл, применив в конструкторе append
                        "C:\\Users\\Maximus\\Desktop\\work\\SHIFT Java\\Task\\CFT-filter-utility\\files\\output\\integers.txt");
                FileWriter floatWriter = new FileWriter(  // можно ДОБАВЛЯТЬ в файл, применив в конструкторе append
                        "C:\\Users\\Maximus\\Desktop\\work\\SHIFT Java\\Task\\CFT-filter-utility\\files\\output\\floats.txt");
                FileWriter stringWriter = new FileWriter(  // можно ДОБАВЛЯТЬ в файл, применив в конструкторе append
                        "C:\\Users\\Maximus\\Desktop\\work\\SHIFT Java\\Task\\CFT-filter-utility\\files\\output\\strings.txt")
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                if (line.matches("^[+-]?\\d+$")) {
                    integerWriter.write(line);
                    integerWriter.write(System.lineSeparator()); // Добавляем перевод строки
                    integerWriter.flush(); // Принудительная запись в файл
                } else if (line.matches("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][+-]?\\d+)?")) {
                    floatWriter.write(line);
                    floatWriter.write(System.lineSeparator()); // Добавляем перевод строки
                    floatWriter.flush(); // Принудительная запись в файл
                } else {
                    stringWriter.write(line);
                    stringWriter.write(System.lineSeparator()); // Добавляем перевод строки
                    stringWriter.flush(); // Принудительная запись в файл
                }

            }
        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлами: " + e.getMessage());
        }


    }
}