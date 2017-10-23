/*
    Point of Sale System Project
    Authors: Clayton Barber, Brandon Barton, Declan Brennan, Maximilian Hasselbusch, Eric Metcalf
    Last Updated: 20 November 2015
 */
package pos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
            try{
                Statement s = con.createStatement();
                ResultSet res = s.executeQuery("select * from pos.games where sku = " + sku);
                if(res.next()){
                    int qty = res.getInt("quantity");
                    if(quant > qty) throw new ArithmeticException();
                }
            }
            catch(SQLException se){
                System.err.println("This should never print.");
            }
            l.add(it, quant, rental);
            subtotal.add(subtotal.getValue(), (quant * Money.toDouble(it.getPrice())));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void editPrice(int sku, Money newP, boolean rental) {
        if(newP.getValue() > 200) newP.setValue(200.00);
        Item it = new Item(sku, ps, con);
        Node n;
        if (it.getName() != null && (n = (l.getNode(it, rental))) != null) {
            if (n.getQuant() == 1 && n.under == null) {
                if(newP.getValue() != n.getItem().getPrice().getValue()){
                    subtotal.subtract(subtotal.getValue(), it.getPrice().toDouble() - newP.toDouble());
                    n.setPrice(newP);
                    n.getItem().setEdit();
                    printRecipt();
                }
                else printRecipt();
            } else {
                ItemSelectDialog isd = new ItemSelectDialog(n, newP);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    void removeItem(int iid, boolean rental) {
        Item it = new Item(iid, ps, con);
        Node n = l.getNode(it, rental);
        if(n != null) {
            if (n.getQuant() > 1 || n.under != null) {
                ItemSelectDialog isd = new ItemSelectDialog(n);
            } else {
                subtotal.subtract(subtotal.getValue(), n.getItem().getPrice().getValue());
                l.remove(n.getItem(), n.rental);
                printRecipt();
            }
        }
        else throw new IllegalArgumentException();
    }

    public void commitSale(String ccn) {
        int newoid = 0;
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("insert into pos.orders (eid) values ('" + PointofSale.system.eid + "')");
            //gets the highest oid in the table (which is the one generated up above, since it auto-incremenrs)
            stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select oid from pos.orders order by oid desc limit 1");
            if(result.next()) newoid = result.getInt(1);
        } catch (SQLException sqe) {
            System.err.println("Unable to insert into orders");
        }
        Node current = l.head;
        try {
            Statement s = con.createStatement();
            while (current != null) {
                s.executeUpdate("update pos.games set quantity = quantity - " + current.getQuant() + " where sku =  " + current.getItem().getID());
                s.executeUpdate("insert into pos.order_items (quantity, price, edited, sku, oid) values (" + current.getQuant() + ", " + current.getItem().getPrice().getValue() + ", " + current.getItem().isEdited() + ", " + current.getItem().getID() + ", " + newoid + ")");
                Node turtle = current.under;
                while(turtle != null){
                    s.executeUpdate("update pos.games set quantity = quantity - " + turtle.getQuant() + " where sku =  " + turtle.getItem().getID());
                    s.executeUpdate("insert into pos.order_items (quantity, price, edited, sku, oid) values (" + turtle.getQuant() + ", " + turtle.getItem().getPrice().getValue() + ", " + turtle.getItem().isEdited() + ", " + turtle.getItem().getID() + ", " + newoid + ")");
                    turtle = turtle.under;
                }
                current = current.next;
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
        //this.
        //System.out.print(this);
        Node current = l.head;
        while(current != null){
            System.out.println(current.toString());
            if(current.under != null){
                Node turtleDown = current.under;
                while(turtleDown != null){
                    System.out.println(turtleDown.toString());
                    turtleDown = turtleDown.under;
                }
            }
            current = current.next;
        }
        TaxCalculator tcalc = TaxCalculator.getInstance();
        tax.setValue(tcalc.calculateTax(subtotal));
        total.add(subtotal.getValue(), tax.getValue());
        System.out.println("____________________________________");
        System.out.printf("Subtotal:%45s\n", getSubtotal().toString());
        System.out.printf("Tax:        %45s\n", getTax().toString());
        System.out.printf("Total:     %45s\n", getTotal().toString());

    }

    //verify shopping cart game quantities before checking out...
    public boolean finalVerify(){
        Node current = l.head;
        Statement s;
        while(current != null){
            Node turtle = current;
            int itemQuant = 0;
            while(turtle != null) {
                itemQuant += turtle.getQuant();
                turtle = turtle.under;
            }
            try {
                s = con.createStatement();
                ResultSet res = s.executeQuery("select * from pos.games where sku =" + current.getItem().getID());
                if(res.next()){
                    if (itemQuant > res.getInt("quantity")) return false;
                }
            }
            catch(SQLException se){
                System.err.println("Unable to query item sku=" + current.getItem().getID());
            }
           current = current.next;
        }
        return true;
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
                    found = true;
                    if (n.getItem().getPrice().getValue() != i.getPrice().getValue()) {
                        while (n.under != null && n.under.getItem().getPrice().getValue() != i.getPrice().getValue())
                            n = n.under;
                        if (n.under == null) {
                            n.under = new Node(i, q, null, false);
                            n.under.above = n;
                        } else n.under.setQuant(q + n.under.getQuant());
                    } else n.setQuant(q + n.getQuant());
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

        /*added function 4:48 10/20*/
        private Node getNode(Item i, boolean rental) {
            boolean found = false;
            Node n = head;
            while (n != null) {
                if (n.getItem().isEqual(i) && (rental == n.rental)) {
                    found = true;
                    break;
                }
                n = n.next;
            }
            return n;
        }

        private boolean remove(Item i, boolean rental) {
            Node current = head;
            Node prev = null;
            while(current != null){
                if(current.getItem().isEqual(i) && current.rental == rental){
                    Node turtle = current;
                    while(turtle != null){
                        if(turtle.getItem().getPrice().getValue() == i.getPrice().getValue()){
                            if(turtle.above == null) { //then it is 'head' of the nested doubly linked list
                                if (turtle.under == null) {
                                    if (turtle == head) head = turtle.next;
                                    else prev.next = turtle.next;
                                    if(turtle == tail) tail = prev;
                                    return true;
                                }
                                else{ //else it is 'head' of the nested doubly linked list and has nodes under
                                    turtle.under.next = turtle.next;
                                    if(turtle == head) head = turtle.under;
                                    else prev.next = turtle.under;
                                    turtle.under.above = null;
                                    if(turtle == tail) tail = turtle.under;
                                    return true;
                                }
                            }
                            else{   //turtle is not 'head' of the nested doubly linked list and is some node under the 'head'
                                if(turtle.under != null) turtle.under.above = turtle.above;
                                turtle.above.under = turtle.under;
                                return true;
                            }
                        }
                        else turtle = turtle.under;
                    }
                }
                else{
                    prev = current;
                    current = current.next;
                }
            }
            return false;
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
        /*If someone buys 10 Sonic Hedgehogs and wants to apply a $1 discount to 3 of them, they get put in their separate node "under" with qty 3 as a different Item object*/
        /*Then if the customer applies a 50 cent discount to two of them, the original list will be one item object with qty 5. "under" it will be the $1 discount Item w/ qty 3, then under that will be 50 cent discount qty 2*/
        private Node under, above;
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
            this.quant = q;
        }

        public int getQuant() {
            return quant;
        }

        public void setPrice(Money p) {
            this.current.getPrice().setValue(p.getValue());
        }

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

    class ItemSelectDialog extends JDialog {

        //we'll need an arraylist of arraylists here to correspond with the node and node unders
        ArrayList<JTextField> boxes = new ArrayList<JTextField>();
        ArrayList<JPanel> jpans = new ArrayList<JPanel>();
        ArrayList<JLabel> jlabs = new ArrayList<JLabel>();
        ArrayList<uPair> thePairs;

        ArrayList<JPanel> drostes;;
        JButton ok = new JButton("Okay");
        JButton cancel = new JButton("Cancel");
        //ActionListener okLis, canceLis;
        int quant;
        Money newMoney;
        Node theNode;

        ItemSelectDialog(Node n, Money nM) {
            newMoney = nM;
            theNode = n;
            initComponents();
            setVisible(true);
        }
        ItemSelectDialog(Node n){
            theNode = n;
            newMoney = null;
            initComponents();
            setVisible(true);
        }

        public void initComponents() {
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    printRecipt();
                    dispose();
                }
            });
            Node current = theNode;
            int i = 0;
            while(current != null){
                boxes.add(new JTextField());
                jlabs.add(new JLabel(""+ current.getItem().getName() + " " + current.getItem().getPrice().toString() + " (" + current.getQuant() + "): "));
                jpans.add(new JPanel(new BorderLayout()));
                jpans.get(i).add(jlabs.get(i), BorderLayout.WEST);
                jpans.get(i).add(boxes.get(i), BorderLayout.CENTER);
                current = current.under;
                i++;
            }
            JPanel mainPanel = new JPanel(new BorderLayout());
            /*height of the edit items GUI will be a function of # of items to edit*/
            int height = 100;
            int size = jpans.size();
            if (size > 9) size = 9;
            if (size > 23) size = 13;
            if(jpans.size() > 1) height += (10+size)*jpans.size();
            if(size > 24) height = 130*25;
            setBounds(815, 0, 400, height);
            /*by here jpans.size can determine the boundaries of the window*/
            if(jpans.size() == 1) mainPanel = jpans.get(0);

            else{
                drostes = new ArrayList<JPanel>(jpans.size());
                mainPanel = drosteTime(0);
                //mainPanel.add(jpans.get(0), BorderLayout.NORTH);
                //mainPanel.add(jpans.get(1), BorderLayout.SOUTH);
            }
            JPanel buttHolder = new JPanel();
            buttHolder.add(ok);
            buttHolder.add(cancel);
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean error = okActionPerformed(e);
                    if(error == false){
                        if(newMoney != null){
                            for(int i = 0; i < thePairs.size(); i++){
                                Node current = theNode;
                                int index = thePairs.get(i).getIndex();
                                int qty = thePairs.get(i).getQuantity();
                                int j = 0;
                               while(current != null && j++ < index ) current = current.under;
                               if(current.getItem().getPrice().getValue() != newMoney.getValue()){
                                   current.setQuant(current.getQuant()-qty);
                                   Item it = new Item(current.getItem().getID(), ps, con);
                                   it.setPrice(newMoney);
                                   it.setEdit();
                                   l.add(it, qty, current.rental);
                                   subtotal.add(subtotal.getValue(), it.getPrice().getValue() * qty);
                                   subtotal.subtract(subtotal.getValue(), current.getItem().getPrice().getValue() * qty );
                                   if(current.getQuant() == 0) l.remove(current.getItem(), current.rental);
                               }
                            }
                            //then edit
                            //we have a linked list of the valid things to edit the price of
                        }
                        else{
                            for(int i = 0; i < thePairs.size(); i++){
                                Node current = theNode;
                                int index = thePairs.get(i).getIndex();
                                int qty = thePairs.get(i).getQuantity();
                                int j = 0;
                                while(current != null && j++ < index ) current = current.under;
                                current.setQuant(current.getQuant()-qty);
                                subtotal.subtract(subtotal.getValue(), current.getItem().getPrice().getValue() * qty );
                                if(current.getQuant() == 0) l.remove(current.getItem(), current.rental);
                            }
                            //then delete
                        }
                        PointofSale.system.s.printRecipt();
                        dispose();
                        /* then perhaps we set a class level bool var 'validPairList' which indicates if it is safe to delete/edit*/
                        //call new delete, which calls ogdelete if qty == 0;
                        //dispose
                    }
                }
            });
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    printRecipt();
                    dispose();
                }
            });
            getContentPane().add(buttHolder, BorderLayout.SOUTH);
            getContentPane().add(mainPanel, BorderLayout.CENTER);
        }

        public JPanel drosteTime(int count){
            drostes.add(new JPanel(new BorderLayout()));
            drostes.get(count).add(jpans.get(count), BorderLayout.NORTH);
            if(count + 2 == jpans.size()){
                drostes.get(count).add(jpans.get(count+1), BorderLayout.SOUTH);
                return drostes.get(count);
            }
            else{
                drostes.get(count).add(drosteTime(count+1), BorderLayout.SOUTH);
            }
            return drostes.get(count);
        }
            //name = new JLabel(n.getItem().getName());
        private boolean okActionPerformed(java.awt.event.ActionEvent e){
            boolean error = false;
            thePairs = new ArrayList<uPair>();
            Node current = theNode;
            int i = 0;
            while(current != null){
               int qty;
               if(!boxes.get(i).getText().isEmpty()){
                   try{
                       qty = Integer.parseInt(boxes.get(i).getText());
                       if(qty > current.getQuant() || qty < 0) throw new Exception();
                       else if(qty != 0){   //treat 0 like an empty string
                          thePairs.add(new uPair(i, qty));
                       }
                   }
                   catch(Exception ne){
                       boxes.get(i).setText("Invalid Input");
                       error = true;
                   }
               }
               current = current.under;
               i++;
            }
            return error;
        }
    }
    //uniquePair
    public class uPair{
        int index, quantity;
        uPair(int i, int q){
            index = i;
            quantity = q;
        }
        public int getIndex(){return this.index;}
        public int getQuantity(){return this.quantity;}
    }
}
