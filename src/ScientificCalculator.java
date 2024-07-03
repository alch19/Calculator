package src;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class ScientificCalculator extends JFrame implements ActionListener {
    private JTextField display;
    private String currentInput = "";
    private String expression = "";
    private JToggleButton toggleButton;
    private boolean isDegrees = true;
    
    public ScientificCalculator() {
        setTitle("Scientific Calculator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);    

        display = new JTextField();
        display.setFont(new Font("MV Boli", Font.PLAIN, 20));
        display.setEditable(false);
        display.setFocusable(false);
        display.setOpaque(false);
        display.setBorder(new RoundedBorder(15));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 1;
        gbc.weighty = 1;

        String[][] buttons = {
            {"2nd", "Mode", "Del", "AC", "+/-", "π", "e"},
            {"7", "8", "9", "÷", "sin", "cos", "tan"},
            {"4", "5", "6", "×", "sinh", "cosh", "tanh"},
            {"1", "2", "3", "-", "ln", "log", "^"},
            {"0", ".", "=", "+", "(", ")", "√"}
        };

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                JButton button = new JButton(buttons[row][col]);
                button.setFont(new Font("MV Boli", Font.PLAIN, 20));
                button.addActionListener(this);
                button.setFocusable(false);
                button.setOpaque(true);
                button.setContentAreaFilled(true);
                
                gbc.gridx = col;
                gbc.gridy = row;

                if (buttons[row][col].matches("\\d") || buttons[row][col].equals(".")) {
                    button.setBackground(new Color(0x87CEEB));
                } else if(buttons[row][col].equals("=")) {
                    button.setBackground(new Color(0x5DB85B));
                } else if(buttons[row][col].equals("+") || buttons[row][col].equals("-") || buttons[row][col].equals("×") || buttons[row][col].equals("÷")) {
                    button.setBackground(new Color(0xE24949));
                } else if(buttons[row][col].equals("π") || buttons[row][col].equals("e") || buttons[row][col].equals("2nd") || buttons[row][col].equals("Mode") || buttons[row][col].equals("Del") || buttons[row][col].equals("AC") || buttons[row][col].equals("+/-")) {
                    button.setBackground(new Color(0x167570));
                } else {
                    button.setBackground(new Color(0xFFA07A));
                }

                buttonPanel.add(button, gbc);
            }
        }

        toggleButton = new JToggleButton("Degrees");
        toggleButton.setFont(new Font("MV Boli", Font.PLAIN, 15));
        toggleButton.setBackground(new Color(0x69B85B));
        toggleButton.setOpaque(true);
        toggleButton.setBorderPainted(true);
        toggleButton.setFocusable(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setForeground(new Color(0x69B85B));
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (toggleButton.isSelected()) {
                    toggleButton.setText("Radians");
                    toggleButton.setForeground(new Color(0xB85B5B));
                    isDegrees = false;
                } else {
                    toggleButton.setText("Degrees");
                    toggleButton.setForeground(new Color(0x69B85B));
                    isDegrees = true;
                }
                toggleButton.repaint();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 0));
        topPanel.setBackground(display.getBackground());
        topPanel.add(display, BorderLayout.CENTER);
        topPanel.add(toggleButton, BorderLayout.EAST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.matches("\\d") || command.equals(".")) {
            currentInput += command;
            expression += command;
            display.setText(expression);
        } else if (command.equals("AC")) {
            currentInput = "";
            expression = "";
            display.setText("");
        } else if (command.equals("π")) {
            currentInput += Math.PI;
            expression += Math.PI;
            display.setText(expression);
        } else if (command.equals("e")) {
            currentInput += Math.E;
            expression += Math.E;
            display.setText(expression);
        } else if (command.equals("+/-")) {
            if (!currentInput.isEmpty()) {
                currentInput += "-";
                expression += "-";
                display.setText(expression);
            }
        } else if (command.equals("√")) {
            if (!currentInput.isEmpty() && !isOperator(expression.charAt(expression.length() - 1))) {
                expression += "×";
            }
            expression += "sqrt(";
            display.setText(expression);
        } else if (command.equals("Del")) {
            if (!expression.isEmpty()) {
                expression = expression.substring(0, expression.length() - 1);
                display.setText(expression);
            }
        } else if (command.equals("sin") || command.equals("cos") || command.equals("tan") ||
            command.equals("sinh") || command.equals("cosh") || command.equals("tanh") ||
            command.equals("log") || command.equals("ln")) {
            if (!currentInput.isEmpty() && !isOperator(expression.charAt(expression.length() - 1))) {
                expression += "×";
            }
            expression += command + "(";
            display.setText(expression);
        } else if (command.equals("(")) {
            if (!currentInput.isEmpty() && Character.isDigit(currentInput.charAt(currentInput.length() - 1))) {
                expression += "×";
            }
            expression += "(";
            display.setText(expression);
        } else if (command.equals(")")) {
            expression += ")";
            display.setText(expression);
        } else if (command.equals("=")) {
            try {
                double result = evaluateExpression(expression);
                display.setText(String.valueOf(result));
                expression = String.valueOf(result);
                currentInput = "";
            } catch (Exception ex) {
                display.setText("Error");
                expression = "";
                currentInput = "";
            }
        } else {
            if (!expression.isEmpty() && (Character.isDigit(expression.charAt(expression.length() - 1)) || expression.charAt(expression.length() - 1) == ')')) {
                if (isOperator(expression.charAt(expression.length() - 1))) {
                    expression = expression.substring(0, expression.length() - 1) + command;
                } else {
                    expression += command;
                }
                currentInput = "";
                display.setText(expression);
            }
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '×' || c == '÷' || c == '^';
    }

    private double evaluateExpression(String expression) throws Exception {
        Stack<Double> values = new Stack<>();
        Stack<String> ops = new Stack<>();
        char[] tokens = expression.toCharArray();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ')
                continue;

            if (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.') {
                StringBuilder sbuf = new StringBuilder();
                while (i < tokens.length && (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.'))
                    sbuf.append(tokens[i++]);
                values.push(Double.parseDouble(sbuf.toString()));
                i--;
            } else if (tokens[i] == '(')
                ops.push(String.valueOf(tokens[i]));
                else if (tokens[i] == ')') {
                while (!ops.peek().equals("("))
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.pop();
                if (!ops.isEmpty() && isFunction(ops.peek())) {
                    double val = values.pop();
                    values.push(applyFunction(ops.pop(), val));
                }
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '×' || tokens[i] == '÷' || tokens[i] == '^') {
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek().charAt(0)))
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.push(String.valueOf(tokens[i]));
            } else {
                StringBuilder sbuf = new StringBuilder();
                while (i < tokens.length && Character.isLetter(tokens[i]))
                    sbuf.append(tokens[i++]);
                if (!sbuf.toString().equals("")) {
                    ops.push(sbuf.toString());
                    i--;
                }
            }
        }

        while (!ops.empty())
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));

        return values.pop();
    }

    private boolean isFunction(String op) {
        return op.equals("sin") || op.equals("cos") || op.equals("tan") ||
            op.equals("sinh") || op.equals("cosh") || op.equals("tanh") ||
            op.equals("log") || op.equals("ln") || op.equals("sqrt") || op.equals("abs");
    }

    private double applyFunction(String function, double value) {
        switch (function) {
            case "sin":
                return isDegrees ? Math.sin(Math.toRadians(value)) : Math.sin(value);
            case "cos":
                return isDegrees ? Math.cos(Math.toRadians(value)) : Math.cos(value);
            case "tan":
                return isDegrees ? Math.tan(Math.toRadians(value)) : Math.tan(value);
            case "sinh":
                return Math.sinh(value);
            case "cosh":
                return Math.cosh(value);
            case "tanh":
                return Math.tanh(value);
            case "log":
                return Math.log10(value);
            case "ln":
                return Math.log(value);
            case "sqrt":
                return Math.sqrt(value);
            case "abs":
                return Math.abs(value);
            default:
                return value;
        }
    }


    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '×' || op1 == '÷' || op1 == '^') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    private double applyOp(String op, double b, double a) {
        switch (op) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "×":
                return a * b;
            case "÷":
                if (b == 0)
                    throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
            case "^":
                return Math.pow(a, b);
        }
        return 0;
    }

    public static void main(String[] args) {
        ScientificCalculator calculator = new ScientificCalculator();
        calculator.setVisible(true);
    }
}

class RoundedBorder implements Border {

    private int radius;

    RoundedBorder(int radius) {
        this.radius = radius;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius, this.radius, this.radius, this.radius);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
}