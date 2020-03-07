package com.avdeev.docs.core.network.pojo;

import org.json.JSONObject;

public class Action {

    private long date;
    private String person;
    private String description;

    public Action() {
        date = 0;
        person = "";
        description = "";
    }

    public Action(JSONObject object) throws Exception {

        date = object.getLong("date");
        person = object.getString("person");
        description = object.getString("description");
    }

    public Action(Action action) {

        this.date = action.getDate();
        this.person = action.getPerson();
        this.description = action.getDescription();
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
