package SuperLi.src;

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
    @Override
    public boolean equals(Object size){
        if(!(size instanceof Size other))
            return false;
        return amount == other.get_amount() && type == other.get_measureunit();
    }
}