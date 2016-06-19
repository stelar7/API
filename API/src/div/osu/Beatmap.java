package div.osu;

import java.util.*;

import com.badlogic.gdx.math.*;

public class Beatmap
{
    public static class Break
    {
        long startTime;

        long endTime;

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

            String code;

            CurveType(final String t)
            {
                this.code = t;
            }
        };

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
                List<SoundType>     soundTypes = new ArrayList<>();
            }

            Vector2       endPosition;
            int           repeatCount;
            float         pixelLength;
            List<Edge>    edges  = new ArrayList<>();
            List<Vector2> points = new ArrayList<>();
            int           duration;

            CurveType     curveType;

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

            int bitFlag;

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

            int bitFlag;

            Type(final int t)
            {
                this.bitFlag = t;
            }
        }

        int                 startTime;
        boolean             newCombo;
        List<SoundType>     soundTypes = new ArrayList<>();

        Vector2             position;

        Type                type;

        Map<String, String> additions;

        @Override
        public String toString()
        {
            return "HitObject [startTime=" + this.startTime + ", newCombo=" + this.newCombo + ", soundTypes=" + this.soundTypes + ", position=" + this.position + ", type=" + this.type + ", additions=" + this.additions + "]";
        }

    }

    public static class TimingPoint
    {
        int     offset;
        float   beatlength;
        float   velocity = 1;
        int     timingSignature;
        int     sampleSetId;
        int     customSampleIndex;
        int     sampleVolume;
        boolean timingChange;
        boolean kiaiTimeActive;
        float   bpm;

        @Override
        public String toString()
        {
            return "TimingPoint [offset=" + this.offset + ", beatlength=" + this.beatlength + ", velocity=" + this.velocity + ", timingSignature=" + this.timingSignature + ", sampleSetId=" + this.sampleSetId + ", customSampleIndex=" + this.customSampleIndex + ", sampleVolume=" + this.sampleVolume + ", timingChange=" + this.timingChange + ", kiaiTimeActive=" + this.kiaiTimeActive + ", bpm=" + this.bpm + "]";
        }
    }

    public static final Vector2 maxSize      = new Vector2(512, 384);
    String                      fileFormat   = null;
    Map<String, String>         keys         = new HashMap<>();
    List<Break>                 breakTimes   = new ArrayList<>();
    List<TimingPoint>           timingPoints = new ArrayList<>();
    String[]                    tags;
    String                      bgFile;
    float                       bpmMin;
    float                       bpmAvg;
    float                       bpmMax;
    int                         maxCombo;
    int                         drainingTime;
    int                         totalTime;
    int                         circleCount;
    int                         spinnerCount;
    int                         sliderCount;
    List<HitObject>             hitObjects   = new ArrayList<>();

    @Override
    public String toString()
    {
        return "Beatmap [fileFormat=" + this.fileFormat + ", keys=" + this.keys + ", breakTimes=" + this.breakTimes + ", timingPoints=" + this.timingPoints + ", tags=" + Arrays.toString(this.tags) + ", bgFile=" + this.bgFile + ", bpmMin=" + this.bpmMin + ", bpmMax=" + this.bpmMax + ", circleCount=" + this.circleCount + ", spinnerCount=" + this.spinnerCount + ", sliderCount=" + this.sliderCount + ", hitObjects=" + this.hitObjects + "]";
    }

}
