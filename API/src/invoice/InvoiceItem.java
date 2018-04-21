package invoice;


import javax.money.*;
public class InvoiceItem
{
    private int            itemCount;
    private String         itemId;
    private String         itemDescription;
    private MonetaryAmount pricePerCount;
    
    public int getItemCount()
    {
        return itemCount;
    }
    
    public void setItemCount(int itemCount)
    {
        this.itemCount = itemCount;
    }
    
    public String getItemId()
    {
        return itemId;
    }
    
    public void setItemId(String itemId)
    {
        this.itemId = itemId;
    }
    
    public String getItemDescription()
    {
        return itemDescription;
    }
    
    public void setItemDescription(String itemDescription)
    {
        this.itemDescription = itemDescription;
    }
    
    public MonetaryAmount getPricePerCount()
    {
        return pricePerCount;
    }
    
    public void setPricePerCount(MonetaryAmount pricePerCount)
    {
        this.pricePerCount = pricePerCount;
    }
    
    public MonetaryAmount getTotalPrice()
    {
        return pricePerCount.multiply(itemCount);
    }
    
}
