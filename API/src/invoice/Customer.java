package invoice;


public class Customer
{
    private String name;
    private String postAddress;
    private String postCode;
    
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
}
