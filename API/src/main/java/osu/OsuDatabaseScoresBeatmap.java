package osu;

import java.util.*;

public class OsuDatabaseScoresBeatmap
{
    String hash;
    List<OsuDatabaseScoresBeatmapScore> scores = new ArrayList<>();
    
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
}
