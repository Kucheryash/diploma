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

        if (profit < market_profit && employeeCount > market_employee) {
            advice += "Рекомендуется сосредоточиться на повышении эффективности и оптимизации процессов, чтобы улучшить рентабельность на каждого сотрудника.\n";
        }
        if (profit < market_profit && employeeCount < market_employee) {
            advice += "Следует рассмотреть возможность увеличения штата сотрудников для повышения производительности и конкурентоспособности.\n";
        }
        if (profit / employeeCount < (double) market_profit / market_employee) {
            advice += "Обратите внимание на эффективность использования ресурсов, повышение продуктивности и снижение издержек для улучшения финансовых показателей.\n";
        }
        if (profit > market_profit && employeeCount < market_employee) {
            advice += "Оптимально используйте имеющиеся ресурсы, автоматизируйте и повышайте эффективность работы сотрудников.\n";
        }
        if (profit > market_profit && employeeCount > market_employee) {
            advice += "Рекомендуется сосредоточиться на инновациях, развитии новых продуктов и услуг, чтобы укрепить свои позиции и диверсифицировать бизнес.\n";
        }
        if (profit == market_profit && employeeCount < market_employee) {
            advice += "Оптимизируйте рабочие процессы, улучшайте коммуникацию и развитие навыков сотрудников.\n";
        }
        if (profit == market_profit && employeeCount > market_employee) {
            advice += "Следует сосредоточиться на управлении ростом, расширении клиентской базы и укреплении отношений существующих клиентов.\n";
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
