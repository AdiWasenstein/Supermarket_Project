package SuperLi.src.Presentation.GUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            return number;
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
        return -1;
    }
    public LocalDate generateDate(String date){
        try{
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("d/M/yy"));
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private void changeScreen(LinkedList<JComponent> components, int amountOfScreen){
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, amountOfScreen));
        for(JComponent component : components) {
            component.setBackground(backgroundColor);
            mainPanel.add(component);
        }
        for(int i = 0; i < amountOfScreen - components.size(); i++){
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(backgroundColor);
            mainPanel.add(emptyPanel);
        }
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            showMainMenu();
        });
        JPanel lastPanel = new JPanel();
        int placeOfHomeButton = 8;
        lastPanel.setLayout(new GridLayout(placeOfHomeButton, 1));
        lastPanel.setBackground(backgroundColor);
        for(int i = 0; i < placeOfHomeButton - 1; i++){
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(backgroundColor);
            lastPanel.add(emptyPanel);
        }
        JPanel homePanel = new JPanel();
        homePanel.setBackground(backgroundColor);
        homePanel.setLayout(new GridLayout(2, 1));
        homePanel.add(homeButton);
        lastPanel.add(homePanel);
        mainPanel.add(lastPanel);
        jFrame.getContentPane().removeAll();
        jFrame.getContentPane().add(mainPanel);
        jFrame.revalidate();
    }
    public void showOptionsMenu(LinkedList<String>optionsNames, LinkedList<Runnable> operations){
        int amountOfScreen = 3;
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(10, 1));  // Maximum amount of buttons
        for(int optionIndex = 0; optionIndex < optionsNames.size(); optionIndex++){
            String optionName = optionsNames.get(optionIndex);
            Runnable operation = operations.get(optionIndex);
            JButton button = new JButton(optionName); button.setHorizontalAlignment(SwingConstants.LEFT);
            button.addActionListener(e -> operation.run());
            buttonsPanel.add(button);
        }
        changeScreen(new LinkedList<>(List.of(buttonsPanel)), amountOfScreen);
    }
    public JPanel createFillMenu(LinkedList<String> labelNames, LinkedList<JComponent> fields, LinkedList<LinkedList<String>> optionForField) {
        JPanel fillPanel = new JPanel();
        fillPanel.setLayout(new GridLayout(10, 1));  // Maximum amount of buttons
        for(int i = 0; i < labelNames.size(); i++)
        {
            String labelName = labelNames.get(i);
            LinkedList<String> currentOptions = optionForField.get(i);
            JComponent field;
            if(currentOptions.size() == 0){
                field = new JTextField(labelName);
                field.addFocusListener(new FocusAdapter() {
                    public void focusGained(FocusEvent e) {
                        JTextField source = (JTextField) e.getComponent();
                        source.setText("");
                        source.removeFocusListener(this);
                    }
                });
            }
            else {
                currentOptions.add(0, labelName);
                field = new JComboBox<>(currentOptions.toArray());
            }
            fields.add(field);
            fillPanel.add(field);
        }
//        for (String labelName : labelNames) {
//            JTextField field = new JTextField(labelName);
//            field.addFocusListener(new FocusAdapter() {
//                public void focusGained(FocusEvent e) {
//                    JTextField source = (JTextField) e.getComponent();
//                    source.setText("");
//                    source.removeFocusListener(this);
//                }
//            });
//            fields.add(field);
//            fillPanel.add(field);
//        }
        return fillPanel;
    }
    public void showFillMenu(LinkedList<String> labelNames, LinkedList<LinkedList<String>> optionsForButton, Function<LinkedList<String>, Boolean> operation, String success, String failure, boolean returnAfterFinish, int amountOfScreen) {
        LinkedList<JComponent> fields = new LinkedList<>();
        JPanel fillPanel = createFillMenu(labelNames, fields, optionsForButton);
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            for(int i = 0; i < fields.size(); i++)
            {
                JComponent field = fields.get(i);
                if(field instanceof JTextField)
                    ((JTextField) field).setText(labelNames.get(i));
                else
                    ((JComboBox) field).setSelectedIndex(0);
            }
        });
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            LinkedList<String> values = new LinkedList<>();
            for(JComponent field : fields) {
                if (field instanceof JTextField)
                    values.add(((JTextField) field).getText());
                else
                    values.add(((JComboBox) field).getSelectedItem().toString());
            }
            boolean status = operation.apply(values);
            String message = status ? success : failure;
            int logo = status ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
            JOptionPane.showMessageDialog(jFrame.getComponent(0), message,
                    "Operation Status", logo);
            if(returnAfterFinish || !status)
                showMainMenu();
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(10, 1));
        buttonsPanel.add(clearButton);buttonsPanel.add(submitButton);
        changeScreen(new LinkedList<>(Arrays.asList(fillPanel, buttonsPanel)), amountOfScreen);
    }
    public void showInfiniteFillMenu(LinkedList<String> labelNames, LinkedList<LinkedList<String>> optionsForField,Function<LinkedList<LinkedList<String>>, Boolean> operation, String success, String failure, boolean returnAfterFinish, int amountOfScreen){
        LinkedList<JComponent> fields = new LinkedList<>();
        JPanel fillPanel = createFillMenu(labelNames, fields, optionsForField);
        LinkedList<LinkedList<String>> allValues = new LinkedList<>();
        Runnable clearContent = () -> {
            for(int i = 0; i < fields.size(); i++) {
                JComponent field = fields.get(i);
                if(field instanceof JTextField) {
                    ((JTextField) field).setText(labelNames.get(i));
                    field.addFocusListener(new FocusAdapter() {
                        public void focusGained(FocusEvent e) {
                            JTextField source = (JTextField) e.getComponent();
                            source.setText("");
                            source.removeFocusListener(this);
                        }
                    });
                }
                else
                    ((JComboBox) field).setSelectedIndex(0); //TODO check if works
            }
        };
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearContent.run());
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            LinkedList<String> currentFill = new LinkedList<>();
            int emptyCellsCount = 0;
            for(int i = 0; i < fields.size(); i++) {
                JComponent field = fields.get(i);
                String currentCell;
                if(field instanceof JTextField)
                    currentCell = ((JTextField) field).getText();
                else
                    currentCell = ((JComboBox) field).getSelectedItem().toString();
                String currentLabel = labelNames.get(i);
                boolean emptyCell = currentLabel.equals(currentCell);
                currentFill.add(emptyCell ? "" : currentCell);
                if(emptyCell)
                    emptyCellsCount++;
            }
            if(emptyCellsCount < fields.size())
                allValues.add(currentFill);
            clearContent.run();
        });
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            LinkedList<String> currentFill = new LinkedList<>();
            String currentCell;
            int emptyCellsCount = 0;
            for(int i = 0; i < fields.size(); i++) {
                JComponent field = fields.get(i);
                if (field instanceof JTextField)
                    currentCell = ((JTextField) field).getText();
                else
                    currentCell = ((JComboBox) field).getSelectedItem().toString();
                String currentLabel = labelNames.get(i);
                boolean emptyCell = currentLabel.equals(currentCell);
                currentFill.add(emptyCell ? "" : currentCell);
                if(emptyCell)
                    emptyCellsCount++;
            }
            if(emptyCellsCount < fields.size())
                allValues.add(currentFill);
            boolean status = operation.apply(allValues);
            String message = status ? success : failure;
            int logo = status ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
            JOptionPane.showMessageDialog(jFrame.getComponent(0), message,
                    "Operation Status", logo);
            if(returnAfterFinish)
                showMainMenu();
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(10, 1));
        buttonsPanel.add(clearButton); buttonsPanel.add(addButton); buttonsPanel.add(submitButton);
        changeScreen(new LinkedList<>(Arrays.asList(fillPanel, buttonsPanel)), amountOfScreen);
    }
    public void showTable(LinkedList<String> columns, LinkedList<LinkedList<String>> records, int amountFromScreen) {
        String[] columnsArray = columns.toArray(new String[0]);
        String[][] recordsArray = new String[records.size()][];
        int i = 0;
        for (LinkedList<String> record : records)
            recordsArray[i++] = record.toArray(new String[0]);
        JTable reportTable = new JTable(recordsArray, columnsArray);
        JScrollPane panel = new JScrollPane(reportTable);
        changeScreen(new LinkedList<>(Arrays.asList(panel)), amountFromScreen);
    }
//    public static void writeHello(int x){
//        JOptionPane.showMessageDialog(jFrame.getComponent(0), "Please say hi to option " + x,
//                "Swing Tester", JOptionPane.WARNING_MESSAGE);
//    }
//    public static void main(){
//        LinkedList<String> columns = new LinkedList<String>(Arrays.asList("Name", "ID"));
//        LinkedList<LinkedList<String>> records = new LinkedList<>();
//        LinkedList<String> record1 = new LinkedList<String>(Arrays.asList("Milk", "1"));
//        LinkedList<String> record2 = new LinkedList<String>(Arrays.asList("Chocolate", "2"));
//        LinkedList<String> record3 = new LinkedList<String>(Arrays.asList("Toilet Paper", "3"));
//        records.add(record1);records.add(record2);records.add(record3);
//    }
}