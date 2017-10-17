/*
    Point of Sale System Project
    Authors: Clayton Barber, Brandon Barton, Declan Brennan, Maximilian Hasselbusch, Eric Metcalf
    Last Updated: 20 November 2015
 */
package pos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PointofSale {

    public static Sys system;
    public static Connection con;
    public static OSys offlinesystem;

    public static void main(String[] args) throws ClassNotFoundException {
        Backround bg = new Backround();
        Class.forName("com.mysql.jdbc.Driver");
        try {
            //the below getConnection is left blank to maintain security and privacy.  Normally, the method would have parameters.
            con = DriverManager.getConnection("jdbc:mysql://thedbs.cxqavhggxnny.us-west-1.rds.amazonaws.com:3306/?user=collinharmon", "user_name_here", "password_here");
            Statement s2 = con.createStatement();
            ResultSet result2 = s2.executeQuery("select * from pos.game");
            while(result2.next()) System.out.println(result2.getNString(1));
            system = new Sys(con);
            doLogin();
        } catch (SQLException e) {
            System.out.println("Connection Error");
            offlinesystem = new OSys();
            Login l = new Login(offlinesystem);
        }
    }

    public static void doLogin() {
        LoginNew l = new LoginNew(system, con);
    }

    public static void doWork() {
        //system.updateDatabase();
        SysFrame sf = new SysFrame(con, system);
    }

    public static void close() {
        System.exit(0);
    }

    static void doAdminWork() {
        //system.updateDatabase();
        AdminFrame af = new AdminFrame(con, system);
    }

    static void doOffileWork() {
        OfflineFrame of = new OfflineFrame(offlinesystem);
    }
}
