package osu;

public enum OsuGrade
{
    B(9 | 5),
    D(7),
    C(6),
    A(4),
    S(3),
    HS(-1),
    SS(2),
    HSS(-1),
    UNSURE(-2);
    byte value;
    
    
    OsuGrade(int i)
    {
        this.value = (byte) i;
    }
    
    
    public static OsuGrade get(byte b)
    {
        for (OsuGrade s : values())
        {
            if ((s.value & b) == b)
            {
                return s;
            }
        }
        return UNSURE;
    }
}
