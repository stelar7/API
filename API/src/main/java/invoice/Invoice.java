package invoice;

import lombok.*;
import org.javamoney.moneta.*;

import javax.money.*;
import java.time.*;
import java.util.*;

@Data
public class Invoice
{
	private long customerId;
	private long invoiceId;
	
	private LocalDate invoiceDate;
	private LocalDate paymentDue;
	
	private String sellerRef;
	private String buyerRef;
	private String payToAccount;
	
	
	private Business seller;
	private Customer buyer;
	
	private final List<InvoiceItem> items = new ArrayList<>();
	
	
	public MonetaryAmount getTotalPrice()
	{
		MonetaryAmount total = Money.of(0, "NOK");
		items.forEach(i -> total.add(i.getPricePerCount().multiply(i.getItemCount())));
		return total;
	}
	
	public void addItem(final InvoiceItem item)
	{
		items.add(item);
	}
}
