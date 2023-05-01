package SuperLi.src.BusinessLogic;
import java.util.*;

public class Category{
    ArrayList<String> categories;
    Size size;

    public Category(ArrayList<String> categories, Size size){
        this.categories = categories;
        this.size = size;
    }
    public String toString(){
        String categoriesStr = this.categories.toString().substring(1, this.categories.toString().length() - 2);
        return String.format("%s, %.1f %ss", categoriesStr, this.size.get_amount(), this.size.get_measureunit().name());
    }
    public ArrayList<String> getCategories(){ return this.categories;}
    public Size getSize(){return this.size;}
    public MeasureUnit getMeasureUnit(){return this.size.get_measureunit();}
    public double getSizeAmount(){
        return this.size.get_amount();
    }
    @Override
    public boolean equals(Object category){
        if(!(category instanceof Category other))
            return false;
        if(!size.equals(other.getSize()))
            return false;
        return categories.containsAll(other.getCategories());
    }
}
