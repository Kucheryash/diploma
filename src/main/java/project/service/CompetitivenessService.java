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
    CompanyDataService companyDataService;
    @Autowired
    SWOTService swotService;
    @Autowired
    StrategicPlanService planService;
    @Autowired
    ChartService chartService;
    @Autowired
    CompetitorsService competitorsService;

    public Competitiveness makeAnalisys(Company company){
        CompanyData companyData = companyDataService.findByCompanyId(company.getId());
        Competitiveness competitiveness = new Competitiveness();
        competitiveness.setRevenue(companyData.getRevenue22());
        competitiveness.setEmployees(companyData.getEmployees22());
        competitiveness.setActivity(companyData.getActivity());
        competitiveness.setCompany(company);
        double revenueGrowth = ((double) (companyData.getRevenue22()-companyData.getRevenue21()) /companyData.getRevenue22())*100;
        double profitability = ((double) companyData.getProfit22()/companyData.getRevenue22())*100;
        double sumMarkerRevenue = summaryMarketRev(companyData);
        double marketShare = (companyData.getRevenue22()/sumMarkerRevenue)*100;
        Date date = Date.valueOf(java.time.LocalDate.now());
        competitiveness.setDate(date);
        competitiveness.setRevenueGrowth(Math.round(revenueGrowth * 10) / 10.0);
        competitiveness.setProfitability(Math.round(profitability * 10) / 10.0);
        competitiveness.setMarketShare(Math.round(marketShare * 10) / 10.0);
        save(competitiveness);
        return competitiveness;
    }

    public double summaryMarketRev(CompanyData companyData){
        List<Object[]> marketRevenues = competitorsService.getRevenue22ValuesByActivity(companyData.getActivity());
        double sumMarketRevenue = 0;
        for (Object[] revenueArray : marketRevenues) {
            if (revenueArray != null && revenueArray.length > 0) {
                Object revenue = revenueArray[0];
                if (revenue instanceof Number) {
                    sumMarketRevenue += ((Number) revenue).doubleValue();
                }
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
        File templateFile = new File("D:\\Учёба\\7 семестр\\Курсовая работа\\template.docx");
        XWPFDocument newDoc = new XWPFDocument(new FileInputStream(templateFile));
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
        File directory = null;
        if (absolutePath != null) {
            directory = new File(absolutePath);
        }
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = absolutePath + File.separator + docName + ".docx";
        FileOutputStream out = new FileOutputStream(filePath);
        newDoc.write(out);
        out.close();

    }

    private void replaceVariable(XWPFDocument doc, String variable, String value) {
        List<XWPFParagraph> paragraphs = doc.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            String text = paragraph.getText();
            if (text.contains(variable)) {
                String replacedText = text.replace(variable, value);
                while (paragraph.getRuns().size() > 0) {
                    paragraph.removeRun(0);
                }
                XWPFRun newRun = paragraph.createRun();
                newRun.setText(replacedText);
            }
        }
    }

    private void replaceVariableTable(XWPFDocument doc, String variable, String value) {
        XWPFTable table = doc.getTables().get(0);
        for (int row = 1; row < table.getNumberOfRows(); row++) {
            XWPFTableRow tableRow = table.getRow(row);
            for (int col = 0; col < tableRow.getTableCells().size(); col++) {
                XWPFTableCell cell = tableRow.getCell(col);
                String cellText = cell.getText();
                if (cellText.contains(variable)) {
                    cellText = cellText.replace(variable, value);
                    cell.removeParagraph(0);
                    cell.setText(cellText);
                }
            }
        }
    }

    private void insertChartImage(XWPFDocument doc, String variable, File imageFile) throws IOException, InvalidFormatException {
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            List<XWPFRun> runs = paragraph.getRuns();
            if (runs.size() > 0) {
                StringBuilder fullText = new StringBuilder();
                for (XWPFRun run : runs) {
                    fullText.append(run.getText(0));
                }
                String text = fullText.toString();
                int variableIndex = text.indexOf(variable);
                if (variableIndex != -1) {
                    text = text.substring(0, variableIndex) + text.substring(variableIndex + variable.length());
                    for (int i = runs.size() - 1; i >= 0; i--) {
                        paragraph.removeRun(i);
                    }
                    XWPFRun newRun = paragraph.createRun();
                    newRun.setText(text);
                    XWPFRun runImage = paragraph.createRun();
                    InputStream imageStream = new FileInputStream(imageFile);
                    runImage.addPicture(imageStream, XWPFDocument.PICTURE_TYPE_PNG, imageFile.getName(), Units.toEMU(400), Units.toEMU(300));
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
}
