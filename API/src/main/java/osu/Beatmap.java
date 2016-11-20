package osu;

import com.badlogic.gdx.math.Vector2;

import java.util.*;


public class Beatmap
{
	public static class Break
	{
		final long startTime;
		final long endTime;
		
		public Break(final long start, final long end)
		{
			this.startTime = start;
			this.endTime = end;
		}
		
		@Override
		public String toString()
		{
			return "Break [startTime=" + this.startTime + ", endTime=" + this.endTime + "]";
		}
	}
	
	public static class HitObject
	{
		
		enum CurveType
		{
			CATMULL("C"),
			BEZIER("B"),
			LINEAR("L"),
			UNKNOWN(""),
			PASS_THROUGH("P");
			
			public static CurveType fromChar(final String string)
			{
				for (final CurveType c : CurveType.values())
				{
					if (c.code.equals(string))
					{
						return c;
					}
				}
				return null;
			}
			
			final String code;
			
			CurveType(final String t)
			{
				this.code = t;
			}
		}
		
		public static class ManiaSlider extends Spinner
		{
			public ManiaSlider(final HitObject hitObject)
			{
				super(hitObject);
			}
		}
		
		public static class Slider extends Spinner
		{
			public static class Edge
			{
				Map<String, String> additions;
				final List<SoundType> soundTypes = new ArrayList<>();
			}
			
			Vector2 endPosition;
			
			int repeatCount;
			int duration;
			
			float pixelLength;
			
			final List<Edge>    edges  = new ArrayList<>();
			final List<Vector2> points = new ArrayList<>();
			
			CurveType curveType;
			
			public Slider(final HitObject hitObject)
			{
				super(hitObject);
			}
			
			@Override
			public String toString()
			{
				return "Slider [endPosition=" + this.endPosition + ", repeatCount=" + this.repeatCount + ", pixelLength=" + this.pixelLength + ", edges=" + this.edges + ", points=" + this.points + ", duration=" + this.duration + ", curveType=" + this.curveType + ", endTime=" + this.endTime + ", startTime=" + this.startTime + ", newCombo=" + this.newCombo + ", soundTypes=" + this.soundTypes + ", position=" + this.position + ", type=" + this.type + ", additions=" + this.additions + "]";
			}
		}
		
		enum SoundType
		{
			NORMAL(0),
			WHISTLE(2),
			FINISH(4),
			CLAP(8);
			
			final int bitFlag;
			
			SoundType(final int t)
			{
				this.bitFlag = t;
			}
		}
		
		public static class Spinner extends HitObject
		{
			int endTime;
			
			public Spinner(final HitObject hitObject)
			{
				this.additions = hitObject.additions;
				this.startTime = hitObject.startTime;
				this.newCombo = hitObject.newCombo;
				this.position = hitObject.position;
				this.soundTypes = hitObject.soundTypes;
				this.type = hitObject.type;
			}
			
			@Override
			public String toString()
			{
				return "Spinner [endTime=" + this.endTime + ", startTime=" + this.startTime + ", newCombo=" + this.newCombo + ", soundTypes=" + this.soundTypes + ", position=" + this.position + ", type=" + this.type + ", additions=" + this.additions + "]";
			}
		}
		
		enum Type
		{
			CIRCLE(1),
			SPINNER(2),
			SLIDER(8),
			MANIA_SLIDER(128),
			UNKNOWN(-1);
			
			final int bitFlag;
			
			Type(final int t)
			{
				this.bitFlag = t;
			}
		}
		
		int startTime;
		
		boolean newCombo;
		
		Vector2 position;
		
		Type type;
		
		List<SoundType> soundTypes = new ArrayList<>();
		
		Map<String, String> additions;
		
		@Override
		public String toString()
		{
			return "HitObject [startTime=" + this.startTime + ", newCombo=" + this.newCombo + ", soundTypes=" + this.soundTypes + ", position=" + this.position + ", type=" + this.type + ", additions=" + this.additions + "]";
		}
		
	}
	
	public static class TimingPoint
	{
		float beatlength;
		float velocity = 1;
		float bpm;
		
		int offset;
		int timingSignature;
		int sampleSetId;
		int customSampleIndex;
		int sampleVolume;
		
		boolean timingChange;
		boolean kiaiTimeActive;
		
		@Override
		public String toString()
		{
			return "TimingPoint [offset=" + this.offset + ", beatlength=" + this.beatlength + ", velocity=" + this.velocity + ", timingSignature=" + this.timingSignature + ", sampleSetId=" + this.sampleSetId + ", customSampleIndex=" + this.customSampleIndex + ", sampleVolume=" + this.sampleVolume + ", timingChange=" + this.timingChange + ", kiaiTimeActive=" + this.kiaiTimeActive + ", bpm=" + this.bpm + "]";
		}
	}
	
	public static final Vector2 maxSize = new Vector2(512, 384);
	
	final Map<String, String> keys         = new HashMap<>();
	final List<Break>         breakTimes   = new ArrayList<>();
	final List<TimingPoint>   timingPoints = new ArrayList<>();
	final List<HitObject>     hitObjects   = new ArrayList<>();
	
	String[] tags;
	
	String bgFile;
	String osuFileVersion;
	
	float bpmMin;
	float bpmAvg;
	float bpmMax;
	
	int maxCombo;
	int circleCount;
	int spinnerCount;
	int sliderCount;
	
	int drainingTime;
	int totalTime;
	
	int beatmapVersion;
	int stackLenient;
	
	Mode mode;
	
	float arMod = 0;
	float odMod = 0;
	float csMod = 0;
	
	float od;
	float ar;
	float cs;
	final float od0_ms = 79.5f,
		od10_ms        = 19.5f,
		ar0_ms         = 1800f,
		ar5_ms         = 1200f,
		ar10_ms        = 450f;
	
	
	final float od_ms_step = 6f,
		ar_ms_step1        = 120f, // ar0-5
		ar_ms_step2        = 150f; // ar5-10
	
	public String getArtist()
	{
		return keys.get("Artist");
	}
	
	public String getTitle()
	{
		return keys.get("Title");
	}
	
	public String getCreator()
	{
		return keys.get("Creator");
	}
	
	public String getVersion()
	{
		return keys.get("Version");
	}
	
	public float getHp()
	{
		return Float.valueOf(keys.get("HPDrainRate"));
	}
	
	public float getCs()
	{
		return cs;
	}
	
	public float getOd()
	{
		return od;
	}
	
	public float getAr()
	{
		return ar;
	}
	
	public float getSv()
	{
		return Float.valueOf(keys.get("SliderMultiplier"));
	}
	
	public float getTickRate()
	{
		return Float.valueOf(keys.get("SliderTickRate"));
	}
	
	
	public void applyMods(Mod[] mods)
	{
		List<Mod> modList = Arrays.asList(mods);
		
		float speed = 1;
		
		if (modList.contains(Mod.DT) || modList.contains(Mod.NC))
		{
			speed *= 1.5d;
		}
		if (modList.contains(Mod.HT))
		{
			speed *= .75d;
		}
		
		odMod = 1;
		if (modList.contains(Mod.HR))
		{
			odMod *= 1.4d;
		}
		if (modList.contains(Mod.EZ))
		{
			odMod *= .5d;
		}
		od = Float.valueOf(keys.get("OverallDifficulty")) * odMod;
		
		arMod = 1;
		if (modList.contains(Mod.HR))
		{
			arMod *= 1.4d;
		}
		if (modList.contains(Mod.EZ))
		{
			arMod *= .5d;
		}
		float ar = Float.valueOf(keys.get("ApproachRate")) * arMod;
		
		
		csMod = 1;
		if (modList.contains(Mod.HR))
		{
			csMod *= 1.3d;
		}
		if (modList.contains(Mod.EZ))
		{
			csMod *= .5d;
		}
		float cs = Float.valueOf(keys.get("CircleSize")) * csMod;
		
		float arms = getAr() <= 5 ? (ar0_ms - ar_ms_step1 * getAr()) : (ar5_ms - ar_ms_step2 * (getAr() - 5));
		float odms = (float) (od0_ms - Math.ceil(od_ms_step * getOd()));
		
		odms = Math.min(od0_ms, Math.max(od10_ms, odms));
		arms = Math.min(ar0_ms, Math.max(ar10_ms, arms));
		
		odms /= speed;
		arms /= speed;
		
		this.od = (od0_ms - odms) / od_ms_step;
		this.ar = ar <= 5.0f ? ((ar0_ms - arms) / ar_ms_step1) : (5.0f + (ar5_ms - arms) / ar_ms_step2);
		
		this.cs *= csMod;
		
		this.cs = Math.max(0.0f, Math.min(10.0f, cs));
		
		for (TimingPoint tp : this.timingPoints)
		{
			tp.offset = (int) (tp.offset / speed);
			if (!tp.timingChange)
			{
				tp.beatlength /= speed;
			}
		}
		
		for (HitObject o : hitObjects)
		{
			o.startTime = (int) (o.startTime / speed);
			if (o instanceof HitObject.Spinner)
			{
				((HitObject.Spinner) o).endTime = (int) (((HitObject.Spinner) o).endTime / speed);
			}
		}
	}
	
	public enum Mode
	{
		OSU,
		TAIKO,
		CTB,
		MANIA
	}
	
	public enum Mod
	{
		NOMOD,
		NF,
		EZ,
		HD,
		HR,
		DT,
		HT,
		NC,
		FL,
		SO
	}
	
	@Override
	public String toString()
	{
		return "Beatmap [osuFileVersion=" + this.osuFileVersion + ", keys=" + this.keys + ", breakTimes=" + this.breakTimes + ", timingPoints=" + this.timingPoints + ", tags=" + Arrays.toString(this.tags) + ", bgFile=" + this.bgFile + ", bpmMin=" + this.bpmMin + ", bpmMax=" + this.bpmMax + ", circleCount=" + this.circleCount + ", spinnerCount=" + this.spinnerCount + ", sliderCount=" + this.sliderCount + ", hitObjects=" + this.hitObjects + "]";
	}
	
}
