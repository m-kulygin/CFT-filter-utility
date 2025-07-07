package filter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface FilterArgsChecker {

    Character[] PREFIX_ILLEGAL_CHARACTERS =
            {'/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};

    static List<String> checkAndFillValidInputFiles(List<String> inputFileNames) throws IOException {
        if (inputFileNames.isEmpty()) {
            throw new IOException("ERROR: Input files must contain at least one valid file.");
        }
        List<String> validInputFileNames = new ArrayList<>();
        for (String inputFileName : inputFileNames) {
            File file = new File(inputFileName);
            if ((file.exists()) && (file.isFile()) && (file.canRead())) {
                validInputFileNames.add(inputFileName);
            } else {
                System.err.println("Invalid input file: " + inputFileName);
            }
        }
        if (validInputFileNames.isEmpty()) {
            throw new IOException("ERROR: Input files must contain at least one valid file.");
        }
        return validInputFileNames;
    }

    static boolean isValidPrefix(String prefix) {
        return (prefix != null) && (!prefix.isBlank())
                && (Arrays.stream(PREFIX_ILLEGAL_CHARACTERS).noneMatch(ch -> prefix.contains(ch.toString())));
    }

    static boolean isValidOutputPath(String outputPath) {
        if (outputPath == null) {
            return false;
        }
        File file = new File(outputPath);
        return file.isDirectory() && file.canWrite();
    }

    static boolean isExistingNonWritableOutputFile(String outputFilePath) {
        if (outputFilePath == null) {
            return false;
        }
        File file = new File(outputFilePath);
        return (file.exists()) && (file.isFile()) && !(file.canWrite());
    }

}
