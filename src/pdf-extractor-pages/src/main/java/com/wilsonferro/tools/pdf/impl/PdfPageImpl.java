package com.wilsonferro.tools.pdf.impl;

import com.wilsonferro.tools.pdf.PdfPage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;

@AllArgsConstructor
@Getter
public class PdfPageImpl implements PdfPage {

    private int number;
    private PDPage page;
    private File temporaryFile;

    @Override
    public int number() {
        return number;
    }

    @Override
    public float width() {
        return page.getMediaBox().getWidth();
    }

    @Override
    public float height() {
        return page.getMediaBox().getHeight();
    }

}
