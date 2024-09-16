package com.wilsonferro.tools.pdf.tests;

import com.wilsonferro.tools.pdf.PdfDocument;
import com.wilsonferro.tools.pdf.PdfPage;
import com.wilsonferro.tools.pdf.impl.PdfFileDocumentImpl;
import com.wilsonferro.tools.pdf.impl.PdfPageImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class LoadPdfFileTest {

    private static final String pdfLocalFile = "pdf-files/EIU_BestCities.pdf";
    private static final String pdfInternetFile = "https://seoulsolution.kr/sites/default/files/gettoknowus/EIU_BestCities.pdf";
    private static PdfDocument doc;

    @BeforeAll
    public static void load_pdf_file_from_local() throws IOException {
        if (doc != null) {
            return;
        }

        ClassLoader classLoader = LoadPdfFileTest.class.getClassLoader();
        URL url = classLoader.getResource(pdfLocalFile);
        assert url != null;
        File file = Paths.get(url.getPath()).toFile();
        doc = new PdfFileDocumentImpl(file);
    }

    @Test
    public void load_pdf_file_from_internet_url() throws IOException {
        URL url = URI.create(pdfInternetFile).toURL();
        PdfDocument doc = new PdfFileDocumentImpl(url);

        assertEquals("/sites/default/files/gettoknowus/EIU_BestCities.pdf", doc.name());
        assertEquals(23, doc.totalPages());
        assertEquals(23, doc.pages().size());
    }

    @Test
    public void must_match_pdf_name_and_have_23_total_pages() {
        assertEquals("EIU_BestCities.pdf", doc.name());
        assertEquals(23, doc.totalPages());
        assertEquals(23, doc.pages().size());
    }

    @Test
    public void each_page_must_match_page_number() {
        List<PdfPage> pages = doc.pages();
        assertEquals(1, pages.get(0).number());
        assertEquals(2, pages.get(1).number());
        assertEquals(3, pages.get(2).number());
        assertEquals(4, pages.get(3).number());
        for (var i = 5; i <= 23; i++) {
            assertEquals(i, pages.get(i - 1).number());
        }
    }

    @Test
    public void must_extract_each_page_to_multiple_separate_pdf_file() {
        List<PdfPage> pages = doc.pages();
        for (PdfPage page : pages) {
            PdfPageImpl pdi = (PdfPageImpl) page;
            File tempFile = pdi.getTemporaryFile();
            assertTrue(tempFile.exists());
            log.atDebug().log("Page {} was extracted to the file: {}", page.number(), tempFile);
        }
    }

}
