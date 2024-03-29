package electronics;

import electronics.CalculateStuff.*;

import javax.swing.*;
import java.awt.event.*;

public class WireResistanceGUI extends JFrame implements ActionListener, KeyListener
{
	
	private static final long                    serialVersionUID = 1L;
	final                JButton                 calc             = new JButton("Calculate");
	final                JComboBox<WireMaterial> material         = new JComboBox<>();
	final                JLabel                  materiall        = new JLabel("Material:");
	final                JTextField              leng             = new JTextField();
	final                JLabel                  lengl            = new JLabel("Length:");
	final                JLabel                  lengll           = new JLabel("Meters");
	final                JTextField              area             = new JTextField();
	final                JLabel                  areal            = new JLabel("Area:");
	final                JLabel                  areall           = new JLabel("mm\u00B2");
	final                JTextField              wires            = new JTextField();
	final                JLabel                  wiresl           = new JLabel("No. of wires:");
	final                JLabel                  dummy            = new JLabel();
	
	public WireResistanceGUI()
	{
		super("Wire Resistance");
		this.setBounds(500, 500, 250, 300);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
	
	public static void main(final String[] args)
	{
		new WireResistanceGUI();
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
					double             len, are, wir;
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
	}
	
	@Override
	public void keyTyped(final KeyEvent arg0)
	{
	}
	
}
