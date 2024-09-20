package com.wilsonferro.tools.pdf;

import java.util.List;

public interface PdfDocument {

    String name();
    int totalPages();
    List<PdfPage> pages();

}
