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
    
    public static void main(final String[] args)
    {
        new WireResistanceGUI();
    }
    
    private JButton                 calc      = new JButton("Calculate");
    private JComboBox<WireMaterial> material  = new JComboBox<>();
    private JLabel                  materiall = new JLabel("Material:");
    private JTextField              leng      = new JTextField();
    private JLabel                  lengl     = new JLabel("Length:");
    private JLabel                  lengll    = new JLabel("Meters");
    private JTextField              area      = new JTextField();
    private JLabel                  areal     = new JLabel("Area:");
    private JLabel                  areall    = new JLabel("mm²");
    private JTextField              wires     = new JTextField();
    private JLabel                  wiresl    = new JLabel("No. of wires:");
    
    private JLabel dummy = new JLabel();
    
    public WireResistanceGUI()
    {
        super("Wire Resistance");
        this.setBounds(500, 500, 250, 300);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.calc.addKeyListener(this);
        this.material.addKeyListener(this);
        this.leng.addKeyListener(this);
        this.area.addKeyListener(this);
        this.wires.addKeyListener(this);
        this.add(this.calc);
        this.add(this.material);
        this.add(this.materiall);
        this.add(this.leng);
        this.add(this.lengl);
        this.add(this.lengll);
        this.add(this.area);
        this.add(this.areal);
        this.add(this.areall);
        this.add(this.wires);
        this.add(this.wiresl);
        this.add(this.dummy);
        this.calc.addActionListener(this);
        this.calc.setBounds(100, 225, 125, 30);
        this.material.setBounds(75, 40, 150, 30);
        for (final WireMaterial m : WireMaterial.values())
        {
            this.material.addItem(m);
        }
        this.materiall.setBounds(10, 40, 125, 30);
        this.leng.setBounds(75, 80, 100, 30);
        this.leng.setHorizontalAlignment(SwingConstants.RIGHT);
        this.lengl.setBounds(10, 80, 125, 30);
        this.lengll.setBounds(180, 80, 100, 30);
        this.area.setBounds(75, 120, 100, 30);
        this.area.setHorizontalAlignment(SwingConstants.RIGHT);
        this.areal.setBounds(10, 120, 100, 30);
        this.areall.setBounds(180, 120, 125, 30);
        this.wires.setBounds(125, 160, 100, 30);
        this.wires.setHorizontalAlignment(SwingConstants.RIGHT);
        this.wiresl.setBounds(10, 160, 100, 30);
        this.validate();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (!this.leng.getText().isEmpty())
        {
            if (!this.area.getText().isEmpty())
            {
                if (!this.wires.getText().isEmpty())
                {
                    final WireMaterial mat = (WireMaterial) this.material.getSelectedItem();
                    double             len;
                    double             are;
                    double             wir;
                    try
                    {
                        len = Double.parseDouble(this.leng.getText().replace(",", "."));
                    } catch (final NumberFormatException ex)
                    {
                        JOptionPane.showMessageDialog(this, "Invalid entry in 'Length'\nValue: " + this.leng.getText() + "\nNumerical value expected!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try
                    {
                        are = Double.parseDouble(this.area.getText().replace(",", "."));
                    } catch (final NumberFormatException ex)
                    {
                        JOptionPane.showMessageDialog(this, "Invalid entry in 'Area'\nValue:  " + this.area.getText() + "\nNumerical value expected!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try
                    {
                        wir = Double.parseDouble(this.wires.getText().replace(",", "."));
                    } catch (final NumberFormatException ex)
                    {
                        JOptionPane.showMessageDialog(this, "Invalid entry in 'No.of wires'\nValue: " + this.wires.getText() + "\nNumerical value expected!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    JOptionPane.showMessageDialog(this, "Wire resistance is: " + CalculateStuff.getWireResistance(mat, len, are, wir) + "\u2126");
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
            this.calc.doClick();
        }
    }
    
    @Override
    public void keyReleased(final KeyEvent arg0)
    {
        // not used
    }
    
    @Override
    public void keyTyped(final KeyEvent arg0)
    {
        // not used
        
    }
}
