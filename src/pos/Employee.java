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

public class Employee {

    Connection con;

    public Employee(Connection con, Sys system) {
        this.con = con;
    }

    private int getNumEmployees() {
        int numemp;
        try {
            Statement s = con.createStatement();
            ResultSet result = s.executeQuery("select numemployees from dual");
            result.next();
            numemp = Integer.parseInt(result.getNString(1));
        } catch (SQLException sqe) {
            System.err.println("Unable to fetch numemployees from dual");
            numemp = -1;
        }
        return numemp;
    }

    void addEmployee() {
        EmployeeInfo eif = new EmployeeInfo(con);
    }

    void removeEmployee() {
        RemoveEmployeeInfo reif = new RemoveEmployeeInfo(con);
    }

    void editManPrivilege() {
        EditEmployeeInfo eeif = new EditEmployeeInfo(con);
    }

    void changePassword() {
        ChangePassword changePassword = new ChangePassword(con);
    }
}
