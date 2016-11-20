package div;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.*;

public class Waybill
{
	public static class BaseData
	{
		private final LocationReference sender;
		private final LocationReference reciver;
		private final Incoterm          type;
		
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
			return "BaseData [sender=" + this.sender + ", reciver=" + this.reciver + ", type=" + this.type + "]";
		}
	}
	
	public static class Hazard
	{
		private final UnNumber unNumber;
		private final double   amount;
		private final String   amountSpec; // kg/ltr
		
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
		
		public String getDescription()
		{
			return this.unNumber.desc;
		}
		
		public String getGroup()
		{
			return this.unNumber.clazz;
		}
		
		public int getId()
		{
			return this.unNumber.number;
		}
		
		@Override
		public String toString()
		{
			return "Hazard [" + this.unNumber + ", amount=" + this.amount + ", amountSpec=" + this.amountSpec + "]";
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
		private final String name;
		private final String address;
		
		private final PostCode area;
		
		public Location(final String name, final String address, final PostCode area)
		{
			super();
			this.name = name;
			this.address = address;
			this.area = area;
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
		private final Long transporterId;
		private final Long reference;
		
		public LocationReference(final Long transporterId, final Long reference)
		{
			super();
			this.transporterId = transporterId;
			this.reference = reference;
		}
		
		@Override
		public String toString()
		{
			return "LocationReference [transporterId=" + this.transporterId + ", reference=" + this.reference + "]";
		}
	}
	
	public static class PostCode
	{
		private Long code;
		
		private final String name;
		
		public PostCode(final Long code, final String name)
		{
			super();
			this.code = code;
			this.name = name;
		}
		
		public PostCode(final String other)
		{
			super();
			this.name = other;
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
		private final String mark;
		private final String name;
		
		private final Long count;
		private final Long weight;
		
		private final List<Hazard> hazards;
		
		private final Volume size;
		
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
		
		public boolean isHazardous()
		{
			return !this.getHazards().isEmpty();
		}
		
		@Override
		public String toString()
		{
			final StringJoiner returnJoiner  = new StringJoiner("\n\t\t\t", "Product [\n\t\t\t", "\n\t\t\t]");
			final StringJoiner productJoiner = new StringJoiner("\n\t\t\t\t", "[\n\t\t\t\t", "\n\t\t\t\t]\t\t\t");
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
	
	public static class UnNumber
	{
		static volatile Map<Integer, UnNumber> numbers;
		
		public static UnNumber from(final int id)
		{
			if (UnNumber.numbers == null)
			{
				try
				{
					UnNumber.numbers = new HashMap<>();
					
					final String numberdata = Internet.getPageSource("https://www.dropbox.com/s/sjl8otk8mz77upg/unnumbers.txt?raw=1");
					
					final List<UnNumber> numberlist = new Gson().fromJson(numberdata, new TypeToken<List<UnNumber>>()
					{}.getType());
					
					numberlist.forEach(e -> UnNumber.numbers.put(e.number, e));
				} catch (final IOException e)
				{
					e.printStackTrace();
				}
			}
			
			return UnNumber.numbers.get(id);
		}
		
		private final int number;
		
		private final String clazz;
		
		private final String desc;
		
		public UnNumber(final int id, final String clazz, final String desc)
		{
			super();
			this.number = id;
			this.clazz = clazz;
			this.desc = desc;
		}
		
		@Override
		public String toString()
		{
			return "UnNumber [id=" + this.number + ", class=" + this.clazz + ", description=" + this.desc + "]";
		}
	}
	
	public static class Volume
	{
		private final Long width;
		private final Long height;
		private final Long depth;
		
		// IN CM!!
		public Volume(final Long width, final Long depth, final Long height)
		{
			super();
			this.width = width;
			this.height = height;
			this.depth = depth;
		}
		
		public Long getDepth()
		{
			return this.depth;
		}
		
		public Long getHeight()
		{
			return this.height;
		}
		
		public double getLM()
		{
			return ((((double) this.width / 100D) * (((double) this.depth) / 100D))) / 2.4D;
		}
		
		public double getVolume()
		{
			return (double) this.width * (double) this.height * (double) this.depth;
		}
		
		public Long getWidth()
		{
			return this.width;
		}
		
		@Override
		public String toString()
		{
			return "Volume [width=" + this.width + ", depth=" + this.depth + ", height=" + this.height + "]";
		}
	}
	
	private Location sender;
	private Location reciver;
	private Location pickup;
	private Location dropoff;
	
	private String transporter;
	
	private String   productType;
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
	
	public Location getDropoff()
	{
		return this.dropoff;
	}
	
	public List<Product> getGoods()
	{
		return this.goods;
	}
	
	public String getNotes()
	{
		return this.notes;
	}
	
	public Location getPickup()
	{
		return this.pickup;
	}
	
	public String getProductType()
	{
		return this.productType;
	}
	
	public Location getReciver()
	{
		return this.reciver;
	}
	
	public Location getSender()
	{
		return this.sender;
	}
	
	public String getTransporter()
	{
		return this.transporter;
	}
	
	public void setData(final BaseData data)
	{
		this.data = data;
	}
	
	public void setDropoff(final Location dropoff)
	{
		this.dropoff = dropoff;
	}
	
	public void setGoods(final List<Product> goods)
	{
		this.goods = goods;
	}
	
	public void setNotes(final String notes)
	{
		this.notes = notes;
	}
	
	public void setPickup(final Location pickup)
	{
		this.pickup = pickup;
	}
	
	public void setProductType(final String productType)
	{
		this.productType = productType;
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
		final StringJoiner returnJoiner  = new StringJoiner("\n\t", "Waybill [\n\t", "]");
		final StringJoiner productJoiner = new StringJoiner("\n\t\t", "[\n\t\t", "\n\t\t]\n\t");
		this.goods.forEach(a -> productJoiner.add(a.toString()));
		
		returnJoiner.add("Sender: " + this.sender);
		returnJoiner.add("Pickup: " + this.pickup);
		returnJoiner.add("Reciver: " + this.reciver);
		returnJoiner.add("Dropoff: " + this.dropoff);
		returnJoiner.add("Transporter: " + this.transporter);
		returnJoiner.add("ProductType: " + this.productType);
		returnJoiner.add("Notes: \n\n" + this.notes);
		returnJoiner.add("\n\tData: " + this.data);
		returnJoiner.add("Goods: \t" + productJoiner);
		
		return returnJoiner.toString();
	}
	
}
