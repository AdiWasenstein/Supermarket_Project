package SuperLi.src.Presentation.GUI;

import SuperLi.src.BusinessLogic.AReport;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;


public abstract class AMenuGUI {
    static JFrame jFrame;
    Color backgroundColor;
    JPanel backgroundImagePanel;
    public abstract void showMainMenu();

    public void communicate(){
        showMainMenu();
    }
    public AMenuGUI()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        jFrame = new JFrame();
        jFrame.setTitle("Super-Li");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(screenWidth, screenHeight);
        jFrame.setResizable(false);
        jFrame.setVisible((true));
        backgroundColor = new Color(0,100,200, 30);
        backgroundImagePanel = new JPanel(new BorderLayout()) {
            public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(backgroundColor);
                URL resource = getClass().getClassLoader().getResource("Logo.png");
                try {
                    assert resource != null;
                    Image image = ImageIO.read(new File(resource.toURI()));
                    int imageWidth = ImageIO.read(new File(resource.toURI())).getWidth();
                    int imageHeight = ImageIO.read(new File(resource.toURI())).getHeight();
                    imageWidth = (int) (imageWidth * 1.5);
                    imageHeight = (int) (imageHeight * 1.5);
                    int x = screenWidth / 2 - imageWidth / 2;
                    int y = (int) (screenHeight / 2.5 - imageHeight / 2);
                    // Draw the background image.
                    g.drawImage(image, x, y,imageWidth, imageHeight, backgroundColor, this);
                }
                catch (Exception ignored){}
            }
        };
        jFrame.setContentPane(backgroundImagePanel);
        jFrame.getContentPane().revalidate();
        jFrame.getContentPane().repaint();
        jFrame.revalidate();
    }
    public int generateInt(String str){
        try{
            return Integer.parseInt(str);
        }
        catch (NumberFormatException ex){
            return -1;
        }
    }
    public double generateDouble(String str){
        try{
            return Double.parseDouble(str);
        }
        catch (NumberFormatException ex){
            return -1;
        }
    }
    public LocalDate generateDate(String date){
        try{
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("d/M/yy"));
        }
        catch(Exception e){
            return null;
        }
    }
    public void changeScreen(LinkedList<JComponent> components, int amountOfScreen){
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(1, amountOfScreen));
        contentPanel.setBackground(backgroundColor);
        for(JComponent component : components) {
            component.setBackground(backgroundColor);
            contentPanel.add(component);
        }
        for(int i = 0; i < amountOfScreen - components.size(); i++){
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(backgroundColor);
            contentPanel.add(emptyPanel);
        }
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> showMainMenu());
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
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(backgroundColor);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(lastPanel, BorderLayout.EAST);
        jFrame.getContentPane().removeAll();
        jFrame.getContentPane().revalidate();
        jFrame.getContentPane().repaint();
        jFrame.getContentPane().add(mainPanel);
        jFrame.revalidate();
    }

    public void resetFillOption(JComponent field){
        if(field instanceof JTextField)
            field.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    JTextField source = (JTextField) e.getComponent();
                    source.setText("");
                    source.removeFocusListener(this);
                }
            });
        else if (field instanceof JComboBox<?>)
            ((JComboBox<?>) field).setSelectedIndex(0);
        else
            System.out.println("Invalid type");
    }
    public String getInsertedValue(JComponent field, String fieldLabel){
        String str = null;
        if(field instanceof JTextField)
            str = ((JTextField) field).getText();
        else if (field instanceof JComboBox<?>) {
            Object value = ((JComboBox<?>) field).getSelectedItem();
            if(value != null)
                str = value.toString();
        }
        if(str == null)
            return null;
        return str.equals(fieldLabel) ? "" : str;
    }
    public void showMessage(boolean status, String successMessage, String failureMessage){
        String message = status ? successMessage : failureMessage;
        if (message.equals(""))
            return;
        int logo = status ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(jFrame.getComponent(0), message,
                "Operation Status", logo);
    }
    public void showOptionsMenu(LinkedList<String>optionsNames, LinkedList<Runnable> operations){
        int amountOfScreen = 3;
        JPanel buttonsPanel = getPanelOfButtons(optionsNames, operations, 10, 1);
        changeScreen(new LinkedList<>(List.of(buttonsPanel)), amountOfScreen);
    }
    public JPanel getPanelOfButtons(LinkedList<String>optionsNames, LinkedList<Runnable> operations, int rows, int cols){
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(rows, cols));  // Maximum amount of buttons
        buttonsPanel.setBackground(backgroundColor);
        for(int optionIndex = 0; optionIndex < optionsNames.size(); optionIndex++){
            String optionName = optionsNames.get(optionIndex);
            Runnable operation = operations.get(optionIndex);
            JButton button = new JButton(optionName); button.setHorizontalAlignment(SwingConstants.LEFT);
            button.addActionListener(e -> operation.run());
            buttonsPanel.add(button);
        }
        return buttonsPanel;
    }
    public JPanel createFillMenu(LinkedList<String> labelNames, LinkedList<JComponent> fields, LinkedList<LinkedList<String>> closeOptions) {
        JPanel fillPanel = new JPanel();
        fillPanel.setLayout(new GridLayout(10, 1));  // Maximum amount of buttons
        for(int i = 0; i < labelNames.size(); i++)
        {
            String labelName = labelNames.get(i);
            LinkedList<String> currentOptions = new LinkedList<>(closeOptions.get(i));
            JComponent field;
            if(currentOptions.size() == 0){
                field = new JTextField(labelName);
                resetFillOption(field);
            }
            else {
                currentOptions.add(0, labelName);
                field = new JComboBox<>(currentOptions.toArray());
            }
            fields.add(field);
            fillPanel.add(field);
        }
        return fillPanel;
    }
    public void showFillPage(LinkedList<String> labelNames, LinkedList<LinkedList<String>> optionsForButton, Function<LinkedList<String>, Boolean> operation, String success, String failure, boolean returnAfterFinish, int amountOfScreen) {
        LinkedList<JComponent> fields = new LinkedList<>();
        JPanel fillPanel = createFillMenu(labelNames, fields, optionsForButton);
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            for(int i = 0; i < fields.size(); i++)
            {
                JComponent field = fields.get(i);
                if(field instanceof JTextField)
                    ((JTextField) field).setText(labelNames.get(i));
                resetFillOption(field);
            }
        });
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            LinkedList<String> values = new LinkedList<>();
            for(int i = 0; i < fields.size(); i++)
            {
                JComponent field = fields.get(i);
                String fieldLabel = labelNames.get(i);
                values.add(getInsertedValue(field, fieldLabel));
            }
            boolean status = operation.apply(values);
            showMessage(status, success, failure);
            if(returnAfterFinish || !status)
                showMainMenu();
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(10, 1));
        buttonsPanel.add(clearButton);buttonsPanel.add(submitButton);
        changeScreen(new LinkedList<>(Arrays.asList(fillPanel, buttonsPanel)), amountOfScreen);
    }
    public void showInfiniteFillPage(LinkedList<String> labelNames, LinkedList<LinkedList<String>> optionsForField, Function<LinkedList<LinkedList<String>>, Boolean> operation, String success, String failure, boolean returnAfterFinish, int amountOfScreen){
        LinkedList<JComponent> fields = new LinkedList<>();
        JPanel fillPanel = createFillMenu(labelNames, fields, optionsForField);
        LinkedList<LinkedList<String>> allValues = new LinkedList<>();
        Runnable clearContent = () -> {
            for(int i = 0; i < fields.size(); i++) {
                JComponent field = fields.get(i);
                if(field instanceof JTextField)
                    ((JTextField) field).setText(labelNames.get(i));
                resetFillOption(field);
            }
        };
        Runnable collectContent = () -> {
            LinkedList<String> currentFill = new LinkedList<>();
            int emptyCellsCount = 0;
            for(int i = 0; i < fields.size(); i++) {
                JComponent field = fields.get(i);
                String currentLabel = labelNames.get(i);
                String currentCell = getInsertedValue(field, currentLabel);
                currentFill.add(currentCell);
                if(currentCell.equals(""))
                    emptyCellsCount++;
            }
            if(emptyCellsCount < fields.size())
                allValues.add(currentFill);
            clearContent.run();
        };
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearContent.run());
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> collectContent.run());
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            collectContent.run();
            boolean status = operation.apply(allValues);
            showMessage(status, success, failure);
            if(returnAfterFinish || !status)
                showMainMenu();
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(10, 1));
        buttonsPanel.add(clearButton); buttonsPanel.add(addButton); buttonsPanel.add(submitButton);
        changeScreen(new LinkedList<>(Arrays.asList(fillPanel, buttonsPanel)), amountOfScreen);
    }
//    public void showTable(LinkedList<String> columns, LinkedList<LinkedList<String>> records, int amountFromScreen) {
//        String[] columnsArray = columns.toArray(new String[0]);
//        String[][] recordsArray = new String[records.size()][];
//        int i = 0;
//        for (LinkedList<String> record : records)
//            recordsArray[i++] = record.toArray(new String[0]);
//        showTable(columnsArray, recordsArray, amountFromScreen);
//    }
//    public void showTable(String[] columns, String[][] records, int amountFromScreen){
//        JScrollPane panel = getTable(columns, records);
//        changeScreen(new LinkedList<>(List.of(panel)), amountFromScreen);
//    }
    public void showTable(AReport report){showTable(getTable(report), 1);}
    public void showTable(JScrollPane table, int amountFromScreen){
        changeScreen(new LinkedList<>(List.of(table)), amountFromScreen);
    }
    public JScrollPane getTable(AReport report){
        LinkedList<String[]> records = report.initializeRecords();
        String[][] recordsArray = new String[records.size()][];
        int i = 0;
        for (String[] record : records)
            recordsArray[i++] = record;
        return getTable(report.getHeaders(), recordsArray);
    }
    public JScrollPane getTable(String[] columns, String[][] records) {
        JTable reportTable = new JTable(records, columns);
        JScrollPane panel = new JScrollPane(reportTable);
        panel.getViewport().setBackground(backgroundColor);
        panel.setOpaque(false);
        return panel;
    }
    public void reportSelector(LinkedList<String> labelNames, LinkedList<AReport> reports, Function<Integer, Boolean> submitOperation, String success, String failure){
        LinkedList<JScrollPane> tables = new LinkedList<>();
        for(AReport report : reports)
            tables.add(getTable(report));
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BorderLayout());
        LinkedList<Runnable> operations = new LinkedList<>();
        JScrollPane[] currentSelectedTable = {tables.get(0)};
        totalPanel.add(currentSelectedTable[0], BorderLayout.CENTER);
        for(JScrollPane table : tables)
            operations.add(() -> {
                totalPanel.remove(currentSelectedTable[0]);
                currentSelectedTable[0] = table;
                totalPanel.add(table, BorderLayout.CENTER);
                changeScreen(new LinkedList<>(List.of(totalPanel)), 1);
            });
        JPanel buttonsPanel = getPanelOfButtons(labelNames, operations, 10, 1);
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            int selectedIndex = tables.indexOf(currentSelectedTable[0]);
            boolean status = submitOperation.apply(selectedIndex);
            showMessage(status, success, failure);
            showMainMenu();
        });
        totalPanel.add(buttonsPanel, BorderLayout.WEST);
        totalPanel.add(submitButton, BorderLayout.NORTH);
        changeScreen(new LinkedList<>(List.of(totalPanel)), 1);
    }

}