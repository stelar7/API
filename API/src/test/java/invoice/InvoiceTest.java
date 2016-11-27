package invoice;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.image.*;
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
		seller.setPhone("+47 95 44 81 77");
		seller.setEmail("service@stelar7.no");
		seller.setWebsite("stelar7.no");
		seller.setOrgNr("");
		
		Customer buyer = new Customer();
		buyer.setName("Åse Johansen");
		buyer.setPostAddress("Vintraleitet 63");
		buyer.setPostCode("5363 Ågotnes");
		
		InvoiceItem item = seller.getInvoiceItem("IT Diagnostikk");
		
		Invoice invoice = new Invoice();
		
		invoice.setCustomerId(seller.getCustomerId(buyer));
		invoice.setInvoiceId(seller.getNextInvoiceId());
		
		invoice.setInvoiceDate(LocalDate.now());
		invoice.setPaymentDue(LocalDate.now().plusDays(14).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)));
		
		invoice.setSellerRef("");
		invoice.setBuyerRef("");
		invoice.setPayToAccount("3625.15.09866");
		
		
		invoice.setSeller(seller);
		invoice.setBuyer(buyer);
		invoice.addItem(item);
		
		drawInvoice(invoice);
	}
	
	private void drawInvoice(final Invoice invoice)
	{
		try
		{
			PDDocument doc  = new PDDocument();
			PDPage     page = new PDPage(PDRectangle.A4);
			doc.addPage(page);
			
			System.out.println("Height: " + page.getMediaBox().getHeight());
			System.out.println("Widht: " + page.getMediaBox().getWidth());
			System.out.println("Yellow box 300 -> 0");
			
			float font8Skip  = -10;
			float font10Skip = -12;
			float font12Skip = -14;
			float scale      = 0.3f;
			
			PDImageXObject      logo   = PDImageXObject.createFromFile("logo.png", doc);
			PDPageContentStream stream = new PDPageContentStream(doc, page);
			
			// Draw logo
			float drawHeight = logo.getHeight() * scale;
			float drawWidth = logo.getWidth() * scale;
			stream.drawImage(logo, 40, 800-drawHeight, drawWidth, drawHeight);
			
			// Draw seller info
			stream.setFont(PDType1Font.HELVETICA, 10);
			stream.beginText();
			stream.newLineAtOffset(250, 790);
			stream.showText(invoice.getSeller().getName());
			stream.newLineAtOffset(0, font10Skip);
			stream.showText(invoice.getSeller().getPostAddress());
			stream.newLineAtOffset(0, font10Skip * 2);
			stream.showText(invoice.getSeller().getPostCode());
			stream.endText();
			
			// Draw extra seller info
			stream.setFont(PDType1Font.HELVETICA, 8);
			stream.beginText();
			stream.newLineAtOffset(425, 790);
			stream.showText("Org.nr:");
			stream.newLineAtOffset(50, 0);
			stream.showText(invoice.getSeller().getOrgNr());
			stream.newLineAtOffset(-50, font8Skip);
			stream.showText("Telefon:");
			stream.newLineAtOffset(50, 0);
			stream.showText(invoice.getSeller().getPhone());
			stream.newLineAtOffset(-50, -10);
			stream.showText("Epost:");
			stream.newLineAtOffset(50, 0);
			stream.showText(invoice.getSeller().getEmail());
			stream.newLineAtOffset(-50, font8Skip);
			stream.showText("Web:");
			stream.newLineAtOffset(50, 0);
			stream.showText(invoice.getSeller().getWebsite());
			stream.newLineAtOffset(-50, font8Skip);
			stream.endText();
			
			// Draw buyer info
			stream.setFont(PDType1Font.HELVETICA, 10);
			stream.beginText();
			stream.newLineAtOffset(60, 700);
			stream.showText(invoice.getBuyer().getName());
			stream.newLineAtOffset(0, font10Skip);
			stream.showText(invoice.getBuyer().getPostAddress());
			stream.newLineAtOffset(0, font10Skip * 2);
			stream.showText(invoice.getBuyer().getPostCode());
			stream.endText();
			
			// Draw invoice text
			stream.setFont(PDType1Font.HELVETICA_BOLD, 14);
			stream.beginText();
			stream.newLineAtOffset(425, 700);
			stream.showText("Faktura");
			stream.endText();
			
			// Draw separation line
			stream.moveTo(40, 625);
			stream.lineTo(555, 625);
			stream.stroke();
			
			// Draw payment info
			stream.setFont(PDType1Font.HELVETICA, 8);
			stream.beginText();
			
			// TODO kundenr, fakturanr, betalingsbetingelse, dato, selger ref, kjøper ref, konto, forfall
			stream.endText();
			
			// Draw separation line
			stream.moveTo(40, 600 + font10Skip * 1.5f);
			stream.lineTo(555, 600 + font10Skip * 1.5f);
			stream.stroke();
			
			// Draw purchases header
			// Draw separation line
			// Draw purchases
			// Draw separation line
			// Draw total price
			
			// Draw yellow box
			
			
			// Save to file
			stream.close();
			doc.save("test.pdf");
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
