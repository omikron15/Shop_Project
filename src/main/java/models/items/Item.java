package models.items;

import db.DBHelper;
import models.Order;
import models.OrderQuantity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "items")
public abstract class Item {

    private int id;
    private String name;
    private double price;
    private String description;
    private List<OrderQuantity> orderQuantities;
    private String pictureLink;
    private int itemStock;

    public Item(String name, double price, String description, String pictureLink, int itemStock) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.pictureLink = pictureLink;
        this.orderQuantities = new ArrayList<OrderQuantity>();
        this.itemStock = itemStock;
    }

    public Item() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//  Fetch type changed from EAGER to LAZY
    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<OrderQuantity> getOrderQuantities() {
        return orderQuantities;
    }

    public void setOrderQuantities(List<OrderQuantity> orderQuantities) {
        this.orderQuantities = orderQuantities;
    }

    @Column(name = "picture_link")
    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    @Column(name = "item_stock")
    public int getItemStock() {
        return itemStock;
    }

    public void setItemStock(int itemStock) {
        this.itemStock = itemStock;
    }

    public void increaseStock(int stockValue){
        this.itemStock += stockValue;
    }

    public void decreaseStock(int stockValue){
        this.itemStock -= stockValue;
    }

    public void addOrderQuantityEntry(OrderQuantity orderQuantity){
        this.orderQuantities.add(orderQuantity);
    }

    public String itemType(){
        String result = this.getClass().toString();
        result = result.substring(result.lastIndexOf(".") + 1);
        return  result;
    }

    public static ArrayList<String> allItemTypes(){
        List<Item> allItems = DBHelper.getAll(Item.class);
        ArrayList<String> allItemClasses = new ArrayList<>();

        for (Item item : allItems){
            String itemclass = item.itemType();
            if(!allItemClasses.contains(itemclass)){
                allItemClasses.add(itemclass);
            }

        }
        Collections.sort(allItemClasses);
        return allItemClasses;
    }

    public Integer returnNumberOfItemInOrder(Order order){
        return DBHelper.showQuantityOfItemInOrder(order, this);
    }

    public String formatPrice(){
        return String.format ("£%.2f", this.getPrice());
    }

    public String returnItemTimesQuantityInOrder(Order order){
        double total = returnNumberOfItemInOrder(order) * this.getPrice();
        return String.format("£%.2f", total);
    }
}
