package com.monitorjbl.xlsx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.BeforeClass;
import org.junit.Test;

public class StreamingWorkbookTest {
    @BeforeClass
    public static void init() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    public void testIterateSheets() throws Exception {
        InputStream is = new FileInputStream(new File("src/test/resources/sheets.xlsx"));
        Workbook workbook = StreamingReader.builder().open(is);

        assertEquals(2, workbook.getNumberOfSheets());

        Sheet alpha = workbook.getSheetAt(0);
        Sheet zulu = workbook.getSheetAt(1);
        assertEquals("SheetAlpha", alpha.getSheetName());
        assertEquals("SheetZulu", zulu.getSheetName());

        Row rowA = alpha.rowIterator().next();
        Row rowZ = zulu.rowIterator().next();

        assertEquals("stuff", rowA.getCell(0).getStringCellValue());
        assertEquals("yeah", rowZ.getCell(0).getStringCellValue());
    }

    @Test
    public void testHiddenCells() throws Exception {
        InputStream is = new FileInputStream(new File("src/test/resources/hidden_cells.xlsx"));
        Workbook workbook = StreamingReader.builder().open(is);
        assertEquals(1, workbook.getNumberOfSheets());
        Sheet sheet = workbook.getSheetAt(0);

        assertFalse("Column 0 should not be hidden", sheet.isColumnHidden(0));
        assertTrue("Column 1 should be hidden", sheet.isColumnHidden(1));
        assertFalse("Column 2 should not be hidden", sheet.isColumnHidden(2));

        assertFalse("Row 0 should not be hidden", sheet.rowIterator().next().getZeroHeight());
        assertTrue("Row 1 should be hidden", sheet.rowIterator().next().getZeroHeight());
        assertFalse("Row 2 should not be hidden", sheet.rowIterator().next().getZeroHeight());
    }
}
