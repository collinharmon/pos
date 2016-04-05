/*
    Point of Sale System Project
    Authors: Clayton Barber, Brandon Barton, Declan Brennan, Maximilian Hasselbusch, Eric Metcalf
    Last Updated: 20 November 2015
 */
package pos;

import java.sql.Connection;

public class AdministrativeFunctions {

    Connection con;

    public AdministrativeFunctions(Connection con) {
        this.con = con;
    }

    void addItem() {
        AddNewItem ani = new AddNewItem(con);
    }

    void InventoryLevels() {
        InventoryLevels il = new InventoryLevels(con);
    }

    void removeItem() {
        RemoveOldItem roi = new RemoveOldItem(con);
    }

    void updatePrice() {
        UpdatePrice up = new UpdatePrice(con);
    }

}
