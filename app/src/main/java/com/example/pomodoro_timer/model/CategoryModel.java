package com.example.pomodoro_timer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class CategoryModel {

    //Fields
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_Id")
    private int userId;

    @ColumnInfo(name = "category_title")
    private String categoryTitle;

    @ColumnInfo(name = "icon")
    private Integer icon;

    //Constructor
    public CategoryModel(int userId, String categoryTitle, Integer icon){
        this.userId = userId;
        this.categoryTitle = categoryTitle;
        this.icon = icon;
    }

    //Getters and Setters
    public int getUserId(){
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

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
