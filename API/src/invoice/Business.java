package invoice;

import org.javamoney.moneta.*;

public class Business
{
    private static int id = 0;
    private String  name;
    private String  postAddress;
    private String  postCode;
    private Long    orgNr;
    private String  phone;
    private String  email;
    private String  website;
    private boolean mva;
    
    public InvoiceItem getInvoiceTestItem()
    {
        InvoiceItem item = new InvoiceItem();
        item.setItemCount(1);
        item.setItemDescription("This is a test item");
        item.setItemId(String.valueOf(id++));
        item.setPricePerCount(Money.of(1234, "NOK"));
        
        return item;
    }
    
    // TODO hook this up to a database
    public InvoiceItem getInvoiceItem(final String itemName)
    {
        return getInvoiceTestItem();
    }
    
    // TODO hook this up to a database
    public String getCustomerId(final Customer buyer)
    {
        return "20001";
    }
    
    // TODO hook this up to a database
    public String getNextInvoiceId()
    {
        return "2016_11_27_01";
    }
    
    
    public static int getId()
    {
        return id;
    }
    
    public static void setId(int id)
    {
        Business.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getPostAddress()
    {
        return postAddress;
    }
    
    public void setPostAddress(String postAddress)
    {
        this.postAddress = postAddress;
    }
    
    public String getPostCode()
    {
        return postCode;
    }
    
    public void setPostCode(String postCode)
    {
        this.postCode = postCode;
    }
    
    public Long getOrgNr()
    {
        return orgNr;
    }
    
    public void setOrgNr(Long orgNr)
    {
        this.orgNr = orgNr;
    }
    
    public String getPhone()
    {
        return phone;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getWebsite()
    {
        return website;
    }
    
    public void setWebsite(String website)
    {
        this.website = website;
    }
    
    public boolean isMva()
    {
        return mva;
    }
    
    public void setMva(boolean mva)
    {
        this.mva = mva;
    }
}
