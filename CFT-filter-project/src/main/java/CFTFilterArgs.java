import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CFTFilterArgs {

    @Parameter(
            names = "-p",
            description = "File name prefix",
            required = false,
            arity = 1,
            validateWith = PrefixValidator.class
    )
    private String prefix;

    @Parameter(
            names = "-o",
            description = "Output files path",
            required = false,
            arity = 1,
            validateWith = OutputPathValidator.class
    )
    private String output;

    @Parameter(
            names = "-a",
            description = "Enable adding (not rewriting) for output",
            required = false,
            arity = 0
    )
    private boolean addMode;

    @Parameter(
            names = "-s",
            description = "Enable short stats mode",
            required = false,
            arity = 0
    )
    private boolean shortStatsMode;

    @Parameter(
            names = "-f",
            description = "Enable full stats mode",
            required = false,
            arity = 0
    )
    private boolean fullStatsMode;

    @Parameter(
            description = "Input files' names",
            required = true
    )
    private List<String> inputFiles = new ArrayList<>();


    public String getPrefix() {
        return prefix;
    }

    public String getOutput() {
        return output;
    }

    public boolean addModeEnabled() {
        return addMode;
    }

    public boolean shortStatsModeEnabled() {
        return shortStatsMode;
    }

    public boolean fullStatsModeEnabled() {
        return fullStatsMode;
    }

    public List<String> getInputFiles() {
        return inputFiles;
    }


    public static class OutputPathValidator implements IParameterValidator {
        @Override
        public void validate(String name, String value) throws ParameterException {
//            System.out.println("OUTPUT PATH VALIDATING: " + value);
            File file = new File(value);
            if (!(file.exists() && file.isDirectory())) {
                throw new ParameterException("Parameter " + name + " should be a correct path (found " + value + ")");
            }
        }
    }

    public static class PrefixValidator implements IParameterValidator {
        private static final Character[] ILLEGAL_CHARACTERS =
                {'/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};

        @Override
        public void validate(String name, String value) throws ParameterException {
//            System.out.println("PREFIX VALIDATING: " + value);
            if ((value == null) || (value.isBlank())
                    || (Arrays.stream(ILLEGAL_CHARACTERS).anyMatch(ch -> value.contains(ch.toString())))) {
                throw new ParameterException("Parameter " + name + " should be a non-blank correct prefix (found " + value + ")");
            }
        }
    }
}
