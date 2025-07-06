import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class OutputStringWriter implements AutoCloseable {
    private BufferedWriter writer;
    private final String outputFileName;
    private final boolean appendModeEnabled;

    public OutputStringWriter(String outputFileName, boolean appendModeEnabled) {
        writer = null;
        this.outputFileName = outputFileName;
        this.appendModeEnabled = appendModeEnabled;
    }

    public void writeStringToFile(String stringLine) throws IOException {
        if (writer == null) {
            if (appendModeEnabled) {
                writer = Files.newBufferedWriter(
                        Paths.get(outputFileName),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND,
                        StandardOpenOption.WRITE
                );
            } else {
                writer = Files.newBufferedWriter(
                        Paths.get(outputFileName),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.TRUNCATE_EXISTING
                );
            }

        }
        writer.write(stringLine);
        writer.write(System.lineSeparator());
    }

    @Override
    public void close() throws Exception {
        if (writer != null) {
            writer.close();
        }
    }
}
