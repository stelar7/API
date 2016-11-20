package osu;

import java.util.*;

public class DatabaseBeatmap
{
	int size;
	int drainTime;
	int totalTime;
	int audioPreviewStart;
	int mapId;
	int setId;
	int threadID;
	int lastModified;
	
	
	String artist;
	String artistUnicode;
	String song;
	String songUnicode;
	String creator;
	String difficulty;
	String audioFileName;
	String hash;
	String osuFileName;
	String songSource;
	String tags;
	String font;
	String folder;
	
	short hitcircleCount;
	short sliderCount;
	short spinnerCount;
	short localOffset;
	short onlineOffset;
	short unknown;
	
	long lastPlayed;
	long lastUpdateCheck;
	long lastModifiedWindowsTick;
	
	float approachRate;
	float circleSize;
	float HPDrain;
	float overallDifficulty;
	float stackLeniency;
	float maniaScrollSpeed;
	
	double sliderVelocity;
	
	boolean unplayed;
	boolean osz2;
	boolean ignoreMapSounds;
	boolean ignoreMapSkin;
	boolean disableStory;
	boolean disableVideo;
	boolean visualOverride;
	
	Map<Integer, Double> starRating      = new HashMap<>();
	Map<Integer, Double> taikoStarRating = new HashMap<>();
	Map<Integer, Double> CTBStarRating   = new HashMap<>();
	Map<Integer, Double> maniaStarRating = new HashMap<>();
	
	List<DatabaseBeatmapTimingPoint> timingPoints = new ArrayList<>();
	
	OsuGrade grade;
	OsuGrade taikoGrade;
	OsuGrade CTBGrade;
	OsuGrade maniaGrade;
	
	RankedStatus status;
	
	OsuMode mode;
	
	
	@Override
	public String toString()
	{
		return "DatabaseBeatmap{" +
		       "size=" + size +
		       ", drainTime=" + drainTime +
		       ", totalTime=" + totalTime +
		       ", audioPreviewStart=" + audioPreviewStart +
		       ", mapId=" + mapId +
		       ", setId=" + setId +
		       ", threadID=" + threadID +
		       ", lastModified=" + lastModified +
		       ", artist='" + artist + '\'' +
		       ", artistUnicode='" + artistUnicode + '\'' +
		       ", song='" + song + '\'' +
		       ", songUnicode='" + songUnicode + '\'' +
		       ", creator='" + creator + '\'' +
		       ", difficulty='" + difficulty + '\'' +
		       ", audioFileName='" + audioFileName + '\'' +
		       ", hash='" + hash + '\'' +
		       ", osuFileName='" + osuFileName + '\'' +
		       ", songSource='" + songSource + '\'' +
		       ", tags='" + tags + '\'' +
		       ", font='" + font + '\'' +
		       ", folder='" + folder + '\'' +
		       ", hitcircleCount=" + hitcircleCount +
		       ", sliderCount=" + sliderCount +
		       ", spinnerCount=" + spinnerCount +
		       ", localOffset=" + localOffset +
		       ", onlineOffset=" + onlineOffset +
		       ", unknown=" + unknown +
		       ", lastPlayed=" + lastPlayed +
		       ", lastUpdateCheck=" + lastUpdateCheck +
		       ", lastModifiedWindowsTick=" + lastModifiedWindowsTick +
		       ", approachRate=" + approachRate +
		       ", circleSize=" + circleSize +
		       ", HPDrain=" + HPDrain +
		       ", overallDifficulty=" + overallDifficulty +
		       ", stackLeniency=" + stackLeniency +
		       ", maniaScrollSpeed=" + maniaScrollSpeed +
		       ", sliderVelocity=" + sliderVelocity +
		       ", unplayed=" + unplayed +
		       ", osz2=" + osz2 +
		       ", ignoreMapSounds=" + ignoreMapSounds +
		       ", ignoreMapSkin=" + ignoreMapSkin +
		       ", disableStory=" + disableStory +
		       ", disableVideo=" + disableVideo +
		       ", visualOverride=" + visualOverride +
		       ", starRating=" + starRating +
		       ", taikoStarRating=" + taikoStarRating +
		       ", CTBStarRating=" + CTBStarRating +
		       ", maniaStarRating=" + maniaStarRating +
		       ", timingPoints=" + timingPoints +
		       ", grade=" + grade +
		       ", taikoGrade=" + taikoGrade +
		       ", CTBGrade=" + CTBGrade +
		       ", maniaGrade=" + maniaGrade +
		       ", status=" + status +
		       ", mode=" + mode +
		       '}';
	}
	
	public int getSize()
	{
		return size;
	}
	
	public void setSize(int size)
	{
		this.size = size;
	}
	
	public int getDrainTime()
	{
		return drainTime;
	}
	
	public void setDrainTime(int drainTime)
	{
		this.drainTime = drainTime;
	}
	
	public int getTotalTime()
	{
		return totalTime;
	}
	
	public void setTotalTime(int totalTime)
	{
		this.totalTime = totalTime;
	}
	
	public int getAudioPreviewStart()
	{
		return audioPreviewStart;
	}
	
	public void setAudioPreviewStart(int audioPreviewStart)
	{
		this.audioPreviewStart = audioPreviewStart;
	}
	
	public int getMapId()
	{
		return mapId;
	}
	
	public void setMapId(int mapId)
	{
		this.mapId = mapId;
	}
	
	public int getSetId()
	{
		return setId;
	}
	
	public void setSetId(int setId)
	{
		this.setId = setId;
	}
	
	public int getThreadID()
	{
		return threadID;
	}
	
	public void setThreadID(int threadID)
	{
		this.threadID = threadID;
	}
	
	public int getLastModified()
	{
		return lastModified;
	}
	
	public void setLastModified(int lastModified)
	{
		this.lastModified = lastModified;
	}
	
	public String getArtist()
	{
		return artist;
	}
	
	public void setArtist(String artist)
	{
		this.artist = artist;
	}
	
	public String getArtistUnicode()
	{
		return artistUnicode;
	}
	
	public void setArtistUnicode(String artistUnicode)
	{
		this.artistUnicode = artistUnicode;
	}
	
	public String getSong()
	{
		return song;
	}
	
	public void setSong(String song)
	{
		this.song = song;
	}
	
	public String getSongUnicode()
	{
		return songUnicode;
	}
	
	public void setSongUnicode(String songUnicode)
	{
		this.songUnicode = songUnicode;
	}
	
	public String getCreator()
	{
		return creator;
	}
	
	public void setCreator(String creator)
	{
		this.creator = creator;
	}
	
	public String getDifficulty()
	{
		return difficulty;
	}
	
	public void setDifficulty(String difficulty)
	{
		this.difficulty = difficulty;
	}
	
	public String getAudioFileName()
	{
		return audioFileName;
	}
	
	public void setAudioFileName(String audioFileName)
	{
		this.audioFileName = audioFileName;
	}
	
	public String getHash()
	{
		return hash;
	}
	
	public void setHash(String hash)
	{
		this.hash = hash;
	}
	
	public String getOsuFileName()
	{
		return osuFileName;
	}
	
	public void setOsuFileName(String osuFileName)
	{
		this.osuFileName = osuFileName;
	}
	
	public String getSongSource()
	{
		return songSource;
	}
	
	public void setSongSource(String songSource)
	{
		this.songSource = songSource;
	}
	
	public String getTags()
	{
		return tags;
	}
	
	public void setTags(String tags)
	{
		this.tags = tags;
	}
	
	public String getFont()
	{
		return font;
	}
	
	public void setFont(String font)
	{
		this.font = font;
	}
	
	public String getFolder()
	{
		return folder;
	}
	
	public void setFolder(String folder)
	{
		this.folder = folder;
	}
	
	public short getHitcircleCount()
	{
		return hitcircleCount;
	}
	
	public void setHitcircleCount(short hitcircleCount)
	{
		this.hitcircleCount = hitcircleCount;
	}
	
	public short getSliderCount()
	{
		return sliderCount;
	}
	
	public void setSliderCount(short sliderCount)
	{
		this.sliderCount = sliderCount;
	}
	
	public short getSpinnerCount()
	{
		return spinnerCount;
	}
	
	public void setSpinnerCount(short spinnerCount)
	{
		this.spinnerCount = spinnerCount;
	}
	
	public short getLocalOffset()
	{
		return localOffset;
	}
	
	public void setLocalOffset(short localOffset)
	{
		this.localOffset = localOffset;
	}
	
	public short getOnlineOffset()
	{
		return onlineOffset;
	}
	
	public void setOnlineOffset(short onlineOffset)
	{
		this.onlineOffset = onlineOffset;
	}
	
	public short getUnknown()
	{
		return unknown;
	}
	
	public void setUnknown(short unknown)
	{
		this.unknown = unknown;
	}
	
	public long getLastPlayed()
	{
		return lastPlayed;
	}
	
	public void setLastPlayed(long lastPlayed)
	{
		this.lastPlayed = lastPlayed;
	}
	
	public long getLastUpdateCheck()
	{
		return lastUpdateCheck;
	}
	
	public void setLastUpdateCheck(long lastUpdateCheck)
	{
		this.lastUpdateCheck = lastUpdateCheck;
	}
	
	public long getLastModifiedWindowsTick()
	{
		return lastModifiedWindowsTick;
	}
	
	public void setLastModifiedWindowsTick(long lastModifiedWindowsTick)
	{
		this.lastModifiedWindowsTick = lastModifiedWindowsTick;
	}
	
	public float getApproachRate()
	{
		return approachRate;
	}
	
	public void setApproachRate(float approachRate)
	{
		this.approachRate = approachRate;
	}
	
	public float getCircleSize()
	{
		return circleSize;
	}
	
	public void setCircleSize(float circleSize)
	{
		this.circleSize = circleSize;
	}
	
	public float getHPDrain()
	{
		return HPDrain;
	}
	
	public void setHPDrain(float HPDrain)
	{
		this.HPDrain = HPDrain;
	}
	
	public float getOverallDifficulty()
	{
		return overallDifficulty;
	}
	
	public void setOverallDifficulty(float overallDifficulty)
	{
		this.overallDifficulty = overallDifficulty;
	}
	
	public float getStackLeniency()
	{
		return stackLeniency;
	}
	
	public void setStackLeniency(float stackLeniency)
	{
		this.stackLeniency = stackLeniency;
	}
	
	public float getManiaScrollSpeed()
	{
		return maniaScrollSpeed;
	}
	
	public void setManiaScrollSpeed(float maniaScrollSpeed)
	{
		this.maniaScrollSpeed = maniaScrollSpeed;
	}
	
	public double getSliderVelocity()
	{
		return sliderVelocity;
	}
	
	public void setSliderVelocity(double sliderVelocity)
	{
		this.sliderVelocity = sliderVelocity;
	}
	
	public boolean isUnplayed()
	{
		return unplayed;
	}
	
	public void setUnplayed(boolean unplayed)
	{
		this.unplayed = unplayed;
	}
	
	public boolean isOsz2()
	{
		return osz2;
	}
	
	public void setOsz2(boolean osz2)
	{
		this.osz2 = osz2;
	}
	
	public boolean isIgnoreMapSounds()
	{
		return ignoreMapSounds;
	}
	
	public void setIgnoreMapSounds(boolean ignoreMapSounds)
	{
		this.ignoreMapSounds = ignoreMapSounds;
	}
	
	public boolean isIgnoreMapSkin()
	{
		return ignoreMapSkin;
	}
	
	public void setIgnoreMapSkin(boolean ignoreMapSkin)
	{
		this.ignoreMapSkin = ignoreMapSkin;
	}
	
	public boolean isDisableStory()
	{
		return disableStory;
	}
	
	public void setDisableStory(boolean disableStory)
	{
		this.disableStory = disableStory;
	}
	
	public boolean isDisableVideo()
	{
		return disableVideo;
	}
	
	public void setDisableVideo(boolean disableVideo)
	{
		this.disableVideo = disableVideo;
	}
	
	public boolean isVisualOverride()
	{
		return visualOverride;
	}
	
	public void setVisualOverride(boolean visualOverride)
	{
		this.visualOverride = visualOverride;
	}
	
	public Map<Integer, Double> getStarRating()
	{
		return starRating;
	}
	
	public void setStarRating(Map<Integer, Double> starRating)
	{
		this.starRating = starRating;
	}
	
	public Map<Integer, Double> getTaikoStarRating()
	{
		return taikoStarRating;
	}
	
	public void setTaikoStarRating(Map<Integer, Double> taikoStarRating)
	{
		this.taikoStarRating = taikoStarRating;
	}
	
	public Map<Integer, Double> getCTBStarRating()
	{
		return CTBStarRating;
	}
	
	public void setCTBStarRating(Map<Integer, Double> CTBStarRating)
	{
		this.CTBStarRating = CTBStarRating;
	}
	
	public Map<Integer, Double> getManiaStarRating()
	{
		return maniaStarRating;
	}
	
	public void setManiaStarRating(Map<Integer, Double> maniaStarRating)
	{
		this.maniaStarRating = maniaStarRating;
	}
	
	public List<DatabaseBeatmapTimingPoint> getTimingPoints()
	{
		return timingPoints;
	}
	
	public void setTimingPoints(List<DatabaseBeatmapTimingPoint> timingPoints)
	{
		this.timingPoints = timingPoints;
	}
	
	public OsuGrade getGrade()
	{
		return grade;
	}
	
	public void setGrade(OsuGrade grade)
	{
		this.grade = grade;
	}
	
	public OsuGrade getTaikoGrade()
	{
		return taikoGrade;
	}
	
	public void setTaikoGrade(OsuGrade taikoGrade)
	{
		this.taikoGrade = taikoGrade;
	}
	
	public OsuGrade getCTBGrade()
	{
		return CTBGrade;
	}
	
	public void setCTBGrade(OsuGrade CTBGrade)
	{
		this.CTBGrade = CTBGrade;
	}
	
	public OsuGrade getManiaGrade()
	{
		return maniaGrade;
	}
	
	public void setManiaGrade(OsuGrade maniaGrade)
	{
		this.maniaGrade = maniaGrade;
	}
	
	public RankedStatus getStatus()
	{
		return status;
	}
	
	public void setStatus(RankedStatus status)
	{
		this.status = status;
	}
	
	public OsuMode getMode()
	{
		return mode;
	}
	
	public void setMode(OsuMode mode)
	{
		this.mode = mode;
	}
}

