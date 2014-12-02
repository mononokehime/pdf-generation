package org.project.company.greenhomes.service.pdf.templates;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import org.project.company.greenhomes.service.pdf.extra.PdfColorFactory;
import org.project.company.greenhomes.service.pdf.extra.PdfConstants;

import java.io.IOException;

public class PdfLineDrawer {

	private PdfContentByte cb;

	public PdfLineDrawer (PdfContentByte cbUnder) throws DocumentException, IOException {

		// create all the ratings
		this.cb = cbUnder;
	}

	public void drawGreyDottedBox (boolean addressWrapped) {
		//DottedLinesSeparator sep = new DottedLinesSeparator();
		cb.saveState();
		cb.setLineWidth(2);
		//	float[] dash2 = { 9, 6, 0, 6 };
		cb.setColorStroke(PdfColorFactory.getGreyLineColor());
		cb.setLineCap(PdfContentByte.LINE_CAP_ROUND);
		int gap = PdfConstants.GREY_LINE_GAP;
		cb.setLineDash(0, gap, gap / 2);

		cb.moveTo(PdfConstants.GREY_BOX_START_X, PdfConstants.GREY_BOX_START_Y);
		int bottomYCoord = PdfConstants.GREY_BOX_START_Y - PdfConstants.GREY_BOX_VERTICAL_LINE_LENGTH;
		// if address Wrapped, then we need to make the box a little bigger
		if (addressWrapped) {
			bottomYCoord = bottomYCoord - 10;
		}
		cb.lineTo(PdfConstants.GREY_BOX_START_X, bottomYCoord);
		cb.lineTo(PdfConstants.GREY_BOX_START_X + PdfConstants.GREY_BOX_HORIZONTAL_LINE_LENGTH, bottomYCoord);
		cb.lineTo(PdfConstants.GREY_BOX_START_X + PdfConstants.GREY_BOX_HORIZONTAL_LINE_LENGTH,
				PdfConstants.GREY_BOX_START_Y);
		cb.lineTo(PdfConstants.GREY_BOX_START_X, PdfConstants.GREY_BOX_START_Y);
		cb.stroke();
		cb.restoreState();
	}

	public void drawGreyDottedLine () {
		//DottedLinesSeparator sep = new DottedLinesSeparator();
		cb.saveState();
		cb.setLineWidth(2);
		cb.setColorStroke(PdfColorFactory.getGreyLineColor());
		cb.setLineCap(PdfContentByte.LINE_CAP_ROUND);
		int gap = PdfConstants.GREY_LINE_GAP;
		cb.setLineDash(0, gap, gap / 2);
		cb.moveTo(PdfConstants.GREY_LINE_X, PdfConstants.GREY_LINE_Y);
		cb.lineTo(PdfConstants.GREY_LINE_X + PdfConstants.GREY_LINE_LENGTH, PdfConstants.GREY_LINE_Y);
		cb.stroke();
		cb.restoreState();
	}

	public void drawLongGreyDottedLine (float y) {
		//DottedLinesSeparator sep = new DottedLinesSeparator();
		cb.saveState();
		cb.setLineWidth(2);
		cb.setColorStroke(PdfColorFactory.getGreyLineColor());
		cb.setLineCap(PdfContentByte.LINE_CAP_ROUND);
		int gap = PdfConstants.LONG_GREY_LINE_GAP;
		cb.setLineDash(0, gap, gap / 2);
		cb.moveTo(PdfConstants.LONG_GREY_LINE_X, y);
		cb.lineTo(PdfConstants.LONG_GREY_LINE_X + PdfConstants.LONG_GREY_LINE_LENGTH, y);
		cb.stroke();
		cb.restoreState();
	}

	public void drawTurqoiseLine () {
		//DottedLinesSeparator sep = new DottedLinesSeparator();
		cb.saveState();

		cb.setLineWidth(3);
		//	float[] dash2 = { 9, 6, 0, 6 };
		cb.setColorStroke(PdfColorFactory.getTurquoiseColor());
		cb.setLineCap(PdfContentByte.LINE_CAP_ROUND);
		int gap = PdfConstants.TURQUOISE_LINE_GAP;
		cb.setLineDash(0, gap, gap / 2);
		cb.moveTo(PdfConstants.TURQUOISE_LINE_X, PdfConstants.TURQUOISE_LINE_Y);
		cb.lineTo(PdfConstants.TURQUOISE_LINE_X + PdfConstants.TURQUOISE_LINE_LENGTH, PdfConstants.TURQUOISE_LINE_Y);
		cb.stroke();
		cb.restoreState();
	}

	//	/**
	//	 * @param args
	//	 */
	//	public static void main(String[] args) {
	//		// TODO Auto-generated method stub
	//		Rectangle rectangle = new Rectangle(PageSize.A4);
	//		Document document = new Document(rectangle, 43,43,43,43);
	//		//document.setPageCount(2);
	//		try {
	//			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("c:/mydoc.pdf"));
	//
	//			document.open();
	//			//BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	//			PdfContentByte cbUnder = writer.getDirectContent();
	//
	//			PdfLineDrawer me = new PdfLineDrawer(cbUnder);
	//			me.drawGreyDottedLine();
	//			me.drawGreyDottedBox(false);
	//			me.drawTurqoiseLine();
	//			System.out.println("document:"+document.getPageSize().getWidth());
	//			document.close();
	//			System.out.println("done");
	//		} catch (FileNotFoundException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (DocumentException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//
	//	}

}
