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
    String esrb;
    private Connection con;
    private String name;
    private Money price;

    public Item(int id, PrintStream ps, Connection con) {
        this.ps = ps;
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

    public Money getPrice() {
        return price;
    }

    public boolean isEqual(Item i) {
        return this.getID() == i.getID();
    }

}
