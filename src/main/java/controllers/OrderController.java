package controllers;

import db.DBHelper;
import models.Customer;
import models.Order;
import models.OrderQuantity;
import models.items.Item;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class OrderController {

    public OrderController(){
        this.setUpEndpoints();
    }

    private void setUpEndpoints(){

        get("/orders", (req,res) -> {
            Map<String, Object> model = new HashMap<>();

            LoginController.setupLoginInfo(model, req, res);

            List<Order> orders = DBHelper.getAll(Order.class);
            model.put("orders", orders);

            model.put("template", "templates/orders/index.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/orders/remove/:id", (req, res) -> {

            Customer customer = LoginController.getLoggedInCustomer(req, res);
            int customerId = customer.getId();
            Order basket = DBHelper.showCurrentOrder(customer);
            int basketId = basket.getId();
            int itemId = Integer.parseInt(req.params(":id"));
            Item item = DBHelper.find(itemId, Item.class);

            OrderQuantity orderEntry =  DBHelper.showOrderQuantityforItemInOrder(item, basket);
            DBHelper.delete(orderEntry);
            basket.updatePriceRemove(item.getPrice(), orderEntry.getQuantity());
            DBHelper.save(basket);

            res.redirect("/customers/" + basketId + "/order");
            return null;
        }, new VelocityTemplateEngine());

        post("/orders/complete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            LoginController.setupLoginInfo(model, req, res);

            Customer customer = LoginController.getLoggedInCustomer(req, res);
            int customerId = customer.getId();
            Order basket = DBHelper.showCurrentOrder(customer);

            basket.completeOrder(customer);

            res.redirect("/customers/"+ customerId );
            return null;
        }, new VelocityTemplateEngine());



        get("/orders/:id", (req,res) -> {
            Map<String, Object> model = new HashMap<>();
            LoginController.setupLoginInfo(model, req, res);

            int id = Integer.parseInt(req.params(":id"));
            Order order = DBHelper.find(id, Order.class);

            List<Item> items = DBHelper.listAllItemsForOrder(order);

            List<String> itemTypes = Item.allItemTypes();

            model.put("itemType", itemTypes);
            model.put("items", items);
            model.put("order", order);
            model.put("template", "templates/orders/show.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

    }
}
