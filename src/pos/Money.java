/*
    Point of Sale System Project
    Authors: Clayton Barber, Brandon Barton, Declan Brennan, Maximilian Hasselbusch, Eric Metcalf
    Last Updated: 20 November 2015
 */
package pos;

public class Money {

    private double value;

    public Money(double value) {
        long temp = (long) value;
        double holder = (double) temp;
        if (holder == value) {
            this.value = value;
        } else {
            value *= 100;
            temp = (long) value;
            holder = (double) temp;
            if (holder == value) {
                this.value = value/100;
            } else {
                int cons = (int) ((value * 10) % 10);
                int x = (int) value;
                if (cons > 4) {
                    x += 1;
                }
                double y = x;
                y /= 100;
                this.value = y;
            }

        }
    }

    public double getValue() {
        return dbparse(value);
    }
    
    public void setValue(double d){
        value = dbparse(d);
    }
    
    public static double toDouble(Money m){
        return m.getValue();
    }
    public double toDouble(){
        return this.getValue();
    }
    public void add(double d1, double d2){
        this.value = dbparse(d1) + dbparse(d2);
    } 
    
    public void subtract(double d1, double d2){
        this.value = dbparse(d1) - dbparse(d2);
    }
    
    public double dbparse(double d){
        long temp = (long) d;
        double holder = (double) temp;
        if (holder == d) {
            return d;
        } else {
            d *= 100;
            temp = (long) d;
            holder = (double) temp;
            if (holder == d) {
                return (d / 100);
            } else {
                int cons = (int) ((d * 10) % 10);
                int x = (int) d;
                if (cons > 4) {
                    x += 1;
                }
                double y = (double) x;
                y /= 100;
                return y;
            }
        }
    }
    public String toString(){
        return String.format("%.2f", this.value);
    }
}
