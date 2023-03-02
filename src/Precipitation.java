import org.apache.poi.ss.usermodel.*;
import org.postgresql.util.PSQLException;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
                        date(preparedStatement, row);
                        time(preparedStatement, row);
                        rainGauge(preparedStatement, row, cell);
                        amount(preparedStatement, row, cell);

                        preparedStatement.execute();
                    } catch (PSQLException exception) {
                        System.out.println("PSQLException registered at row " + r + " cell " + cell);
                    } catch (IllegalStateException illegalStateException) {
                        System.out.println("IllegalStateException registered at row " + r + " cell " + cell);
                    }
                }
            }
            System.out.println("done working with " + file.getAbsolutePath());
        } catch (NullPointerException | IOException | SQLException exception) {
            exception.printStackTrace();
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

    static void date(PreparedStatement preparedStatement, Row row) throws SQLException {
        try {
            Cell dateCell = row.getCell(1);
            if (dateCell.getCellType() == CellType.STRING) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH);
                preparedStatement.setObject(1, LocalDate.parse(dateCell.getStringCellValue(), formatter));
            } else {
                preparedStatement.setObject(1, dateCell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
        } catch (IllegalStateException e) {
            System.out.println("date");
        }
    }

    static void time (PreparedStatement preparedStatement, Row row) throws SQLException {
        try {
            Cell timeCell = row.getCell(2);
            if (timeCell.getCellType() == CellType.STRING) {
                preparedStatement.setObject(2, LocalTime.parse(timeCell.getStringCellValue(), DateTimeFormatter.ISO_LOCAL_TIME));
            } else {
                preparedStatement.setObject(2, timeCell.getLocalDateTimeCellValue().toLocalTime());
            }
        } catch (IllegalStateException e) {
            System.out.println("time");
        }
    }

    static void rainGauge(PreparedStatement preparedStatement, Row row, int cell) throws SQLException{
        try {
            Cell gaugeNumberCell = row.getCell(cell);
            if (gaugeNumberCell.getCellType() == CellType.STRING) {
                preparedStatement.setInt(3, Integer.parseInt(gaugeNumberCell.getStringCellValue()));
            } else {
                preparedStatement.setInt(3, (int) gaugeNumberCell.getNumericCellValue());
            }
        } catch (IllegalStateException e) {
            System.out.println("rainGauge");
        }
    }

    static void amount(PreparedStatement preparedStatement, Row row, int cell) throws SQLException{
        try {
            Cell amountCell = row.getCell(cell+3);
            if (amountCell.getCellType() == CellType.STRING) {
                preparedStatement.setDouble(4, Double.parseDouble(amountCell.getStringCellValue()));
            } else {
                preparedStatement.setDouble(4, new BigDecimal(row.getCell(cell + 3).getNumericCellValue()
                        + row.getSheet().getRow(row.getRowNum() - 1).getCell(cell + 3).getNumericCellValue(),
                        new MathContext(2)).doubleValue());
            }
        } catch (IllegalStateException e) {
            System.out.println("amount");
        }
    }
}
