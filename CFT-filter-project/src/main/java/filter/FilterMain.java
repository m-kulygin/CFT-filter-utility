package filter;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterDescription;
import output_writers.OutputFloatWriter;
import output_writers.OutputIntegerWriter;
import output_writers.OutputStringWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class FilterMain {

    private static final String INTEGER_FILE_DEFAULT_NAME = "integers.txt";
    private static final String FLOAT_FILE_DEFAULT_NAME = "floats.txt";
    private static final String STRING_FILE_DEFAULT_NAME = "strings.txt";

    private final JCommander filterCmd;
    private FilterStats stats;

    private final List<String> validInputFiles;
    private final String prefix;
    private final String outputPath;
    private final boolean addModeEnabled;
    private final boolean shortStatsModeEnabled;
    private final boolean fullStatsModeEnabled;

    private String fullIntegerFileName;
    private String fullFloatFileName;
    private String fullStringFileName;

    public FilterMain(String[] args) throws IOException {
        FilterArgs jArgs = new FilterArgs();
        filterCmd = JCommander.newBuilder().addObject(jArgs).build();
        try {
            filterCmd.parse(args);
        } catch (Exception e) {
            System.err.println("Args parsing error: " + e.getMessage());
        }

        validInputFiles = FilterArgsChecker.checkAndFillValidInputFiles(jArgs.getInputFiles());

        prefix = jArgs.getPrefix();
        outputPath = jArgs.getOutput();
        addModeEnabled = jArgs.addModeEnabled();
        shortStatsModeEnabled = jArgs.shortStatsModeEnabled();
        fullStatsModeEnabled = jArgs.fullStatsModeEnabled();

        fullIntegerFileName = INTEGER_FILE_DEFAULT_NAME;
        fullFloatFileName = FLOAT_FILE_DEFAULT_NAME;
        fullStringFileName = STRING_FILE_DEFAULT_NAME;
        parsePrefixAndUpdateOutputFilenames();
        parseOutputPathAndUpdateOutputFilenames();

        fullIntegerFileName = makeOutputFileNameDefaultIfNeeded(fullIntegerFileName, INTEGER_FILE_DEFAULT_NAME);
        fullFloatFileName = makeOutputFileNameDefaultIfNeeded(fullFloatFileName, FLOAT_FILE_DEFAULT_NAME);
        fullStringFileName = makeOutputFileNameDefaultIfNeeded(fullStringFileName, STRING_FILE_DEFAULT_NAME);
    }

    public void doFilter() {
        stats = new FilterStats();
        try (
                OutputIntegerWriter integerWriter = new OutputIntegerWriter(fullIntegerFileName, addModeEnabled);
                OutputFloatWriter floatWriter = new OutputFloatWriter(fullFloatFileName, addModeEnabled);
                OutputStringWriter stringWriter = new OutputStringWriter(fullStringFileName, addModeEnabled)
        ) {
            for (String inputFile : validInputFiles) {
                try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        filterAndWriteLine(line, integerWriter, floatWriter, stringWriter);
                    }
                } catch (IOException e) {
                    System.err.println("Error while reading input file: " + inputFile);
                }
            }
        } catch (Exception e) {
            System.err.println("Error while writing to output: " + e.getMessage());
        }
    }

    public String getResultStatsString() {
        if (fullStatsModeEnabled)
            return stats.getFullStats();
        else if (shortStatsModeEnabled)
            return stats.getShortStats();
        else
            return "";
    }

    private void filterAndWriteLine(String line,
                                    OutputIntegerWriter integerWriter,
                                    OutputFloatWriter floatWriter,
                                    OutputStringWriter stringWriter) {
        String currentOutputFileName = "";
        try {
            if (line.matches("[+-]?\\d+")) { // целые
                currentOutputFileName = fullIntegerFileName;
                BigInteger num = new BigInteger(line);
                if (fullStatsModeEnabled)
                    stats.updateIntegerFullStats(num);
                integerWriter.writeIntegerToFile(line);
                stats.integerCount++;
            } else if (line.matches("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][+-]?\\d+)?")) { // флоат
                currentOutputFileName = fullFloatFileName;
                BigDecimal num = new BigDecimal(line);
                if (fullStatsModeEnabled)
                    stats.updateFloatFullStats(num);
                floatWriter.writeFloatToFile(line);
                stats.floatCount++;
            } else { // строки
                currentOutputFileName = fullStringFileName;
                if (fullStatsModeEnabled)
                    stats.updateStringFullStats(line);
                stringWriter.writeStringToFile(line);
                stats.stringCount++;
            }
        } catch (Exception e) {
            System.err.println("Error while writing to output: " + e.getMessage());
            System.err.println("Line: " + line);
            System.err.println("Output file: " + currentOutputFileName);
        }

    }

    private void parsePrefixAndUpdateOutputFilenames() {
        Optional<ParameterDescription> prefixParam = filterCmd.getParameters().stream()
                .filter(p -> p.getNames().equals("-p"))
                .findFirst();
        if ((prefixParam.isPresent()) && (prefixParam.get().isAssigned())) {
            if (FilterArgsChecker.isValidPrefix(prefix)) {
                fullIntegerFileName = prefix + fullIntegerFileName;
                fullFloatFileName = prefix + fullFloatFileName;
                fullStringFileName = prefix + fullStringFileName;
            } else {
                System.err.println("Invalid prefix. Using empty now. Should be a non-blank correct file prefix, but found: " + prefix);
            }
        }
    }

    private void parseOutputPathAndUpdateOutputFilenames() {
        Optional<ParameterDescription> outputPathParam = filterCmd.getParameters().stream()
                .filter(p -> p.getNames().equals("-o"))
                .findFirst();
        if ((outputPathParam.isPresent()) && (outputPathParam.get().isAssigned())) {
            if (FilterArgsChecker.isValidOutputPath(outputPath)) {
                String actualAbsolutePath = new File(outputPath).getAbsolutePath() + File.separator;
                fullIntegerFileName = actualAbsolutePath + fullIntegerFileName;
                fullFloatFileName = actualAbsolutePath + fullFloatFileName;
                fullStringFileName = actualAbsolutePath + fullStringFileName;
            } else {
                System.err.println("Invalid output path. Using default now. Should be a correct existing path, " +
                        "representing a writable directory, but found: " + outputPath);
            }
        }
    }

    private String makeOutputFileNameDefaultIfNeeded(String outputFilePath, String defaultFilePath)
            throws IOException {
        String result = outputFilePath;
        if (FilterArgsChecker.isExistingNonWritableOutputFile(result)) {
            System.err.println("Non-writable output file: " + result);
            System.err.println("Attempting to write in default.");
            result = defaultFilePath;
            if (!(new File(System.getProperty("user.dir")).canWrite()) ||
                    (FilterArgsChecker.isExistingNonWritableOutputFile(result))) {
                throw new IOException("Cannot write to default: " + result);
            }
        }
        return result;
    }
}
