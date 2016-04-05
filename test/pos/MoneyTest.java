package pos;

import junit.framework.TestCase;

public class MoneyTest extends TestCase {

    public MoneyTest() {

    }

    public void testGetValue() {
        System.out.println("getValue");
        Money instance = new Money(0.0);
        double expResult = 0.0;
        double result = instance.getValue();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setValue method, of class Money.
     */
    public void testSetValue() {
        System.out.println("setValue");
        double d = 0.0;
        Money instance = new Money(d);
        instance.setValue(d);
    }

    /**
     * Test of toDouble method, of class Money.
     */
    public void testToDouble_Money() {
        System.out.println("toDouble");
        Money m = new Money(15);
        double expResult = 15.0;
        double result = Money.toDouble(m);
        assertEquals(expResult, result, 15.0);
    }

    /**
     * Test of toDouble method, of class Money.
     */
    public void testToDouble_0args() {
        System.out.println("toDouble");
        Money instance = new Money(584);
        double expResult = 584.0;
        double result = instance.toDouble();
        assertEquals(expResult, result, 584.0);
    }

    /**
     * Test of add method, of class Money.
     */
    public void testAdd() {
        System.out.println("add");
        double d1 = 6.0;
        double d2 = 5.0;
        Money instance = new Money(4);
        instance.add(d1, d2);
    }

    /**
     * Test of dbparse method, of class Money.
     */
    public void testDbparse() {
        System.out.println("dbparse");
        double d = 15.5847;
        Money instance = new Money(d);
        double expResult = 15.5847;
        double result = instance.dbparse(d);
        assertEquals(expResult, result, 15.5847);
    }

    /**
     * Test of toString method, of class Money.
     */
    public void testToString() {
        System.out.println("toString");
        Money instance = new Money(14);
        String expResult = "14.00";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
}