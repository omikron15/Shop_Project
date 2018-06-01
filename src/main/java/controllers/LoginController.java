package controllers;

import db.DBHelper;
import models.Customer;
import models.Order;
import spark.Request;
import spark.Response;
import spark.template.velocity.VelocityTemplateEngine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static spark.Spark.post;

public class LoginController {

    public LoginController(){
        this.setUpEndpoints();
    }

    private void setUpEndpoints(){

        post("/login/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Customer user = DBHelper.find(id, Customer.class);

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String currentDate = dateFormat.format(date);

            Order newOrder = new Order(currentDate, user);
            DBHelper.save(newOrder);

            req.session().attribute("currentCustomer", user);
            res.redirect("/");
            return null;
        }, new VelocityTemplateEngine());
    }

    public static boolean isLoggedIn(Request req, Response res){
        if( req.session().attribute("currentCustomer") == (null)){
            return false;
        }
        return true;
    }

    public static Customer getLoggedInCustomer(Request req, Response res) {
        Customer customer = req.session().attribute("currentCustomer");
        int id = customer.getId();
        return DBHelper.find(id, Customer.class);
    }

    public static void setupLoginInfo(Map<String, Object> model, Request req, Response res){

        boolean isLoggedIn = LoginController.isLoggedIn(req,res);
        if(isLoggedIn){
            Customer loggedInCutomer = LoginController.getLoggedInCustomer(req, res);
            model.put("user", loggedInCutomer);
        }
        model.put("isLoggedIn", isLoggedIn);
    }

}
