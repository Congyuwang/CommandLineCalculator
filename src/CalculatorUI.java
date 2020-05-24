import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

import calculator.CalculatorProcessor;
import calculator.PreservedKeywordException;

public class CalculatorUI extends JFrame {

    private final SpringLayout frameLayout;
    private final SpringLayout mainPanelLayout;
    private final JPanel mainPanel;
    private final JScrollPane scrollPane;
    private final CalculatorProcessor calculator;
    private final KeyListener globalKeyListener;
    private JTextField inputField;
    private JTextArea inputGuide;
    private HistoryNode current;
    private final int[] round;
    private final JComponent[] previousLine;
    private final JButton helpButton;
    private final JButton backButton;
    private final JScrollPane helpMenu;

    private static class HistoryNode {
        int index;
        HistoryNode previous;
        HistoryNode next;
        String input;
    }

    private HistoryNode lastInput;

    private void addHistory(String input, int index) {
        HistoryNode oldLast = lastInput;
        lastInput = new HistoryNode();
        if (oldLast != null) {
            oldLast.next = lastInput;
        }
        lastInput.input = input;
        lastInput.previous = oldLast;
        lastInput.next = null;
        lastInput.index = index;
    }

    public CalculatorUI() {
        super("Calculator");
        calculator = new CalculatorProcessor();
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(Color.WHITE);
        getContentPane().setForeground(Color.WHITE);
        setMinimumSize(new Dimension(300, 200));

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

        mainPanelLayout = new SpringLayout();
        frameLayout = new SpringLayout();
        getContentPane().setLayout(frameLayout);
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.setLayout(mainPanelLayout);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setForeground(Color.WHITE);
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setForeground(Color.WHITE);

        helpButton = new JButton("HELP");
        backButton = new JButton("BACK");
        helpMenu = HelpMenu.createHelp();
        displayScrollPane(scrollPane, null);
        displayButton(helpButton, null);

        helpButton.addActionListener(e -> {
            displayScrollPane(helpMenu, scrollPane);
            displayButton(backButton, helpButton);
        });

        backButton.addActionListener(e -> {
            displayScrollPane(scrollPane, helpMenu);
            displayButton(helpButton, backButton);
        });

        globalKeyListener = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        if (lastInput != null) {
                            lastInput.input = inputField.getText();
                        }
                        mainOperation(round, previousLine, inputField.getText());
                        inputField.requestFocus();
                        break;
                    case KeyEvent.VK_UP:
                        current.input = inputField.getText();
                        if (current.previous != null) {
                            current = current.previous;
                        }
                        inputGuide.setText(String.format("In[%d]:", current.index));
                        inputField.setText(current.input);
                        inputField.requestFocus();
                        break;
                    case KeyEvent.VK_DOWN:
                        current.input = inputField.getText();
                        if (current.next != null) {
                            current = current.next;
                        }
                        inputGuide.setText(String.format("In[%d]:", current.index));
                        inputField.setText(current.input);
                        inputField.requestFocus();
                        break;
                    default:
                        break;
                }
            }
        };

        previousLine = new JComponent[] {null};
        round = new int[] {1};
        inputGuide = printNewString(String.format("In[%d]:", round[0]), Color.BLACK, Color.LIGHT_GRAY);
        addComponentToNewLine(inputGuide, previousLine[0]);
        inputField = newInput();
        addHistory(inputField.getText(), round[0]);
        current = lastInput;
        appendComponentToLine(inputField, inputGuide);
        previousLine[0] = inputField;
        inputGuide.addKeyListener(globalKeyListener);
        addKeyListener(globalKeyListener);

        setVisible(true);
        inputField.requestFocus();
    }

    private void mainOperation(int[] round, JComponent[] previousLine, String inputString) {
        if (!"".equals(inputString.trim())) {
            inputField.setEditable(false);
            round[0]++;
            String output;
            try {
                output = calculator.expression(inputString);
            } catch (IllegalArgumentException | PreservedKeywordException | ArithmeticException e) {
                output = e.getMessage();
            }
            inputGuide = printNewString(String.format("In[%d]:", round[0]), Color.BLACK, Color.LIGHT_GRAY);
            inputGuide.addKeyListener(globalKeyListener);
            inputField = newInput();
            addHistory(inputField.getText(), round[0]);
            current = lastInput;
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
        }
        revalidate();
        JScrollBar vScrollBar = scrollPane.getVerticalScrollBar();
        vScrollBar.setValue(vScrollBar.getModel().getMaximum() - vScrollBar.getModel().getExtent());
    }

    private JTextField newInput() {
        return new JTextField() {
            private static final long serialVersionUID = -2671807274130384111L;
            {
                setBackground(Color.LIGHT_GRAY);
                addKeyListener(globalKeyListener);
                setMaximumSize(getMinimumSize());
                setPreferredSize(getMinimumSize());
            }
        };
    }

    private JTextArea printNewString(String s, Color foreground, Color background) {
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

    private void addComponentToNewLine(JComponent newLine, JComponent previousLine) {
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

    private void appendComponentToLine(JComponent newComponent, JComponent leftComponent) {
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

    private void displayScrollPane(JScrollPane s, JScrollPane old) {
        if (old != null) {
            remove(old);
            frameLayout.removeLayoutComponent(old);
        }
        add(s);
        frameLayout.putConstraint(SpringLayout.NORTH, s, 30, SpringLayout.NORTH, getContentPane());
        frameLayout.putConstraint(SpringLayout.SOUTH, s, -30, SpringLayout.SOUTH, getContentPane());
        frameLayout.putConstraint(SpringLayout.EAST, s, 0, SpringLayout.EAST, getContentPane());
        frameLayout.putConstraint(SpringLayout.WEST, s, 0, SpringLayout.WEST, getContentPane());
        revalidate();
        repaint();
    }

    private void displayButton(JButton s, JButton old) {
        if (old != null) {
            remove(old);
            frameLayout.removeLayoutComponent(old);
        }
        add(s);
        frameLayout.putConstraint(SpringLayout.NORTH, s, 2, SpringLayout.NORTH, getContentPane());
        frameLayout.putConstraint(SpringLayout.WEST, s, 2, SpringLayout.WEST, getContentPane());
        revalidate();
        repaint();
    }

    private static final long serialVersionUID = 8477203213070754493L;

}
