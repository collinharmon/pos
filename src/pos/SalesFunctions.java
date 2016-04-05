/*
    Point of Sale System Project
    Authors: Clayton Barber, Brandon Barton, Declan Brennan, Maximilian Hasselbusch, Eric Metcalf
    Last Updated: 20 November 2015
 */
package pos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class SalesFunctions {
    Connection con;
    
    public SalesFunctions(Connection con){
        this.con = con;
    }

    void viewSales() {
        SalesFrame sf = new SalesFrame(con);
    }

    void voidTransaction() {
        VoidFrame vf = new VoidFrame(con);
    }
    
    
}
