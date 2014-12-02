package org.project.company.greenhomes.service.pdf.templates;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import org.project.company.greenhomes.common.util.EnergyRatings;
import org.project.company.greenhomes.common.util.EnergyRatings.EnergyRating;
import org.project.company.greenhomes.exception.ApplicationException;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.service.pdf.extra.EnergyEfficiencyGraphProperties;
import org.project.company.greenhomes.service.pdf.extra.PdfColorFactory;
import org.project.company.greenhomes.service.pdf.extra.PdfFontFactory;

import java.awt.*;
import java.io.IOException;

/**
 * Class that writes the A-G rating graph at the top of the pdf for template A
 *
 *
 */
public class PdfBuildRatingGraph {
	private final static float ADDRESS_LENGTH_MAXIMUM = 80f;
	/**
	 * content writer for graphics
	 */
	private PdfContentByte cbUnder;
	/*
	 * content writer for text
	 */
	private PdfContentByte cbOver;
	/*
	 * The user energy efficiency rating
	 */
	private Integer energyRatingCurrent;
	/*
	 * potential energy rating
	 */
	private Integer energyRatingPotential;
	private String address;

	private boolean addressWrapped;
	private float yAddressWrapOffset = 8;

	/**
	 * Restricted constructor to make sure we have all required elements. No null checking
	 * but they will fail.
	 *
	 * @param cbUnder
	 * @param cbOver
	 * @param energyRatingCurrent
	 * @param energyRatingPotential
	 * @throws DocumentException
	 * @throws IOException
	 */
	public PdfBuildRatingGraph (PdfContentByte cbUnder, PdfContentByte cbOver, final Integer energyRatingCurrent,
			final Integer energyRatingPotential, final String address) throws DocumentException, IOException {

		// create all the ratings
		this.address = address;
		this.cbUnder = cbUnder;
		this.cbOver = cbOver;
		this.energyRatingCurrent = energyRatingCurrent;
		this.energyRatingPotential = energyRatingPotential;

	}

	/*
	 * Method to wrap all the various elements
	 */
	public void createEfficiencyGraph ()
			throws InvalidDataException, ApplicationException, DocumentException, IOException {
		// add the address
		addAddress();
		PdfLineDrawer drawer = new PdfLineDrawer(cbOver);
		drawer.drawGreyDottedLine();
		drawer.drawGreyDottedBox(addressWrapped);
		// add each of the colour rectangles
		addRectangle(getARectangleProperties());
		addRectangle(getBRectangleProperties());
		addRectangle(getCRectangleProperties());
		addRectangle(getDRectangleProperties());
		addRectangle(getERectangleProperties());
		addRectangle(getFRectangleProperties());
		addRectangle(getGRectangleProperties());
		// add the extra pointer bit
		addUserRatingValue();
		addPotentialRatingValue();
	}

	/**
	 * Checks the upper case value as the address will need different ADDRESS_LENGTH_MAXIMUM
	 * if it is upper case
	 *
	 * @throws ApplicationException
	 */
	private void addAddress () throws ApplicationException {
		cbOver.saveState();
		cbOver.beginText();
		cbOver.setColorFill(Color.BLACK);
		cbOver.setFontAndSize(PdfFontFactory.getFrutiger65Bold(), 9);
		cbOver.setTextMatrix(EnergyEfficiencyGraphProperties.x, EnergyEfficiencyGraphProperties.y + 20);

		// need to check the address to see if it needs wrapping

		if (PdfFontFactory.getFrutiger65BoldWidthInPoints(address, 9) > ADDRESS_LENGTH_MAXIMUM) {
			String text = "energy efficiency rating";
			cbOver.showText(this.address + "\u0027s");
			cbOver.setTextMatrix(EnergyEfficiencyGraphProperties.x,
					EnergyEfficiencyGraphProperties.y + yAddressWrapOffset);
			cbOver.showText(text);
			addressWrapped = true;
		} else {
			String text = "\u0027s energy efficiency rating";
			cbOver.showText(this.address + text);
		}

		cbOver.endText();
		cbOver.restoreState();
	}

	/**
	 * Builds the rectangle with the supplied properties object
	 *
	 * @param props includes the x, y width etc etc
	 * @throws ApplicationException
	 */
	private void addRectangle (EnergyEfficiencyGraphProperties props) throws ApplicationException {
		cbOver.saveState();
		cbOver.beginText();
		cbOver.setColorFill(Color.BLACK);
		cbOver.setFontAndSize(PdfFontFactory.getFrutiger65Bold(), 5);
		float yCoord = props.getY() - props.getYOffset() + 2;
		if (addressWrapped) {
			yCoord = yCoord - yAddressWrapOffset;
		}
		//addressSqueezed
		cbOver.setTextMatrix(props.getX() + 3, yCoord);

		cbOver.showText(props.getRatingRange());

		cbOver.setFontAndSize(PdfFontFactory.getFrutiger65Bold(), 9);
		float yWhiteLetterCoord = props.getY() - props.getYOffset() + 1;
		if (addressWrapped) {
			yWhiteLetterCoord = yWhiteLetterCoord - yAddressWrapOffset;
		}
		cbOver.setTextMatrix(props.getX() + props.getWidth() - 9, yWhiteLetterCoord);

		cbOver.setColorFill(Color.WHITE);
		cbOver.showText(props.getRating());
		cbOver.endText();
		cbUnder.setColorFill(props.getColor());
		cbUnder.setColorStroke(props.getColor());
		float yRectangleCoor = props.getY() - props.getYOffset();
		if (addressWrapped) {
			yRectangleCoor = yRectangleCoor - yAddressWrapOffset;
		}
		cbUnder.rectangle(props.getX(), yRectangleCoor, props.getWidth(), props.getHeight());
		cbUnder.fill();
		cbOver.restoreState();
	}

	/**
	 * Writes the potential rating to the graph. Each A-G is hard coded as the start
	 * point/offsets, length  for each will be different. Once finished will call
	 * addRatingText to add the rating
	 *
	 * @throws ApplicationException
	 */
	private void addPotentialRatingValue () throws ApplicationException {
		float startPointX = 15;
		float endPointY = 0;
		Color color = null;
		EnergyRating rating = EnergyRatings.getRating(energyRatingPotential);
		boolean overFlow = false;
		if ("A".equalsIgnoreCase(rating.getRating())) {
			// adding a rating
			color = PdfColorFactory.getARatingColor();
			startPointX += EnergyEfficiencyGraphProperties.x + EnergyEfficiencyGraphProperties.startingWidth + (
					EnergyEfficiencyGraphProperties.growBy * 5) + EnergyEfficiencyGraphProperties.growBy + 25;
			// the text needs to go below the arrow rating for these otherwise the formatting goes weird
			endPointY = EnergyEfficiencyGraphProperties.y + (EnergyEfficiencyGraphProperties.height / 2);
			overFlow = true;

		}
		if ("B".equalsIgnoreCase(rating.getRating())) {
			// adding b rating
			color = PdfColorFactory.getBRatingColor();
			startPointX += EnergyEfficiencyGraphProperties.x + EnergyEfficiencyGraphProperties.startingWidth + (
					EnergyEfficiencyGraphProperties.growBy * 5) + EnergyEfficiencyGraphProperties.growBy + 25;
			//the text needs to go below the arrow rating for these otherwise the formatting goes weird
			endPointY = EnergyEfficiencyGraphProperties.y - EnergyEfficiencyGraphProperties.offSet + (
					EnergyEfficiencyGraphProperties.height / 2);
			overFlow = true;
		}
		if ("C".equalsIgnoreCase(rating.getRating())) {
			// adding c rating
			color = PdfColorFactory.getCRatingColor();
			startPointX += EnergyEfficiencyGraphProperties.x + EnergyEfficiencyGraphProperties.startingWidth + (
					EnergyEfficiencyGraphProperties.growBy * 5) + EnergyEfficiencyGraphProperties.growBy + 25;
			endPointY = EnergyEfficiencyGraphProperties.y - EnergyEfficiencyGraphProperties.offSet * 2 + (
					EnergyEfficiencyGraphProperties.height / 2);
		}
		if ("D".equalsIgnoreCase(rating.getRating())) {
			// adding d rating
			color = PdfColorFactory.getDRatingColor();
			startPointX += EnergyEfficiencyGraphProperties.x + EnergyEfficiencyGraphProperties.startingWidth + (
					EnergyEfficiencyGraphProperties.growBy * 5) + EnergyEfficiencyGraphProperties.growBy + 25;
			endPointY = EnergyEfficiencyGraphProperties.y - EnergyEfficiencyGraphProperties.offSet * 3 + (
					EnergyEfficiencyGraphProperties.height / 2);
		}
		if ("E".equalsIgnoreCase(rating.getRating())) {
			// adding e rating
			color = PdfColorFactory.getERatingColor();
			startPointX += EnergyEfficiencyGraphProperties.x + EnergyEfficiencyGraphProperties.startingWidth + (
					EnergyEfficiencyGraphProperties.growBy * 5) + EnergyEfficiencyGraphProperties.growBy + 25;
			endPointY = EnergyEfficiencyGraphProperties.y - EnergyEfficiencyGraphProperties.offSet * 4 + (
					EnergyEfficiencyGraphProperties.height / 2);
		}
		if ("F".equalsIgnoreCase(rating.getRating())) {
			// adding f rating
			color = PdfColorFactory.getFRatingColor();
			startPointX += EnergyEfficiencyGraphProperties.x + EnergyEfficiencyGraphProperties.startingWidth + (
					EnergyEfficiencyGraphProperties.growBy * 5) + EnergyEfficiencyGraphProperties.growBy + 25;
			endPointY = EnergyEfficiencyGraphProperties.y - EnergyEfficiencyGraphProperties.offSet * 5 + (
					EnergyEfficiencyGraphProperties.height / 2);
		}
		if ("G".equalsIgnoreCase(rating.getRating())) {
			// adding g rating
			color = PdfColorFactory.getGRatingColor();
			startPointX += EnergyEfficiencyGraphProperties.x + EnergyEfficiencyGraphProperties.startingWidth + (
					EnergyEfficiencyGraphProperties.growBy * 5) + EnergyEfficiencyGraphProperties.growBy + 25;
			endPointY = EnergyEfficiencyGraphProperties.y - EnergyEfficiencyGraphProperties.offSet * 6 + (
					EnergyEfficiencyGraphProperties.height / 2);
		}
		if (addressWrapped) {
			endPointY = endPointY - yAddressWrapOffset;
		}
		cbUnder.saveState();
		cbUnder.setColorStroke(color);
		cbUnder.setColorFill(color);
		cbUnder.moveTo(startPointX, endPointY);
		cbUnder.lineTo(startPointX + 8, endPointY - (EnergyEfficiencyGraphProperties.height / 2));
		cbUnder.lineTo(startPointX + 20, endPointY - (EnergyEfficiencyGraphProperties.height / 2));
		cbUnder.lineTo(startPointX + 20, endPointY + (EnergyEfficiencyGraphProperties.height / 2));
		cbUnder.lineTo(startPointX + 8, endPointY + (EnergyEfficiencyGraphProperties.height / 2));
		cbUnder.lineTo(startPointX, endPointY);
		cbUnder.fill();
		cbUnder.restoreState();
		addRatingText(startPointX, endPointY, energyRatingPotential, "potential", overFlow);
	}

	/**
	 * Adds the user rating. Only F and G are accepted
	 *
	 * @throws InvalidDataException
	 * @throws ApplicationException
	 */
	private void addUserRatingValue () throws InvalidDataException, ApplicationException {

		float startPointX = 0;
		float endPointY = 0;
		Color color = null;
		EnergyRating rating = EnergyRatings.getRating(energyRatingCurrent);

		if ("F".equalsIgnoreCase(rating.getRating())) {
			// adding f rating
			color = PdfColorFactory.getFRatingColor();
			startPointX = EnergyEfficiencyGraphProperties.x + EnergyEfficiencyGraphProperties.startingWidth + (
					EnergyEfficiencyGraphProperties.growBy * 5) + EnergyEfficiencyGraphProperties.growBy + 5;
			endPointY = EnergyEfficiencyGraphProperties.y - EnergyEfficiencyGraphProperties.offSet * 5 + (
					EnergyEfficiencyGraphProperties.height / 2);
		} else if ("G".equalsIgnoreCase(rating.getRating())) {
			// adding f rating
			color = PdfColorFactory.getGRatingColor();
			startPointX = EnergyEfficiencyGraphProperties.x + EnergyEfficiencyGraphProperties.startingWidth + (
					EnergyEfficiencyGraphProperties.growBy * 5) + EnergyEfficiencyGraphProperties.growBy + 5;
			endPointY = EnergyEfficiencyGraphProperties.y - EnergyEfficiencyGraphProperties.offSet * 6 + (
					EnergyEfficiencyGraphProperties.height / 2);
		} else {
			throw new InvalidDataException(
					"Rating was not F or G, it was:" + rating.getRating() + ", which is currently not allowed.");
		}
		if (addressWrapped) {
			endPointY = endPointY - yAddressWrapOffset;
		}
		// save state
		cbUnder.saveState();
		// set colours
		cbUnder.setColorStroke(color);
		cbUnder.setColorFill(color);
		// draw the rectangle!
		cbUnder.moveTo(startPointX, endPointY);
		cbUnder.lineTo(startPointX + 8, endPointY - (EnergyEfficiencyGraphProperties.height / 2));
		cbUnder.lineTo(startPointX + 20, endPointY - (EnergyEfficiencyGraphProperties.height / 2));
		cbUnder.lineTo(startPointX + 20, endPointY + (EnergyEfficiencyGraphProperties.height / 2));
		cbUnder.lineTo(startPointX + 8, endPointY + (EnergyEfficiencyGraphProperties.height / 2));
		cbUnder.lineTo(startPointX, endPointY);
		cbUnder.fill();
		cbUnder.restoreState();
		addRatingText(startPointX, endPointY, energyRatingCurrent, "current", false);
	}

	/*
	 * Adds the rating text to a rectangle element
	 */
	private void addRatingText (float startPointX, float endPointY, Integer userScore, String ratingType,
			boolean overflow) throws ApplicationException {
		// now need to add the text in
		cbUnder.saveState();
		cbUnder.beginText();
		cbUnder.setColorFill(Color.BLACK);
		cbUnder.setFontAndSize(PdfFontFactory.getFrutiger45Light(), 6);
		// now the rating score
		cbUnder.setTextMatrix(startPointX + 10, endPointY - 2);
		cbUnder.showText(userScore + "");
		// now move the cursor 
		if (overflow) {
			endPointY = endPointY - (EnergyEfficiencyGraphProperties.offSet);
		} else {
			endPointY = endPointY + (EnergyEfficiencyGraphProperties.offSet * 2);
		}

		cbUnder.setTextMatrix(startPointX, endPointY);

		cbUnder.showText("Your");
		cbUnder.moveText(0, -6);
		cbUnder.newlineShowText(ratingType);
		cbUnder.moveText(0, -6);
		cbUnder.newlineShowText("rating");

		cbUnder.endText();
		cbUnder.saveState();
	}

	private EnergyEfficiencyGraphProperties getARectangleProperties () {
		Color color = PdfColorFactory.getARatingColor();
		String text = "(92 to 100)";
		return new EnergyEfficiencyGraphProperties(color, 0, EnergyEfficiencyGraphProperties.startingWidth, text, "A");
	}

	private EnergyEfficiencyGraphProperties getBRectangleProperties () {
		Color color = PdfColorFactory.getBRatingColor();
		String text = "(81 to 91)";
		return new EnergyEfficiencyGraphProperties(color, EnergyEfficiencyGraphProperties.offSet,
				EnergyEfficiencyGraphProperties.startingWidth + EnergyEfficiencyGraphProperties.growBy, text, "B");
	}

	private EnergyEfficiencyGraphProperties getCRectangleProperties () {
		Color color = PdfColorFactory.getCRatingColor();
		String text = "(69 to 80)";
		return new EnergyEfficiencyGraphProperties(color, EnergyEfficiencyGraphProperties.offSet * 2,
				EnergyEfficiencyGraphProperties.startingWidth + (EnergyEfficiencyGraphProperties.growBy * 2), text,
				"C");
	}

	private EnergyEfficiencyGraphProperties getDRectangleProperties () {
		Color color = PdfColorFactory.getDRatingColor();
		String text = "(55 to 68)";
		return new EnergyEfficiencyGraphProperties(color, EnergyEfficiencyGraphProperties.offSet * 3,
				EnergyEfficiencyGraphProperties.startingWidth + (EnergyEfficiencyGraphProperties.growBy * 3), text,
				"D");
	}

	private EnergyEfficiencyGraphProperties getERectangleProperties () {
		Color color = PdfColorFactory.getERatingColor();
		String text = "(39 to 54)";
		return new EnergyEfficiencyGraphProperties(color, EnergyEfficiencyGraphProperties.offSet * 4,
				EnergyEfficiencyGraphProperties.startingWidth + (EnergyEfficiencyGraphProperties.growBy * 4), text,
				"E");
	}

	private EnergyEfficiencyGraphProperties getFRectangleProperties () {
		Color color = PdfColorFactory.getFRatingColor();
		String text = "(21 to 38)";
		return new EnergyEfficiencyGraphProperties(color, EnergyEfficiencyGraphProperties.offSet * 5,
				EnergyEfficiencyGraphProperties.startingWidth + (EnergyEfficiencyGraphProperties.growBy * 5), text,
				"F");
	}

	private EnergyEfficiencyGraphProperties getGRectangleProperties () {
		Color color = PdfColorFactory.getGRatingColor();
		String text = "(1 to 20)";
		return new EnergyEfficiencyGraphProperties(color, EnergyEfficiencyGraphProperties.offSet * 6,
				EnergyEfficiencyGraphProperties.startingWidth + (EnergyEfficiencyGraphProperties.growBy * 6), text,
				"G");
	}

	//
	//	/**
	//	 * @param args
	//	 */
	//	public static void main(String[] args) {
	//		// TODO Auto-generated method stub
	//		Rectangle rectangle = new Rectangle(PageSize.A4);
	//		Document document = new Document(rectangle, 43,43,43,43);
	//		document.setPageCount(2);
	//		try {
	//			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("c:/mydoc.pdf"));
	//
	//			document.open();
	////			Paragraph par = new Paragraph("CO",12);
	////			Chunk ch = new Chunk("2");
	////			ch.setTextRise(-6.0f);
	////			par.add(ch);
	////			document.add(par);
	//			//BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	////			PdfContentByte cbUnder = writer.getDirectContentUnder();
	////			PdfContentByte cbOver = writer.getDirectContent();
	////			PdfBuildRatingGraph me = new PdfBuildRatingGraph(cbUnder, cbOver, 34, 56, "11 Tangmere");
	////			me.createEfficiencyGraph();
	//
	//
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
	////		catch (InvalidDataException e) {
	////			// TODO Auto-generated catch block
	////			e.printStackTrace();
	////		} catch (ApplicationException e) {
	////			// TODO Auto-generated catch block
	////			e.printStackTrace();
	////		}
	//
	//	}

}
