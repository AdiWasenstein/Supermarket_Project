package SuperLi.src.Presentation;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.time.LocalDate;

public abstract class AMenu {
    static Scanner input = new Scanner(System.in);

    public int input_number(){
        int num;
        try{
            num = input.nextInt();
        }
        catch(Exception e) {
            num = -1;
        }
        input.nextLine();
        return num;
    }
    public double input_double(){
        double num;
        try{
            num = input.nextDouble();
        }
        catch(Exception e){
            return -1;
        }
        input.nextLine();
        return num;
    }
    public LocalDate input_date(){
        String date = input.nextLine();
        try{
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("d/M/yy"));
        }
        catch(Exception e){
            return null;
        }
    }
}
