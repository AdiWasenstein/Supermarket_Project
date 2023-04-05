package Stock.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public abstract class AReport {
    int fields_count;
    String[] headers;
    ArrayList<String[]> records = new ArrayList<>();
    public abstract void initialize_records();
    public abstract String[] get_header();
    public void generate_report(){
        headers = get_header();
        fields_count = headers.length;
        initialize_records();
        int[] widths = columns_widths();
        print_seperator(widths);
        print_record(headers, widths);
        print_seperator(widths);
        for(String[] record : records)
            print_record(record, widths);
        print_seperator(widths);
    }
    public int[] columns_widths(){
        int[] widths= Arrays.stream(headers).mapToInt(String::length).toArray();
        for(String[] cells : records)
            for(int i = 0; i < fields_count; i++)
                widths[i] = Math.max(widths[i], cells[i].length());
        return widths;
    }
    private void print_seperator(int[] widths){
        for(int i = 0; i < fields_count; i++){
            String line = String.join("", Collections.nCopies(widths[i] + 2, "-"));
            System.out.print("+" + line + (i == fields_count - 1 ? "+" : ""));
        }
        System.out.println();
    }
    private void print_record(String[] cells, int[] widths){
        for(int i = 0; i < fields_count; i++){
            String cell = cells[i];
            String ver_str = i == fields_count - 1 ? "|" : "";
            System.out.printf("%s %-" + widths[i] + "s %s", "|", cell, ver_str);
        }
        System.out.println();
    }
}
