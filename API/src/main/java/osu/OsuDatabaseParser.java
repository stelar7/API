package osu;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class OsuDatabaseParser
{
	private static final int dbUpdatePatch = 20140609;
	
	
	public static OsuDatabaseScores parseScoresDB(Path pathToFile) throws IOException
	{
		ByteBuffer file = ByteBuffer.wrap(Files.readAllBytes(pathToFile));
		file.order(ByteOrder.LITTLE_ENDIAN);
		
		OsuDatabaseScores s = new OsuDatabaseScores();
		
		s.version = file.getInt();
		
		int count = file.getInt();
		for (int i = 0; i < count; i++)
		{
			s.maps.add(readScoresBeatmap(file));
		}
		
		return s;
	}
	
	private static OsuDatabaseScoresBeatmap readScoresBeatmap(ByteBuffer file)
	{
		OsuDatabaseScoresBeatmap map = new OsuDatabaseScoresBeatmap();
		
		map.hash = readString(file);
		int count = file.getInt();
		for (int i = 0; i < count; i++)
		{
			map.scores.add(readScoresBeatmapScores(file));
		}
		
		return map;
	}
	
	private static OsuDatabaseScoresBeatmapScore readScoresBeatmapScores(ByteBuffer file)
	{
		OsuDatabaseScoresBeatmapScore score = new OsuDatabaseScoresBeatmapScore();
		score.mode = OsuMode.get(file.get());
		score.version = file.getInt();
		score.mapHash = readString(file);
		score.player = readString(file);
		score.replayHash = readString(file);
		score.c300 = file.getShort();
		score.c100 = file.getShort();
		score.c50 = file.getShort();
		score.cGeki = file.getShort();
		score.cKatsu = file.getShort();
		score.cMiss = file.getShort();
		score.score = file.getInt();
		score.maxCombo = file.getShort();
		score.perfect = file.get() == 0;
		score.mods = file.getInt();
		score.empty = readString(file);
		score.timestampWindowsTick = file.getLong();
		score.minusOne = file.getInt();
		score.onlineScoreId = file.getLong();
		
		return score;
	}
	
	
	public static OsuDatabaseCollection parseCollectionDB(Path pathToFile) throws IOException
	{
		ByteBuffer file = ByteBuffer.wrap(Files.readAllBytes(pathToFile));
		file.order(ByteOrder.LITTLE_ENDIAN);
		
		OsuDatabaseCollection c = new OsuDatabaseCollection();
		c.version = file.getInt();
		
		int count = file.getInt();
		for (int i = 0; i < count; i++)
		{
			c.collections.add(readCollection(file));
		}
		return c;
	}
	
	private static OsuDatabaseCollectionItem readCollection(ByteBuffer file)
	{
		OsuDatabaseCollectionItem item = new OsuDatabaseCollectionItem();
		item.name = readString(file);
		int count = file.getInt();
		
		for (int i = 0; i < count; i++)
		{
			item.maps.add(readString(file));
		}
		return item;
	}
	
	public static OsuDatabase parseOSUDB(Path pathToFile) throws IOException
	{
		ByteBuffer file = ByteBuffer.wrap(Files.readAllBytes(pathToFile));
		file.order(ByteOrder.LITTLE_ENDIAN);
		
		OsuDatabase db = new OsuDatabase();
		
		db.version = file.getInt();
		db.folderCount = file.getInt();
		db.locked = file.get() == 0;
		db.time = Instant.ofEpochMilli(file.getLong());
		db.name = readString(file);
		db.beatmapCount = file.getInt();
		
		for (int i = 0; i < db.beatmapCount; i++)
		{
			db.beatmaps.add(readBeatmap(file, db.version));
		}
		
		db.unknown = file.getInt();
		
		return db;
	}
	
	
	private static DatabaseBeatmap readBeatmap(ByteBuffer file, int version)
	{
		DatabaseBeatmap map = new DatabaseBeatmap();
		
		map.size = file.getInt();
		map.artist = readString(file);
		map.artistUnicode = readString(file);
		map.song = readString(file);
		map.songUnicode = readString(file);
		map.creator = readString(file);
		map.difficulty = readString(file);
		map.audioFileName = readString(file);
		map.hash = readString(file);
		map.osuFileName = readString(file);
		map.status = RankedStatus.get(file.get());
		map.hitcircleCount = file.getShort();
		map.sliderCount = file.getShort();
		map.spinnerCount = file.getShort();
		map.lastModifiedWindowsTick = file.getLong();
		if (version >= dbUpdatePatch)
		{
			map.approachRate = file.getFloat();
			map.circleSize = file.getFloat();
			map.HPDrain = file.getFloat();
			map.overallDifficulty = file.getFloat();
		} else
		{
			map.approachRate = file.get();
			map.circleSize = file.get();
			map.HPDrain = file.get();
			map.overallDifficulty = file.get();
		}
		
		map.sliderVelocity = file.getDouble();
		
		if (version >= dbUpdatePatch)
		{
			map.starRating = readMap(file);
			map.taikoStarRating = readMap(file);
			map.CTBStarRating = readMap(file);
			map.maniaStarRating = readMap(file);
		}
		
		map.drainTime = file.getInt();
		map.totalTime = file.getInt();
		map.audioPreviewStart = file.getInt();
		
		int tpCount = file.getInt();
		for (int i = 0; i < tpCount; i++)
		{
			map.timingPoints.add(readTimingPoint(file));
		}
		
		map.mapId = file.getInt();
		map.setId = file.getInt();
		map.threadID = file.getInt();
		map.grade = OsuGrade.get(file.get());
		map.taikoGrade = OsuGrade.get(file.get());
		map.CTBGrade = OsuGrade.get(file.get());
		map.maniaGrade = OsuGrade.get(file.get());
		map.localOffset = file.getShort();
		map.stackLeniency = file.getFloat();
		map.mode = OsuMode.get(file.get());
		map.songSource = readString(file);
		map.tags = readString(file);
		map.onlineOffset = file.getShort();
		map.font = readString(file);
		map.unplayed = file.get() == 0;
		map.lastPlayed = file.getLong();
		map.osz2 = file.get() == 0;
		map.folder = readString(file);
		map.lastUpdateCheck = file.getLong();
		map.ignoreMapSounds = file.get() == 0;
		map.ignoreMapSkin = file.get() == 0;
		map.disableStory = file.get() == 0;
		map.disableVideo = file.get() == 0;
		map.visualOverride = file.get() == 0;
		if (version < dbUpdatePatch)
		{
			map.unknown = file.getShort();
		}
		map.lastModified = file.getInt();
		map.maniaScrollSpeed = file.get();
		
		return map;
	}
	
	private static Map<Integer, Double> readMap(ByteBuffer file)
	{
		Map<Integer, Double> result = new HashMap<>();
		
		int pairs = file.getInt();
		for (int i = 0; i < pairs; i++)
		{
			file.get(); // 0x08
			int mod = file.getInt();
			file.get(); // 0x0d
			double rating = file.getDouble();
			result.put(mod, rating);
		}
		
		return result;
	}
	
	private static DatabaseBeatmapTimingPoint readTimingPoint(ByteBuffer file)
	{
		DatabaseBeatmapTimingPoint tp = new DatabaseBeatmapTimingPoint();
		tp.bpm = file.getDouble();
		tp.offset = file.getDouble();
		tp.inherited = file.get() == 0;
		return tp;
	}
	
	private static String readString(ByteBuffer buffer)
	{
		byte type = buffer.get();
		
		if (type == 0)
		{
			return "";
		}
		
		if (type != 11)
		{
			throw new RuntimeException("Unknown String format");
		}
		
		byte[] data = new byte[readULEB128(buffer)];
		buffer.get(data);
		
		return new String(data);
	}
	
	private static int readULEB128(ByteBuffer buffer)
	{
		int result = 0;
		int cur;
		int count  = 0;
		do
		{
			cur = buffer.get() & 0xff;
			result |= (cur & 0x7f) << (count * 7);
			count++;
		} while (((cur & 0x80) == 0x80) && count < 5);
		if ((cur & 0x80) == 0x80)
		{
			throw new RuntimeException("invalid LEB128 sequence");
		}
		return result;
		
	}
	
}
