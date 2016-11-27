package invoice;

import lombok.*;

import javax.money.*;

@Data
public class InvoiceItem
{
	private long           itemId;
	private int            itemCount;
	private String         itemDescription;
	private MonetaryAmount pricePerCount;
}
