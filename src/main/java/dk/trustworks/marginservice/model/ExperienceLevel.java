package dk.trustworks.marginservice.model;

import dk.trustworks.marginservice.utils.BeanUtils;
import io.vertx.core.json.JsonObject;

/**
 * Created by hans on 23/06/2017.
 */

public class ExperienceLevel {

    private int id;
    private int level;
    private String name;
    private int seniority;
    private int salary;

    public ExperienceLevel() {
    }

    public ExperienceLevel(int level, String name, int seniority, int salary) {
        this.level = level;
        this.name = name;
        this.seniority = seniority;
        this.salary = salary;
    }

    public ExperienceLevel(JsonObject json)  {
        BeanUtils.populateFields(this, json);
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeniority() {
        return seniority;
    }

    public void setSeniority(int seniority) {
        this.seniority = seniority;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "ExperienceLevel{" +
                "id=" + id +
                ", level=" + level +
                ", name='" + name + '\'' +
                ", seniority=" + seniority +
                ", salary=" + salary +
                '}';
    }
}
