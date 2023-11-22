package project.service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Competitors;
import project.entity.Market;
import project.repository.CompetitorsRepository;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompetitorsService {
    @Autowired
    CompetitorsRepository repo;

    public List<Object[]> getRevAndEmpNotNull(){
        return repo.findAllNotNullFields();
    }

    public List<Market> getListFromExcel(){
        String excelFile = "D:\\Учёба\\7 семестр\\Курсовая работа\\market.xlsx";
        List<Market> dataList = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(excelFile);
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                int id = (int) row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                String revenue22 = String.valueOf(row.getCell(2).getNumericCellValue());

                String revenue21;
                Cell revenue21Cell = row.getCell(3);
                if (revenue21Cell.getCellType() == CellType.NUMERIC)
                    revenue21 = String.valueOf(revenue21Cell.getNumericCellValue());
                else
                    revenue21 = revenue21Cell.getStringCellValue();

                String growthRev;
                Cell growthRevCell = row.getCell(4);
                if (growthRevCell.getCellType() == CellType.NUMERIC) {
                    int growthRevInt = (int) growthRevCell.getNumericCellValue();
                    growthRev = growthRevInt * 100 + "%";
                } else {
                    growthRev = growthRevCell.getStringCellValue();
                }

                String employees22;
                Cell employees22Cell = row.getCell(5);
                if (employees22Cell.getCellType() == CellType.NUMERIC)
                    employees22 = String.valueOf(employees22Cell.getNumericCellValue());
                else
                    employees22 = employees22Cell.getStringCellValue();

                String employees21;
                Cell employees21Cell = row.getCell(6);
                if (employees21Cell.getCellType() == CellType.NUMERIC)
                    employees21 = String.valueOf(employees21Cell.getNumericCellValue());
                else
                    employees21 = employees21Cell.getStringCellValue();

                String growthEmpl;
                Cell growthEmplCell = row.getCell(7);
                if (growthEmplCell.getCellType() == CellType.NUMERIC) {
                    int growthEmplInt = (int) growthEmplCell.getNumericCellValue();
                    growthEmpl = growthEmplInt * 100 + "%";
                } else {
                    growthEmpl = growthEmplCell.getStringCellValue();
                }

                String activity = row.getCell(8).getStringCellValue();

                Market data = new Market(id, name, revenue22, revenue21, growthRev, employees22, employees21, growthEmpl, activity);
                dataList.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public Competitors get(Long id) {
        return repo.findById(id).get();
    }
}
