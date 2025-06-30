import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {


        List<String> lines = new ArrayList<String>();


        try (BufferedReader reader = Files.newBufferedReader(Paths.get(
                "C:\\Users\\Maximus\\Desktop\\work\\SHIFT Java\\Task\\CFT-filter-utility\\files\\input\\input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // можно ДОБАВЛЯТЬ в файл, применив в конструкторе append
        try (FileWriter writer = new FileWriter(
                "C:\\Users\\Maximus\\Desktop\\work\\SHIFT Java\\Task\\CFT-filter-utility\\files\\output\\output.txt")) {
            for (String line : lines) {
                writer.write(line);
                writer.write(System.lineSeparator()); // Добавляем перевод строки
                writer.flush(); // Принудительная запись в файл
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        }



    }
}