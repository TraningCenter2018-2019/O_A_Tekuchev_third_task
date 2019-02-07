import org.junit.Test;
import tables.unloadingprocesstable.TableRow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestRowEqual {
  @Test
  public void testRowEqual() {
    TableRow tableRow1 = new TableRow(1, 1, "1", 1, 1);
    TableRow tableRow2 = new TableRow(1, 1, "1", 1, 1);
    assertTrue(tableRow1.equalRows(tableRow2));
    tableRow1.setId(0);
    assertTrue(tableRow1.equalRowsWithoutId(tableRow2));
    assertFalse(tableRow1.equalRows(null));
    assertFalse(tableRow1.equalRowsWithoutId(null));
  }
}
