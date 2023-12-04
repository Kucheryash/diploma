package project.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.entity.*;
import project.repository.CompetitivenessRepository;
import project.repository.CompetitorsRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;


@Service
@Transactional
public class CompetitivenessService {
    @Autowired
    CompetitivenessRepository repo;
    @Autowired
    CompetitorsRepository repoCompetitors;

    @Autowired
    CompanyDataService companyDataService;
    @Autowired
    SWOTService swotService;
    @Autowired
    StrategicPlanService planService;
    @Autowired
    ChartService chartService;

    public Competitiveness makeAnalisys(Company company){
        List<Object> marketRevenues = repoCompetitors.findRevenue22Values();
        CompanyData companyData = companyDataService.findByCompanyId(company.getId());

        Competitiveness competitiveness = new Competitiveness();
        competitiveness.setRevenue(companyData.getRevenue22());
        competitiveness.setEmployees(companyData.getEmployees22());
        competitiveness.setActivity(companyData.getActivity());
        competitiveness.setCompany(company);

        double revenueGrowth = ((double) (companyData.getRevenue22()-companyData.getRevenue21()) /companyData.getRevenue22())*100;
        double profitability = ((double) companyData.getProfit22()/companyData.getRevenue22())*100;

        double sumMarkerRevenue = summaryMarketRev();
        double marketShare = (companyData.getRevenue22()/sumMarkerRevenue)*100;

        Date date = Date.valueOf(java.time.LocalDate.now());
        competitiveness.setDate(date);
        competitiveness.setRevenueGrowth(Math.round(revenueGrowth * 10) / 10.0);
        competitiveness.setProfitability(Math.round(profitability * 10) / 10.0);
        competitiveness.setMarketShare(Math.round(marketShare * 10) / 10.0);

        save(competitiveness);

        return competitiveness;
    }

    public double summaryMarketRev(){
        List<Object> marketRevenues = repoCompetitors.findRevenue22Values();
        double sumMarketRevenue = 0;
        for (Object revenue : marketRevenues) {
            if (revenue instanceof Number) {
                sumMarketRevenue += ((Number) revenue).doubleValue();
            }
        }
        return sumMarketRevenue;
    }

    public void fillReportTemplate(Company company, String directoryPath) throws IOException, InvalidFormatException {
        CompanyData companyData = companyDataService.findByCompanyId(company.getId());
        SWOT swot = swotService.findByCompany(company);
        Competitiveness competitiveness = findByCompany(company);
        StrategicPlan plan = planService.findByCompany(company);
        String docName = "Отчет компании '" + company.getName() + "'";

        Charts charts = chartService.findByCompanyData(companyData);
        String revCompPath = charts.getRevenuePath();
        String marketSharePath = charts.getMarketSharePath();

        // Читаем шаблонный файл
        File templateFile = new File("D:\\Учёба\\7 семестр\\Курсовая работа\\template.docx");
        XWPFDocument newDoc = new XWPFDocument(new FileInputStream(templateFile));

        // Заменяем переменные в документе на значения
        replaceVariable(newDoc, "${companyName}", company.getName());
        replaceVariable(newDoc, "${industry}", companyData.getActivity());
        replaceVariable(newDoc, "${analysisDate}", String.valueOf(companyData.getDate()));
        replaceVariableTable(newDoc, "${strengths}", swot.getStrengths());
        replaceVariableTable(newDoc, "${weaknesses}", swot.getWeaknesses());
        replaceVariableTable(newDoc, "${opportunities}", swot.getOpportunities());
        replaceVariableTable(newDoc, "${threats}", swot.getThreats());
        replaceVariable(newDoc, "${revenue}", String.valueOf(competitiveness.getRevenue()));
        replaceVariable(newDoc, "${employeeCount}", String.valueOf(competitiveness.getEmployees()));
        replaceVariable(newDoc, "${revenueGrowth}", String.valueOf(competitiveness.getRevenueGrowth()));
        replaceVariable(newDoc, "${profitability}", String.valueOf(competitiveness.getProfitability()));
        replaceVariable(newDoc, "${marketShare}", String.valueOf(competitiveness.getMarketShare()));
        insertChartImage(newDoc, "${revCompChart}", new File(revCompPath));
        insertChartImage(newDoc, "${marketShareChart}", new File(marketSharePath));
        replaceVariable(newDoc, "${recommendations}", plan.getDescription());

        String absolutePath = "";
        absolutePath = findAbsolutePathInDirectory("D:\\", directoryPath);

        // Создание директории, если она не существует
        File directory = null;
        if (absolutePath != null) {
            directory = new File(absolutePath);
        }
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Путь и имя файла для сохранения отчета
        String filePath = absolutePath + File.separator + docName + ".docx";

        // Сохранение документа
        FileOutputStream out = new FileOutputStream(filePath);
        newDoc.write(out);
        out.close();

    }

    private void replaceVariable(XWPFDocument doc, String variable, String value) {
        List<XWPFParagraph> paragraphs = doc.getParagraphs();
        // Вставка данных в параграфы
        for (XWPFParagraph paragraph : paragraphs) {
            String text = paragraph.getText();
            if (text.contains(variable)) {
                // Заменяем переменную на значение
                String replacedText = text.replace(variable, value);

                // Удаляем все старые раны (текстовые части) из параграфа
                while (paragraph.getRuns().size() > 0) {
                    paragraph.removeRun(0);
                }

                // Создаем новую рану и устанавливаем в нее измененный текст
                XWPFRun newRun = paragraph.createRun();
                newRun.setText(replacedText);
            }
        }
    }

    private void replaceVariableTable(XWPFDocument doc, String variable, String value) {
        // Получение таблицы из документа (предполагается, что таблица находится на первой странице)
        XWPFTable table = doc.getTables().get(0);

        // Обход строк таблицы (начиная со второй строки, так как первая строка - заголовки столбцов)
        for (int row = 1; row < table.getNumberOfRows(); row++) {
            XWPFTableRow tableRow = table.getRow(row);

            // Обход ячеек в строке
            for (int col = 0; col < tableRow.getTableCells().size(); col++) {
                XWPFTableCell cell = tableRow.getCell(col);
                String cellText = cell.getText();

                if (cellText.contains(variable)) {
                    // Замена заполнителей переменных в ячейке
                    cellText = cellText.replace(variable, value);
                    cell.removeParagraph(0); // Удаление исходного текста ячейки
                    cell.setText(cellText); // Вставка нового текста
                }
            }
        }
    }

    private void insertChartImage(XWPFDocument document, String placeholder, File imageFile) throws IOException, InvalidFormatException {
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();

            for (XWPFRun run : runs) {
                String text = run.getText(0);

                if (text != null && text.contains(placeholder)) {
                    run.setText("", 0);  // Очистка плейсхолдера

                    // Вставка изображения
                    InputStream imageStream = new FileInputStream(imageFile);
                    run.addPicture(imageStream, XWPFDocument.PICTURE_TYPE_PNG, imageFile.getName(), Units.toEMU(400), Units.toEMU(300));
                    imageStream.close();
                }
            }
        }
    }


    public Competitiveness findByCompany(Company company) {
        return repo.findByCompany(company);
    }

    public void save(Competitiveness competitiveness){
        repo.save(competitiveness);
    }

    public Competitiveness get(Long id) {
        return repo.findById(id).get();
    }


    private static String findAbsolutePathInDirectory(String directoryPath, String directoryName) {
        File[] files = new File(directoryPath).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && file.getName().equals(directoryName)) {
                    return file.getAbsolutePath();
                }
                if (file.isDirectory()) {
                    String absolutePath = findAbsolutePathInDirectory(file.getAbsolutePath(), directoryName);
                    if (absolutePath != null) {
                        return absolutePath;
                    }
                }
            }
        }
        return null;
    }

    private String saveChartImage(MultipartFile chartImage, String filename) throws IOException {
        // Сохранение изображения на диск
        byte[] bytes = chartImage.getBytes();
        Path path = Paths.get("D:\\Учёба\\7 семестр\\Курсовая работа\\Графики\\", filename);
        Files.write(path, bytes);
        return path.toString();
    }

}
