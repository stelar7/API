package osu;

import java.util.Arrays;
import java.util.List;

public class PPCalculator
{

    public static double calculateAccuracy(double c300, double c100, double c50, double miss)
    {
        double total = c300 + c100 + c50 + miss;

        double acc = 0;

        if (total > 0)
        {
            acc = (c50 * 50 + c100 * 100 + c300 * 300) / (total * 300);
        }

        return acc;
    }

    static class CalculationResult
    {
        double accuracy   = 0;
        double pp         = 0;
        double aimPP      = 0;
        double speedPP    = 0;
        double accuracyPP = 0;
    }

    // map should have .applyMods used on it before use..
    public static CalculationResult calculateAccuracyPP(double aim, double speed, Beatmap map, double acc, Beatmap.Mod[] mods, double combo, double misses, double scoreVersion)
    {
        misses = Math.min(map.hitObjects.size(), misses);
        double m300 = map.hitObjects.size() - misses;
        acc = Math.max(0, Math.min(calculateAccuracy(m300, 0, 0, misses) * 100, acc));

        double c50 = 0;
        double c100 = Math.round(-3 * ((acc * 0.01d - 1d) * map.hitObjects.size() + misses) * .5d);
        if (c100 > (map.hitObjects.size() - misses))
        {
            c50 = Math.round(-6 * ((acc * 0.01d - 1d) * map.hitObjects.size() + misses) * .2d);
            c50 = Math.min(m300, c50);
            c100 = 0;
        } else
        {
            c100 = Math.min(m300, c100);
        }

        double c300 = map.hitObjects.size() - c100 - c50 - misses;

        return calculatePP(aim, speed, map, mods, combo, misses, c300, c100, c50, scoreVersion);
    }

    // map should have .applyMods used on it before use..
    public static CalculationResult calculatePP(double aim, double speed, Beatmap map, Beatmap.Mod[] mods, double combo, double misses, double c300, double c100, double c50, double scoreVersion)
    {
        CalculationResult result = new CalculationResult();
        List<Beatmap.Mod> modsList = Arrays.asList(mods);

        if (c300 == -1)
        {
            c300 = map.hitObjects.size() - c100 - c50 - misses;
        }

        if (combo == -1)
        {
            combo = map.maxCombo;
        }

        if (combo == 0)
        {
            return result;
        }

        double hits = c300 + c100 + c50 + misses;
        if (hits != map.hitObjects.size())
        {
            System.err.println("hits and objects dont match");
        }

        if (scoreVersion != 1 && scoreVersion != 2)
        {
            return result;
        }

        double acc = calculateAccuracy(c300, c100, c50, misses);
        result.accuracy = acc * 100;

        double aimValue = baseStrain(aim);
        double hitsOver2k = hits / 2000;

        double lengthBonus = .95d + .4d * Math.min(1, hitsOver2k) + (hits > 2000 ? Math.log10(hitsOver2k) * .5d : 0);
        double missPen = Math.pow(.97d, misses);
        double comboPen = Math.pow(combo, .8d) / Math.pow(map.maxCombo, .8d);

        aimValue *= lengthBonus;
        aimValue *= missPen;
        aimValue *= comboPen;

        double ar = 1;

        if (map.getAr() > 10.33d)
        {
            ar += .45d * (map.getAr() - 10.33);
        } else if (ar < 8)
        {
            double bonus = 0.01d * (8d - map.getAr());
            if (modsList.contains(Beatmap.Mod.HD))
            {
                bonus *= 2;
            }
            ar += bonus;
        }

        aimValue *= ar;

        if (modsList.contains(Beatmap.Mod.HD))
        {
            aimValue *= 1.18d;
        }
        if (modsList.contains(Beatmap.Mod.FL))
        {
            aimValue *= 1.45d * lengthBonus;
        }

        double accBonus = .5d + acc / 2;
        double odBonus = .98d + Math.pow(map.getOd(), 2) / 2500;

        aimValue *= accBonus;
        aimValue *= odBonus;
        result.aimPP = aimValue;

        double speedValue = baseStrain(speed);

        speedValue *= lengthBonus;
        speedValue *= missPen;
        speedValue *= comboPen;
        speedValue *= accBonus;
        speedValue *= odBonus;

        result.speedPP = speedValue;

        double circles = map.circleCount;
        double realAcc = 0;
        if (scoreVersion == 2)
        {
            circles = hits;
            realAcc = acc;
        } else
        {
            if (circles > 0)
            {
                realAcc = ((c300 - hits - circles) * 300 + c100 * 100 + c50 * 50) / (circles * 3000);
            }

            realAcc = Math.max(0, realAcc);
        }

        double accValue = Math.pow(1.52163, map.getOd()) * Math.pow(realAcc, 24) * 2.83d;
        accValue *= Math.min(1.15, Math.pow(circles / 1000, .3d));

        if (modsList.contains(Beatmap.Mod.HD))
        {
            accValue *= 1.02d;
        }
        if (modsList.contains(Beatmap.Mod.FL))
        {
            accValue *= 1.02d;
        }

        result.accuracyPP = accValue;

        double modPen = 1.12d;
        if (modsList.contains(Beatmap.Mod.NF))
        {
            modPen *= .9d;
        }
        if (modsList.contains(Beatmap.Mod.SO))
        {
            modPen *= .95d;
        }

        result.pp = Math.pow(Math.pow(aimValue, 1.1d) + Math.pow(speedValue, 1.1d) + Math.pow(accValue, 1.1d), 1d / 1.1d) * modPen;

        return result;
    }

    private static double baseStrain(double strain)
    {
        return Math.pow(5 * Math.max(1, (strain / 0.0675d) - 4), 3) / 100000;
    }
}
