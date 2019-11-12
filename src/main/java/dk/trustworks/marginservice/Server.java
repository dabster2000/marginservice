package dk.trustworks.marginservice;

import dk.trustworks.marginservice.network.queries.ExperienceConsultantQueryController;
import dk.trustworks.marginservice.network.queries.ExperienceLevelQueryController;
import dk.trustworks.marginservice.repository.ExperienceConsultantRepository;
import dk.trustworks.marginservice.repository.ExperienceLevelRepository;
import io.jaegertracing.Configuration;
import io.opentracing.util.GlobalTracer;
import io.reactivex.Completable;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

import java.util.Locale;
import java.util.TimeZone;

public class Server extends AbstractVerticle {

    private ExperienceLevelQueryController experienceLevelQueryController;
    private ExperienceConsultantQueryController experienceConsultantQueryController;

    public Server() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void start(Future<Void> fut) {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
        Locale.setDefault(new Locale("da", "DK"));

        Configuration.SamplerConfiguration samplerConfiguration = Configuration.SamplerConfiguration.fromEnv().withType("const").withParam(1);
        Configuration.ReporterConfiguration reporterConfiguration = Configuration.ReporterConfiguration.fromEnv().withLogSpans(true);
        Configuration configuration = new Configuration("marginservice").withSampler(samplerConfiguration).withReporter(reporterConfiguration);

        GlobalTracer.registerIfAbsent(configuration.getTracer());

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        ConfigRetriever retriever = ConfigRetriever.create(vertx);
        retriever.rxGetConfig()
                .doOnSuccess(config -> {
                    experienceLevelQueryController = new ExperienceLevelQueryController(new ExperienceLevelRepository(JDBCClient.createShared(vertx, config, "marginservice")));
                    experienceConsultantQueryController = new ExperienceConsultantQueryController(new ExperienceConsultantRepository(JDBCClient.createShared(vertx, config, "marginservice")), new ExperienceLevelRepository(JDBCClient.createShared(vertx, config, "marginservice")));

                    router.get("/experiencelevels").handler(experienceLevelQueryController::getAll);
                    router.get("/experiencelevels/:id").handler(experienceLevelQueryController::getOne);
                    router.post("/experiencelevels/:level").handler(experienceLevelQueryController::findByLevel);

                    router.get("/margin/:useruuid/:rate").handler(experienceConsultantQueryController::calculateMargin);
                })
                .flatMapCompletable(config -> createHttpServer(config, router))
                .subscribe(CompletableHelper.toObserver(fut));
    }

    private Completable createHttpServer(JsonObject config, Router router) {
        return vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .rxListen(config.getInteger("HTTP_PORT", 5560))
                .ignoreElement();
    }
}