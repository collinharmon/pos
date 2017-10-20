/*
    Point of Sale System Project
    Authors: Clayton Barber, Brandon Barton, Declan Brennan, Maximilian Hasselbusch, Eric Metcalf
    Last Updated: 20 November 2015
 */
package pos;

import java.io.PrintStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Sale {

    //This will be a linked list of items
    private PrintStream ps;
    LinkedListOfItems l;
    Money total = new Money(0);
    Money subtotal = new Money(0);
    Money tax = new Money(0);
    Connection con;

    //This will create the sales class
    public Sale(Connection con, PrintStream ps) {
        l = new LinkedListOfItems();
        this.con = con;
        this.ps = ps;
    }

    //This function adds the Item to the class
    public void addItem(int sku, int quant, boolean rental) {
        Item it = new Item(sku, ps, con);
        if (it.getName() != null) {
            l.add(it, quant, rental);
            subtotal.add(subtotal.getValue(), (quant * Money.toDouble(it.getPrice())));
        } else {
            System.out.println("Invalid SKU added nothing");

        }
    }

    void removeItem(int iid, boolean rental) {
        Item it = new Item(iid, ps, con);
        if (it.getName() != null && l.find(it, rental)) {
            int q = l.remove(it, rental);
            subtotal.subtract(subtotal.getValue(), (q * Money.toDouble(it.getPrice())));
        } else {
            System.out.println("Invalid SKU, removed nothing");
        }
    }

    public void commitSale(String ccn) {
        int newoid =0;
        try {
            Statement stmt = con.createStatement();
            //ResultSet result = stmt.executeQuery("select oid from pos.orders order by oid desc limit 1");
            //result.next();
            //newoid = result.getInt(1) + 1;
            stmt.executeUpdate("insert into pos.orders (eid) values ('" + PointofSale.system.eid + "')");
        }
        catch(SQLException sqe){
            System.err.println("Unable to insert into orders");
        }
        //s.executeUpdate("insert into orders values('" + tid + "', SYSDATE, " + getSubtotal() + ", " + getTax() + ", " + getTotal() + ")");
        Node temp = l.head;
        try {
            while (temp != null) {
                Statement s = con.createStatement();
                int num = 0;
                /*if (temp.rental) {
                    num = 1;
                    retid = tid;
                    retid = retid.concat("010");
                    s.executeUpdate("insert into RETTRANSACTION values ('" + retid + "', SYSDATE+30)");
                }*/

                s.executeUpdate("update pos.games set quantity = quantity - " + temp.getQuant() + " where sku =  " + temp.getItem().getID() );
                s.executeUpdate("insert into pos.order_items (quantity, price, sku, oid) values (" + temp.getQuant() + ", " + temp.getItem().getPrice().getValue() + ", " + temp.getItem().getID() + ", " + newoid + ")");
                temp = temp.next;
            }
        } catch (SQLException sqe) {
            System.err.println("Unable to update or insert");
            System.err.println(sqe.getMessage());
        }
        /*try {
            Statement s = con.createStatement();
            s.executeUpdate("insert into TRANSACTION values('" + tid + "', SYSDATE, " + getSubtotal() + ", " + getTax() + ", " + getTotal() + ")");
        } catch (SQLException sqe) {
            System.err.println("Unable to insert into TRANSACTION");
            System.err.println(sqe.getMessage());
        }
        if (retid != null) {
            Calendar c = new GregorianCalendar();
            c.add(Calendar.DATE, 30);
            java.util.Date d = c.getTime();
            System.out.println("Your RETURN ID: " + retid + ".\nItems are due back on: " + d.getDate() + "-" + (d.getMonth() + 1) + "-15");
        }*/
    }

    public boolean checknumber(String cnum) {
        if (cnum.equals("12345")) {
            return true;
        }
        int chars = cnum.length() - 10;
        if (chars < 0) {
            throw new IllegalArgumentException("Credit card number mustbe at least 10 digits");
        }
        cnum = cnum.substring(chars, 10 + chars);
        int sum = 0;
        for (int i = 0; i < cnum.length(); i++) {
            char tmp = cnum.charAt(i);
            int num = tmp - '0';
            int product;
            if (i % 2 != 0) {
                product = num;
            } else {
                product = num * 2;
            }
            if (product > 9) {
                product -= 9;
            }
            sum += product;
        }
        return (sum % 10 == 0);
    }

    public Money getTotal() {
        return total;

    }

    public Money getSubtotal() {
        return subtotal;
    }

    public Money getTax() {
        return tax;
    }

    public String toString() {
        String s = "";
        Node n = l.head;
        while (n != null) {
            if (n.isRental()) {
                s = s + n.toString() + "*\n";
            } else {
                s = s + n.toString() + "\n";
            }
            n = n.next;
        }
        return s;
    }

    void printRecipt() {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        System.out.println("             \n                " + df.format(cal.getTime()) + "\n                       Thank you\n____________________________________");
        System.out.print(this);
        TaxCalculator tcalc = TaxCalculator.getInstance();
        tax.setValue(tcalc.calculateTax(subtotal));
        total.add(subtotal.getValue(), tax.getValue());
        System.out.println("____________________________________");
        System.out.printf("Subtotal:%45s\n", getSubtotal().toString());
        System.out.printf("Tax:        %45s\n", getTax().toString());
        System.out.printf("Total:     %45s\n", getTotal().toString());

    }

    class LinkedListOfItems {

        Node head;
        Node tail;

        private LinkedListOfItems() {
            head = null;
            tail = null;
        }

        private boolean findAndUpdate(Item i, int q, boolean rental) {
            boolean found = false;
            Node n = head;
            while (n != null) {
                if (n.getItem().isEqual(i) && (rental == n.rental)) {
                    n.setQuant(q);
                    found = true;
                    break;
                }
                n = n.next;
            }
            return found;
        }

        private boolean find(Item i, boolean rental) {
            boolean found = false;
            Node n = head;
            while (n != null) {
                if (n.getItem().isEqual(i) && (rental == n.rental)) {
                    found = true;
                    break;
                }
                n = n.next;
            }
            return found;
        }

        private int remove(Item i, boolean rental) {
            Node current = head;
            Node prev = head;
            while (current != null) {
                if (current.getItem().isEqual(i) && rental == current.rental) {
                    if (current == head) {
                        head = head.next;
                    } else {
                        prev.next = current.next;
                    }
                    return current.quant;
                }
                if (current != head) {
                    prev = prev.next;
                }
                current = current.next;
            }
            return 0;
        }

        private void add(Item i, int q, boolean rental) {
            Node n = new Node(i, q, null, rental);
            Node temp = head;
            if (head == null) {
                head = n;
                tail = n;
            } else if (findAndUpdate(i, q, rental)) {

            } else {
                tail.setNext(n);
                tail = n;
            }
        }
    }

//This is the node class for creating the linked list
    class Node {

        private Node next;
        private Item current;
        private int quant;
        private boolean rental;

        private Node(Item current, int quant, Node next, boolean rental) {
            this.current = current;
            this.quant = quant;
            this.next = next;
            this.rental = rental;
        }

        private void setNext(Node next) {
            this.next = next;
        }

        private void setQuant(int q) {
            this.quant += q;
        }
        public int getQuant(){return quant;}

        boolean isRental() {
            return rental;
        }

        Item getItem() {
            return current;
        }

        @Override
        public String toString() {
            return "SKU: " + current.getID() + "\t" + current.getName() + "\nESRB: " + current.getESRB() + "\tQuantity: "
                    + quant + "\tPrice: " + current.getPrice();
        }
    }
}
