import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Класс Excel создан для чтения из эксель файлов
 * Объектом класса является непосредственно файл .xlsx
 * В конструкторе класса Excel в качестве аргумента
 * передается путь к файлу
 */

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

    /**
     * Чтение данных из ячеек файла.
     * Данные собираются в Arraylist array.
     * Для того, чтобы привести количество выпавших осадков,
     * зафиксированных осадкомером в array передается два значения
     * количества осадков (n пятиминутка + n-1 пятиминутка)
     */
    void locator(Connection connection) {
        try {
            //получение данных с Листа Excel
            Sheet sheet = Excel.workbook.getSheetAt(0);
            //итерация по рядам с шагом 2 для суммирования по 10 минут в будущем
            for (int r = 1; r < sheet.getLastRowNum(); r += 2) {
                Row row = sheet.getRow(r);
                //итерация по колонкам ряда
                //начинается с 2, потому что 0 и 1 выделены для Даты и Времени
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

                    /*
                      метод DataBase.insertLocator() вызывается при условии
                      что поле excel-таблицы, содержащее номер осадкомера не NULL
                     */
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
