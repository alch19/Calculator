package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScientificCalculator extends JFrame implements ActionListener{
    private JTextField display;
    private String currentInput="";
    private double result=0;
    private String operator="";
    private boolean reset=false;
    private boolean lastIsOp = false;

    public ScientificCalculator() {
        setTitle("Scientific Calculator");
        setSize(600,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        display = new JTextField();
        display.setFont(new Font("MV Boli", Font.PLAIN, 25));
        display.setEditable(false);
        display.setFocusable(false);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);
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
                button.setFont(new Font("Arial", Font.PLAIN, 20));
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
            if (reset) {
                currentInput = "";
                reset = false;
            }
            currentInput += command;
            display.setText(currentInput);
            lastIsOp = false;
        } else if (command.equals("AC")) {
            currentInput = "";
            result = 0;
            operator = "";
            display.setText("");
            lastIsOp = false;
        } else if (command.equals("=")) {
            calculate();
            display.setText(String.valueOf(result));
            
        } else {
            if (!currentInput.isEmpty() && !lastIsOp) {
                calculate();
                operator = command;
                reset = true;
                lastIsOp = true;
            } else {
                operator=command;
            }
        }
    }

    private void calculate() {
        double currentNumber = Double.parseDouble(currentInput);
        switch (operator) {
            case "":
                result = currentNumber;
                break;
            case "+":
                result += currentNumber;
                break;
            case "-":
                result -= currentNumber;
                break;
            case "×":
                result *= currentNumber;
                break;
            case "÷":
                result /= currentNumber;
                break;
            case "sin":
                result = Math.sin(Math.toRadians(currentNumber));
                break;
            case "cos":
                result = Math.cos(Math.toRadians(currentNumber));
                break;
            case "tan":
                result = Math.tan(Math.toRadians(currentNumber));
                break;
            case "sinh":
                result = Math.sinh(currentNumber);
                break;
            case "cosh":
                result = Math.cosh(currentNumber);
                break;
            case "tanh":
                result = Math.tanh(currentNumber);
                break;
            case "log":
                result = Math.log10(currentNumber);
                break;
            case "ln":
                result = Math.log(currentNumber);
                break;
            case "^":
                result = Math.pow(result, currentNumber);
                break;
            case "√":
                result = Math.sqrt(currentNumber);
                break;
            case "π":
                result = Math.PI;
                break;
            case "e":
                result = Math.E;
                break;
        }
        
        currentInput = "";
    }

    public static void main(String[] args) {
        ScientificCalculator calculator = new ScientificCalculator();
        calculator.setVisible(true);
    }
}
