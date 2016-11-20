package electronics;

import java.util.Locale;

public class CalculateStuff
{
	
	public enum TempreatureCoefficient
	{
		TEN(1.22, 1.15, 1.1, 1.07),
		FIFTEEN(1.17, 1.12, 1.05, 1.04),
		TWENTY(1.12, 1.08, 1, 1),
		TWENTYFIVE(1.06, 1.04, .95, .96),
		THIRTY(1, 1, .89, .93),
		THIRTYFIVE(.94, .96, .84, .93),
		FOURTY(.87, .91, .77, .85),
		FORTYFIVE(.79, .87, .71, .8),
		FIFTY(.71, .82, .63, .76),
		FIFTYFIVE(.61, .76, .55, .71),
		SIXTY(.5, .71, .45, .65);
		
		final double pvcair, pexair, pvcearth, pexearth;
		
		TempreatureCoefficient(final double pva, final double pea, final double pve, final double pee)
		{
			this.pvcair = pva;
			this.pexair = pea;
			this.pvcearth = pve;
			this.pexearth = pee;
		}
	}
	
	public enum WireMaterial
	{
		ALUMINIUM(0.265),
		ALUMINIUM_BRONZE(0.0934),
		BRASS(0.645),
		CONSTANTAN(0.5),
		COPPER(0.0175),
		CHROMIUM(0.028),
		GOLD(0.023),
		IRON(0.0971),
		KANTHAL_A(1.39),
		KANTHAL_A_1(1.45),
		KANTHAT_1_DSD(1.35),
		LEAD(0.207),
		MERCURY(0.958),
		MANHANESE(0.43),
		NICKEL(0.069),
		NICHROME(1.1),
		NICKEL_SILVER(0.227),
		PLATINUM(0.107),
		SILVER(0.016),
		TIN(0.115),
		TIN_BRONZE(0.105),
		ZINC(0.06),
		WOLFRAM(0.0555);
		
		final double resistance;
		
		WireMaterial(final double d)
		{
			this.resistance = d;
		}
		
		@Override
		public String toString()
		{
			return this.name().substring(0, 1) + this.name().replace("_", " ").substring(1).toLowerCase(Locale.ENGLISH);
		}
	}
	
	public enum Electricity
	{
		AMPERE
			{
				@Override
				public Electricity toVolt(double resistance)
				{
					return VOLT.withValue(resistance * this.value);
				}
				
				@Override
				public Electricity toAmpere(double value)
				{
					return AMPERE.withValue(value);
				}
				
				@Override
				public Electricity toOhm(double volt)
				{
					return OHM.withValue(volt / this.value);
				}
				
				@Override
				public Electricity toWatt(double resistance)
				{
					return WATT.withValue(Math.pow(this.value, 2) * resistance);
				}
				
				@Override
				public Electricity inParallel(double... value)
				{
					throw new UnsupportedOperationException();
				}
				
				@Override
				public Electricity inSeries(double... value)
				{
					throw new UnsupportedOperationException();
				}
				
			},
		VOLT
			{
				@Override
				public Electricity toVolt(double value)
				{
					return VOLT.withValue(value);
				}
				
				@Override
				public Electricity toAmpere(double resistance)
				{
					return AMPERE.withValue(this.value / resistance);
				}
				
				@Override
				public Electricity toOhm(double watt)
				{
					return OHM.withValue(Math.pow(this.value, 2) / watt);
				}
				
				@Override
				public Electricity toWatt(double ampere)
				{
					return WATT.withValue(this.value * ampere);
				}
				
				@Override
				public Electricity inParallel(double... value)
				{
					throw new UnsupportedOperationException();
				}
				
				@Override
				public Electricity inSeries(double... value)
				{
					throw new UnsupportedOperationException();
				}
				
			},
		OHM
			{
				@Override
				public Electricity toVolt(double watt)
				{
					return VOLT.withValue(Math.sqrt(watt * this.value));
				}
				
				@Override
				public Electricity toAmpere(double watt)
				{
					return AMPERE.withValue(Math.sqrt(watt / this.value));
				}
				
				@Override
				public Electricity toOhm(double value)
				{
					return OHM.withValue(value);
				}
				
				@Override
				public Electricity toWatt(double volt)
				{
					return WATT.withValue(Math.pow(volt, 2) / this.value);
				}
				
				public Electricity inParallel(final double... resistances)
				{
					double i = 0;
					for (final double ii : resistances)
					{
						i += 1 / ii;
					}
					return OHM.withValue(1 / i);
				}
				
				public Electricity inSeries(final double... resistances)
				{
					double i = 0;
					for (final double ii : resistances)
					{
						i += ii;
					}
					return OHM.withValue(i);
				}
			},
		WATT
			{
				@Override
				public Electricity toVolt(double ampere)
				{
					
					return VOLT.withValue(this.value / ampere);
				}
				
				@Override
				public Electricity toAmpere(double volt)
				{
					return AMPERE.withValue(this.value / volt);
				}
				
				@Override
				public Electricity toOhm(double ampere)
				{
					return OHM.withValue(this.value / Math.pow(ampere, 2));
				}
				
				@Override
				public Electricity toWatt(double value)
				{
					return WATT.withValue(value);
				}
				
				@Override
				public Electricity inParallel(double... value)
				{
					throw new UnsupportedOperationException();
				}
				
				@Override
				public Electricity inSeries(double... value)
				{
					throw new UnsupportedOperationException();
				}
			},
		FARAD
			{
				@Override
				public Electricity toVolt(double value)
				{
					throw new UnsupportedOperationException();
				}
				
				@Override
				public Electricity toAmpere(double value)
				{
					throw new UnsupportedOperationException();
				}
				
				@Override
				public Electricity toOhm(double value)
				{
					throw new UnsupportedOperationException();
				}
				
				@Override
				public Electricity toWatt(double value)
				{
					throw new UnsupportedOperationException();
				}
				
				public Electricity inParallel(final double... capasitors)
				{
					double i = 0;
					for (final double d : capasitors)
					{
						i += d;
					}
					return FARAD.withValue(i);
				}
				
				public Electricity inSeries(final double... capasitors)
				{
					double i = 0;
					for (final double ii : capasitors)
					{
						i += 1 / ii;
					}
					return FARAD.withValue(1 / i);
				}
			};
		
		double value;
		
		public double getValue()
		{
			return value;
		}
		
		public Electricity withValue(double value)
		{
			this.value = value;
			return this;
		}
		
		abstract public Electricity toVolt(double value);
		
		abstract public Electricity toAmpere(double value);
		
		abstract public Electricity toOhm(double value);
		
		abstract public Electricity toWatt(double value);
		
		abstract public Electricity inParallel(double... value);
		
		abstract public Electricity inSeries(double... value);
	}
	
	
	public static double getEnergyConversionEfficiency(final double input, final double output)
	{
		return output / input;
	}
	
	public static double getVoltageDrop(final double ampere, final WireMaterial material, final double length, final double area)
	{
		return (ampere * material.resistance * length) / area;
	}
	
	public static double getVoltageDrop3PEngine(final double ampere, final WireMaterial material, final double length, final double area)
	{
		
		return Math.sqrt(3) * ampere * CalculateStuff.getWireResistance(material, length, area, 1);
	}
	
	public static double getWireResistance(final WireMaterial mat, final double length, final double area, final double wires)
	{
		return ((mat.resistance * length) / area) * wires;
	}
}
