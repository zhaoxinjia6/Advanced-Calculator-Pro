import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class AdvancedCalculatorGUI extends JFrame implements ActionListener {

    // ========== 深色背景 + 暗色调稳重字体 ==========
    // 主背景
    private static final Color BG = new Color(22, 22, 32);
    // 显示框
    private static final Color DISPLAY_BG = new Color(12, 12, 22);
    // 数字按钮 - 深灰底 + 暗青色
    private static final Color NUM_BG = new Color(48, 52, 68);
    private static final Color NUM_TEXT = new Color(120, 200, 185);
    // 运算符按钮 - 暗橙底 + 暗橙色
    private static final Color OP_BG = new Color(130, 85, 25);
    private static final Color OP_TEXT = new Color(210, 175, 120);
    // 等号按钮 - 暗蓝底 + 暗蓝色
    private static final Color EQ_BG = new Color(25, 65, 160);
    private static final Color EQ_TEXT = new Color(150, 200, 230);
    // 清除按钮 - 暗红底 + 暗红色
    private static final Color CLR_BG = new Color(140, 35, 35);
    private static final Color CLR_TEXT = new Color(210, 155, 155);
    // 存储按钮 - 暗紫底 + 暗紫色
    private static final Color MEM_BG = new Color(65, 50, 100);
    private static final Color MEM_TEXT = new Color(190, 170, 220);
    // 科学函数 - 暗蓝底 + 暗蓝色
    private static final Color FUNC_BG = new Color(28, 40, 85);
    private static final Color FUNC_TEXT = new Color(150, 195, 225);
    // 统计按钮 - 暗绿底 + 暗绿色
    private static final Color STAT_BG = new Color(20, 90, 75);
    private static final Color STAT_TEXT = new Color(150, 210, 195);
    // 转换按钮 - 暗紫底 + 暗紫色
    private static final Color CONV_BG = new Color(75, 50, 125);
    private static final Color CONV_TEXT = new Color(185, 170, 220);
    // 正负号 - 暗灰底 + 暗黄色
    private static final Color SIGN_BG = new Color(52, 52, 72);
    private static final Color SIGN_TEXT = new Color(210, 200, 150);
    // 菜单栏
    private static final Color MENU_BG = new Color(28, 28, 42);
    private static final Color MENU_TEXT = new Color(180, 185, 200);

    private JTextField displayField;
    private JTextArea historyArea;
    private JTextArea statArea;
    private JLabel expressionLabel;
    private JLabel modeLabel;
    private AdvancedCalculator calculator;

    private double firstOperand = 0;
    private double secondOperand = 0;
    private String pendingOperator = "";
    private boolean isNewNumber = true;
    private double currentInput = 0;
    private String currentMode = "basic";

    public AdvancedCalculatorGUI() {
        calculator = new AdvancedCalculator();
        initUI();
    }

    private void initUI() {
        setTitle("Advanced Calculator Pro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(720, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(680, 720));
        getContentPane().setBackground(BG);

        createMenuBar();
        createDisplayPanel();
        createCenterPanel();

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(MENU_BG);
        menuBar.setBorder(BorderFactory.createLineBorder(new Color(45, 45, 60)));

        JMenu modeMenu = new JMenu(" Mode ");
        modeMenu.setForeground(MENU_TEXT);
        modeMenu.setBackground(MENU_BG);
        modeMenu.setFont(new Font("Dialog", Font.BOLD, 14));

        JMenuItem basicMode = new JMenuItem("Basic");
        basicMode.setBackground(MENU_BG);
        basicMode.setForeground(MENU_TEXT);
        basicMode.addActionListener(e -> switchMode("basic"));

        JMenuItem scientificMode = new JMenuItem("Scientific");
        scientificMode.setBackground(MENU_BG);
        scientificMode.setForeground(MENU_TEXT);
        scientificMode.addActionListener(e -> switchMode("scientific"));

        JMenuItem statMode = new JMenuItem("Statistics");
        statMode.setBackground(MENU_BG);
        statMode.setForeground(MENU_TEXT);
        statMode.addActionListener(e -> switchMode("statistics"));

        JMenuItem convertMode = new JMenuItem("Convert");
        convertMode.setBackground(MENU_BG);
        convertMode.setForeground(MENU_TEXT);
        convertMode.addActionListener(e -> switchMode("convert"));

        modeMenu.add(basicMode);
        modeMenu.add(scientificMode);
        modeMenu.add(statMode);
        modeMenu.add(convertMode);

        JMenu angleMenu = new JMenu(" Angle ");
        angleMenu.setForeground(MENU_TEXT);
        angleMenu.setBackground(MENU_BG);
        angleMenu.setFont(new Font("Dialog", Font.BOLD, 14));

        JMenuItem degMode = new JMenuItem("DEG");
        degMode.setBackground(MENU_BG);
        degMode.setForeground(MENU_TEXT);
        degMode.addActionListener(e -> { calculator.setAngleMode("deg"); modeLabel.setText("DEG"); });

        JMenuItem radMode = new JMenuItem("RAD");
        radMode.setBackground(MENU_BG);
        radMode.setForeground(MENU_TEXT);
        radMode.addActionListener(e -> { calculator.setAngleMode("rad"); modeLabel.setText("RAD"); });

        angleMenu.add(degMode);
        angleMenu.add(radMode);

        JMenu formatMenu = new JMenu(" Format ");
        formatMenu.setForeground(MENU_TEXT);
        formatMenu.setBackground(MENU_BG);
        formatMenu.setFont(new Font("Dialog", Font.BOLD, 14));

        JMenuItem decFormat = new JMenuItem("Decimal");
        decFormat.setBackground(MENU_BG);
        decFormat.setForeground(MENU_TEXT);
        decFormat.addActionListener(e -> { calculator.setNumberFormat("decimal"); updateDisplay(); });

        JMenuItem binFormat = new JMenuItem("Binary");
        binFormat.setBackground(MENU_BG);
        binFormat.setForeground(MENU_TEXT);
        binFormat.addActionListener(e -> { calculator.setNumberFormat("binary"); updateDisplay(); });

        JMenuItem octFormat = new JMenuItem("Octal");
        octFormat.setBackground(MENU_BG);
        octFormat.setForeground(MENU_TEXT);
        octFormat.addActionListener(e -> { calculator.setNumberFormat("octal"); updateDisplay(); });

        JMenuItem hexFormat = new JMenuItem("Hex");
        hexFormat.setBackground(MENU_BG);
        hexFormat.setForeground(MENU_TEXT);
        hexFormat.addActionListener(e -> { calculator.setNumberFormat("hex"); updateDisplay(); });

        formatMenu.add(decFormat);
        formatMenu.add(binFormat);
        formatMenu.add(octFormat);
        formatMenu.add(hexFormat);

        menuBar.add(modeMenu);
        menuBar.add(angleMenu);
        menuBar.add(formatMenu);

        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            menu.setBackground(MENU_BG);
            menu.setForeground(MENU_TEXT);
        }

        setJMenuBar(menuBar);
    }

    private void switchMode(String mode) {
        this.currentMode = mode;
        calculator.setCurrentMode(mode);
        modeLabel.setText(mode.toUpperCase());
        getContentPane().removeAll();
        createDisplayPanel();
        createCenterPanel();
        revalidate();
        repaint();
    }

    private void createDisplayPanel() {
        JPanel displayPanel = new JPanel(new BorderLayout(5, 5));
        displayPanel.setBackground(BG);
        displayPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG);

        modeLabel = new JLabel("BASIC");
        modeLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        modeLabel.setForeground(new Color(120, 200, 230));
        topPanel.add(modeLabel, BorderLayout.WEST);

        JLabel memLabel = new JLabel("M: 0");
        memLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        memLabel.setForeground(new Color(170, 175, 190));
        memLabel.setName("memLabel");
        topPanel.add(memLabel, BorderLayout.EAST);

        displayPanel.add(topPanel, BorderLayout.NORTH);

        expressionLabel = new JLabel(" ");
        expressionLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        expressionLabel.setForeground(new Color(160, 165, 180));
        expressionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        displayPanel.add(expressionLabel, BorderLayout.CENTER);

        displayField = new JTextField("0");
        displayField.setFont(new Font("Dialog", Font.BOLD, 52));
        displayField.setHorizontalAlignment(SwingConstants.RIGHT);
        displayField.setEditable(false);
        displayField.setBackground(DISPLAY_BG);
        displayField.setForeground(new Color(130, 210, 195));
        displayField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 55, 75), 2, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        displayPanel.add(displayField, BorderLayout.SOUTH);

        add(displayPanel, BorderLayout.NORTH);
    }

    private void createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(BG);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel leftPanel = new JPanel(new CardLayout());
        leftPanel.setPreferredSize(new Dimension(200, 0));
        leftPanel.setBackground(BG);

        // History Panel
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(new Color(20, 20, 35));
        historyPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 45, 60), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel historyTitle = new JLabel(" HISTORY ");
        historyTitle.setFont(new Font("Dialog", Font.BOLD, 16));
        historyTitle.setForeground(new Color(130, 210, 195));
        historyPanel.add(historyTitle, BorderLayout.NORTH);

        historyArea = new JTextArea();
        historyArea.setFont(new Font("Monospaced", Font.BOLD, 13));
        historyArea.setBackground(new Color(14, 14, 26));
        historyArea.setForeground(new Color(130, 210, 195));
        historyArea.setEditable(false);
        historyArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyScroll.setBackground(new Color(14, 14, 26));
        historyScroll.setBorder(BorderFactory.createLineBorder(new Color(38, 38, 55), 1));
        historyPanel.add(historyScroll, BorderLayout.CENTER);

        JButton clearHistoryBtn = new JButton("Clear");
        clearHistoryBtn.setFont(new Font("Dialog", Font.BOLD, 13));
        clearHistoryBtn.setBackground(new Color(50, 50, 72));
        clearHistoryBtn.setForeground(new Color(180, 185, 200));
        clearHistoryBtn.setFocusPainted(false);
        clearHistoryBtn.setBorder(BorderFactory.createLineBorder(new Color(45, 45, 60), 1));
        clearHistoryBtn.addActionListener(e -> {
            calculator.clearHistory();
            updateHistory();
        });
        historyPanel.add(clearHistoryBtn, BorderLayout.SOUTH);

        // Statistics Panel
        JPanel statPanel = new JPanel(new BorderLayout());
        statPanel.setBackground(new Color(20, 32, 42));
        statPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 45, 60), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel statTitle = new JLabel(" STATISTICS ");
        statTitle.setFont(new Font("Dialog", Font.BOLD, 16));
        statTitle.setForeground(new Color(210, 200, 140));
        statPanel.add(statTitle, BorderLayout.NORTH);

        statArea = new JTextArea();
        statArea.setFont(new Font("Monospaced", Font.BOLD, 13));
        statArea.setBackground(new Color(14, 26, 35));
        statArea.setForeground(new Color(210, 200, 140));
        statArea.setEditable(false);
        statArea.setText("No data");
        statArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane statScroll = new JScrollPane(statArea);
        statScroll.setBackground(new Color(14, 26, 35));
        statScroll.setBorder(BorderFactory.createLineBorder(new Color(38, 38, 55), 1));
        statPanel.add(statScroll, BorderLayout.CENTER);

        JPanel statBtnPanel = new JPanel(new FlowLayout());
        statBtnPanel.setBackground(new Color(20, 32, 42));
        JButton addDataBtn = new JButton("Add Data");
        addDataBtn.setFont(new Font("Dialog", Font.BOLD, 13));
        addDataBtn.setBackground(new Color(20, 90, 75));
        addDataBtn.setForeground(new Color(150, 210, 195));
        addDataBtn.setFocusPainted(false);
        addDataBtn.setBorder(BorderFactory.createLineBorder(new Color(18, 70, 60), 1));
        addDataBtn.addActionListener(e -> addDataPoint());
        statBtnPanel.add(addDataBtn);

        JButton clearDataBtn = new JButton("Clear");
        clearDataBtn.setFont(new Font("Dialog", Font.BOLD, 13));
        clearDataBtn.setBackground(new Color(130, 35, 35));
        clearDataBtn.setForeground(new Color(210, 155, 155));
        clearDataBtn.setFocusPainted(false);
        clearDataBtn.setBorder(BorderFactory.createLineBorder(new Color(100, 25, 25), 1));
        clearDataBtn.addActionListener(e -> {
            calculator.clearData();
            updateStat();
        });
        statBtnPanel.add(clearDataBtn);
        statPanel.add(statBtnPanel, BorderLayout.SOUTH);

        leftPanel.add(historyPanel, "history");
        leftPanel.add(statPanel, "statistics");
        leftPanel.add(historyPanel, "basic");
        leftPanel.add(historyPanel, "convert");

        centerPanel.add(leftPanel, BorderLayout.WEST);

        JPanel buttonPanel = createButtonPanel();
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(4, 4, 4, 4);

        String[][] basicButtons = {
                {"MC", "MR", "M+", "M-", "C"},
                {"x2", "sqrt", "!", "+/-", "<<"},
                {"7", "8", "9", "/", "CE"},
                {"4", "5", "6", "*", ""},
                {"1", "2", "3", "-", ""},
                {"0", ".", "=", "+", ""}
        };

        String[][] currentLayout = basicButtons;
        if (currentMode.equals("scientific")) {
            currentLayout = new String[][]{
                    {"sin", "cos", "tan", "log", "ln"},
                    {"asin", "acos", "atan", "log2", "pi"},
                    {"x2", "x3", "sqrt", "cbrt", "e"},
                    {"1/x", "x!", "x^y", "(", ")"},
                    {"7", "8", "9", "/", "C"},
                    {"4", "5", "6", "*", "CE"},
                    {"1", "2", "3", "-", "<<"},
                    {"0", ".", "=", "+", "+/-"}
            };
        } else if (currentMode.equals("statistics")) {
            currentLayout = new String[][]{
                    {"Add", "Clear", "Mean", "Var"},
                    {"StdDev", "Min", "Max", "Median"},
                    {"Sum", "Count", "C", "CE"},
                    {"7", "8", "9", "/"},
                    {"4", "5", "6", "*"},
                    {"1", "2", "3", "-"},
                    {"0", ".", "=", "+"}
            };
        } else if (currentMode.equals("convert")) {
            currentLayout = new String[][]{
                    {"C->F", "F->C", "C->K", "K->C"},
                    {"cm->in", "in->cm", "km->mi", "mi->km"},
                    {"C", "CE", "<<", "+/-"},
                    {"7", "8", "9", "/"},
                    {"4", "5", "6", "*"},
                    {"1", "2", "3", "-"},
                    {"0", ".", "=", "+"}
            };
        }

        for (int row = 0; row < currentLayout.length; row++) {
            for (int col = 0; col < currentLayout[row].length; col++) {
                String text = currentLayout[row][col];
                if (text == null || text.isEmpty()) continue;

                JButton btn = new JButton(text);
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(45, 45, 62), 1, true),
                        BorderFactory.createEmptyBorder(10, 8, 10, 8)
                ));
                btn.addActionListener(this);

                if (text.matches("[0-9]") || text.equals(".")) {
                    btn.setBackground(NUM_BG);
                    btn.setForeground(NUM_TEXT);
                    btn.setFont(new Font("Dialog", Font.BOLD, 24));
                } else if (text.equals("=")) {
                    btn.setBackground(EQ_BG);
                    btn.setForeground(EQ_TEXT);
                    btn.setFont(new Font("Dialog", Font.BOLD, 32));
                } else if (text.matches("[+\\-*/%]")) {
                    btn.setBackground(OP_BG);
                    btn.setForeground(OP_TEXT);
                    btn.setFont(new Font("Dialog", Font.BOLD, 22));
                } else if (text.equals("C") || text.equals("CE") || text.equals("<<")) {
                    btn.setBackground(CLR_BG);
                    btn.setForeground(CLR_TEXT);
                    btn.setFont(new Font("Dialog", Font.BOLD, 18));
                } else if (text.matches("MC|MR|M\\+|M-")) {
                    btn.setBackground(MEM_BG);
                    btn.setForeground(MEM_TEXT);
                    btn.setFont(new Font("Dialog", Font.BOLD, 14));
                } else if (text.matches("sin|cos|tan|asin|acos|atan|log|ln|log2|x2|x3|sqrt|cbrt|1/x|x!|x\\^y|pi|e")) {
                    btn.setBackground(FUNC_BG);
                    btn.setForeground(FUNC_TEXT);
                    btn.setFont(new Font("Dialog", Font.BOLD, 14));
                } else if (text.equals("+/-")) {
                    btn.setBackground(SIGN_BG);
                    btn.setForeground(SIGN_TEXT);
                    btn.setFont(new Font("Dialog", Font.BOLD, 18));
                } else if (text.matches("Add|Clear|Mean|Var|StdDev|Min|Max|Median|Sum|Count")) {
                    btn.setBackground(STAT_BG);
                    btn.setForeground(STAT_TEXT);
                    btn.setFont(new Font("Dialog", Font.BOLD, 13));
                } else if (text.matches("C->F|F->C|C->K|K->C|cm->in|in->cm|km->mi|mi->km")) {
                    btn.setBackground(CONV_BG);
                    btn.setForeground(CONV_TEXT);
                    btn.setFont(new Font("Dialog", Font.BOLD, 12));
                } else {
                    btn.setBackground(new Color(46, 46, 66));
                    btn.setForeground(new Color(180, 185, 200));
                    btn.setFont(new Font("Dialog", Font.BOLD, 16));
                }

                gbc.gridx = col;
                gbc.gridy = row;
                if (text.equals("=")) {
                    gbc.gridheight = 2;
                } else {
                    gbc.gridheight = 1;
                }
                panel.add(btn, gbc);
            }
        }

        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        try {
            if (cmd.matches("[0-9]")) {
                if (isNewNumber) {
                    currentInput = Double.parseDouble(cmd);
                    isNewNumber = false;
                } else {
                    currentInput = Double.parseDouble(displayField.getText() + cmd);
                }
                calculator.setCurrentValue(currentInput);
                updateDisplay();
            } else if (cmd.equals(".")) {
                if (isNewNumber) {
                    currentInput = 0.0;
                    isNewNumber = false;
                }
                if (!displayField.getText().contains(".")) {
                    displayField.setText(displayField.getText() + ".");
                    currentInput = Double.parseDouble(displayField.getText());
                    calculator.setCurrentValue(currentInput);
                }
            } else if (cmd.matches("[+\\-*/%]")) {
                if (!isNewNumber) {
                    if (!pendingOperator.isEmpty()) {
                        performCalculation();
                    }
                    firstOperand = currentInput;
                } else {
                    firstOperand = currentInput;
                }
                pendingOperator = cmd;
                isNewNumber = true;
                expressionLabel.setText(firstOperand + " " + cmd + " ");
                updateDisplay();
            } else if (cmd.equals("=")) {
                if (!pendingOperator.isEmpty()) {
                    secondOperand = currentInput;
                    performCalculation();
                    pendingOperator = "";
                    isNewNumber = true;
                    expressionLabel.setText(firstOperand + " = ");
                    updateDisplay();
                    updateHistory();
                } else {
                    calculator.setCurrentValue(currentInput);
                    updateDisplay();
                }
            } else if (cmd.equals("C")) {
                firstOperand = 0;
                secondOperand = 0;
                currentInput = 0;
                pendingOperator = "";
                isNewNumber = true;
                calculator.clear();
                expressionLabel.setText(" ");
                updateDisplay();
            } else if (cmd.equals("CE")) {
                currentInput = 0;
                isNewNumber = true;
                displayField.setText("0");
            } else if (cmd.equals("<<")) {
                String text = displayField.getText();
                if (text.length() > 1) {
                    text = text.substring(0, text.length() - 1);
                    if (text.equals("-")) { text = "0"; }
                    currentInput = Double.parseDouble(text);
                    calculator.setCurrentValue(currentInput);
                    updateDisplay();
                } else {
                    displayField.setText("0");
                    currentInput = 0;
                    calculator.setCurrentValue(0);
                }
            } else if (cmd.equals("+/-")) {
                currentInput = -currentInput;
                calculator.setCurrentValue(currentInput);
                updateDisplay();
            } else if (cmd.equals("x2")) {
                double result = currentInput * currentInput;
                currentInput = result;
                calculator.setCurrentValue(result);
                expressionLabel.setText("sqr(" + currentInput + ")");
                isNewNumber = true;
                updateDisplay();
                updateHistory();
            } else if (cmd.equals("sqrt")) {
                if (currentInput < 0) {
                    throw new IllegalArgumentException("Cannot sqrt negative!");
                }
                double result = Math.sqrt(currentInput);
                currentInput = result;
                calculator.setCurrentValue(result);
                expressionLabel.setText("sqrt(" + currentInput + ")");
                isNewNumber = true;
                updateDisplay();
                updateHistory();
            } else if (cmd.equals("!")) {
                long val = (long) currentInput;
                if (currentInput < 0 || currentInput != val || val > 20) {
                    throw new IllegalArgumentException("Factorial: 0-20 only!");
                }
                long result = 1;
                for (int i = 2; i <= val; i++) {
                    result *= i;
                }
                currentInput = result;
                calculator.setCurrentValue(result);
                expressionLabel.setText("! = " + currentInput);
                isNewNumber = true;
                updateDisplay();
                updateHistory();
            } else if (cmd.equals("MC")) {
                calculator.memoryClear();
                updateDisplay();
            } else if (cmd.equals("MR")) {
                calculator.memoryRecall();
                currentInput = calculator.getCurrentValue();
                isNewNumber = true;
                updateDisplay();
            } else if (cmd.equals("M+")) {
                calculator.memoryAdd();
                updateDisplay();
            } else if (cmd.equals("M-")) {
                calculator.memorySubtract();
                updateDisplay();
            } else if (cmd.equals("sin")) {
                calculator.sin();
                currentInput = calculator.getCurrentValue();
                isNewNumber = true;
                updateDisplay();
                updateHistory();
            } else if (cmd.equals("cos")) {
                calculator.cos();
                currentInput = calculator.getCurrentValue();
                isNewNumber = true;
                updateDisplay();
                updateHistory();
            } else if (cmd.equals("tan")) {
                calculator.tan();
                currentInput = calculator.getCurrentValue();
                isNewNumber = true;
                updateDisplay();
                updateHistory();
            } else if (cmd.equals("log")) {
                calculator.log10();
                currentInput = calculator.getCurrentValue();
                isNewNumber = true;
                updateDisplay();
                updateHistory();
            } else if (cmd.equals("ln")) {
                calculator.ln();
                currentInput = calculator.getCurrentValue();
                isNewNumber = true;
                updateDisplay();
                updateHistory();
            } else if (cmd.equals("pi")) {
                calculator.inputPi();
                currentInput = calculator.getCurrentValue();
                isNewNumber = true;
                updateDisplay();
                updateHistory();
            } else if (cmd.equals("e")) {
                calculator.inputE();
                currentInput = calculator.getCurrentValue();
                isNewNumber = true;
                updateDisplay();
                updateHistory();
            } else if (cmd.equals("Add")) {
                addDataPoint();
            } else if (cmd.equals("Clear")) {
                calculator.clearData();
                updateStat();
            } else if (cmd.equals("Mean")) {
                if (calculator.getDataCount() > 0) {
                    double result = calculator.getDataMean();
                    currentInput = result;
                    calculator.setCurrentValue(result);
                    updateDisplay();
                }
            } else if (cmd.equals("Var")) {
                if (calculator.getDataCount() > 1) {
                    double result = calculator.getDataVariance();
                    currentInput = result;
                    calculator.setCurrentValue(result);
                    updateDisplay();
                }
            } else if (cmd.equals("StdDev")) {
                if (calculator.getDataCount() > 1) {
                    double result = calculator.getDataStdDev();
                    currentInput = result;
                    calculator.setCurrentValue(result);
                    updateDisplay();
                }
            } else if (cmd.equals("Min")) {
                if (calculator.getDataCount() > 0) {
                    double result = calculator.getDataMin();
                    currentInput = result;
                    calculator.setCurrentValue(result);
                    updateDisplay();
                }
            } else if (cmd.equals("Max")) {
                if (calculator.getDataCount() > 0) {
                    double result = calculator.getDataMax();
                    currentInput = result;
                    calculator.setCurrentValue(result);
                    updateDisplay();
                }
            } else if (cmd.equals("Median")) {
                if (calculator.getDataCount() > 0) {
                    double result = calculator.getDataMedian();
                    currentInput = result;
                    calculator.setCurrentValue(result);
                    updateDisplay();
                }
            } else if (cmd.equals("Sum")) {
                if (calculator.getDataCount() > 0) {
                    double result = calculator.getDataSum();
                    currentInput = result;
                    calculator.setCurrentValue(result);
                    updateDisplay();
                }
            } else if (cmd.equals("Count")) {
                int count = calculator.getDataCount();
                currentInput = count;
                calculator.setCurrentValue(count);
                updateDisplay();
            } else if (cmd.equals("C->F")) {
                double result = calculator.celsiusToFahrenheit(currentInput);
                currentInput = result;
                calculator.setCurrentValue(result);
                updateDisplay();
            } else if (cmd.equals("F->C")) {
                double result = calculator.fahrenheitToCelsius(currentInput);
                currentInput = result;
                calculator.setCurrentValue(result);
                updateDisplay();
            } else if (cmd.equals("C->K")) {
                double result = calculator.celsiusToKelvin(currentInput);
                currentInput = result;
                calculator.setCurrentValue(result);
                updateDisplay();
            } else if (cmd.equals("K->C")) {
                double result = calculator.kelvinToCelsius(currentInput);
                currentInput = result;
                calculator.setCurrentValue(result);
                updateDisplay();
            } else if (cmd.equals("cm->in")) {
                double result = calculator.cmToInch(currentInput);
                currentInput = result;
                calculator.setCurrentValue(result);
                updateDisplay();
            } else if (cmd.equals("in->cm")) {
                double result = calculator.inchToCm(currentInput);
                currentInput = result;
                calculator.setCurrentValue(result);
                updateDisplay();
            } else if (cmd.equals("km->mi")) {
                double result = calculator.kmToMile(currentInput);
                currentInput = result;
                calculator.setCurrentValue(result);
                updateDisplay();
            } else if (cmd.equals("mi->km")) {
                double result = calculator.mileToKm(currentInput);
                currentInput = result;
                calculator.setCurrentValue(result);
                updateDisplay();
            } else if (cmd.equals("x^y")) {
                JOptionPane.showMessageDialog(this, "x^y: 输入指数后按 =", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

            updateMemoryLabel();

        } catch (Exception ex) {
            displayField.setText("ERROR");
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            calculator.clear();
            updateDisplay();
        }
    }

    private void performCalculation() {
        double result = 0;
        switch (pendingOperator) {
            case "+":
                result = firstOperand + secondOperand;
                break;
            case "-":
                result = firstOperand - secondOperand;
                break;
            case "*":
                result = firstOperand * secondOperand;
                break;
            case "/":
                if (secondOperand == 0) {
                    throw new ArithmeticException("Divide by zero!");
                }
                result = firstOperand / secondOperand;
                break;
            case "%":
                if (secondOperand == 0) {
                    throw new ArithmeticException("Modulo by zero!");
                }
                result = firstOperand % secondOperand;
                break;
            default:
                result = secondOperand;
        }
        calculator.setCurrentValue(result);
        firstOperand = result;
        currentInput = result;
        displayField.setText(calculator.getDisplayValue());
    }

    private void addDataPoint() {
        String input = JOptionPane.showInputDialog(this, "Enter a number:", "Add Data", JOptionPane.PLAIN_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                double value = Double.parseDouble(input.trim());
                calculator.addDataPoint(value);
                updateStat();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateDisplay() {
        displayField.setText(calculator.getDisplayValue());
    }

    private void updateHistory() {
        StringBuilder sb = new StringBuilder();
        for (AdvancedCalculator.HistoryEntry entry : calculator.getHistory()) {
            sb.append(entry.toString()).append("\n");
        }
        historyArea.setText(sb.toString());
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }

    private void updateStat() {
        statArea.setText(calculator.getDataSummary());
    }

    private void updateMemoryLabel() {
        Component[] comps = getContentPane().getComponents();
        for (Component c : comps) {
            if (c instanceof JPanel) {
                for (Component sub : ((JPanel) c).getComponents()) {
                    if (sub instanceof JPanel) {
                        for (Component sub2 : ((JPanel) sub).getComponents()) {
                            if (sub2 instanceof JLabel && "memLabel".equals(((JLabel) sub2).getName())) {
                                double mem = calculator.getMemoryValue();
                                ((JLabel) sub2).setText("M: " + (mem == (long) mem ?
                                        String.valueOf((long) mem) :
                                        String.format("%.4f", mem)));
                            }
                        }
                    }
                    if (sub instanceof JLabel && "memLabel".equals(((JLabel) sub).getName())) {
                        double mem = calculator.getMemoryValue();
                        ((JLabel) sub).setText("M: " + (mem == (long) mem ?
                                String.valueOf((long) mem) :
                                String.format("%.4f", mem)));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdvancedCalculatorGUI();
            }
        });
    }
}