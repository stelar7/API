package electronics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import electronics.CalculateStuff.WireMaterial;

public class WireResistanceGUI extends JFrame implements ActionListener, KeyListener
{

    private static final long serialVersionUID = 1L;

    JButton                   calc             = new JButton("Calculate");

    JComboBox<WireMaterial>   material         = new JComboBox<WireMaterial>();
    JLabel                    materiall        = new JLabel("Material:");
    JTextField                leng             = new JTextField();
    JLabel                    lengl            = new JLabel("Length:");
    JLabel                    lengll           = new JLabel("Meters");
    JTextField                area             = new JTextField();
    JLabel                    areal            = new JLabel("Area:");
    JLabel                    areall           = new JLabel("mm\u00B2");
    JTextField                wires            = new JTextField();
    JLabel                    wiresl           = new JLabel("No. of wires:");
    JLabel                    dummy            = new JLabel();

    public WireResistanceGUI()
    {
        super("Wire Resistance");
        setBounds(500, 500, 250, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addKeyListener(this);
        calc.addKeyListener(this);
        material.addKeyListener(this);
        leng.addKeyListener(this);
        area.addKeyListener(this);
        wires.addKeyListener(this);
        add(calc);
        add(material);
        add(materiall);
        add(leng);
        add(lengl);
        add(lengll);
        add(area);
        add(areal);
        add(areall);
        add(wires);
        add(wiresl);
        add(dummy);
        calc.addActionListener(this);
        calc.setBounds(100, 225, 125, 30);
        material.setBounds(75, 40, 150, 30);
        for (final WireMaterial m : WireMaterial.values())
        {
            material.addItem(m);
        }
        materiall.setBounds(10, 40, 125, 30);
        leng.setBounds(75, 80, 100, 30);
        leng.setHorizontalAlignment(SwingConstants.RIGHT);
        lengl.setBounds(10, 80, 125, 30);
        lengll.setBounds(180, 80, 100, 30);
        area.setBounds(75, 120, 100, 30);
        area.setHorizontalAlignment(SwingConstants.RIGHT);
        areal.setBounds(10, 120, 100, 30);
        areall.setBounds(180, 120, 125, 30);
        wires.setBounds(125, 160, 100, 30);
        wires.setHorizontalAlignment(SwingConstants.RIGHT);
        wiresl.setBounds(10, 160, 100, 30);
        validate();
        setVisible(true);
    }

    public static void main(final String[] args)
    {
        new WireResistanceGUI();
    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (!leng.getText().isEmpty())
        {
            if (!area.getText().isEmpty())
            {
                if (!wires.getText().isEmpty())
                {
                    final WireMaterial mat = (WireMaterial) material.getSelectedItem();
                    double len, are, wir;
                    try
                    {
                        len = Double.parseDouble(leng.getText().replace(",", "."));
                    } catch (final NumberFormatException ex)
                    {
                        JOptionPane.showMessageDialog(this, "Invalid entry in 'Length'\nValue: " + leng.getText() + "\nNumerical value expected!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try
                    {
                        are = Double.parseDouble(area.getText().replace(",", "."));
                    } catch (final NumberFormatException ex)
                    {
                        JOptionPane.showMessageDialog(this, "Invalid entry in 'Area'\nValue:  " + area.getText() + "\nNumerical value expected!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try
                    {
                        wir = Double.parseDouble(wires.getText().replace(",", "."));
                    } catch (final NumberFormatException ex)
                    {
                        JOptionPane.showMessageDialog(this, "Invalid entry in 'No.of wires'\nValue: " + wires.getText() + "\nNumerical value expected!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    JOptionPane.showMessageDialog(this, "Wire resistance is: " + CalculateStuff.wireResistance(mat, len, are, wir) + "\u2126");
                } else
                {
                    JOptionPane.showMessageDialog(this, "Missing value: No. of wires");
                }
            } else
            {
                JOptionPane.showMessageDialog(this, "Missing value: Area");
            }
        } else
        {
            JOptionPane.showMessageDialog(this, "Missing value: Length");
        }
    }

    @Override
    public void keyPressed(final KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            calc.doClick();
        }
    }

    @Override
    public void keyReleased(final KeyEvent arg0)
    {}

    @Override
    public void keyTyped(final KeyEvent arg0)
    {}

}
