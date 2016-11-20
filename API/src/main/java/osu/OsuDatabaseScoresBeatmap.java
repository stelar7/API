package osu;

import java.util.ArrayList;
import java.util.List;

public class OsuDatabaseScoresBeatmap
{
	String hash;
	
	public String getHash()
	{
		return hash;
	}
	
	public void setHash(String hash)
	{
		this.hash = hash;
	}
	
	public List<OsuDatabaseScoresBeatmapScore> getScores()
	{
		return scores;
	}
	
	public void setScores(List<OsuDatabaseScoresBeatmapScore> scores)
	{
		this.scores = scores;
	}
	
	List<OsuDatabaseScoresBeatmapScore> scores = new ArrayList<>();
}
