package div;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class Waybill
{
    public static class BaseData
    {
        private LocationReference sender;
        private LocationReference reciver;
        private Incoterm          type;
        
        public BaseData(final LocationReference sender, final LocationReference reciver, final Incoterm type)
        {
            super();
            this.sender = sender;
            this.reciver = reciver;
            this.type = type;
        }
        
        public LocationReference getReciver()
        {
            return this.reciver;
        }
        
        public LocationReference getSender()
        {
            return this.sender;
        }
        
        public Incoterm getType()
        {
            return this.type;
        }
        
        @Override
        public String toString()
        {
            return "[sender=" + this.sender + ", reciver=" + this.reciver + ", type=" + this.type + "]";
        }
    }
    
    public static class UnNumber
    {
        private int    number;
        @SerializedName("class")
        private String clazz;
        private String desc;
        
        @Override
        public String toString()
        {
            return "UnNumber [id=" + number + ", class=" + clazz + ", description=" + desc + "]";
        }
        
        private static volatile Map<Integer, UnNumber> numbers;
        
        public static UnNumber from(int id)
        {
            if (numbers == null)
            {
                try
                {
                    numbers = new HashMap<>();
                    
                    String numberdata = Internet.getPageSource("https://www.dropbox.com/s/uiwav8t3vl3v7p1/unnumbers.json?raw=1");
                    
                    List<UnNumber> numberlist = new Gson().fromJson(numberdata, new TypeToken<List<UnNumber>>()
                    {}.getType());
                    
                    numberlist.forEach(e -> numbers.put(e.number, e));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            
            return numbers.get(id);
        }
        
        public UnNumber(int id, String clazz, String desc)
        {
            super();
            this.number = id;
            this.clazz = clazz;
            this.desc = desc;
        }
    }
    
    public static class Hazard
    {
        private UnNumber unNumber;
        private double   amount;
        private String   amountSpec; // kg/ltr
        
        public Hazard(final int id, final double amount, final String amountSpec)
        {
            super();
            this.unNumber = UnNumber.from(id);
            this.amount = amount;
            this.amountSpec = amountSpec;
        }
        
        public double getAmount()
        {
            return this.amount;
        }
        
        public String getAmountSpec()
        {
            return this.amountSpec;
        }
        
        public int getId()
        {
            return this.unNumber.number;
        }
        
        public String getDescription()
        {
            return this.unNumber.desc;
        }
        
        public String getGroup()
        {
            return this.unNumber.clazz;
        }
        
        @Override
        public String toString()
        {
            return "Hazard [" + unNumber + ", amount=" + amount + ", amountSpec=" + amountSpec + "]";
        }
        
    }
    
    public enum Incoterm
    {
        EXW,
        FCA,
        CPT,
        CIP,
        DAT,
        DAP,
        DDP,
        FAS,
        FOB,
        CFR,
        CIF,
        DAF,
        DES,
        DEQ,
        DDU
    }
    
    public static class Location
    {
        private String name;
        private String address;
        
        private PostCode area;
        
        public Location(final String name, final String address, final PostCode area)
        {
            super();
            this.name = name;
            this.address = address;
            this.area = area;
        }
        
        public static Location from(Long locationId)
        {
            // database lookup..?
            return new Location("test", "location", PostCode.from(5337L));
        }
        
        public String getAddress()
        {
            return this.address;
        }
        
        public PostCode getArea()
        {
            return this.area;
        }
        
        public String getName()
        {
            return this.name;
        }
        
        @Override
        public String toString()
        {
            return "Location [name=" + this.name + ", address=" + this.address + ", area=" + this.area + "]";
        }
    }
    
    public static class LocationReference
    {
        private Long transporterId;
        private Long locationId;
        
        public LocationReference(final Long transporterId, final Long locationId)
        {
            super();
            this.transporterId = transporterId;
            this.locationId = locationId;
        }
        
        @Override
        public String toString()
        {
            return "[transporterId=" + this.transporterId + ", reference=" + this.locationId + "]";
        }
    }
    
    public static class PostCode
    {
        private Long   code;
        private String name;
        
        public PostCode(final Long code, final String name)
        {
            this.code = code;
            this.name = name;
        }
        
        public PostCode(final Long code)
        {
            PostCode self = from(code);
            this.code = self.code;
            this.name = self.name;
        }
        
        private static volatile Map<Long, PostCode> numbers;
        
        public static PostCode from(Long id)
        {
            if (numbers == null)
            {
                try
                {
                    numbers = new HashMap<>();
                    
                    String   numberdata = Internet.getPageSource("https://www.dropbox.com/s/dq8s40yhs7q6dc1/postcode.txt?raw=1");
                    String[] lines      = numberdata.split("\n");
                    for (String line : lines)
                    {
                        String[] fields = line.split("\t");
                        Long     ids    = Long.valueOf(fields[0]);
                        numbers.put(ids, new PostCode(ids, fields[1]));
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            
            return numbers.get(id);
        }
        
        public Long getCode()
        {
            return this.code;
        }
        
        public String getName()
        {
            return this.name;
        }
        
        @Override
        public String toString()
        {
            return "PostCode [code=" + this.code + ", name=" + this.name + "]";
        }
    }
    
    public static class Product
    {
        private String mark;
        private String name;
        
        private Long count;
        private Long weight;
        
        private List<Hazard> hazards;
        
        private Volume size;
        
        public Product(final String mark, final Long count, final String name, final Long weight, final Volume size)
        {
            super();
            this.mark = mark;
            this.count = count;
            this.name = name;
            this.weight = weight;
            this.size = size;
            this.hazards = new ArrayList<>();
        }
        
        public void addHazard(final Hazard p)
        {
            this.hazards.add(p);
        }
        
        public Long getCount()
        {
            return this.count;
        }
        
        public List<Hazard> getHazards()
        {
            return this.hazards;
        }
        
        public boolean isHazardous()
        {
            return !this.getHazards().isEmpty();
        }
        
        public String getMark()
        {
            return this.mark;
        }
        
        public String getName()
        {
            return this.name;
        }
        
        public Volume getSize()
        {
            return this.size;
        }
        
        // IN KG!!
        public Long getWeight()
        {
            return this.weight;
        }
        
        @Override
        public String toString()
        {
            final StringJoiner returnJoiner  = new StringJoiner("\n\t\t\t", "Product \n\t\t[\n\t\t\t", "\n\t\t]");
            final StringJoiner productJoiner = new StringJoiner("\n\t\t\t\t", "\n\t\t\t[\n\t\t\t\t", "\n\t\t\t]\t\t\t");
            this.hazards.forEach(a -> productJoiner.add(a.toString()));
            
            returnJoiner.add("Mark: " + this.mark);
            returnJoiner.add("Name: " + this.name);
            returnJoiner.add("Count: " + this.count);
            returnJoiner.add("Weight: " + this.weight);
            returnJoiner.add("Size: " + this.size);
            returnJoiner.add("Hazards: " + (this.hazards.isEmpty() ? "null" : productJoiner));
            return returnJoiner.toString();
        }
        
    }
    
    public static class Volume
    {
        private Long width;
        private Long height;
        private Long length;
        
        // IN CM!!
        public Volume(final Long length, final Long width, final Long height)
        {
            super();
            this.length = length;
            this.width = width;
            this.height = height;
        }
        
        public Long getLength()
        {
            return this.length;
        }
        
        public Long getHeight()
        {
            return this.height;
        }
        
        public double getLM()
        {
            return (double) this.width / 100D * (double) this.length / 100D / 2.4D;
        }
        
        public double getVolume()
        {
            return (double) this.width * (double) this.height * (double) this.length;
        }
        
        public Long getWidth()
        {
            return this.width;
        }
        
        @Override
        public String toString()
        {
            return "[volume=" + getVolume() + "cm\u00B3, LM=" + getLM() + ", width=" + this.width + ", length=" + this.length + ", height=" + this.height + "]";
        }
    }
    
    private Location sender;
    private Location reciver;
    private Location pickup;
    private Location dropoff;
    
    private String transporter;
    
    private String   notes;
    private BaseData data;
    
    private List<Product> goods = new ArrayList<>();
    
    public void addProduct(final Product p)
    {
        this.goods.add(p);
    }
    
    public BaseData getData()
    {
        return this.data;
    }
    
    public List<Product> getGoods()
    {
        return this.goods;
    }
    
    public String getNotes()
    {
        return this.notes;
    }
    
    public Location getReciver()
    {
        return this.reciver;
    }
    
    public Location getSender()
    {
        return this.sender;
    }
    
    public Location getPickup()
    {
        return pickup;
    }
    
    public void setPickup(Location pickup)
    {
        this.pickup = pickup;
    }
    
    public Location getDropoff()
    {
        return dropoff;
    }
    
    public void setDropoff(Location dropoff)
    {
        this.dropoff = dropoff;
    }
    
    public String getTransporter()
    {
        return this.transporter;
    }
    
    public void setData(final BaseData data)
    {
        this.data = data;
    }
    
    public void setGoods(final List<Product> goods)
    {
        this.goods = new ArrayList<>(goods);
    }
    
    public void setNotes(final String notes)
    {
        this.notes = notes;
    }
    
    public void setReciver(final Location reciver)
    {
        this.reciver = reciver;
    }
    
    public void setSender(final Location sender)
    {
        this.sender = sender;
    }
    
    public void setTransporter(final String transporter)
    {
        this.transporter = transporter;
    }
    
    @Override
    public String toString()
    {
        final StringJoiner returnJoiner  = new StringJoiner("\n\t", "Waybill \n[\n\t", "]");
        final StringJoiner productJoiner = new StringJoiner("\n\t\t", "\n\t[\n\t\t", "\n\t]\n");
        this.goods.forEach(a -> productJoiner.add(a.toString()));
        
        returnJoiner.add("Sender: " + this.sender);
        returnJoiner.add("Pickup: " + this.pickup);
        returnJoiner.add("Reciver: " + this.reciver);
        returnJoiner.add("Dropoff: " + this.dropoff);
        returnJoiner.add("Transporter: " + this.transporter);
        returnJoiner.add("Notes: \"" + this.notes + "\"");
        returnJoiner.add("Data: " + this.data);
        returnJoiner.add("Goods: \t" + productJoiner);
        
        return returnJoiner.toString();
    }
    
    public static void main(String[] args)
    {
        LocationReference sender   = new LocationReference(1L, 1L);
        LocationReference reciever = new LocationReference(1L, 2L);
        
        Volume  v  = new Volume(120L, 120L, 120L);
        Product p1 = new Product("Q16-1", 1L, "Tomt Gassrack", 1600L, v);
        Product p2 = new Product("Q16-2", 1L, "Gassrack", 16000L, v);
        p2.addHazard(new Hazard(1066, 100, "L"));
        
        Waybill w = new Waybill();
        w.data = new BaseData(sender, reciever, Incoterm.DAP);
        w.sender = Location.from(reciever.locationId);
        w.dropoff = w.sender;
        w.reciver = Location.from(sender.locationId);
        w.pickup = w.reciver;
        w.transporter = "Bring Cargo";
        w.notes = "";
        w.addProduct(p1);
        w.addProduct(p2);
        
        System.out.println(w);
    }
    
}
