package invoice;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Invoice
{
    private DateTimeFormatter sdf = DateTimeFormatter.ofPattern("uuuu.MM.dd");
    
    private final List<InvoiceItem> items = new ArrayList<>();
    private String    customerId;
    private String    invoiceId;
    private LocalDate invoiceDate;
    private LocalDate paymentDue;
    private String    sellerRef;
    private String    buyerRef;
    private String    payToAccount;
    private Business  seller;
    private Customer  buyer;
    
    public DateTimeFormatter getSdf()
    {
        return sdf;
    }
    
    public void setSdf(DateTimeFormatter sdf)
    {
        this.sdf = sdf;
    }
    
    public List<InvoiceItem> getItems()
    {
        return items;
    }
    
    public String getCustomerId()
    {
        return customerId;
    }
    
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }
    
    public String getInvoiceId()
    {
        return invoiceId;
    }
    
    public void setInvoiceId(String invoiceId)
    {
        this.invoiceId = invoiceId;
    }
    
    public LocalDate getInvoiceDate()
    {
        return invoiceDate;
    }
    
    public void setInvoiceDate(LocalDate invoiceDate)
    {
        this.invoiceDate = invoiceDate;
    }
    
    public LocalDate getPaymentDue()
    {
        return paymentDue;
    }
    
    public void setPaymentDue(LocalDate paymentDue)
    {
        this.paymentDue = paymentDue;
    }
    
    public String getSellerRef()
    {
        return sellerRef;
    }
    
    public void setSellerRef(String sellerRef)
    {
        this.sellerRef = sellerRef;
    }
    
    public String getBuyerRef()
    {
        return buyerRef;
    }
    
    public void setBuyerRef(String buyerRef)
    {
        this.buyerRef = buyerRef;
    }
    
    public String getPayToAccount()
    {
        return payToAccount;
    }
    
    public void setPayToAccount(String payToAccount)
    {
        this.payToAccount = payToAccount;
    }
    
    public Business getSeller()
    {
        return seller;
    }
    
    public void setSeller(Business seller)
    {
        this.seller = seller;
    }
    
    public Customer getBuyer()
    {
        return buyer;
    }
    
    public void setBuyer(Customer buyer)
    {
        this.buyer = buyer;
    }
    
    public void addItem(final InvoiceItem item)
    {
        // Add count instead of another item, if its already in the list
        InvoiceItem found = null;
        for (InvoiceItem i : items)
        {
            if (i.getItemId().equals(item.getItemId()))
            {
                found = i;
            }
        }
        
        if (found != null)
        {
            found.setItemCount(found.getItemCount() + 1);
        } else
        {
            items.add(item);
        }
        
    }
    
    
    public void save(String filename)
    {
        
        if (items.size() > 14)
        {
            System.err.println("We are unable to render more than 14 items at a time. Please generate another invoice, or wait for it to be supported!");
            return;
        }
        
        try
        {
            PDDocument doc  = new PDDocument();
            PDPage     page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            
            float font8Skip  = -10;
            float font10Skip = -12;
            float font12Skip = -14;
            float scale      = 0.3f;
            
            PDImageXObject      logo   = PDImageXObject.createFromFile("logo.png", doc);
            PDPageContentStream stream = new PDPageContentStream(doc, page);
            
            // Draw logo
            float drawHeight = logo.getHeight() * scale;
            float drawWidth  = logo.getWidth() * scale;
            stream.drawImage(logo, 40, 800 - drawHeight, drawWidth, drawHeight);
            
            // Draw seller info
            stream.setFont(PDType1Font.HELVETICA, 10);
            stream.beginText();
            stream.newLineAtOffset(250, 790);
            stream.showText(getSeller().getName());
            stream.newLineAtOffset(0, font10Skip);
            stream.showText(getSeller().getPostAddress());
            stream.newLineAtOffset(0, font10Skip * 2);
            stream.showText(getSeller().getPostCode());
            stream.endText();
            
            // Draw extra seller info
            stream.setFont(PDType1Font.HELVETICA, 8);
            stream.beginText();
            stream.newLineAtOffset(425, 790);
            stream.showText("Org.nr:");
            stream.newLineAtOffset(50, 0);
            stream.showText(getSeller().getOrgNr() + (getSeller().isMva() ? " MVA" : ""));
            stream.newLineAtOffset(-50, font8Skip);
            stream.showText("Telefon:");
            stream.newLineAtOffset(50, 0);
            stream.showText(getSeller().getPhone());
            stream.newLineAtOffset(-50, font8Skip);
            stream.showText("Epost:");
            stream.newLineAtOffset(50, 0);
            stream.showText(getSeller().getEmail());
            stream.newLineAtOffset(-50, font8Skip);
            stream.showText("Web:");
            stream.newLineAtOffset(50, 0);
            stream.showText(getSeller().getWebsite());
            stream.newLineAtOffset(-50, font8Skip);
            stream.endText();
            
            // Draw buyer info
            stream.setFont(PDType1Font.HELVETICA, 10);
            stream.beginText();
            stream.newLineAtOffset(60, 700);
            stream.showText(getBuyer().getName());
            stream.newLineAtOffset(0, font10Skip);
            stream.showText(getBuyer().getPostAddress());
            stream.newLineAtOffset(0, font10Skip * 2);
            stream.showText(getBuyer().getPostCode());
            stream.endText();
            
            // Draw invoice text
            stream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            stream.beginText();
            stream.newLineAtOffset(400, 725);
            stream.showText("Faktura");
            stream.endText();
            
            // Draw payment info
            stream.setFont(PDType1Font.HELVETICA, 8);
            stream.beginText();
            
            stream.newLineAtOffset(400, 700);
            stream.showText("Kundenr.:");
            stream.newLineAtOffset(50, 0);
            stream.showText(getCustomerId());
            stream.newLineAtOffset(-50, font8Skip);
            stream.showText("Fakturanr.:");
            stream.newLineAtOffset(50, 0);
            stream.showText(getInvoiceId());
            stream.newLineAtOffset(-50, font8Skip);
            stream.showText("Fakturadato:");
            stream.newLineAtOffset(50, 0);
            stream.showText(sdf.format(getInvoiceDate()));
            stream.newLineAtOffset(-50, font8Skip);
            stream.showText("Forfallsdato");
            stream.newLineAtOffset(50, 0);
            stream.showText(sdf.format(getPaymentDue()));
            stream.newLineAtOffset(-50, font8Skip);
            stream.showText("Deres ref.:");
            stream.newLineAtOffset(50, 0);
            if (getBuyerRef() != null)
            {
                stream.showText(getBuyerRef());
            }
            stream.newLineAtOffset(-50, font8Skip);
            stream.showText("Våres ref.:");
            stream.newLineAtOffset(50, 0);
            if (getSellerRef() != null)
            {
                stream.showText(getSellerRef());
            }
            stream.endText();
            
            
            float gridLeftX  = 39.505f;
            float gridRightX = 555.5f;
            
            
            // Draw separation line
            stream.moveTo(gridLeftX, 620);
            stream.lineTo(gridRightX, 620);
            stream.stroke();
            
            // Draw sub-header line
            stream.moveTo(gridLeftX, 580);
            stream.lineTo(gridRightX, 580);
            stream.stroke();
            
            
            int gridPadding = 20;
            int gridTop     = 620;
            int gridBottom  = 180;
            
            // Draw grid lines
            
            float xMin;
            float xMax;
            float x;
            float y = 600 - getStringHeight(8) / 2;
            
            // ID
            xMin = 40;
            xMax = 90;
            x = getCenter(xMin, xMax, "Id", 8);
            
            stream.moveTo(xMin, gridTop);
            stream.lineTo(xMin, gridBottom);
            stream.stroke();
            
            stream.beginText();
            stream.newLineAtOffset(x, y);
            stream.showText("Id");
            stream.endText();
            
            
            xMin = xMax;
            xMax = 380;
            x = getCenter(xMin, xMax, "Beskrivelse", 8);
            
            // Beskrivelse
            stream.moveTo(xMin, gridTop);
            stream.lineTo(xMin, gridBottom);
            stream.stroke();
            
            stream.beginText();
            stream.newLineAtOffset(x, y);
            stream.showText("Beskrivelse");
            stream.endText();
            
            //Enhetspris
            
            xMin = xMax;
            xMax = 450;
            x = getCenter(xMin, xMax, "Enhetspris", 8);
            
            stream.moveTo(xMin, gridTop);
            stream.lineTo(xMin, gridBottom);
            stream.stroke();
            
            stream.beginText();
            stream.newLineAtOffset(x, y);
            stream.showText("Enhetspris");
            stream.endText();
            
            
            //Antall
            xMin = xMax;
            xMax = 490;
            x = getCenter(xMin, xMax, "Antall", 8);
            
            stream.moveTo(xMin, gridTop);
            stream.lineTo(xMin, gridBottom);
            stream.stroke();
            
            stream.beginText();
            stream.newLineAtOffset(x, y);
            stream.showText("Antall");
            stream.endText();
            
            
            // Total
            xMin = xMax;
            xMax = 555;
            x = getCenter(xMin, xMax, "Total", 8);
            
            stream.moveTo(xMin, gridTop);
            stream.lineTo(xMin, gridBottom);
            stream.stroke();
            
            stream.beginText();
            stream.newLineAtOffset(x, y);
            stream.showText("Total");
            stream.endText();
            
            stream.moveTo(xMax, gridTop);
            stream.lineTo(xMax, gridBottom);
            stream.stroke();
            
            
            int skipSize       = 25;
            int iterationCount = 1;
            
            // Draw purchases
            for (InvoiceItem item : getItems())
            {
                float yOffset = 580f - skipSize * (iterationCount++);
                
                x = getCenter(40, 90, item.getItemId(), 8);
                
                stream.beginText();
                stream.newLineAtOffset(x, yOffset);
                stream.showText(item.getItemId());
                stream.endText();
                
                x = 90f + gridPadding;
                
                stream.beginText();
                stream.newLineAtOffset(x, yOffset);
                stream.showText(item.getItemDescription());
                stream.endText();
                
                x = getCenter(380, 450, String.valueOf(item.getPricePerCount()), 8);
                
                stream.beginText();
                stream.newLineAtOffset(x, yOffset);
                stream.showText(String.valueOf(item.getPricePerCount()));
                stream.endText();
                
                x = getCenter(450, 490, String.valueOf(item.getItemCount()), 8);
                
                stream.beginText();
                stream.newLineAtOffset(x, yOffset);
                stream.showText(String.valueOf(item.getItemCount()));
                stream.endText();
                
                x = getCenter(490, 555, String.valueOf(item.getTotalPrice()), 8);
                
                stream.beginText();
                stream.newLineAtOffset(x, yOffset);
                stream.showText(String.valueOf(item.getTotalPrice()));
                
                
                stream.endText();
            }
            
            // Draw separation line
            
            stream.moveTo(gridLeftX, 220);
            stream.lineTo(gridRightX, 220);
            stream.stroke();
            
            // Draw sub line
            stream.moveTo(gridLeftX, 180);
            stream.lineTo(gridRightX, 180);
            stream.stroke();
            
            // Draw total price
            
            x = getCenter(40, 90, "Total", 8);
            stream.beginText();
            stream.newLineAtOffset(x, 200 - getStringHeight(8) / 2);
            stream.showText("Total");
            stream.endText();
            
            x = 90f + gridPadding;
            stream.beginText();
            stream.newLineAtOffset(x, 200 - getStringHeight(8) / 2);
            stream.showText("Totalt antall varer, og pris");
            stream.endText();
            
            x = getCenter(450, 490, String.valueOf(getTotalItemCount()), 8);
            stream.beginText();
            stream.newLineAtOffset(x, 200 - getStringHeight(8) / 2);
            stream.showText(String.valueOf(getTotalItemCount()));
            stream.endText();
            
            x = getCenter(490, 555, String.valueOf(getTotalPrice()), 8);
            stream.beginText();
            stream.newLineAtOffset(x, 200 - getStringHeight(8) / 2);
            stream.showText(String.valueOf(getTotalPrice()));
            stream.endText();
            
            
            stream.setFont(PDType1Font.HELVETICA, 12);
            y = 150;
            stream.beginText();
            stream.newLineAtOffset(40, y);
            stream.showText("Ved forsinket betaling kan det påløpe purregebyr etter statens satser. (70kr per 01.01.2017)");
            stream.endText();
            
            y += font12Skip;
            
            if (getSeller().isMva())
            {
                stream.beginText();
                stream.newLineAtOffset(40, y);
                stream.showText("Alle priser er inkludert MVA.");
                stream.endText();
            }
            
            y += font12Skip * 3;
            
            stream.beginText();
            stream.newLineAtOffset(40, y);
            stream.showText("Innbetales til konto: ");
            stream.endText();
            stream.beginText();
            stream.newLineAtOffset(200, y);
            stream.showText(getPayToAccount());
            stream.endText();
            
            y += font12Skip;
            
            stream.beginText();
            stream.newLineAtOffset(40, y);
            stream.showText("KID/Melding til mottaker: ");
            stream.endText();
            stream.beginText();
            stream.newLineAtOffset(200, y);
            stream.showText(getCustomerId() + "/" + getInvoiceId());
            stream.endText();
            
            y += font12Skip;
            
            stream.beginText();
            stream.newLineAtOffset(40, y);
            stream.showText("Kroner:");
            stream.endText();
            stream.beginText();
            stream.newLineAtOffset(200, y);
            stream.showText(getTotalPrice().toString());
            stream.endText();
            
            // Save to file
            stream.close();
            doc.save(filename + ".pdf");
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private float getCenter(float xMin, float xMax, String text, int fontSize) throws IOException
    {
        float w = getStringWidth(text, fontSize);
        return (xMin + xMax) / 2 - (w / 2);
        
    }
    
    private float getStringHeight(int fontsize)
    {
        return (float) (PDType1Font.HELVETICA.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontsize * 0.808);
    }
    
    
    private float getStringWidth(String text, int fontsize) throws IOException
    {
        return (PDType1Font.HELVETICA.getStringWidth(text) * fontsize) / 1000;
    }
    
    public int getTotalItemCount()
    {
        // Disgusting "objects in lambda needs to be final" hack...
        int[] total = {0};
        items.forEach(i -> total[0] = total[0] + i.getItemCount());
        return total[0];
    }
    
    
    public MonetaryAmount getTotalPrice()
    {
        // Disgusting "objects in lambda needs to be final" hack...
        MonetaryAmount[] total = {Money.of(0, "NOK")};
        items.forEach(i -> total[0] = total[0].add(i.getTotalPrice()));
        return total[0];
    }
    
}
