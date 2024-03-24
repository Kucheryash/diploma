package project.services;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entities.Charts;
import project.entities.CompanyData;
import project.repositories.ChartsRepository;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@Service
@Transactional
public class ChartService{
    @Autowired
    ChartsRepository repo;

    @Autowired
    CompanyDataService companyDataService;

    public void createCharts(List<Double> forecastRevComp, List<Double> forecastRevMarket, List<Double> forecastMarketShare, CompanyData companyData) {
        String outputPath = "D:\\Учёба\\Диплом\\Графики\\";
        generateLineChart(forecastRevComp, forecastRevMarket,  outputPath+"revenue_chart"+companyData.getCompany().getId()+".png");
        generateSingleLineChart(forecastMarketShare, outputPath+"market_share_chart"+companyData.getCompany().getId()+".png");

        Charts charts = new Charts();
        Date date = Date.valueOf(java.time.LocalDate.now());
        charts.setDate(date);
        charts.setData(companyData);
        charts.setRevenuePath(outputPath+"revenue_chart"+companyData.getCompany().getId()+".png");
        charts.setMarketSharePath(outputPath+"market_share_chart"+companyData.getCompany().getId()+".png");
        save(charts);
    }

    public void generateLineChart(List<Double> data1, List<Double> data2, String outputPath) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < data1.size(); i++) {
            dataset.addValue(data1.get(i), "Выручка компании", String.valueOf(i + 1));
        }
        for (int i = 0; i < data2.size(); i++) {
            dataset.addValue(data2.get(i), "Выручка рынка", String.valueOf(i + 1));
        }
        JFreeChart chart = ChartFactory.createLineChart(
                "Месячная выручка",
                "Месяц",
                "Выручка, USD",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlineStroke(new BasicStroke(0.5f));
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlineStroke(new BasicStroke(0.5f));
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-3, -3, 8, 8));
        renderer.setSeriesShape(1, new java.awt.geom.Ellipse2D.Double(-3, -3, 8, 8));
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
        renderer.setSeriesStroke(1, new BasicStroke(4.0f));
        renderer.setSeriesPaint(0, Color.decode("#EC6A32"));
        renderer.setSeriesPaint(1, Color.decode("#4F5D75"));
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        chart.setBackgroundPaint(Color.WHITE);
        double minY = Math.min(getMinValue(data1), getMinValue(data2)) - 500;
        double maxY = Math.max(getMaxValue(data1), getMaxValue(data2)) + 500;
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(minY, maxY);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        try {
            ChartUtils.saveChartAsPNG(new File(outputPath), chart, 800, 600);
            System.out.println("График сохранен как " + outputPath);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении графика: " + e.getMessage());
        }
    }

    public void generateSingleLineChart(List<Double> data, String outputPath) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < data.size(); i++) {
            dataset.addValue(data.get(i), "Рыночная доля", String.valueOf(i + 1));
        }
        JFreeChart chart = ChartFactory.createLineChart(
                "Месячная рыночная доля",
                "Месяц",
                "Доля, %",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlineStroke(new BasicStroke(0.5f));
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlineStroke(new BasicStroke(0.5f));
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-3, -3, 8, 8));
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
        renderer.setSeriesPaint(0, Color.decode("#2D3142"));
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        chart.setBackgroundPaint(Color.WHITE);
        double minY = getMinValue(data)-0.1;
        double maxY = getMaxValue(data)+0.1;
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(minY, maxY);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        try {
            ChartUtils.saveChartAsPNG(new File(outputPath), chart, 800, 600);
            System.out.println("График сохранен как " + outputPath);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении графика: " + e.getMessage());
        }
    }

    public Charts findByCompanyData(CompanyData companyData) {
        return repo.findByData(companyData);
    }

    public void save(Charts charts){
        repo.save(charts);
    }

    public Charts get(Long id) {
        return repo.findById(id).get();
    }


    private static double getMinValue(List<Double> data) {
        double minValue = Double.MAX_VALUE;
        for (Double value : data) {
            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }

    private static double getMaxValue(List<Double> data) {
        double maxValue = Double.MIN_VALUE;
        for (Double value : data) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }
}
