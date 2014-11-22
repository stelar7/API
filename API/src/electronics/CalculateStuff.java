package electronics;

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
            pvcair = pva;
            pexair = pea;
            pvcearth = pve;
            pexearth = pee;
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
            resistance = d;
        }

        @Override
        public String toString()
        {
            return this.name().substring(0, 1) + this.name().replace("_", " ").substring(1).toLowerCase();
        }
    }

    public static double AmpereFromVoltAndResistance(final double volt, final double resistance)
    {
        return volt / resistance;
    }

    public static double AmpereFromWattAndResistance(final double watt, final double resistance)
    {
        return Math.sqrt(watt / resistance);
    }

    public static double AmpereFromWattAndVolt(final double watt, final double volt)
    {
        return watt / volt;
    }

    public static double capasitanceInParallel(final double... capasitors)
    {
        double i = 0;
        for (final double d : capasitors)
        {
            i += d;
        }
        return i;
    }

    public static double capasitanceInSeries(final double... capasitors)
    {
        double i = 0;
        for (final double ii : capasitors)
        {
            i += 1 / ii;
        }
        return 1 / i;
    }

    public static double EnergyConversionEfficiency(final double input, final double output)
    {
        return (output / input);
    }

    public static double ResistanceFromVoltAndAmpere(final double volt, final double ampere)
    {
        return volt / ampere;
    }

    public static double ResistanceFromVoltAndWatt(final double volt, final double watt)
    {
        return Math.pow(volt, 2) / watt;
    }

    public static double ResistanceFromWattAndAmpere(final double watt, final double ampere)
    {
        return (watt / Math.pow(ampere, 2));
    }

    public static double resistanceInParallel(final double... resistances)
    {
        double i = 0;
        for (final double ii : resistances)
        {
            i += 1 / ii;
        }
        return 1 / i;
    }

    public static double resistanceInSeries(final double... resistances)
    {
        double i = 0;
        for (final double ii : resistances)
        {
            i += ii;
        }
        return i;
    }

    public static double voltageDrop(final double ampere, final WireMaterial material, final double length, final double area)
    {
        return ((ampere * material.resistance * length) / area);
    }

    public static double voltageDrop3PEngine(final double ampere, final WireMaterial material, final double length, final double area, final double cosphi)
    {
        return (Math.sqrt(3) * ampere * wireResistance(material, length, area, 1));
    }

    public static double VoltFromResistanceAndAmpere(final double resistance, final double ampere)
    {
        return resistance * ampere;
    }

    public static double VoltFromWattAndAmpere(final double watt, final double ampere)
    {
        return watt / ampere;
    }

    public static double VoltFromWattAndResistance(final double watt, final double resistance)
    {
        return Math.sqrt(watt * resistance);
    }

    public static double WattFromAmpereAndResistance(final double ampere, final double resistance)
    {
        return Math.pow(ampere, 2) * resistance;
    }

    public static double WattFromVoltAndAmpere(final double volt, final double ampere)
    {
        return volt * ampere;
    }

    public static double WattFromVoltAndResistance(final double volt, final double resistance)
    {
        return Math.pow(volt, 2) / resistance;
    }

    public static double wireResistance(final WireMaterial mat, final double length, final double area, final double wires)
    {
        return ((mat.resistance * length) / area) * wires;
    }
}
