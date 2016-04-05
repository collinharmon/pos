/*
    Point of Sale System Project
    Authors: Clayton Barber, Brandon Barton, Declan Brennan, Maximilian Hasselbusch, Eric Metcalf
    Last Updated: 20 November 2015
 */
package pos;

import java.io.PrintStream;

public class OSys {

    OSale s;
    private PrintStream ps;

    OSys() {

    }

    public boolean login(String password) {
        return password.equals("0248");
    }

    public void logout() {
        Login l = new Login(this);
    }

    public void buildSale() {
        s = new OSale(ps);
    }

}
