/*
    Point of Sale System Project
    Authors: Clayton Barber, Brandon Barton, Declan Brennan, Maximilian Hasselbusch, Eric Metcalf
    Last Updated: 20 November 2015
 */
package pos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sys {

    boolean loggedIn;
    String error;
    Sale s;
    Connection con;
    private PrintStream ps;

    Sys(Connection con) {
        loggedIn = false;
        s = null;
        error = "No error";
        this.con = con;
    }

    public int login(String username, String password) {
        return 2;
        /*try {
            Statement smt = con.createStatement();
            String query = "select * from EMPLOYEE where UNAME = '" + username
                    + "' and PWORD = '" + password + "'";
            ResultSet res = smt.executeQuery(query);
            if (res.next()) {
                if (Integer.parseInt(res.getNString(5)) == 1) {
                    return 2;
                }
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException sqe) {
            System.err.println("Unable to find requested entries");
            return 0;
        }*/
    }

    public void logout() {
        LoginNew l = new LoginNew(this, con);
    }

    public void buildSale() {
        s = new Sale(con, ps);
    }

    public String getError() {
        return error;
    }

    public void addEmployee() {
        Employee e = new Employee(con, this);
        e.addEmployee();
    }

    void removeEmployee() {
        Employee e = new Employee(con, this);
        e.removeEmployee();
    }

    void editManPrivilege() {
        Employee e = new Employee(con, this);
        e.editManPrivilege();
    }

    void changePassword() {
        Employee e = new Employee(con, this);
        e.changePassword();
    }

    void addItem() {
        AdministrativeFunctions af = new AdministrativeFunctions(con);
        af.addItem();
    }

    void InventoryLevels() {
        AdministrativeFunctions af = new AdministrativeFunctions(con);
        af.InventoryLevels();
    }

    void removeItem() {
        AdministrativeFunctions af = new AdministrativeFunctions(con);
        af.removeItem();
    }

    void editPrice() {
        AdministrativeFunctions af = new AdministrativeFunctions(con);
        af.updatePrice();
    }

    void viewSales() {
        SalesFunctions sf = new SalesFunctions(con);
        sf.viewSales();
    }

    void voidTransaction() {
        SalesFunctions sf = new SalesFunctions(con);
        sf.voidTransaction();
    }

    void updateDatabase() {
        File file = new File("towrite.txt");
        if (file.exists()) {
            try {
                FileInputStream fs = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fs));
                while (br.ready()) {
                    String line = br.readLine();
                    //System.err.println(line);
                    Statement us = con.createStatement();
                    us.executeUpdate(line);
                }
                br.close();
                file.delete();
            } catch (FileNotFoundException ex) {
                System.err.println("Unable to find file");
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            } catch (SQLException ex) {
                System.err.println("SQL Problems: " + ex.getMessage());
            }
        }
        try {
            Statement q = con.createStatement();
            ResultSet result = q.executeQuery("select mtid from dual");
            result.next();
            File f = new File("peripherals.txt");
            if (!f.exists()) {
                f.createNewFile();
            }
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(result.getNString(1));
            bw.newLine();
            bw.close();
        } catch (SQLException sqe) {
            System.err.println("SQL BADNESS");
        } catch (IOException ex) {
            System.err.println("I/O BADNESS");
        }
    }
}
