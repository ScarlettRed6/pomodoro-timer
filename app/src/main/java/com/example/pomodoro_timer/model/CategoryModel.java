package com.example.pomodoro_timer.model;

public class CategoryModel {

    //Fields
    private String categoryTitle;
    private Integer icon;

    //Constructor
    public CategoryModel(String categoryTitle, Integer icon){
        this.categoryTitle = categoryTitle;
        this.icon = icon;
    }

    //Getters and Setters
    public String getCategoryTitle(){
        return categoryTitle;
    }

    public Integer getIcon(){
        return icon;
    }

    public void setCategoryTitle(String categoryTitle){
        this.categoryTitle = categoryTitle;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

}
