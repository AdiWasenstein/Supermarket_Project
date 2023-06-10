package SuperLi.src.Presentation.GUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import javax.swing.*;


public abstract class AMenuGUI{
    static JFrame jFrame =  new JFrame();
    int screenHeight;
    int screenWidth;
    Color backgroundColor;
    public abstract void showMainMenu();
    public AMenuGUI()
    {
        setPage();
        this.screenHeight = jFrame.getHeight();
        this.screenWidth = jFrame.getWidth();
        backgroundColor = new Color(100,200,200);
    }
    private void setPage()
    {
        jFrame.setTitle("Super-Li");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //TODO : need to add a check that everything was saved
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setSize(screenSize.width, screenSize.height);
        jFrame.setResizable(false);
        jFrame.setVisible((true));
        jFrame.revalidate();
        //TODO: add icon
//        ImageIcon marketIcon = new ImageIcon("marketIcon.png");
//        this.setIconImage(marketIcon.getImage());
    }
    public int generateInt(String str){
        try{
            int number = Integer.parseInt(str);
            System.out.println(number);
            return number;
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
        return -1;
    }
    public double generateDouble(String str){
        try{
            double number = Double.parseDouble(str);
            System.out.println(number);
            return number;
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
        return -1;
    }
    private void changeScreen(LinkedList<JPanel> panels, int amountOfScreen){
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(backgroundColor);
        mainPanel.setLayout(new GridLayout(1, amountOfScreen));
        for(JPanel panel : panels)
            mainPanel.add(panel);
        for(int i = 0; i < amountOfScreen - panels.size(); i++){
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(backgroundColor);
            mainPanel.add(emptyPanel);
        }
        jFrame.getContentPane().removeAll();
        jFrame.getContentPane().add(mainPanel);
        jFrame.revalidate();
    }
    public void showOptionsMenu(LinkedList<String>optionsNames, LinkedList<Runnable> operations){
        int amountOfScreen = 3;
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(backgroundColor);
        buttonsPanel.setLayout(new GridLayout(10, 1));  // Maximum amount of buttons
        for(int optionIndex = 0; optionIndex < optionsNames.size(); optionIndex++){
            String optionName = optionsNames.get(optionIndex);
            Runnable operation = operations.get(optionIndex);
            JButton button = new JButton(optionName);
            button.addActionListener(e -> operation.run());
            buttonsPanel.add(button);
        }
        changeScreen(new LinkedList<>(List.of(buttonsPanel)), amountOfScreen);
    }
    public void showFillMenu(LinkedList<String> labelNames, Function<LinkedList<String>, Boolean> operation, String success, String failure, boolean returnAfterFinish) {
        int amountOfScreen = 3;
        JPanel fillPanel = new JPanel();
        fillPanel.setLayout(new GridLayout(10, 1));  // Maximum amount of buttons
        LinkedList<JTextField> fields = new LinkedList<>();
        for(String labelName : labelNames) {
            JTextField field = new JTextField(labelName);
            field.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    JTextField source = (JTextField)e.getComponent();
                    source.setText("");
                    source.removeFocusListener(this);
                }
            });
            fields.add(field);
            fillPanel.add(field);
        }
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            for(int i = 0; i < fields.size(); i++)
                fields.get(i).setText(labelNames.get(i));
        });
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            LinkedList<String> values = new LinkedList<>();
            for(JTextField field : fields)
                values.add(field.getText());
            boolean status = operation.apply(values);
            String message = status ? success : failure;
            int logo = status ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
            JOptionPane.showMessageDialog(jFrame.getComponent(0), message,
                    "Operation Status", logo);
            if(returnAfterFinish)
                showMainMenu();
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(10, 1));
        buttonsPanel.add(clearButton);buttonsPanel.add(submitButton);
        changeScreen(new LinkedList<>(Arrays.asList(fillPanel, buttonsPanel)), amountOfScreen);
    }
    public void showInfiniteFillMenu(LinkedList<String> labels, Function<LinkedList<String>, Boolean> operation, String success, String failure, boolean returnAfterFinish){
        // TODO
    }
    public void showTable(LinkedList<String> columns, LinkedList<LinkedList<String>> records){
        // TODO
    }
//    public static void writeHello(int x){
//        JOptionPane.showMessageDialog(jFrame.getComponent(0), "Please say hi to option " + x,
//                "Swing Tester", JOptionPane.WARNING_MESSAGE);
//    }
}