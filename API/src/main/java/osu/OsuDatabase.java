package osu;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OsuDatabase
{
	int                   version      = 0;
	int                   folderCount  = 0;
	boolean               locked       = false;
	Instant               time         = Instant.MIN;
	String                name         = "";
	int                   beatmapCount = 0;
	List<DatabaseBeatmap> beatmaps     = new ArrayList<>();
	int                   unknown      = 0;
	
	@Override
	public String toString()
	{
		return "OsuDatabase{" +
		       "version=" + version +
		       ", folderCount=" + folderCount +
		       ", locked=" + locked +
		       ", unlockTime=" + time +
		       ", name='" + name + '\'' +
		       ", beatmapCount=" + beatmapCount +
		       ", beatmaps=" + beatmaps +
		       ", unknown=" + unknown +
		       '}';
	}
	
	
	public int getVersion()
	{
		return version;
	}
	
	public void setVersion(int version)
	{
		this.version = version;
	}
	
	public int getFolderCount()
	{
		return folderCount;
	}
	
	public void setFolderCount(int folderCount)
	{
		this.folderCount = folderCount;
	}
	
	public boolean isLocked()
	{
		return locked;
	}
	
	public void setLocked(boolean locked)
	{
		this.locked = locked;
	}
	
	public Instant getTime()
	{
		return time;
	}
	
	public void setTime(Instant time)
	{
		this.time = time;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getBeatmapCount()
	{
		return beatmapCount;
	}
	
	public void setBeatmapCount(int beatmapCount)
	{
		this.beatmapCount = beatmapCount;
	}
	
	public List<DatabaseBeatmap> getBeatmaps()
	{
		return beatmaps;
	}
	
	public void setBeatmaps(List<DatabaseBeatmap> beatmaps)
	{
		this.beatmaps = beatmaps;
	}
	
	public int getUnknown()
	{
		return unknown;
	}
	
	public void setUnknown(int unknown)
	{
		this.unknown = unknown;
	}
}
