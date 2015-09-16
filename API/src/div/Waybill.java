package div;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Waybill
{
    public class BaseData
    {
        LocationReference sender;

        LocationReference reciver;

        Incoterm type;

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
        DDU;
    }

    public class Location
    {
        String name;
        String address;

        PostCode area;

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

    public class LocationReference
    {
        Long transporterId;
        Long reference;

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

    public class PostCode
    {
        Long code;

        String name;

        String other;

        public PostCode(final Long code, final String name)
        {
            super();
            this.code = code;
            this.name = name;
            other = code + name;
        }

        public PostCode(final String other)
        {
            super();
            this.other = other;
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

    public class Product
    {
        String mark;
        String name;

        Long count;
        Long weight;

        Volume size;

        public Product(final String mark, final Long count, final String name, final Long weight, final Volume size)
        {
            super();
            this.mark = mark;
            this.count = count;
            this.name = name;
            this.weight = weight;
            this.size = size;
        }

        public Long getCount()
        {
            return this.count;
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
            return "Product [mark=" + this.mark + ", name=" + this.name + ", count=" + this.count + ", weight=" + this.weight + ", size=" + this.size + "]";
        }

    }

    public class Volume
    {
        Long width;
        Long height;
        Long depth;

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
            return "Volume [width=" + this.width + ", height=" + this.height + ", depth=" + this.depth + "]";
        }
    }

    Location sender;
    Location reciver;
    Location destination;

    String transporter;

    String   productType;
    String   notes;
    BaseData data;

    List<Product> goods;

    public void addProduct(final Product p)
    {
        if (this.goods == null)
        {
            this.goods = new ArrayList<Product>();
        }
        this.goods.add(p);
    }

    public BaseData getData()
    {
        return this.data;
    }

    public Location getDestination()
    {
        return this.destination;
    }

    public List<Product> getGoods()
    {
        return this.goods;
    }

    public String getNotes()
    {
        return this.notes;
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

    public void setDestination(final Location destination)
    {
        this.destination = destination;
    }

    public void setGoods(final List<Product> goods)
    {
        this.goods = goods;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
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
        final StringJoiner returnJoiner = new StringJoiner("\n\t", "Waybill [\n\t", "]");
        final StringJoiner productJoiner = new StringJoiner("\n\t\t", "[\n\t\t", "\n\t\t]\n\t");
        this.goods.forEach(a -> productJoiner.add(a.toString()));

        returnJoiner.add("Sender: " + this.sender.toString());
        returnJoiner.add("Reciver: " + this.reciver.toString());
        returnJoiner.add("Destination: " + this.destination.toString());
        returnJoiner.add("Transporter: " + this.transporter.toString());
        returnJoiner.add("ProductType: " + this.productType.toString());
        returnJoiner.add("Notes: \n\n" + this.notes.toString());
        returnJoiner.add("\n\tData: " + this.data.toString());
        returnJoiner.add("Goods: \t" + productJoiner.toString());

        return returnJoiner.toString();
    }

}
