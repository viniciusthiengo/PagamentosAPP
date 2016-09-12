package br.com.thiengo.pagamentosapp.domain;

public class Product {
    private String id;
    private String name;
    private String description;
    private int stock;
    private double price;
    private int img;


    public Product( String ident, String n, String d, int s, double p, int i ){
        id = ident;
        name = n;
        description = d;
        stock = s;
        price = p;
        img = i;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getStockString() {
        return "Apenas "+String.valueOf(stock)+" no estoque.";
    }

    public double getPrice() {
        return price;
    }

    public String getPriceString() {
        return "R$ "+String.valueOf(price).replace('.', ',');
    }

    public int getImg() {
        return img;
    }
}
