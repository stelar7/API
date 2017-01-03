package invoice;

import lombok.*;

import javax.money.*;

@Data
public class InvoiceItem
{
	private int            itemCount;
	private String         itemId;
	private String         itemDescription;
	private MonetaryAmount pricePerCount;
	
	
	public MonetaryAmount getTotalPrice()
	{
		return pricePerCount.multiply(itemCount);
	}
	
}
