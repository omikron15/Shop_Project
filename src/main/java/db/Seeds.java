package db;

import models.Customer;
import models.Order;
import models.OrderQuantity;
import models.items.*;

import java.util.List;

public class Seeds {

    public static void seedData(){
        Customer customer1 = new Customer("Connor", "CRUSER", "CRPASS", 1000.00);
//        Customer customer2 = new Customer("Harry Goose", "Goosey", "password ABC", 200.00);
//        Customer customer3 = new Customer("Sasha Buzzard", "Buzzy", "password XYZ", 300.00);
//
        DBHelper.save(customer1);
//        DBHelper.save(customer2);
//        DBHelper.save(customer3);
//
//        Order order1 = new Order("01/02/04", customer1);
//
//        DBHelper.save(order1);

        String pictureLink = "https://images.pexels.com/photos/110854/pexels-photo-110854.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=350";

        Clothing clothing1 = new Clothing("T-Shirt", 15.00, "White T-Shirt", Size.MEDIUM, pictureLink, 100);
        Food food1 = new Food("Tin of Tuna", 1.00, "Tasty Tuna!", "30/9/18", pictureLink, 100);
        Electronics electronics1 = new Electronics("Game Boy", 30.00, "Old school Game Boy", "V6", pictureLink, 50);
        Electronics electronics2 = new Electronics("Game Boy Advanced", 50.00, "Old school Game Boy Advance", "V6", pictureLink, 200);

        DBHelper.save(clothing1);
        DBHelper.save(food1);
        DBHelper.save(electronics1);
        DBHelper.save(electronics2);

//        DBHelper.addItemToOrder(clothing1, order1, 4);
//        DBHelper.addItemToOrder(food1, order1, 5);
//        DBHelper.addItemToOrder(electronics1, order1, 2);

//        Integer test = clothing1.returnNumberOfItemInOrder(order1);


//        List<OrderQuantity> orderOneOrderQuantites = DBHelper.listAllOrderQuantitiesForOrder(order1);
//        OrderQuantity testQuantity = orderOneOrderQuantites.get(0);
//        Item testitem = DBHelper.showItemForOrderQuantity(testQuantity);
//        List<Item> itemsInOrder1 = DBHelper.listAllItemsForOrder(order1);
//
//        List<Order> customer1Orders = DBHelper.listAllOrdersForCustomer(customer1);
//
//        List<Item> allItems = DBHelper.getAll(Item.class);
//        List<Customer> allCustomers = DBHelper.getAll(Customer.class);
//        List<Order> allOrder = DBHelper.getAll(Order.class);
//
//        List<Electronics> allElectronicItems = DBHelper.getAll(Electronics.class);
//        List<Food> allFoodItems = DBHelper.getAll(Food.class);
//        List<Clothing> allClothingItems = DBHelper.getAll(Clothing.class);

    }

}
