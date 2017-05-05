package invoice;

import org.junit.*;

import java.time.*;
import java.time.temporal.*;

public class InvoiceTest
{
    
    @Test
    public void createInvoice()
    {
        Business seller = new Business();
        seller.setName("Larssens IT");
        seller.setPostAddress("Dalekletten 70");
        seller.setPostCode("5337 Rong");
        seller.setPhone("+47 12 34 56 78");
        seller.setEmail("service@stelar7.no");
        seller.setWebsite("stelar7.no");
        seller.setOrgNr(987654321L);
        seller.setMva(true);
        
        Customer buyer = new Customer();
        buyer.setName("Ola Nordman");
        buyer.setPostAddress("Norgesgaten 87");
        buyer.setPostCode("5250 Bergen");
        
        InvoiceItem item = seller.getInvoiceItem("IT Diagnostikk");
        
        Invoice invoice = new Invoice();
        
        invoice.setCustomerId(seller.getCustomerId(buyer));
        invoice.setInvoiceId(seller.getNextInvoiceId());
        
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setPaymentDue(LocalDate.now().plusDays(14).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)));
        
        //invoice.setSellerRef("Seller Ref");
        //invoice.setBuyerRef("Buyer Ref");
        invoice.setPayToAccount("1234.56.78901");
        
        
        invoice.setSeller(seller);
        invoice.setBuyer(buyer);
        for (int i = 0; i < 14; i++)
        {
            invoice.addItem(seller.getInvoiceTestItem());
        }
        
        
        invoice.save("test");
    }
    
    
}
