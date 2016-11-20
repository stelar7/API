package osu;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import javafx.scene.shape.Circle;
import osu.Beatmap.*;
import osu.Beatmap.HitObject.*;
import osu.Beatmap.HitObject.Slider.Edge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OsuParser
{
	
	public static class CurveCalc
	{
		
		public static Circle getCircumCircle(final Vector2 p1, final Vector2 p2, final Vector2 p3)
		{
			final float x1 = p1.x;
			final float y1 = p1.y;
			
			final float x2 = p2.x;
			final float y2 = p2.y;
			
			final float x3 = p3.x;
			final float y3 = p3.y;
			
			// center of circle
			final float D = 2 * ((x1 * (y2 - y3)) + (x2 * (y3 - y1)) + (x3 * (y1 - y2)));
			
			final float Ux = ((((x1 * x1) + (y1 * y1)) * (y2 - y3)) + (((x2 * x2) + (y2 * y2)) * (y3 - y1)) + (((x3 * x3) + (y3 * y3)) * (y1 - y2))) / D;
			final float Uy = ((((x1 * x1) + (y1 * y1)) * (x3 - x2)) + (((x2 * x2) + (y2 * y2)) * (x1 - x3)) + (((x3 * x3) + (y3 * y3)) * (x2 - x1))) / D;
			
			final float px = Ux - x1;
			final float py = Uy - y1;
			final float r  = (float) Math.sqrt((px * px) + (py * py));
			
			return new Circle(Ux, Uy, r);
			
		}
		
		private static Vector2 getEndPoint(final CurveType curve, final float sliderLength, final List<Vector2> points)
		{
			switch (curve)
			{
				case LINEAR:
					return CurveCalc.pointOnLine(points.get(0), points.get(1), sliderLength);
				case CATMULL:
					return null; // not supported
				case BEZIER:
				{
					if (points.size() < 2)
					{
						return null;
					}
					
					if (points.size() == 2)
					{
						return CurveCalc.pointOnLine(points.get(0), points.get(1), sliderLength);
					}
					
					final List<Vector2> pts = new ArrayList<>(points);
					
					// TODO: why doesnt this support higher order curves?
					final List<Vector2> useme = new ArrayList<>(pts).subList(0, 3);
					useme.remove(useme.size() - 1);
					useme.add(pts.get(pts.size() - 1));
					
					final Bezier<Vector2> bezier = new Bezier<>(useme.toArray(new Vector2[useme.size()]));
					final Vector2         out    = Vector2.Zero;
					bezier.valueAt(out, sliderLength);
					return out;
				}
				
				case PASS_THROUGH:
					if (points.size() < 2)
					{
						return null;
					}
					
					if (points.size() == 2)
					{
						return CurveCalc.pointOnLine(points.get(0), points.get(1), sliderLength);
					}
					
					if (points.size() > 3)
					{
						return CurveCalc.getEndPoint(CurveType.BEZIER, sliderLength, points);
					}
					
					final Vector2 a = points.get(0);
					final Vector2 b = points.get(1);
					final Vector2 c = points.get(2);
					
					final Circle circir = CurveCalc.getCircumCircle(a, b, c);
					float radians = (float) (sliderLength / circir.getRadius());
					
					if (CurveCalc.isLeft(a, b, c))
					{
						radians *= -1;
					}
					
					return CurveCalc.rotate((float) circir.getCenterX(), (float) circir.getCenterY(), a.x, a.y, radians);
				
				default:
					return null;
				
			}
		}
		
		public static boolean isLeft(final Vector2 a, final Vector2 b, final Vector2 c)
		{
			return (((b.x - a.x) * (c.y - a.y)) - ((b.y - a.y) * (c.x - a.x))) < 0;
		}
		
		public static Vector2 pointOnLine(final Vector2 p1, final Vector2 p2, final float length)
		{
			final float fullLength = (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
			final float n          = fullLength - length;
			
			final float x = ((n * p1.x) + (length * p2.x)) / fullLength;
			final float y = ((n * p1.y) + (length * p2.y)) / fullLength;
			
			return new Vector2(x, y);
		}
		
		public static Vector2 rotate(final double cx, final double cy, final float x, final float y, final float radians)
		{
			final float cos = (float) Math.cos(radians);
			final float sin = (float) Math.sin(radians);
			
			final float nx = (float) ((cos * (x - cx)) - (sin * (y - cy)) + cx);
			final float ny = (float) ((sin * (x - cx)) + (cos * (y - cy)) + cy);
			
			return new Vector2(nx, ny);
		}
	}
	
	private static final Pattern sectionPattern  = Pattern.compile("^\\[([a-zA-Z0-9]+)\\]$");
	private static final Pattern commentPattern  = Pattern.compile("^\\/\\/[a-zA-Z0-9 )(]*$");
	private static final Pattern keyValuePattern = Pattern.compile("^([a-zA-Z0-9]+)[ ]*:[ ]*(.+)$");
	private static final Pattern versionPattern  = Pattern.compile("^osu file format (v[0-9]+)$");
	private static final Pattern intPattern      = Pattern.compile("^[0-9]+$");
	private static       String  sectionString   = "";
	
	private static Beatmap beatmap;
	
	private static final List<String> timingLines = new ArrayList<>();
	private static final List<String> eventLines  = new ArrayList<>();
	private static final List<String> objectLines = new ArrayList<>();
	
	private static void buildBeatmap()
	{
		if (OsuParser.beatmap.keys.containsKey("Tags"))
		{
			OsuParser.beatmap.tags = OsuParser.beatmap.keys.get("Tags").split(" ");
		}
		
		OsuParser.eventLines.forEach(OsuParser::parseEvent);
		OsuParser.beatmap.breakTimes.sort((a, b) -> a.startTime > b.startTime ? 1 : -1);
		
		OsuParser.timingLines.forEach(OsuParser::parseTimingPoint);
		OsuParser.beatmap.timingPoints.sort((a, b) -> a.offset > b.offset ? 1 : -1);
		
		for (int i = 1; i < OsuParser.beatmap.timingPoints.size(); i++)
		{
			if (OsuParser.beatmap.timingPoints.get(i).bpm == 0)
			{
				OsuParser.beatmap.timingPoints.get(i).beatlength = OsuParser.beatmap.timingPoints.get(i - 1).beatlength;
				OsuParser.beatmap.timingPoints.get(i).bpm = OsuParser.beatmap.timingPoints.get(i - 1).bpm;
			}
		}
		
		OsuParser.beatmap.bpmAvg = OsuParser.beatmap.timingPoints.stream().collect(Collectors.averagingDouble(tp -> tp.bpm)).intValue();
		
		OsuParser.objectLines.forEach(OsuParser::parseHitObject);
		OsuParser.beatmap.hitObjects.sort((a, b) -> a.startTime > b.startTime ? 1 : -1);
		
		OsuParser.computeMaxCombo();
		OsuParser.computeDuration();
	}
	
	private static void computeDuration()
	{
		final HitObject first = OsuParser.beatmap.hitObjects.get(0);
		final HitObject last  = OsuParser.beatmap.hitObjects.get(OsuParser.beatmap.hitObjects.size() - 1);
		
		int breakTime = 0;
		
		for (final Break b : OsuParser.beatmap.breakTimes)
		{
			breakTime += b.endTime - b.startTime;
		}
		
		OsuParser.beatmap.totalTime = (int) Math.floor(last.startTime / 1000);
		OsuParser.beatmap.drainingTime = (int) Math.floor((last.startTime - first.startTime - breakTime) / 1000);
		
	}
	
	private static void computeMaxCombo()
	{
		if (OsuParser.beatmap.timingPoints.size() == 0)
		{
			return;
		}
		
		double      maxCombo  = 0;
		final float sliderMul = OsuParser.beatmap.keys.containsKey("SliderMultiplier") ? Float.parseFloat(OsuParser.beatmap.keys.get("SliderMultiplier")) : 1;
		final int   tickrate  = OsuParser.beatmap.keys.containsKey("SliderTickRate") ? Integer.parseInt(OsuParser.beatmap.keys.get("SliderTickRate")) : 10;
		
		int         i          = 0;
		TimingPoint current    = OsuParser.beatmap.timingPoints.get(i++);
		int         nextOffset = OsuParser.beatmap.timingPoints.size() >= (i + 1) ? OsuParser.beatmap.timingPoints.get(i).offset : Integer.MAX_VALUE;
		
		for (final HitObject o : OsuParser.beatmap.hitObjects)
		{
			if (o.startTime >= nextOffset)
			{
				current = OsuParser.beatmap.timingPoints.get(i++);
				nextOffset = OsuParser.beatmap.timingPoints.size() >= (i + 1) ? OsuParser.beatmap.timingPoints.get(i).offset : Integer.MAX_VALUE;
			}
			
			final float osuPxPerBeat = sliderMul * 100f * current.velocity;
			final float tickLength   = osuPxPerBeat / tickrate;
			
			switch (o.type)
			{
				case SPINNER:
				{
					maxCombo++;
					break;
				}
				case CIRCLE:
				{
					maxCombo++;
					break;
				}
				case MANIA_SLIDER:
				{
					// TODO: this ALMOST works...
					final ManiaSlider ms    = (ManiaSlider) o;
					final double      toAdd = Math.floor(Math.floor(((ms.endTime - ms.startTime) / tickLength) * 100) / 100) + 2;
					maxCombo += toAdd;
					break;
				}
				case SLIDER:
				{
					// TODO: this pretty much works... its off by 1 a few times?
					final Slider s           = (Slider) o;
					final double tickPerSide = Math.ceil((Math.floor((s.pixelLength / tickLength) * 100) / 100) - 1);
					maxCombo += ((s.edges.size() - 1) * (tickPerSide + 1)) + 1;
					break;
				}
				case UNKNOWN:
				{
					break;
				}
				default:
					break;
			}
		}
		OsuParser.beatmap.maxCombo = (int) maxCombo;
	}
	
	private static TimingPoint getTimingPoint(final int startTime)
	{
		for (int i = OsuParser.beatmap.timingPoints.size() - 1; i >= 0; i--)
		{
			if (OsuParser.beatmap.timingPoints.get(i).offset <= startTime)
			{
				return OsuParser.beatmap.timingPoints.get(i);
			}
		}
		return OsuParser.beatmap.timingPoints.get(0);
	}
	
	private static Map<String, String> parseAdditions(final String line)
	{
		final Map<String, String> data = new HashMap<>();
		
		if ((line == null) || line.isEmpty())
		{
			return data;
		}
		
		final String[] adds = line.split(":");
		
		if ((adds.length >= 1) && !adds[0].equalsIgnoreCase("0"))
		{
			String sample;
			switch (adds[0])
			{
				case "1":
				{
					sample = "normal";
					break;
				}
				case "2":
				{
					sample = "soft";
					break;
				}
				case "3":
				{
					sample = "drum";
					break;
				}
				default:
				{
					sample = adds[0];
				}
			}
			data.put("sample", sample);
		}
		
		if ((adds.length >= 2) && !adds[1].equalsIgnoreCase("0"))
		{
			String addSample;
			switch (adds[0])
			{
				case "1":
				{
					addSample = "normal";
					break;
				}
				case "2":
				{
					addSample = "soft";
					break;
				}
				case "3":
				{
					addSample = "drum";
					break;
				}
				default:
				{
					addSample = adds[0];
				}
			}
			data.put("additionalSample", addSample);
		}
		
		if ((adds.length >= 3) && !adds[2].equalsIgnoreCase("0"))
		{
			data.put("customSampleIndex", adds[2]);
		}
		if ((adds.length >= 4) && !adds[3].equalsIgnoreCase("0"))
		{
			data.put("hitsoundVolume", adds[3]);
		}
		if ((adds.length >= 5) && !adds[4].isEmpty())
		{
			data.put("hitsound", adds[3]);
		}
		
		return data;
	}
	
	private static void parseEvent(final String line)
	{
		final String[] members = line.split(",");
		
		final Matcher matcher1 = OsuParser.intPattern.matcher(members[1]);
		final Matcher matcher2 = OsuParser.intPattern.matcher(members[2]);
		
		if (members.length == 3)
		{
			if (members[0].equalsIgnoreCase("0") && members[1].equalsIgnoreCase("0"))
			{
				final String bg  = members[2].trim();
				final int    bgl = bg.length() - 1;
				
				if ((bg.charAt(0) == '"') && (bg.charAt(bgl) == '"'))
				{
					OsuParser.beatmap.bgFile = bg.substring(1, bgl);
				} else
				{
					OsuParser.beatmap.bgFile = bg;
				}
			} else if (members[0].equalsIgnoreCase("2") && matcher1.matches() && matcher2.matches())
			{
				final long start = Long.parseLong(matcher1.group());
				final long end   = Long.parseLong(matcher2.group());
				OsuParser.beatmap.breakTimes.add(new Break(start, end));
			}
		}
	}
	
	private static void parseHitObject(final String line)
	{
		final String[] members = line.split(",");
		
		final int soundType  = Integer.parseInt(members[4]);
		final int objectType = Integer.parseInt(members[3]);
		
		Beatmap.HitObject hitObject = new HitObject();
		
		hitObject.startTime = Integer.parseInt(members[2]);
		hitObject.newCombo = (objectType & 4) == 4;
		int x = Integer.parseInt(members[0]);
		int y = Integer.parseInt(members[1]);
		
		hitObject.position = new Vector2(x, y);
		
		if ((soundType & 2) == 2)
		{
			hitObject.soundTypes.add(HitObject.SoundType.WHISTLE);
		}
		
		if ((soundType & 4) == 4)
		{
			hitObject.soundTypes.add(HitObject.SoundType.FINISH);
		}
		
		if ((soundType & 8) == 8)
		{
			hitObject.soundTypes.add(HitObject.SoundType.CLAP);
		}
		
		if (hitObject.soundTypes.isEmpty())
		{
			hitObject.soundTypes.add(HitObject.SoundType.NORMAL);
		}
		
		if ((objectType & 1) == 1)
		{
			OsuParser.beatmap.circleCount++;
			
			hitObject.type = HitObject.Type.CIRCLE;
			hitObject.additions = OsuParser.parseAdditions(members.length >= 6 ? members[5] : "");
			
		} else if ((objectType & 8) == 8)
		{
			OsuParser.beatmap.spinnerCount++;
			
			hitObject = new Spinner(hitObject);
			hitObject.type = HitObject.Type.SPINNER;
			hitObject.additions = OsuParser.parseAdditions(members.length >= 7 ? members[6] : "");
			
			((Spinner) hitObject).endTime = Integer.parseInt(members[5]);
			
		} else if ((objectType & 128) == 128)
		{
			OsuParser.beatmap.sliderCount++;
			
			hitObject = new ManiaSlider(hitObject);
			hitObject.type = HitObject.Type.MANIA_SLIDER;
			hitObject.additions = OsuParser.parseAdditions(members.length >= 6 ? members[5] : "");
			
			((ManiaSlider) hitObject).endTime = Integer.parseInt(hitObject.additions.get("sample"));
			
		} else if ((objectType & 2) == 2)
		{
			OsuParser.beatmap.sliderCount++;
			
			hitObject = new Slider(hitObject);
			hitObject.type = HitObject.Type.SLIDER;
			hitObject.additions = OsuParser.parseAdditions(members.length >= 11 ? members[10] : "");
			
			((Slider) hitObject).repeatCount = Integer.parseInt(members[6]);
			((Slider) hitObject).pixelLength = Float.parseFloat(members[7]);
			((Slider) hitObject).points.add(hitObject.position);
			
			final TimingPoint timing = OsuParser.getTimingPoint(hitObject.startTime);
			
			final float pxPerBeat   = Float.parseFloat(OsuParser.beatmap.keys.get("SliderMultiplier")) * 100f * timing.velocity;
			final float beatsNumber = (((Slider) hitObject).pixelLength * ((Slider) hitObject).repeatCount) / pxPerBeat;
			((Slider) hitObject).duration = (int) Math.ceil(beatsNumber * timing.beatlength);
			((Slider) hitObject).endTime = hitObject.startTime + ((Slider) hitObject).duration;
			
			final String[] points = members[5].split("\\|");
			((Slider) hitObject).curveType = CurveType.fromChar(points[0]);
			
			for (int i = 1, l = points.length; i < l; i++)
			{
				final String[] coordinates = points[i].split(":");
				x = Integer.parseInt(coordinates[0]);
				y = Integer.parseInt(coordinates[1]);
				((Slider) hitObject).points.add(new Vector2(x, y));
			}
			
			final String[] edgeSounds    = members.length >= 9 ? members[8].split("\\|") : new String[]{};
			final String[] edgeAdditions = members.length >= 10 ? members[9].split("\\|") : new String[]{};
			
			for (int j = 0, lgt = ((Slider) hitObject).repeatCount + 1; j < lgt; j++)
			{
				final Edge e = new Edge();
				
				e.additions = OsuParser.parseAdditions((edgeAdditions.length > 0) && (edgeAdditions.length >= j) ? edgeAdditions[j] : "");
				
				if (edgeSounds.length > j)
				{
					final int sound = Integer.parseInt(edgeSounds[j]);
					if ((sound & 2) == 2)
					{
						e.soundTypes.add(HitObject.SoundType.WHISTLE);
					}
					
					if ((sound & 4) == 4)
					{
						e.soundTypes.add(HitObject.SoundType.FINISH);
					}
					
					if ((sound & 8) == 8)
					{
						e.soundTypes.add(HitObject.SoundType.CLAP);
					}
					
					if (e.soundTypes.isEmpty())
					{
						e.soundTypes.add(HitObject.SoundType.NORMAL);
					}
				}
				((Slider) hitObject).edges.add(e);
			}
			
			final Vector2 endPoint = CurveCalc.getEndPoint(((Slider) hitObject).curveType, ((Slider) hitObject).pixelLength, ((Slider) hitObject).points);
			
			if ((endPoint != null) && ((endPoint.x < Beatmap.maxSize.x) && (endPoint.y < Beatmap.maxSize.y)) && ((endPoint.x > 0) && (endPoint.y > Beatmap.maxSize.y)))
			{
				System.out.println(endPoint);
				((Slider) hitObject).endPosition = endPoint;
			} else
			{
				((Slider) hitObject).endPosition = ((Slider) hitObject).points.get(((Slider) hitObject).points.size() - 1);
			}
			
		} else
		{
			hitObject.type = Type.UNKNOWN;
		}
		
		OsuParser.beatmap.hitObjects.add(hitObject);
		
	}
	
	private static void parseLine(String line)
	{
		line = line.trim();
		if (line.isEmpty())
		{
			return;
		}
		
		Matcher matcher = OsuParser.sectionPattern.matcher(line);
		
		if (matcher.matches())
		{
			OsuParser.sectionString = matcher.group(1).toLowerCase(Locale.ENGLISH);
			return;
		}
		
		matcher = OsuParser.commentPattern.matcher(line);
		if (matcher.matches())
		{
			return;
		}
		
		switch (OsuParser.sectionString)
		{
			case "timingpoints":
			{
				OsuParser.timingLines.add(line);
				break;
			}
			
			case "hitobjects":
			{
				OsuParser.objectLines.add(line);
				break;
			}
			
			case "events":
			{
				OsuParser.eventLines.add(line);
				break;
			}
			
			default:
			{
				if (OsuParser.sectionString.isEmpty())
				{
					matcher = OsuParser.versionPattern.matcher(line);
					
					if (matcher.matches())
					{
						OsuParser.beatmap.osuFileVersion = matcher.group();
						return;
					}
				}
				matcher = OsuParser.keyValuePattern.matcher(line);
				
				if (matcher.matches())
				{
					OsuParser.beatmap.keys.put(matcher.group(1), matcher.group(2));
				}
			}
		}
	}
	
	public static Beatmap parseOsuSong(final Path f, Mod... mods)
	{
		try
		{
			OsuParser.beatmap = new Beatmap();
			
			Files.readAllLines(f).forEach(OsuParser::parseLine);
			OsuParser.buildBeatmap();
			
			return OsuParser.beatmap;
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private static void parseTimingPoint(final String line)
	{
		final String[] members = line.split(",");
		
		final Beatmap.TimingPoint timingPoint = new TimingPoint();
		
		timingPoint.offset = Integer.parseInt(members[0]);
		timingPoint.beatlength = Float.parseFloat(members[1]);
		timingPoint.timingSignature = Integer.parseInt(members[2]);
		timingPoint.sampleSetId = Integer.parseInt(members[3]);
		timingPoint.customSampleIndex = Integer.parseInt(members[4]);
		timingPoint.sampleVolume = Integer.parseInt(members[5]);
		if (members.length > 6)
		{
			timingPoint.timingChange = members[6].equalsIgnoreCase("1");
			timingPoint.kiaiTimeActive = members[7].equalsIgnoreCase("1");
		}
		
		if (timingPoint.beatlength != 0)
		{
			if (timingPoint.beatlength > 0)
			{
				final int bpm = Math.round(60000f / timingPoint.beatlength);
				
				final boolean hasMax = OsuParser.beatmap.bpmMax == 0;
				final boolean hasMin = OsuParser.beatmap.bpmMin != 0;
				
				OsuParser.beatmap.bpmMax = hasMax ? Math.max(OsuParser.beatmap.bpmMax, bpm) : bpm;
				OsuParser.beatmap.bpmMin = hasMin ? Math.min(OsuParser.beatmap.bpmMin, bpm) : bpm;
				
				timingPoint.bpm = bpm;
			} else
			{
				timingPoint.velocity = Math.abs(100 / timingPoint.beatlength);
			}
		}
		
		OsuParser.beatmap.timingPoints.add(timingPoint);
	}
}
