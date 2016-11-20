package osu;

public enum OsuMode
{
	STANDARD(0),
	TAIKO(1),
	CTB(2),
	MANIA(3);
	
	byte value;
	
	OsuMode(int value)
	{
		this.value = (byte) value;
	}
	
	public static OsuMode get(byte b)
	{
		for (OsuMode s : values())
		{
			if (s.value == b) { return s; }
		}
		throw new RuntimeException("MODE NOT FOUND FOR BYTE " + b);
	}
	
	@Override
	public String toString()
	{
		switch (this)
		{
			case STANDARD:
				return "osu!";
			case TAIKO:
				return "Taiko";
			case CTB:
				return "Catch the Beat";
			case MANIA:
				return "osu!mania";
			default:
				return "WHAT?";
		}
	}
}
