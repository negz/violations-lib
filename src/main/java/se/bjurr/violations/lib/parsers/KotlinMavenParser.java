package se.bjurr.violations.lib.parsers;

import static java.lang.Integer.parseInt;
import static se.bjurr.violations.lib.model.SEVERITY.ERROR;
import static se.bjurr.violations.lib.model.SEVERITY.INFO;
import static se.bjurr.violations.lib.model.SEVERITY.WARN;
import static se.bjurr.violations.lib.model.Violation.violationBuilder;
import static se.bjurr.violations.lib.reports.Parser.KOTLINMAVEN;
import static se.bjurr.violations.lib.util.ViolationParserUtils.getLines;

import java.util.ArrayList;
import java.util.List;
import se.bjurr.violations.lib.model.SEVERITY;
import se.bjurr.violations.lib.model.Violation;

public class KotlinMavenParser implements ViolationsParser {
  @Override
  public List<Violation> parseReportOutput(final String string) throws Exception {
    List<Violation> violations = new ArrayList<>();
    List<List<String>> partsPerLine =
        getLines(
            string, "\\[(ERROR|WARNING)\\]([^:]*)[^\\d]+?(\\d+?)[^\\d]+?(\\d+?)[^\\)]+?\\)(.*)");
    for (List<String> parts : partsPerLine) {
      String severity = parts.get(1).trim();
      String filename = parts.get(2).trim();
      Integer line = parseInt(parts.get(3));
      Integer column = parseInt(parts.get(4));
      String message = parts.get(5).trim();
      violations.add( //
          violationBuilder() //
              .setParser(KOTLINMAVEN) //
              .setStartLine(line) //
              .setColumn(column) //
              .setFile(filename) //
              .setSeverity(toSeverity(severity)) //
              .setMessage(message) //
              .build() //
          );
    }
    return violations;
  }

  public SEVERITY toSeverity(final String severity) {
    if (severity.equalsIgnoreCase("error")) {
      return ERROR;
    }
    if (severity.equalsIgnoreCase("warning")) {
      return WARN;
    }
    return INFO;
  }
}
