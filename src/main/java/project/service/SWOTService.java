package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Company;
import project.entity.Competitors;
import project.entity.SWOT;
import project.repository.SWOTRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SWOTService {
    @Autowired
    SWOTRepository repo;

    @Autowired
    CompetitorsService competitorsService;

    public SWOT SWOTAnalysis(int revenue, int num_of_employees, Company company){
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

        String strengths = calculateStrengths(revenue, num_of_employees, avgRev, avgEmp),
            weaknesses = calculateWeaknesses(revenue, num_of_employees, avgRev, avgEmp),
            opportunities = calculateOpportunities(revenue, num_of_employees, avgRev, avgEmp),
            threats = calculateThreats(revenue, num_of_employees, avgRev, avgEmp);

        SWOT analysis = new SWOT();
        Date date = Date.valueOf(java.time.LocalDate.now());
        analysis.setDate(date);
        analysis.setCompany(company);
        analysis.setStrengths(strengths);
        analysis.setWeaknesses(weaknesses);
        analysis.setOpportunities(opportunities);
        analysis.setThreats(threats);
        return analysis;
    }

    private String calculateStrengths(double profit, int employeeCount, int market_profit, int market_employee) {
        String strengths = "";

        if(profit>market_profit && employeeCount>market_employee)
            strengths += "Большие ресурсы и команда способствуют разработке инновационных продуктов и услуг.\nШирокий клиентский спектр.\nКомпания привлекательна для клиентов и партнеров.\n";

        if(profit>market_profit && employeeCount<market_employee)
            strengths += "Меньшее количество сотрудников упрощает управление и контроль за бизнес-процессами, что может повысить эффективность и принятие решений.\nГибкость и манёвренность.\n";

        if(profit<market_profit && employeeCount>market_employee)
            strengths += "Большая клиентская база.\nБольшая команда сотрудников может обеспечить компанию ресурсами и потенциалом для масштабирования бизнеса и роста дохода в будущем.\n";

        if(profit/employeeCount>(double) market_profit /market_employee)
            strengths += "Эффективное использование трудовых ресурсов и высокая производительность сотрудников.\n";

        return strengths.trim();
    }

    private String calculateWeaknesses(double profit, int employeeCount, int market_profit, int market_employee) {
        String weaknesses = "";

        if (profit<market_profit && employeeCount<market_employee)
            weaknesses += "Ограниченные ресурсы.\nПерегруженный персонал, низкое качества работы и долгое времени реагирования на запросы клиентов.\nСложности в привлечении и удержании сотрудников.\n";

        if (profit/employeeCount < (double) market_profit /market_employee)
            weaknesses += "Низкий показатель эффективности использования трудовых ресурсов.\nНерациональные траты.\nНеэффективные бизнес-процессы, недостаточная автоматизация.\nНедостаточная конкурентоспособность.\n";

        return weaknesses.trim();
    }

    private String calculateOpportunities(double profit, int employeeCount, int market_profit, int market_employee) {
        String opportunities = "";

        if (profit>market_profit && employeeCount>market_employee)
            opportunities += "Инвестиции в развитие.\nРасширение клиентской базы. Большее количество сотрудников позволяет компании обслуживать больше клиентов.\n";
        else
            opportunities += "Компания может использовать ограниченные ресурсы и небольшую команду сотрудников для разработки стратегии сокращения издержек, улучшения эффективности и повышения финансовой устойчивости.\nПартнёрство и сотрудничество.\n";

        if (profit>market_profit && employeeCount<market_employee)
            opportunities += "Оптимизация процессов.\nНебольшая команда может быть более гибкой и способной быстро адаптироваться к изменениям рынка и клиентских потребностей.\n";

        if (profit/employeeCount > (double) market_profit /market_employee)
            opportunities += "Высокая прибыльность позволяет компании инвестировать в рост и развитие, выплачивать дивиденды акционерам или реинвестировать средства в новые проекты.\nЕсть возможность предлагать более конкурентоспособные цены.\n";
        else
            opportunities += "Компания может сосредоточиться на разработке продуктов или услуг с более высокой прибыльностью, пересмотреть свою ценовую политику или повысить эффективность своих операций, чтобы увеличить прибыль на каждого сотрудника.\n";

        return opportunities.trim();
    }

    private String calculateThreats(double profit, int employeeCount, int market_profit, int market_employee) {
        String threats = "";

        if (profit<market_profit && employeeCount>market_employee)
            threats += "Финансовым затруднения (отсутствие средств для оплаты зарплат или инвестиций в развитие).\n Потеря клиентов.\n";
        else
            threats += "Перегрузка работы. Ограниченное количество сотрудников может не справиться с растущим объемом работы.\n";

        if (profit<market_profit && employeeCount<market_employee)
            threats += "Ограниченные ресурсы.\nСнижение мотивации сотрудников (недостаточное количество сотрудников может привести к перегрузке работы и выгоранию сотрудников)";

        if (profit/employeeCount < (double) market_profit /market_employee)
            threats += "Недостаток прибыльности по сравнению с другими компаниями на рынке может указывать на проблемы в эффективности и производительности компании.\n";

        return threats.trim();
    }

    public SWOT findByCompany(Company company){
        return repo.findByCompany(company);
    }

    public List<SWOT> findAllByStatus(String status){
        return repo.findAllByStatus(status);
    }

    public void save(SWOT swot){
        repo.save(swot);
    }

    public SWOT get(Long id) {
        return repo.findById(id).get();
    }

}
