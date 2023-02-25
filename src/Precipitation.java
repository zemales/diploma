import org.apache.poi.ss.usermodel.*;
import org.postgresql.util.PSQLException;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.*;
import java.time.ZoneId;
import java.util.Date;

public class Precipitation extends Thread{
    Connection connection;
    FileInputStream inputStream;
    BufferedInputStream bufferedInputStream;
    Workbook workbook;

    Precipitation (File file, Connection connection) {
        try {
            this.connection = connection;
            inputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(inputStream, 200);
            workbook = WorkbookFactory.create(bufferedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Date begin = new Date();
        try {
            if (!existenceCheck(connection)) {
                return;
            }
            String insertion = "insert into precipitation (measurementDate, measurementTime," +
                    " rainGauge, amount) values (?, ?, ?, ?);";
            Sheet sheet = workbook.getSheetAt(0);
            for (int r = 1; r < sheet.getLastRowNum(); r += 2) {
                Row row = sheet.getRow(r);
                for (int cell = 0; cell < row.getLastCellNum(); cell += 4) {
                    try {
                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertion)) {
                            preparedStatement.setObject(1, row.getCell(1).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            preparedStatement.setObject(2, row.getCell(2).getLocalDateTimeCellValue().toLocalTime());
                            preparedStatement.setInt(3, (int) row.getCell(cell).getNumericCellValue());
                            preparedStatement.setDouble(4, new BigDecimal(row.getCell(cell + 3).getNumericCellValue()
                                    + sheet.getRow(r - 1).getCell(cell + 3).getNumericCellValue(), new MathContext(2)).doubleValue());
                            preparedStatement.execute();
                        } catch (PSQLException numericOverFlow) {}
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            }
        } catch(NullPointerException nullPointerException) {}
        Date end = new Date();
        System.out.println("time to complete parseExcel " + (end.getTime() - begin.getTime()));
    }

    boolean existenceCheck(Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "precipitation", null);
            while (resultSet.next()) {
                return true;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
    }
}
