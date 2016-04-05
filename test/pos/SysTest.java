/*
 * WPS JUnit test of Sys method Login (the only method that returns)
 */
package pos;

import java.sql.*;
import junit.framework.TestCase;
import static pos.PointofSale.con;

public class SysTest extends TestCase {

    public SysTest(String testName) {
        super(testName);
    }

    /**
     * Test of login method, of class Sys.
     * login is the only method in Sys that performs any action without using a 
     * GUI interface. Every GUI has been tested as a standalone object, however
     * the sys class was never tested as so.
     * This uses a few different employee username/password combinations. Some 
     * are correct, but some are not. All insertions match the expected result,
     * and inputs. The method returns 2 if the user is a manager, a 1 if the 
     * user is an employee or a 0 if the user is not an employee. 
     */
    public void testLogin() {
        System.out.println("login");
        String username = "";
        String password = "";
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", "crb616cse216",
                    "cheng");
        } catch (SQLException sqe) {
            System.err.println("Unable to contact server");
            System.err.println(sqe.getMessage());
        }catch(ClassNotFoundException cnf){
            System.err.println("Unable to find class");
            System.err.println(cnf.getMessage());
        }
        Sys instance = new Sys(con);
        System.out.println("Beginning tests");
        assertEquals(2, instance.login("sickmedic", "0632"));
        assertEquals(1, instance.login("buschmaster", "1234"));
        assertEquals(0, instance.login("sickmedic", "0623"));
        assertEquals(1, instance.login("bbb", "0323"));
        assertEquals(0, instance.login("emat", "1234"));
        System.out.println("Test complete");

    }
}
