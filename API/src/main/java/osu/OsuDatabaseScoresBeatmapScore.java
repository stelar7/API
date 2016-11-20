package osu;

public class OsuDatabaseScoresBeatmapScore implements Comparable<OsuDatabaseScoresBeatmapScore>
{
	public OsuMode getMode()
	{
		return mode;
	}
	
	public void setMode(OsuMode mode)
	{
		this.mode = mode;
	}
	
	public int getVersion()
	{
		return version;
	}
	
	public void setVersion(int version)
	{
		this.version = version;
	}
	
	public String getMapHash()
	{
		return mapHash;
	}
	
	public void setMapHash(String mapHash)
	{
		this.mapHash = mapHash;
	}
	
	public String getPlayer()
	{
		return player;
	}
	
	public void setPlayer(String player)
	{
		this.player = player;
	}
	
	public String getReplayHash()
	{
		return replayHash;
	}
	
	public void setReplayHash(String replayHash)
	{
		this.replayHash = replayHash;
	}
	
	public short getC300()
	{
		return c300;
	}
	
	public void setC300(short c300)
	{
		this.c300 = c300;
	}
	
	public short getC100()
	{
		return c100;
	}
	
	public void setC100(short c100)
	{
		this.c100 = c100;
	}
	
	public short getC50()
	{
		return c50;
	}
	
	public void setC50(short c50)
	{
		this.c50 = c50;
	}
	
	public short getcGeki()
	{
		return cGeki;
	}
	
	public void setcGeki(short cGeki)
	{
		this.cGeki = cGeki;
	}
	
	public short getcKatsu()
	{
		return cKatsu;
	}
	
	public void setcKatsu(short cKatsu)
	{
		this.cKatsu = cKatsu;
	}
	
	public short getcMiss()
	{
		return cMiss;
	}
	
	public void setcMiss(short cMiss)
	{
		this.cMiss = cMiss;
	}
	
	public float getScore()
	{
		return score;
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}
	
	public short getMaxCombo()
	{
		return maxCombo;
	}
	
	public void setMaxCombo(short maxCombo)
	{
		this.maxCombo = maxCombo;
	}
	
	public boolean isPerfect()
	{
		return perfect;
	}
	
	public void setPerfect(boolean perfect)
	{
		this.perfect = perfect;
	}
	
	public int getMods()
	{
		return mods;
	}
	
	public void setMods(int mods)
	{
		this.mods = mods;
	}
	
	public String getEmpty()
	{
		return empty;
	}
	
	public void setEmpty(String empty)
	{
		this.empty = empty;
	}
	
	public long getTimestampWindowsTick()
	{
		return timestampWindowsTick;
	}
	
	public void setTimestampWindowsTick(long timestampWindowsTick)
	{
		this.timestampWindowsTick = timestampWindowsTick;
	}
	
	public int getMinusOne()
	{
		return minusOne;
	}
	
	public void setMinusOne(int minusOne)
	{
		this.minusOne = minusOne;
	}
	
	public long getOnlineScoreId()
	{
		return onlineScoreId;
	}
	
	public void setOnlineScoreId(long onlineScoreId)
	{
		this.onlineScoreId = onlineScoreId;
	}
	
	OsuMode mode;
	int     version;
	String  mapHash;
	String  player;
	String  replayHash;
	short   c300;
	short   c100;
	short   c50;
	short   cGeki;
	short   cKatsu;
	short   cMiss;
	int     score;
	short   maxCombo;
	boolean perfect;
	int     mods;
	String  empty;
	long    timestampWindowsTick;
	int     minusOne;
	long    onlineScoreId;
	
	@Override
	public String toString()
	{
		return "OsuDatabaseScoresBeatmapScore{" +
		       "mode=" + mode +
		       ", version=" + version +
		       ", mapHash='" + mapHash + '\'' +
		       ", player='" + player + '\'' +
		       ", replayHash='" + replayHash + '\'' +
		       ", c300=" + c300 +
		       ", c100=" + c100 +
		       ", c50=" + c50 +
		       ", cGeki=" + cGeki +
		       ", cKatsu=" + cKatsu +
		       ", cMiss=" + cMiss +
		       ", score=" + score +
		       ", maxCombo=" + maxCombo +
		       ", perfect=" + perfect +
		       ", mods=" + mods +
		       ", empty='" + empty + '\'' +
		       ", timestampWindowsTick=" + timestampWindowsTick +
		       ", minusOne=" + minusOne +
		       ", onlineScoreId=" + onlineScoreId +
		       '}';
	}
	
	@Override
	public int compareTo(OsuDatabaseScoresBeatmapScore o)
	{
		return this.score < o.score ? 1 : -1;
	}
}
