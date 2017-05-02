package osu;

public enum RankedStatus
{
	LOVED(6),
	APPROVED(5),
	RANKED(4),
	UNKNOWN(1),
	UNKNOWN_TWO(0),
	PENDING_OR_GRAVEYARD(2);
	
	
	byte value;
	
	RankedStatus(int value)
	{
		this.value = (byte) value;
	}
	
	public static RankedStatus get(byte b)
	{
		for (RankedStatus s : values())
		{
			if (s.value == b)
			{
				return s;
			}
		}
		throw new RuntimeException("STATUS NOT FOUND FOR BYTE " + b);
	}
}
