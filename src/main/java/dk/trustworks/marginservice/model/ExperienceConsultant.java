package dk.trustworks.marginservice.model;

import dk.trustworks.marginservice.utils.BeanUtils;
import io.vertx.core.json.JsonObject;

/**
 * Created by hans on 23/06/2017.
 */

public class ExperienceConsultant {

    private int id;
    private String useruuid;
    private int seniority;

    public ExperienceConsultant() {
    }

    public ExperienceConsultant(String useruuid, int seniority) {
        this.useruuid = useruuid;
        this.seniority = seniority;
    }

    public ExperienceConsultant(JsonObject json)  {
        BeanUtils.populateFields(this, json);
    }

    public int getId() {
        return id;
    }

    public String getUseruuid() {
        return useruuid;
    }

    public void setUseruuid(String useruuid) {
        this.useruuid = useruuid;
    }

    public int getSeniority() {
        return seniority;
    }

    public void setSeniority(int seniority) {
        this.seniority = seniority;
    }
}
