package invoice;

import lombok.*;
import org.javamoney.moneta.*;

@Data
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
}
