package BusinessLogic;

public class Size {
    double amount;
    MeasureUnit type;

    public Size(double amount, MeasureUnit type){
        this.amount = amount;
        this.type = type;
    }
    public double get_amount(){
        return this.amount;
    }
    public MeasureUnit get_measureunit(){
        return this.type;
    }
}