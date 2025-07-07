import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterDescription;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        CFTFilterArgs jArgs = new CFTFilterArgs();
        JCommander filterCmd = JCommander.newBuilder()
                .addObject(jArgs)
                .build();
        try {
            filterCmd.parse(args);
        } catch (Exception e) {
            System.out.println("Args parsing error: " + e.getMessage());
        }

//        System.out.println();
//        System.out.println("Input files: " + jArgs.getInputFiles()); +
//        System.out.println("Prefix: " + jArgs.getPrefix()); +
//        System.out.println("Output path: " + jArgs.getOutput()); +
//        System.out.println("Adding mode enabled: " + jArgs.addModeEnabled()); // +
//        System.out.println("Short stats enabled: " + jArgs.shortStatsModeEnabled()); // +
//        System.out.println("Full stats enabled: " + jArgs.fullStatsModeEnabled()); // +
//        System.out.println();


        List<String> validInputFiles;
        try {
            validInputFiles = FilterArgsChecker.checkAndFillValidInputFiles(jArgs.getInputFiles());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Terminating utility work.");
            return;
        }

        List<ParameterDescription> parsedParams = filterCmd.getParameters();
        for (var p : parsedParams)
            System.out.println(p.getNames() + " " + p.isAssigned());
        String fullIntegerFileName = "integers.txt";
        String fullFloatFileName = "floats.txt";
        String fullStringFileName = "strings.txt";

        Optional<ParameterDescription> prefixParam = parsedParams.stream()
                .filter(p -> p.getNames().equals("-p"))
                .findFirst();
        if ((prefixParam.isPresent()) && (prefixParam.get().isAssigned())) {
            if (FilterArgsChecker.isValidPrefix(jArgs.getPrefix())) {
                fullIntegerFileName = jArgs.getPrefix() + fullIntegerFileName;
                fullFloatFileName = jArgs.getPrefix() + fullFloatFileName;
                fullStringFileName = jArgs.getPrefix() + fullStringFileName;
            } else {
                System.out.println("Invalid prefix. Should be a non-blank correct file prefix, but found: " + jArgs.getPrefix());
            }
        }

        Optional<ParameterDescription> outputPathParam = parsedParams.stream()
                .filter(p -> p.getNames().equals("-o"))
                .findFirst();
        if ((outputPathParam.isPresent()) && (outputPathParam.get().isAssigned())) {
            if (FilterArgsChecker.isValidOutputPath(jArgs.getOutput())) {
                String actualAbsolutePath = new File(jArgs.getOutput()).getAbsolutePath() + File.separator;
                fullIntegerFileName = actualAbsolutePath + fullIntegerFileName;
                fullFloatFileName = actualAbsolutePath + fullFloatFileName;
                fullStringFileName = actualAbsolutePath + fullStringFileName;
            } else {
                System.out.println("Invalid output path. Should be a correct existing path," +
                        "representing a writable directory, but found: " + jArgs.getOutput());
            }
        }

        try {
            fullIntegerFileName = makeOutputFileNameDefaultIfNeeded(fullIntegerFileName, "integers.txt");
            fullFloatFileName = makeOutputFileNameDefaultIfNeeded(fullFloatFileName, "floats.txt");
            fullStringFileName = makeOutputFileNameDefaultIfNeeded(fullStringFileName, "strings.txt");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        FilterStats stats = new FilterStats();
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
                                System.out.println("Error while writing to file: " + fullIntegerFileName);
                            }

                        } else if (line.matches("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][+-]?\\d+)?")) { // флоат
                            BigDecimal num = new BigDecimal(line);
                            if (jArgs.fullStatsModeEnabled())
                                stats.updateFloatFullStats(num);
                            try {
                                floatWriter.writeFloatToFile(line);
                                stats.floatCount++;
                            } catch (IOException e) {
                                System.out.println("Error while writing to file: " + fullFloatFileName);
                            }

                        } else { // строки
                            if (jArgs.fullStatsModeEnabled())
                                stats.updateStringFullStats(line);
                            try {
                                stringWriter.writeStringToFile(line);
                                stats.stringCount++;
                            } catch (IOException e) {
                                System.out.println("Error while writing to file: " + fullStringFileName);
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
            // !!!
        }
        System.out.println("Filter worked successfully");

    }


    private static String makeOutputFileNameDefaultIfNeeded(String outputFilePath, String defaultFilePath)
            throws IOException {
        String result = outputFilePath;
        if (FilterArgsChecker.isExistingNonWritableOutputFile(result)) {
            System.out.println("Non-writable output file: " + result);
            System.out.println("Attempting to write in default.");
            result = defaultFilePath;
            if (FilterArgsChecker.isExistingNonWritableOutputFile(result)) {
                throw new IOException("Cannot write to default: " + result + "\nTerminating utility work.");
            }
        }
        return result;
    }
}