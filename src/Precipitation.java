import org.apache.poi.ss.usermodel.*;
import org.postgresql.util.PSQLException;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.*;
import java.time.ZoneId;

public class Precipitation {
    static Connection connection;
    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "3515");
        } catch(SQLException e) {
            e.printStackTrace();
        }

        File dir = new File(System.getProperty("user.dir") + "\\DataFiles\\Pluvio");
        Precipitation.showFiles(dir.listFiles());
    }

    static void parseAndInsert(File file) {
        try {
            String insertion = "insert into precipitation (measurementDate, measurementTime," +
                    " rainGauge, amount) values (?, ?, ?, ?);";
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (int r = 1; r < sheet.getLastRowNum(); r += 2) {
                Row row = sheet.getRow(r);
                for (int cell = 0; cell < row.getLastCellNum(); cell += 4) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertion)) {
                        preparedStatement.setObject(1, row.getCell(1).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        preparedStatement.setObject(2, row.getCell(2).getLocalDateTimeCellValue().toLocalTime());
                        preparedStatement.setInt(3, (int) row.getCell(cell).getNumericCellValue());
                        preparedStatement.setDouble(4, new BigDecimal(row.getCell(cell + 3).getNumericCellValue()
                                + sheet.getRow(r - 1).getCell(cell + 3).getNumericCellValue(), new MathContext(2)).doubleValue());
                        preparedStatement.execute();
                    } catch (PSQLException numericOverFlow) {
                        numericOverFlow.getMessage();
                    } catch (IllegalStateException illegalStateException) {
                        System.out.println("wrong formatting in " + file.getAbsolutePath());
                    }
                }
            }
            System.out.println("done working with " + file.getAbsolutePath());
        } catch (NullPointerException | IOException | SQLException exception) {
            exception.getMessage();
        }
    }

    static void showFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                showFiles(file.listFiles());
            } else {
                parseAndInsert(file);
            }
        }
    }
}
