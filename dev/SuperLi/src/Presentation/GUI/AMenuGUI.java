package SuperLi.src.Presentation.GUI;
import java.awt.*;
import java.util.LinkedList;
import java.util.function.Function;
import javax.swing.*;


public class AMenuGUI extends JFrame{
    public AMenuGUI()
    {
        setPage();
    }
    public int generateInt(String str){
        // TODO
        return 0;
    }
    public double generateDouble(string str){
        // TODO
        return 0;
    }
    private void setPage()
    {
        this.setTitle("Super-Li");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //TODO : need to add a check that everything was saved
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width, screenSize.height);
        this.setResizable(false);
        this.setVisible((true));
        //TODO: add icon
//        ImageIcon marketIcon = new ImageIcon("marketIcon.png");
//        this.setIconImage(marketIcon.getImage());
        this.getContentPane().setBackground(new Color(100,200,200));
    }
    public void showOptions(LinkedList<String>optionsNames, LinkedList<Function<Integer, Void>> operations){
        JButton[] buttons = new JButton[optionsNames.size()];
        int i=0;
        for (String option: optionsNames) {
            buttons[i] = new JButton(option);
            i++;
        }
    }
    public LinkedList<String> fillMenu(LinkedList<String> labelNames) {
        LinkedList<JTextField> fields = new LinkedList<>();
        for(String labelName : labelNames) {
            JTextField field = new JTextField(labelName);
            // TODO - Set Size
            fields.add(field);
            add(field);
        }
        return new LinkedList<>();
    }
    public static void main(String[] args)
    {
        AMenuGUI b = new AMenuGUI();
    }
}