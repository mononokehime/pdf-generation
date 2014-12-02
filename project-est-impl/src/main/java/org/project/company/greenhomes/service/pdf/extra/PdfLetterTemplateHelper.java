package org.project.company.greenhomes.service.pdf.extra;

import com.lowagie.text.*;
import org.project.company.greenhomes.service.pdf.templates.PdfTemplateAConstants;

/**
 * Class with generic methods that could be used across all letter templates.
 *
 *
 */
public class PdfLetterTemplateHelper {

	public static Paragraph getParagraphWithSpacingAndAlign (Font font, String text, float spacing, int alignment)
			throws DocumentException {
		Paragraph paragraph = new Paragraph(text, font);
		paragraph.setSpacingBefore(spacing);
		paragraph.setAlignment(alignment);
		paragraph.setIndentationLeft(3);
		return paragraph;

	}

	public static Paragraph getParagraphWithSpacing (Font font, String text, float spacing) throws DocumentException {
		Paragraph paragraph = new Paragraph(text, font);
		paragraph.setSpacingBefore(spacing);
		paragraph.setIndentationLeft(3);
		paragraph.setAlignment(Element.ALIGN_LEFT);
		return paragraph;
	}

	public static Document getDocument () {
		Rectangle rectangle = new Rectangle(PageSize.A4);
		Document document = new Document(rectangle, PdfTemplateAConstants.LEFT_MARGIN, 43, 43, 110);
		return document;
	}
}
