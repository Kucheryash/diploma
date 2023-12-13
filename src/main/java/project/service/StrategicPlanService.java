package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Company;
import project.entity.CompanyData;
import project.entity.StrategicPlan;
import project.repository.StrategicPlanRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StrategicPlanService {
    @Autowired
    StrategicPlanRepository repo;

    @Autowired
    CompetitorsService competitorsService;

    public StrategicPlan makeRecommendations(CompanyData companyData){
        List<Object[]> competitors = competitorsService.getRevAndEmpNotNull();
        List<Integer> revenueList = new ArrayList<>();
        List<Integer> employeeCountList = new ArrayList<>();
        for (Object[] result : competitors) {
            int r = (int) result[0];
            int e = (int) result[1];
            revenueList.add(r);
            employeeCountList.add(e);
        }
        int sum = 0;
        int count = revenueList.size();
        for (int rev : revenueList) {
            sum += rev;
        }
        int avgRev = Math.round((float) sum/count);
        sum = 0;
        count = employeeCountList.size();
        for (int emp : employeeCountList) {
            sum += emp;
        }
        int avgEmp = Math.round((float) sum/count);

        StrategicPlan plan = new StrategicPlan();
        Date date = Date.valueOf(java.time.LocalDate.now());
        plan.setDate(date);
        plan.setDescription(strategicPlan(companyData.getRevenue22(), companyData.getEmployees22(), avgRev, avgEmp));
        plan.setCompany(companyData.getCompany());
        plan.setStatus("создан");
        return plan;
    }

    private String strategicPlan(double profit, int employeeCount, int market_profit, int market_employee) {
        String advice = "";

        if (profit < market_profit && employeeCount < market_employee) {
            advice += "Исследуйте свои текущие ресурсы и процессы, чтобы выявить возможности для оптимизации и повышения эффективности. " +
                    "Рассмотрите автоматизацию рутинных задач, внедрение новых технологий и использование инструментов управления проектами для оптимизации рабочих процессов.\n" +
                    "Обратите внимание на создание мотивирующей и поддерживающей рабочей среды, где сотрудники чувствуют себя ценными и могут достичь личного и профессионального роста.\n" +
                    "Активно изучайте рынок и конкурентов, чтобы быть в курсе последних тенденций и изменений.\n";
        }
        if (profit / employeeCount < (double) market_profit / market_employee) {
            advice += "Проведите анализ текущих процессов и определите, где можно внедрить автоматизацию, чтобы улучшить эффективность и точность операций. " +
                    "Рассмотрите возможности для использования специализированного программного обеспечения и инструментов для управления бизнес-процессами.\n" +
                    "Проведите анализ затрат и посмотрите, где можно сократить издержки и повысить эффективность использования ресурсов.\n" +
                    "Разработайте планы для обучения и развития персонала, чтобы повысить их навыки и компетенции.\n";
        }
        if (profit > market_profit && employeeCount < market_employee) {
            advice += "Проанализируйте текущие бизнес-процессы и выявите области, которые требуют оптимизации и автоматизации. Рассмотрите " +
                    "внедрение новых технологий и систем управления, которые помогут улучшить производительность и качество работы.\n" +
                    "Используйте преимущество небольшой команды, которая может быть более гибкой и способной быстро адаптироваться к изменениям на рынке и клиентским потребностям.\n";
        }
        if (profit > market_profit && employeeCount > market_employee) {
            advice += "Рассмотрите потенциал роста в существующих рынках и исследуйте возможности расширения в новые сегменты.\n" +
                    "Разработайте маркетинговые стратегии, которые помогут привлечь и удержать новых клиентов, такие как улучшение продуктового предложения, " +
                    "разработка целевых маркетинговых компаний, усиление онлайн-присутствия и использование социальных сетей.\n" +
                    "Инвестируйте в обучение, тренинги и развитие навыков сотрудников, чтобы повысить их профессиональный уровень и эффективность работы.\n";
        }

        return advice.trim();
    }

    public StrategicPlan findByCompany(Company company){
        return repo.findByCompany(company);
    }

    public void save(StrategicPlan plan){
        repo.save(plan);
    }

    public StrategicPlan get(Long id) {
        return repo.findById(id).get();
    }

}
