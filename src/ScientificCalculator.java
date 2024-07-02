package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class ScientificCalculator extends JFrame implements ActionListener {
    private JTextField display;
    private String currentInput = "";
    private String expression = "";

    public ScientificCalculator() {
        setTitle("Scientific Calculator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        display = new JTextField();
        display.setFont(new Font("MV Boli", Font.PLAIN, 25));
        display.setEditable(false);
        display.setFocusable(false);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(-5, -5, -5, -5);
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
                gbc.gridx = col;
                gbc.gridy = row;
                buttonPanel.add(button, gbc);
            }
        }

        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
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
                double value = Double.parseDouble(currentInput);
                currentInput = String.valueOf(value * -1);
                expression = expression.substring(0, expression.length() - String.valueOf(value).length()) + currentInput;
                display.setText(expression);
            }
        } else if (command.equals("Del")) {
            if (!expression.isEmpty()) {
                expression = expression.substring(0, expression.length() - 1);
                display.setText(expression);
            }
        } else if (command.equals("Del")) {
            currentInput=currentInput.substring(currentInput.length()-1);
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
            currentInput = "";
            expression += command;
            display.setText(expression);
        }
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
                // Parse function names
                StringBuilder sbuf = new StringBuilder();
                while (i < tokens.length && Character.isLetter(tokens[i]))
                    sbuf.append(tokens[i++]);
                ops.push(sbuf.toString());
                i--;
            }
        }

        while (!ops.empty())
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));

        return values.pop();
    }

    private boolean isFunction(String op) {
        return op.equals("sin") || op.equals("cos") || op.equals("tan") ||
            op.equals("sinh") || op.equals("cosh") || op.equals("tanh") ||
            op.equals("log") || op.equals("ln") || op.equals("√") || op.equals("abs");
    }

    private double applyFunction(String function, double value) {
        switch (function) {
            case "sin":
                return Math.sin(Math.toRadians(value));
            case "cos":
                return Math.cos(Math.toRadians(value));
            case "tan":
                return Math.tan(Math.toRadians(value));
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
            case "√":
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
