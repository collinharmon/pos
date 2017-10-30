/*
    Point of Sale System Project
    Authors: Clayton Barber, Brandon Barton, Declan Brennan, Maximilian Hasselbusch, Eric Metcalf
    Last Updated: 20 November 2015
 */
package pos;

public class TaxCalculator {
    private static TaxCalculator TaxCalculatorInstance = null; 
    TaxCalculator(){}
    
    public synchronized static TaxCalculator getInstance(){
        if(TaxCalculatorInstance == null){
            TaxCalculatorInstance = new TaxCalculator();
        }
        return TaxCalculatorInstance;
    }
    
    double calculateTax(Money subtotal){
       Money tax = new Money(subtotal.getValue() * 0.08);
       return tax.toDouble();
    }
}