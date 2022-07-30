package com.example.easylearn.CommonClasses;

public class DisplayData {
    private int id;
    private String image_links;
    private String model_links;
    private String name;
    private int cost;

    public DisplayData(int id, String image_links, String model_links, String name, int cost) {
        this.id = id;
        this.image_links = image_links;
        this.model_links = model_links;
        this.name = name;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_links() {
        return image_links;
    }

    public void setImage_links(String image_links) {
        this.image_links = image_links;
    }

    public String getModel_links() {
        return model_links;
    }

    public void setModel_links(String model_links) {
        this.model_links = model_links;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public DisplayData() {
    }

}
