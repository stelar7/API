package osu;

import java.util.ArrayList;
import java.util.List;

public class OsuDatabaseScores
{
	int version;
	List<OsuDatabaseScoresBeatmap> maps = new ArrayList<>();
	
	public int getVersion()
	{
		return version;
	}
	
	public void setVersion(int version)
	{
		this.version = version;
	}
	
	public List<OsuDatabaseScoresBeatmap> getMaps()
	{
		return maps;
	}
	
	public void setMaps(List<OsuDatabaseScoresBeatmap> maps)
	{
		this.maps = maps;
	}
}
