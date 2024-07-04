package src;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;

public class MenuPage extends JFrame {

    public MenuPage(ScientificCalculator calculator) {
        setTitle("Settings");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        
        JLabel floatLabel = new JLabel("Float");
        floatLabel.setFont(new Font("MV Boli", Font.BOLD, 20));
        JSpinner spinner1 = new JSpinner(new SpinnerNumberModel(calculator.getDecimalPlace(), 1, 15, 1));
        spinner1.setPreferredSize(new Dimension(20, 20));
        spinner1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int decimalPlaces = (int) spinner1.getValue();
                calculator.setDecimalPlaces(decimalPlaces);
            }
        });
        panel.add(floatLabel);
        panel.add(spinner1);

        add(panel);
    }
}


class SwitchButton extends JComponent {
    private boolean selected;
    private int width = 60;
    private int height = 30;
    private Color backgroundColor = new Color(0x69B85B);
    private Color circleColor = Color.WHITE;

    public SwitchButton() {
        setPreferredSize(new Dimension(width, height));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggle();
            }
        });
    }

    private void toggle() {
        selected = !selected;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (selected) {
            g2.setColor(backgroundColor);
        } else {
            g2.setColor(Color.LIGHT_GRAY);
        }
        g2.fillRoundRect(0, 0, width, height, height, height);

        int circleX = selected ? width - height : 0;
        g2.setColor(circleColor);
        g2.fillOval(circleX, 0, height, height);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }
}