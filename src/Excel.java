import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Excel {
    private final File file;
    private FileInputStream inputStream;
    static Workbook workbook;

    Excel (File file) {
        this.file = file;

        try {
            inputStream = new FileInputStream(file);
            workbook = WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void parseExcel(Connection connection) {
        try {
            Sheet sheet = Excel.workbook.getSheetAt(0);
            for (int r = 1; r < sheet.getLastRowNum(); r += 2) {
                Row row = sheet.getRow(r);
                for (int column = 2; column < row.getLastCellNum(); column += 15) {
                    ArrayList<Cell> array = new ArrayList<>(18);

                    Cell dateCell = row.getCell(0);
                    if (DateUtil.isCellDateFormatted(dateCell)) {
                        array.add(dateCell);
                    }

                    Cell timeCell = row.getCell(1);
                    if (DateUtil.isCellDateFormatted(timeCell)) {
                        array.add(timeCell);
                    }

                    for (int i = 0; i < 15; i++) {
                        Cell cell = row.getCell(column + i);

                        if (i == 1) {
                            array.add(cell);
                            array.add(sheet.getRow(r - 1).getCell(column + i));
                        } else {
                            if (cell == null) {
                                array.add(null);
                            } else {
                                array.add(cell);
                            }
                        }
                    }

                    if (array.get(2) != null) {
                        try {
                            DataBase.insertLocator(connection, array);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return;
        }
    }
}
