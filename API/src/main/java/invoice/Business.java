package invoice;

import lombok.*;
import org.javamoney.moneta.*;

@Data
public class Business
{
	private String name;
	private String postAddress;
	private String postCode;
	private String orgNr;
	private String phone;
	private String email;
	private String website;
	
	// TODO hook this up to a database
	public InvoiceItem getInvoiceItem(final String itemName)
	{
		InvoiceItem item = new InvoiceItem();
		item.setItemCount(1);
		item.setItemDescription("Generell feilsøking på IT utstyr");
		item.setItemId(10001);
		item.setPricePerCount(Money.of(200, "NOK"));
		
		return item;
	}
	
	// TODO hook this up to a database
	public long getCustomerId(final Customer buyer)
	{
		return 20001;
	}
	
	// TODO hook this up to a database
	public long getNextInvoiceId()
	{
		return 2016_11_27_01L;
	}
}
