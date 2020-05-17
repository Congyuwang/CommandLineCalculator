import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

import calculator.Calculator;
import calculator.PreservedKeywordException;

public class CalculatorUI extends JFrame {

    private final SpringLayout frameLayout;
    private final SpringLayout mainPanelLayout;
    private final JPanel mainPanel;
    private final JScrollPane scrollPane;
    private final Calculator calculator;
    private final KeyListener globalKeyListener;
    private JTextField inputField;
    private final int[] round;
    private final JComponent[] previousLine;

    public CalculatorUI() {
        super("Calculator");
        calculator = new Calculator();
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanelLayout = new SpringLayout();
        frameLayout = new SpringLayout();
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setForeground(Color.WHITE);
        getContentPane().setLayout(frameLayout);
        setMinimumSize(new Dimension(300, 200));
        mainPanel = new JPanel();
        mainPanel.setBorder(null);
        mainPanel.setLayout(mainPanelLayout);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setForeground(Color.WHITE);
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setForeground(Color.WHITE);
        frameLayout.putConstraint(SpringLayout.NORTH, scrollPane, 20, SpringLayout.NORTH, getContentPane());
        frameLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -20, SpringLayout.SOUTH, getContentPane());
        frameLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, getContentPane());
        frameLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, getContentPane());

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Dimension d = getSize();
                Dimension minD = getMinimumSize();
                if (d.width < minD.width)
                    d.width = minD.width;
                if (d.height < minD.height)
                    d.height = minD.height;
                setSize(d);
                mainPanel.doLayout();
            }
        });

        previousLine = new JComponent[] {null};
        round = new int[] {1};
        JTextArea inputGuide = printNewString(String.format("In[%d]:", round[0]), Color.BLACK, Color.LIGHT_GRAY);
        addComponentToNewLine(inputGuide, previousLine[0]);
        inputField = newInput();
        appendComponentToLine(inputField, inputGuide);
        previousLine[0] = inputField;
        globalKeyListener = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    mainOperation(round, previousLine);
                };
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        };
        inputGuide.addKeyListener(globalKeyListener);
        addKeyListener(globalKeyListener);

        add(scrollPane);
        setVisible(true);
        inputField.requestFocus();
    }

    private final JTextField newInput() {
        return new JTextField() {
            private static final long serialVersionUID = -2671807274130384111L;
            {
                setBackground(Color.LIGHT_GRAY);
                addKeyListener(new KeyListener() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            mainOperation(round, previousLine);
                        };
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                    }

                });
                setMaximumSize(getMinimumSize());
                setPreferredSize(getMinimumSize());
            }
        };
    }

    private final void mainOperation(int[] round, JComponent[] previousLine) {
        String inputString = inputField.getText();
        inputField.setEditable(false);
        if (!"".equals(inputString.strip())) {
            round[0]++;
            String output;
            try {
                BigDecimal result = calculator.expression(inputString);
                output = result == null ? null : result.toString();
            } catch (PreservedKeywordException | IllegalArgumentException e) {
                output = e.getMessage();
            }
            JTextArea inputGuide = printNewString(String.format("In[%d]:", round[0]), Color.BLACK, Color.LIGHT_GRAY);
            inputGuide.addKeyListener(globalKeyListener);
            inputField = newInput();
            if (output != null) {
                JTextArea newText = printNewString(output, Color.BLACK, Color.WHITE);
                newText.addKeyListener(globalKeyListener);
                addComponentToNewLine(newText, previousLine[0]);
                addComponentToNewLine(inputGuide, newText);
            } else {
                addComponentToNewLine(inputGuide, previousLine[0]);
            }
            appendComponentToLine(inputField, inputGuide);
            previousLine[0] = inputField;
            inputField.requestFocus();
        }
        paintComponents(getGraphics());
        JScrollBar vScrollBar = scrollPane.getVerticalScrollBar();
        vScrollBar.setValue(vScrollBar.getModel().getMaximum() - vScrollBar.getModel().getExtent());
    }


    private final JTextArea printNewString(String s, Color foreground, Color background) {
        return new JTextArea() {
            private static final long serialVersionUID = -5370026524180441819L;
            {
                setText(s);
                setBackground(background);
                setForeground(foreground);
                setEditable(false);
                setWrapStyleWord(true);
                setMaximumSize(getMinimumSize());
                setPreferredSize(getMinimumSize());
            }
        };
    }

    private final void addComponentToNewLine(JComponent newLine, JComponent previousLine) {
        mainPanel.add(newLine);
        mainPanelLayout.putConstraint(SpringLayout.WEST, newLine, 10, SpringLayout.WEST, mainPanel);
        if (previousLine == null) {
            mainPanelLayout.putConstraint(SpringLayout.NORTH, newLine, 10, SpringLayout.NORTH, mainPanel);
        } else {
            mainPanelLayout.putConstraint(SpringLayout.NORTH, newLine, 10, SpringLayout.SOUTH, previousLine);
        }
        mainPanelLayout.removeLayoutComponent(mainPanel);
        mainPanelLayout.putConstraint(SpringLayout.SOUTH, mainPanel, 10, SpringLayout.SOUTH, newLine);
        mainPanel.setMinimumSize(mainPanelLayout.minimumLayoutSize(mainPanel));
        mainPanel.setPreferredSize(mainPanelLayout.minimumLayoutSize(mainPanel));
        mainPanel.setMaximumSize(mainPanelLayout.minimumLayoutSize(mainPanel));
    }

    private final void appendComponentToLine(JComponent newComponent, JComponent leftComponent) {
        mainPanel.add(newComponent);
        if (leftComponent == null) {
            throw new IllegalArgumentException("null left component");
        } else {
            mainPanelLayout.putConstraint(SpringLayout.WEST, newComponent, 0, SpringLayout.EAST, leftComponent);
            mainPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, newComponent, 0, SpringLayout.VERTICAL_CENTER, leftComponent);
            mainPanelLayout.putConstraint(SpringLayout.EAST, newComponent, -10, SpringLayout.EAST, mainPanel);
        }
        mainPanelLayout.removeLayoutComponent(mainPanel);
        mainPanelLayout.putConstraint(SpringLayout.SOUTH, mainPanel, 10, SpringLayout.SOUTH, newComponent);
        mainPanel.setMinimumSize(mainPanelLayout.minimumLayoutSize(mainPanel));
        mainPanel.setPreferredSize(mainPanelLayout.minimumLayoutSize(mainPanel));
        mainPanel.setMaximumSize(mainPanelLayout.minimumLayoutSize(mainPanel));
    }

    private static final long serialVersionUID = 8477203213070754493L;

    public static void main(final String[] args) {
        new CalculatorUI();
    }

}
