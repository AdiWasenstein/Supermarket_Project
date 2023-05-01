package SuperLi.src.Stock.Reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public abstract class AReport {
    public abstract ArrayList<String[]> initializeRecords();
    public abstract String[] getHeaders();
    public void generate_report(){
        String[] headers = getHeaders();
        ArrayList<String[]> records = initializeRecords();
        int[] widths = columns_widths(headers, records);
        print_seperator(widths, headers.length);
        print_record(headers, widths);
        print_seperator(widths, headers.length);
        for(String[] record : records)
            print_record(record, widths);
        print_seperator(widths, headers.length);
    }
    public int[] columns_widths(String[] headers, ArrayList<String[]> records){
        int[] widths= Arrays.stream(headers).mapToInt(String::length).toArray();
        for(String[] cells : records)
            for(int i = 0; i < headers.length; i++)
                widths[i] = Math.max(widths[i], cells[i].length());
        return widths;
    }
    private void print_seperator(int[] widths, int fieldsCount){
        for(int i = 0; i < fieldsCount; i++){
            String line = String.join("", Collections.nCopies(widths[i] + 2, "-"));
            System.out.print("+" + line + (i == fieldsCount - 1 ? "+" : ""));
        }
        System.out.println();
    }
    private void print_record(String[] cells, int[] widths){
        for(int i = 0; i < cells.length; i++){
            String cell = cells[i];
            String ver_str = i == cells.length - 1 ? "|" : "";
            System.out.printf("%s %-" + widths[i] + "s %s", "|", cell, ver_str);
        }
        System.out.println();
    }
}
