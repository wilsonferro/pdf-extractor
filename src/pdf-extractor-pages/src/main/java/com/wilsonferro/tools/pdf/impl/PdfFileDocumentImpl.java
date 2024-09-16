package com.wilsonferro.tools.pdf.impl;

import com.wilsonferro.tools.pdf.PdfDocument;
import com.wilsonferro.tools.pdf.PdfPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PdfFileDocumentImpl implements PdfDocument {

    private final String name;
    private Integer totalPages;
    private List<PdfPage> pages;

    public PdfFileDocumentImpl(File file) throws IOException {
        log.atDebug().log("Creating from file: {}", file);
        try (PDDocument doc = Loader.loadPDF(file)) {
            this.readAllPages(doc);
        } finally {
            this.name = file.getName();
        }
        log.atDebug().log("Created from file: {}", file);
    }

    public PdfFileDocumentImpl(URL url) throws IOException {
        log.atDebug().log("Creating from URL: {}", url);
        try (InputStream input = url.openStream()) {
            try (PDDocument doc = Loader.loadPDF(input.readAllBytes())) {
                this.readAllPages(doc);
            } finally {
                this.name = url.getFile();
            }
        }
        log.atDebug().log("Created from URL: {}", url);
    }

    private void readAllPages(PDDocument doc) {
        int totalPages = doc.getNumberOfPages();
        List<PdfPage> pages = new ArrayList<>(totalPages);
        int pageCount = 1;
        for (PDPage page : doc.getPages()) {
            pages.add(toPdfPage(pageCount++, page.getMediaBox()));
        }

        this.totalPages = totalPages;
        this.pages = pages;
    }

    private static PdfPage toPdfPage(int page, PDRectangle rec) {
        return new PdfPage() {
            @Override
            public int number() {
                return page;
            }

            @Override
            public float width() {
                return rec.getWidth();
            }

            @Override
            public float height() {
                return rec.getHeight();
            }
        };
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int totalPages() {
        return totalPages;
    }

    @Override
    public List<PdfPage> pages() {
        return pages;
    }

}
