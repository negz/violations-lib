package se.bjurr.violations.lib;

import static org.assertj.core.api.Assertions.assertThat;
import static se.bjurr.violations.lib.TestUtils.getRootFolder;
import static se.bjurr.violations.lib.ViolationsApi.violationsApi;
import static se.bjurr.violations.lib.model.SEVERITY.INFO;
import static se.bjurr.violations.lib.reports.Parser.PYLINT;

import java.util.List;
import org.junit.Test;
import se.bjurr.violations.lib.model.Violation;

public class PyLintTest {

  @Test
  public void testThatViolationsCanBeParsed() {
    final String rootFolder = getRootFolder();

    final List<Violation> actual =
        violationsApi() //
            .withPattern(".*/pylint/.*\\.txt$") //
            .inFolder(rootFolder) //
            .findAll(PYLINT) //
            .violations();

    assertThat(actual) //
        .hasSize(136);

    Violation violation = actual.get(0);
    assertThat(violation.getFile()) //
        .isEqualTo("marshmallow/schema.py");
    assertThat(violation.getStartLine()) //
        .isEqualTo(190);
    assertThat(violation.getMessage()) //
        .isEqualTo("Wrong continued indentation (add 1 space).");
    assertThat(violation.getSeverity()) //
        .isEqualTo(INFO);
    assertThat(violation.getRule()) //
        .isEqualTo("C0330(bad-continuation)");
  }
}
