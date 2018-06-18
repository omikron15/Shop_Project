package models;


import db.DBHelper;
import models.items.Item;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    private int id;
    private Customer customer;
    private double totalPrice;
    private Boolean completeOrder;
    private String date;
    private Set<OrderQuantity> orderQuantity;

    public Order(String date, Customer customer) {
        this.customer = customer;
        this.totalPrice = 0;
        this.completeOrder = false;
        this.date = date;
        this.orderQuantity = new HashSet<OrderQuantity>();
    }

    public Order() {
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

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Column(name = "total_price")
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Column(name = "OrderCompleted")
    public Boolean getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(Boolean completeOrder) {
        this.completeOrder = completeOrder;
    }

    @Column(name = "date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    public Set<OrderQuantity> getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Set<OrderQuantity> orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public void updatePriceAdd(double price, int quantity){
        this.totalPrice += (price * quantity);
    }

    public void updatePriceRemove(double price, int quantity){
        this.totalPrice -= (price * quantity);
    }

    public void changeOrderStatusToFalse(){
            this.completeOrder = false;
    }

    public void changeOrderStatusToTrue(){
        this.completeOrder = true;
    }

    public void addOrderQuantityToOrderQuantity(OrderQuantity orderQuantity){
        this.orderQuantity.add(orderQuantity);
    }

    public int totalItemsInOrder(){

        int total = 0;
        List<OrderQuantity> quantities = DBHelper.listAllOrderQuantitiesForOrder(this);

        for(OrderQuantity quantity : quantities){
            total += quantity.getQuantity();
        }
        return total;
    }

    public void completeOrder(Customer customer){
        if(customer.canAfford(getTotalPrice()) && (totalItemsInOrder() > 0) &&(hasEnoughStockForOrder())) {
            customer.reduceCustomerCash(getTotalPrice());
            updateStockTotals();
            this.completeOrder = true;

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String currentDate = dateFormat.format(date);
            Order newBasket = new Order(currentDate, customer);

            DBHelper.save(newBasket);
            DBHelper.save(customer);
            DBHelper.save(this);
        }

    }

    public String formatTotalPrice(){
        return String.format ("£%.2f", this.getTotalPrice());
    }

    public String displayOrderStatus(){
        if(this.completeOrder){
            return "Order Completed";
        }
        return "Order not Completed";
    }

    public void updateStockTotals(){

        List<OrderQuantity> quantities = DBHelper.listAllOrderQuantitiesForOrder(this);

        for(OrderQuantity eachItem : quantities){
            Item item = eachItem.getItem();
            int quantity = eachItem.getQuantity();
            item.decreaseStock(quantity);
            DBHelper.save(item);
        }

    }

    public boolean hasEnoughStockForOrder(){

        List<OrderQuantity> quantities = DBHelper.listAllOrderQuantitiesForOrder(this);

        for(OrderQuantity eachItem : quantities){
            int itemStock = eachItem.getItem().getItemStock();
            int requiredQuantity = eachItem.getQuantity();

            if(requiredQuantity > itemStock){
                return false;
            }
        }
        return true;
    }

}
