package voyager.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToDoTest {
    @Test
    public void testToString_unmarkedTask_success() {
        ToDo todo = new ToDo("read book");
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void testMarkAsDone_success() {
        ToDo todo = new ToDo("read book");
        todo.mark();
        assertEquals("[T][X] read book", todo.toString());
    }
}