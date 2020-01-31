package com.rajcevic.tea.DiaryWebApp.model.form;

public class SearchForm {

    private String query;
    private String target;

    public SearchForm() {
    }

    public SearchForm(String query, String target) {
        this.query = query;
        this.target = target;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
