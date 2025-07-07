package filter;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class FilterArgs {

    @Parameter(
            description = "Input files' names",
            required = true
    )
    private List<String> inputFiles = new ArrayList<>();

    @Parameter(
            names = "-p",
            description = "File name prefix",
            required = false,
            arity = 1
    )
    private String prefix;

    @Parameter(
            names = "-o",
            description = "Output files path",
            required = false,
            arity = 1
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


}
