package dk.trustworks.marginservice.network.queries;

import dk.trustworks.marginservice.model.ExperienceConsultant;
import dk.trustworks.marginservice.model.ExperienceLevel;
import dk.trustworks.marginservice.network.model.MarginResult;
import dk.trustworks.marginservice.repository.ExperienceConsultantRepository;
import dk.trustworks.marginservice.repository.ExperienceLevelRepository;
import dk.trustworks.marginservice.utils.BaseDataContainer;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.vertx.reactivex.ext.web.RoutingContext;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.Collectors;

import static dk.trustworks.marginservice.ActionHelper.*;
import static dk.trustworks.marginservice.utils.BaseDataContainer.*;


public class ExperienceConsultantQueryController {

    private ExperienceConsultantRepository experienceConsultantRepository;
    private ExperienceLevelRepository experienceLevelRepository;

    public ExperienceConsultantQueryController(ExperienceConsultantRepository experienceConsultantRepository, ExperienceLevelRepository experienceLevelRepository) {
        this.experienceConsultantRepository = experienceConsultantRepository;
        this.experienceLevelRepository = experienceLevelRepository;
    }

    public void getAll(RoutingContext rc) {
        experienceConsultantRepository.getAll().subscribe(ok(rc));
    }

    public void getOne(RoutingContext rc) {
        int id = Integer.parseInt(rc.pathParam("id"));
        experienceConsultantRepository.getOne(id).subscribe(ok(rc));
    }

    public void findByUseruuid(RoutingContext rc) {
        String useruuid = rc.request().getParam("useruuid");
        experienceConsultantRepository.findByUseruuid(useruuid).subscribe(ok(rc));
    }

    public void updateOne(RoutingContext rc) {
        int id = Integer.parseInt(rc.request().getParam("id"));
        ExperienceConsultant experienceConsultant = rc.getBodyAsJson().mapTo(ExperienceConsultant.class);
        experienceConsultantRepository.updateOne(id, experienceConsultant).subscribe(noContent(rc), onError(rc));
    }

    public void calculateMargin(RoutingContext rc) {
        String useruuid = rc.request().getParam("useruuid");
        int rate = Integer.parseInt(rc.request().getParam("rate"));

        experienceConsultantRepository.findByUseruuid(useruuid).subscribe(experienceConsultant -> {
            experienceLevelRepository.getAllExperienceLevels().subscribe(experienceLevels -> {
                ExperienceLevel experienceLevel = null;
                for (ExperienceLevel experienceLevelTest : experienceLevels.stream().sorted(Comparator.comparingInt(ExperienceLevel::getSeniority)).collect(Collectors.toList())) {
                    if((LocalDate.now().getYear() - experienceConsultant.getSeniority()) < experienceLevelTest.getSeniority()) break;
                    experienceLevel = experienceLevelTest;
                }

                double realizedHoursPerYear = HOURS_PER_YEAR * EXPECTED_UTILIZATION;
                double salaryPerYear = experienceLevel.getSalary() * 12;
                double expensesPerYear = EXPENSES_PER_YEAR * 12;
                double totalExpensesPerYear = salaryPerYear + expensesPerYear;
                double rateAtZeroMargin = totalExpensesPerYear / realizedHoursPerYear;

                Single.just(new MarginResult((int) Math.floor(((rate / rateAtZeroMargin) - 1) * 100.0))).subscribe(ok(rc));
            });
        }, throwable -> Single.just(new MarginResult()).subscribe(ok(rc)));
    }
}