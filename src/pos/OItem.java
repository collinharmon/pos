/*
    Point of Sale System Project
    Authors: Clayton Barber, Brandon Barton, Declan Brennan, Maximilian Hasselbusch, Eric Metcalf
    Last Updated: 20 November 2015
 */
package pos;

import java.io.PrintStream;

public class OItem {

    PrintStream ps;
    private final int id;
    private String name;
    private final Money price;

    public OItem(int id, Money price) {
        this.id = id;
        this.price = price;
    }

    public int getID() {
        return id;
    }

    public Money getPrice() {
        return price;
    }

    public boolean isEqual(OItem i) {
        return this.getID() == i.getID();
    }

}
