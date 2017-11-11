/*
    Point of Sale System Project
    Authors: Clayton Barber, Brandon Barton, Declan Brennan, Maximilian Hasselbusch, Eric Metcalf
    Last Updated: 20 November 2015
 */
package pos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/*Item tailored 1:08 AM 10/20*/
public class Item {

    PrintStream ps;
    private int id;
    private boolean isEdited;
    String esrb;
    String platform;
    private Connection con;
    private String name;
    private Money price;

    public Item(int id, PrintStream ps, Connection con) {
        this.ps = ps;
        isEdited = false;
        try {
            this.con = con;
            Statement s = this.con.createStatement();
            String query = "select * from pos.games where sku = " + id + "";
            ResultSet res = s.executeQuery(query);
            this.id = id;
            if (res.next()) {
                name = res.getString("name");
                price = new Money(res.getDouble("price"));
                esrb = res.getString("esrb");
                platform = res.getString("platform");
            }
        } catch (SQLException sqe) {
            System.err.println("Unable to find item");
        }
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getESRB(){
        return esrb;
    }
    public String getPlatform() { return platform; }
    public void setEdit(){
        isEdited = true;
    }
    public boolean isEdited(){
        return isEdited;
    }

    public Money getPrice() {
        return price;
    }
    public void setPrice(Money m) {price=m;}

    //redefined isEqual to mean same sku and price
    //imagine a scenario of someone buying three Super Mario 64s and they want to use a coupon on the game, or there is a buy 2 get 1 50% off.
    //the order_items table will have two entries in this scenario, one entry with normal price and 2 qty, and another with edited price and 1 qty, but both have same skus
    /*public boolean isHardEqual(Item i) {
        return (this.getID() == i.getID() && this.getPrice().getValue() == i.getPrice().getValue());
    }*/
    public boolean isEqual(Item i) {
        return (this.getID() == i.getID());
    }

}
