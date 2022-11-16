package se.teamengine.java.test.tasks;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDNamedDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

import java.io.File;
import java.util.ArrayList;

public class TaskThree {

    // TODO Your task is to print the table of content in "example_file.pdf". Your code should work for any pdf file!
    private boolean run() {
        try {
            File examplePDF = new File(System.getProperty("user.dir") + File.separator + "example_file.pdf");
            PDDocument doc = PDDocument.load(examplePDF);
            //add new page
            PDPage page = new PDPage(PDRectangle.A3);
            PDPageTree pages = doc.getDocumentCatalog().getPages();
            pages.insertBefore(page, pages.get(0));

            //get bookmarks for table of content
            PDDocumentOutline outline = doc.getDocumentCatalog().getDocumentOutline();
            PDOutlineItem current = outline.getFirstChild();
            String indentation = "";
            int pageDestination = 0;
            ArrayList<String> contentList = new ArrayList<>();
            while (current != null) {
                // get the page  number of the bookmark
                if (current.getDestination() instanceof PDPageDestination) {
                    PDPageDestination destination = (PDPageDestination) current.getDestination();
                    pageDestination = (destination.retrievePageNumber() + 1);
                } else if (current.getDestination() instanceof PDNamedDestination) {
                    PDPageDestination destination = doc.getDocumentCatalog().findNamedDestinationPage((PDNamedDestination) current.getDestination());
                    if (destination != null) {
                        pageDestination = (destination.retrievePageNumber() + 1);
                    }
                }

                if (current.getAction() instanceof PDActionGoTo) {
                    PDActionGoTo actionGoTo = (PDActionGoTo) current.getAction();
                    if (actionGoTo.getDestination() instanceof PDPageDestination) {
                        PDPageDestination destination = (PDPageDestination) actionGoTo.getDestination();
                        pageDestination = (destination.retrievePageNumber() + 1);
                    } else if (actionGoTo.getDestination() instanceof PDNamedDestination) {
                        PDPageDestination destination = doc.getDocumentCatalog().findNamedDestinationPage((PDNamedDestination) actionGoTo.getDestination());
                        if (destination != null) {
                            pageDestination = (destination.retrievePageNumber() + 1);
                        }
                    }
                }
                contentList.add(indentation + current.getTitle() + "->" + pageDestination);
                //System.out.println(indentation + current.getTitle() + "------------------------------->" + pageDestination);

                //check bookmark tree (if it has children add indentation and print else it is on same level 'siblings')

                if (current.hasChildren()) {
                    current = current.getFirstChild();
                    indentation = "  ";
                } else {
                    current = current.getNextSibling();
                }
            }
            // Build table of content and add to page 1
            PDPage editPage = doc.getDocumentCatalog().getPages().get(0);
            int width = (int) editPage.getTrimBox().getWidth();
            PDPageContentStream contentStream = new PDPageContentStream(doc, editPage);
            int initX = 50;
            int initY = width - 50;
            int cellHeight = 30;
            int cellWidth = 700;
            int numberOfColumns = 2;
            for (String content : contentList) {
                for (int i = 0; i < numberOfColumns; i++) {

                    if (i == 0) {
                        contentStream.addRect(initX, initY, 700, -cellHeight);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(initX + 10, initY - cellHeight + 10);
                        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                        contentStream.showText(StringUtils.substringBefore(content, "->"));
                        contentStream.endText();
                    } else {
                        contentStream.addRect(initX, initY, 50, -cellHeight);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(initX + 10, initY - cellHeight + 10);
                        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                        contentStream.showText(StringUtils.substringAfter(content, "->"));
                        contentStream.endText();
                    }
                    initX += cellWidth;


                }
                initX = 50;
                initY -= cellHeight;
            }

            contentStream.close();
            doc.save(examplePDF);
            doc.close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean verify() {
        try {
            return run();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
