package dk.trustworks.marginservice.network.queries;

import dk.trustworks.marginservice.repository.ExperienceLevelRepository;
import io.vertx.reactivex.ext.web.RoutingContext;

import static dk.trustworks.marginservice.ActionHelper.ok;


public class ExperienceLevelQueryController {

    private ExperienceLevelRepository experienceLevelRepository;

    public ExperienceLevelQueryController(ExperienceLevelRepository experienceLevelRepository) {
        this.experienceLevelRepository = experienceLevelRepository;
    }

    public void getAll(RoutingContext rc) {
        experienceLevelRepository.getAllExperienceLevels().subscribe(ok(rc));
    }

    public void getOne(RoutingContext rc) {
        int id = Integer.parseInt(rc.pathParam("id"));
        experienceLevelRepository.getOne(id).subscribe(ok(rc));
    }

    public void findByLevel(RoutingContext rc) {
        int level = Integer.parseInt(rc.request().getParam("level"));
        experienceLevelRepository.findByLevel(level).subscribe(ok(rc));
    }
}
