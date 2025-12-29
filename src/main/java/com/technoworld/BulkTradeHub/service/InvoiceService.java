package com.technoworld.BulkTradeHub.service;

import java.awt.Color;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.technoworld.BulkTradeHub.entity.OrderItems;
import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.entity.UserOrders;
import com.technoworld.BulkTradeHub.repository.OrderItemsRepository;
import com.technoworld.BulkTradeHub.repository.ProductPostRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class InvoiceService {
    
    @Autowired
    private OrderItemsRepository orderItemsRepository;
    
    @Autowired
    private ProductPostRepository productPostRepository;

    // Define Theme Colors
    private static final Color BRAND_COLOR = new Color(0, 51, 102); // Dark Blue
    private static final Color HEADER_BG = new Color(240, 240, 240); // Light Gray
    private static final Color ACCENT_COLOR = new Color(230, 230, 250); // Lavenderish
    
    public void generateInvoice(HttpServletResponse response, UserOrders order, User buyer) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // --- Header Section ---
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[] { 1, 1 });

        // Left: Company Name (Logo Placeholder)
        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        Font logoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BRAND_COLOR);
        logoCell.addElement(new Paragraph("BulkTradeHub", logoFont));
        logoCell.addElement(new Paragraph("Your Trusted Wholesale Partner", FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY)));
        headerTable.addCell(logoCell);

        // Right: Invoice Title
        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 30, Color.LIGHT_GRAY);
        Paragraph title = new Paragraph("INVOICE", titleFont);
        title.setAlignment(Element.ALIGN_RIGHT);
        titleCell.addElement(title);
        headerTable.addCell(titleCell);

        document.add(headerTable);
        document.add(new Paragraph("\n")); // Spacer
        
        // --- Divider Line ---
        PdfPTable divider = new PdfPTable(1);
        divider.setWidthPercentage(100);
        PdfPCell line = new PdfPCell();
        line.setBorder(Rectangle.BOTTOM);
        line.setBorderColor(BRAND_COLOR);
        line.setBorderWidth(2f);
        line.setFixedHeight(2f);
        divider.addCell(line);
        document.add(divider);
        document.add(new Paragraph("\n"));


        // --- Info Section (Bill To & Order Details) ---
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(10f);
        infoTable.setWidths(new float[] { 1.5f, 1f });

        // Bill To
        PdfPCell billToCell = new PdfPCell();
        billToCell.setBorder(Rectangle.NO_BORDER);
        billToCell.addElement(new Paragraph("BILLED TO:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.GRAY)));
        billToCell.addElement(new Paragraph(buyer.getName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        billToCell.addElement(new Paragraph(buyer.getEmail(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
        if(buyer.getProfile() != null) {
             String address = buyer.getProfile().getAddress() + "\n" + 
                              buyer.getProfile().getCity() + ", " + 
                              buyer.getProfile().getState() + " - " + 
                              buyer.getProfile().getPincode();
             billToCell.addElement(new Paragraph(address, FontFactory.getFont(FontFactory.HELVETICA, 11)));
             billToCell.addElement(new Paragraph("Phone: " + buyer.getProfile().getPhoneNumber(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
        }
        infoTable.addCell(billToCell);

        // Order Details
        PdfPCell detailsCell = new PdfPCell();
        detailsCell.setBorder(Rectangle.NO_BORDER);
        detailsCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        
        PdfPTable innerDetails = new PdfPTable(2);
        innerDetails.setWidthPercentage(100);
        innerDetails.setHorizontalAlignment(Element.ALIGN_RIGHT);
        innerDetails.setWidths(new float[]{1f, 1.5f});
        
        addDetailRow(innerDetails, "Order #:", order.getRazorpayOrderId());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        addDetailRow(innerDetails, "Date:", order.getCreatedAt().format(formatter));
        addDetailRow(innerDetails, "Status:", "PAID");
        
        detailsCell.addElement(innerDetails);
        infoTable.addCell(detailsCell);

        document.add(infoTable);
        document.add(new Paragraph("\n"));

        // --- Items Table ---
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] { 4.0f, 1.0f, 2.0f, 2.0f });
        table.setSpacingBefore(15);
        table.setHeaderRows(1);

        // Header Style
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        Color headerBg = BRAND_COLOR;

        addHeaderCell(table, "Product Description", headerFont, headerBg, Element.ALIGN_LEFT);
        addHeaderCell(table, "Qty", headerFont, headerBg, Element.ALIGN_CENTER);
        addHeaderCell(table, "Price", headerFont, headerBg, Element.ALIGN_RIGHT);
        addHeaderCell(table, "Total", headerFont, headerBg, Element.ALIGN_RIGHT);

        // Data
        List<OrderItems> items = orderItemsRepository.findOrderItemByOderId(order.getId());
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
        boolean alternate = false;
        
        for (OrderItems item : items) {
            Optional<ProductPost> productPostOpt = productPostRepository.findById(item.getProductPostId());
            String productName = productPostOpt.isPresent() ? productPostOpt.get().getProductName() : "Unknown Product";
            
            Color rowBg = alternate ? ACCENT_COLOR : Color.WHITE;
            
            addDataCell(table, productName, dataFont, rowBg, Element.ALIGN_LEFT);
            addDataCell(table, String.valueOf(item.getLotsQuntity()), dataFont, rowBg, Element.ALIGN_CENTER);
            addDataCell(table, String.format("%.2f", item.getLotPrice()), dataFont, rowBg, Element.ALIGN_RIGHT);
            addDataCell(table, String.format("%.2f", item.getSubTotal()), dataFont, rowBg, Element.ALIGN_RIGHT);
            
            alternate = !alternate;
        }

        document.add(table);

        // --- Totals Section ---
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(40);
        totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalTable.setSpacingBefore(10);
        totalTable.setWidths(new float[] { 1f, 1f });

        // Total
        PdfPCell labelCell = new PdfPCell(new Phrase("Total Amount:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        labelCell.setPaddingTop(5);
        totalTable.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(String.format("Rs. %.2f", order.getFinalAmount()), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BRAND_COLOR)));
        valueCell.setBorder(Rectangle.BOTTOM); // Underline for emphasis
        valueCell.setBorderColor(BRAND_COLOR);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setPaddingTop(5);
        totalTable.addCell(valueCell);

        document.add(totalTable);

        // --- Footer ---
        document.add(new Paragraph("\n\n\n"));
        Paragraph footer = new Paragraph("Thank you for your business!", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12, Color.GRAY));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        Paragraph terms = new Paragraph("Terms & Conditions apply. For queries, contact support@bulktradehub.com", FontFactory.getFont(FontFactory.HELVETICA, 9, Color.LIGHT_GRAY));
        terms.setAlignment(Element.ALIGN_CENTER);
        terms.setSpacingBefore(20);
        document.add(terms);

        document.close();
    }
    
    // --- Helpers ---
    
    private void addDetailRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.GRAY)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(labelCell);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA, 10)));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(valueCell);
    }

    private void addHeaderCell(PdfPTable table, String text, Font font, Color bg, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(8);
        cell.setBorderColor(Color.WHITE);
        table.addCell(cell);
    }

    private void addDataCell(PdfPTable table, String text, Font font, Color bg, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        cell.setBorderColor(Color.LIGHT_GRAY);
        table.addCell(cell);
    }
    
    // Alias to fix potential ambiguity with iText Color vs AWT Color if needed
    // In OpenPDF (LibrePDF), com.lowagie.text.pdf.PdfPCell.setBackgroundColor takes java.awt.Color
    private static class BaseColor extends Color {
        public BaseColor(int r, int g, int b) { super(r, g, b); }
    }
}
