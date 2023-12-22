package com.example.cs361_project;

public class AddMealData {
    private int id;
    private String foodname,calories;

    //generate constuctor

    public AddMealData(int id, String foodname, String calories) {
        this.id = id;
        this.foodname = foodname;
        this.calories = calories;
    }

    //generate getter and setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }
}
