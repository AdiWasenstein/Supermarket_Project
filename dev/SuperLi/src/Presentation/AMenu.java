package SuperLi.src.Presentation;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.time.LocalDate;

public abstract class AMenu {
    static Scanner input = new Scanner(System.in);
    public abstract void printMenu();
    public abstract void communicate();
    public int inputNumber(){
        int num;
        try{
            num = input.nextInt();
        }
        catch(Exception e) {
            num = -1;
        }
        input.nextLine();
        return num < 0 ? -1 : num;
    }
    public double inputDouble(){
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
    public LocalDate inputDate(){
        String date = input.nextLine();
        try{
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("d/M/yy"));
        }
        catch(Exception e){
            return null;
        }
    }
}
