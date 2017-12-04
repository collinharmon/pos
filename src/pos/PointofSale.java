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
    static boolean isLoggedIn;

    public static void main(String[] args) throws ClassNotFoundException {
        Backround bg = new Backround();
        Class.forName("com.mysql.jdbc.Driver");
        try {
            //the below getConnection is left blank to maintain security and privacy.  Normally, the method would have parameters.
            con = DriverManager.getConnection("jdbc:mysql://thedbs.cxqavhggxnny.us-west-1.rds.amazonaws.com:3306/?user=pos_team", "pos_team", "Password");
            /*(Statement s2 = con.createStatement();
            ResultSet result2 = s2.executeQuery("select * from pos.game");
            while(result2.next()) System.out.println(result2.getNString(1));*/

            Statement s = con.createStatement();
            s.executeUpdate("set time_zone = 'US/Pacific'");
            system = new Sys(con);
            doLogin();
        } catch (Exception e) {
            System.out.println("Connection Error");
            offlinesystem = new OSys();
            Login l = new Login(offlinesystem);
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if(isLoggedIn) {
                    try {
                        Statement st = con.createStatement();
                        st.executeUpdate("update pos.employees set islogin = 0 where eid = '" + system.eid + "'");
                    } catch (SQLException se) {
                        System.err.println("Unable to log user out of system.");
                    }
                }
            }
        });
    }

    public static void doLogin() {
        LoginNew l = new LoginNew(system, con);
    }

    public static void doWork() {
        system.updateDatabase();
        SysFrame sf = new SysFrame(con, system);
        isLoggedIn = true;
    }

    public static void close() {
        System.exit(0);
    }

    static void doAdminWork() {
        system.updateDatabase();
        AdminFrame af = new AdminFrame(con, system);
        isLoggedIn = true;
    }

    static void doOffileWork() {
        OfflineFrame of = new OfflineFrame(offlinesystem);
    }
}
