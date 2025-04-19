package com.example.pomodoro_timer.model;

public class CategoryModel {

    //Fields
    private String categoryTitle;
    private String icon;

    //Constructor
    public CategoryModel(String categoryTitle, String icon){
        this.categoryTitle = categoryTitle;
        this.icon = icon;
    }

    //Getters and Setters
    public String getCategoryTitle(){
        return categoryTitle;
    }

    public String getIcon(){
        return icon;
    }

    public void setCategoryTitle(String categoryTitle){
        this.categoryTitle = categoryTitle;
    }

    public void setIcon(String icon){
        this.icon = icon;
    }

}
