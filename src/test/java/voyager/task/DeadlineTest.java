package voyager.task;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeadlineTest {
    @Test
    public void testToString_validDate_success() {
        Deadline d = new Deadline("submit report", LocalDate.parse("2026-12-01"));
        assertEquals("[D][ ] submit report (by: Dec 01 2026)", d.toString());
    }
}