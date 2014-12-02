package org.project.company.greenhomes.service.pdf.templates;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import org.project.company.greenhomes.common.enums.PropertyAttributeNames;
import org.project.company.greenhomes.common.util.MathsHelper;
import org.project.company.greenhomes.domain.entity.PropertyAttribute;
import org.project.company.greenhomes.exception.ApplicationException;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.service.pdf.extra.EstimatedSavingsGraphProperties;
import org.project.company.greenhomes.service.pdf.extra.PdfColorFactory;
import org.project.company.greenhomes.service.pdf.extra.PdfConstants;
import org.project.company.greenhomes.service.pdf.extra.PdfFontFactory;

import java.awt.*;
import java.io.IOException;
import java.util.Set;

/**
 * Class that will generate the nice little current and estimated energy use graphs
 */
public class PdfBuildEstimatedGraph {

	//private PdfContentByte cbUnder;
	private PdfContentByte cbOver;
	private Set<PropertyAttribute> attrs;

	public PdfBuildEstimatedGraph (PdfContentByte cbOver, Set<PropertyAttribute> attrs)
			throws DocumentException, IOException {
		// create all the ratings
		//	this.cbUnder = cbUnder;
		this.cbOver = cbOver;
		this.attrs = attrs;
	}

	public void createEstimatedGraph () throws InvalidDataException, ApplicationException {
		addRectangle(getHeatingRectangleProperties(), true, "Heating");
		addRectangle(getHeatingRectangleLightProperties());
		addRectangle(getHotWaterRectangleProperties(), true, "Hot Water");
		addRectangle(getHotWaterRectangleLightProperties());
		addRectangle(getLightingRectangleProperties(), true, "Lighting");
		addRectangle(getLightingRectangleLightProperties());
		addCO2Rectangle(getCO2RectangleProperties(), true, "CO");
		addCO2Rectangle(getCO2RectangleLightProperties());
	}

	private void addRectangle (EstimatedSavingsGraphProperties props) throws ApplicationException {
		addRectangle(props, false, null);
	}

	private void addRectangle (EstimatedSavingsGraphProperties props, Boolean addHeader, String headline)
			throws ApplicationException {
		cbOver.saveState();
		cbOver.setColorFill(props.getColor());
		cbOver.setColorStroke(props.getColor());
		cbOver.rectangle(props.getX(), props.getY(), props.getWidth(), props.getHeight());
		cbOver.fill();
		cbOver.beginText();
		// set the headline for this bit
		if (addHeader) {
			cbOver.setColorFill(PdfColorFactory.getTurquoiseColor());
			cbOver.setFontAndSize(PdfFontFactory.getFrutiger65Bold(), 10);
			cbOver.setTextMatrix(props.getX(), props.getY() + 19);
			cbOver.showText(headline);
		}
		// set the first part of text
		cbOver.setColorFill(Color.BLACK);
		cbOver.setFontAndSize(PdfFontFactory.getFrutiger45Light(), 10);
		cbOver.setTextMatrix(props.getX() + 7, props.getY() + 3);
		cbOver.showText(props.getText());
		// now second part
		cbOver.setTextMatrix(
				props.getX() + 7 + PdfFontFactory.getFrutiger45Light().getWidthPoint(props.getText(), 10) + 3,
				props.getY() + 3);
		cbOver.showText("*" + props.getCost());
		cbOver.endText();

		cbOver.restoreState();
	}

	private void addCO2Rectangle (EstimatedSavingsGraphProperties props) throws ApplicationException {
		addCO2Rectangle(props, false, null);
	}

	private void addCO2Rectangle (EstimatedSavingsGraphProperties props, Boolean addHeader, String headline)
			throws ApplicationException {
		cbOver.saveState();
		cbOver.setColorFill(props.getColor());
		cbOver.setColorStroke(props.getColor());
		cbOver.rectangle(props.getX(), props.getY(), props.getWidth(), props.getHeight());
		cbOver.fill();
		cbOver.beginText();
		if (addHeader) {
			cbOver.setColorFill(PdfColorFactory.getTurquoiseColor());
			cbOver.setTextMatrix(props.getX(), props.getY() + 19);
			cbOver.setFontAndSize(PdfFontFactory.getFrutiger65Bold(), 10);
			cbOver.showText(headline);
			cbOver.setTextMatrix(props.getX() + cbOver.getEffectiveStringWidth("CO", false),
					props.getY() + (19 - PdfConstants.CO2_SUBSCRIPT_Y_OFFSET));
			cbOver.setFontAndSize(PdfFontFactory.getFrutiger65Bold(), 8);
			cbOver.showText("2");
		}
		// set the first part of text
		cbOver.setColorFill(Color.BLACK);
		cbOver.setColorStroke(Color.BLACK);
		cbOver.setFontAndSize(PdfFontFactory.getFrutiger45Light(), 10);
		cbOver.setTextMatrix(props.getX() + 7, props.getY() + 3);
		cbOver.showText(props.getText());
		// now second part
		cbOver.setTextMatrix(
				props.getX() + 7 + PdfFontFactory.getFrutiger45Light().getWidthPoint(props.getText(), 10) + 3,
				props.getY() + 3);
		cbOver.showText(props.getCost() + " tonnes");
		cbOver.endText();

		cbOver.restoreState();
	}

	private EstimatedSavingsGraphProperties getHeatingRectangleProperties () throws InvalidDataException {
		if (null == getNamedAttribute(PropertyAttributeNames.HEATING_COST_CURRENT)) {
			String message = "Unable to find value for " + PropertyAttributeNames.HEATING_COST_CURRENT.getValue();
			throw new InvalidDataException(message);
		}
		Color color = PdfColorFactory.getHeatingColor();
		return new EstimatedSavingsGraphProperties(color, EstimatedSavingsGraphProperties.fullWidth,
				EstimatedSavingsGraphProperties.column1X, EstimatedSavingsGraphProperties.row1Y, "Current: ",
				getNamedAttribute(PropertyAttributeNames.HEATING_COST_CURRENT).getValue());
	}

	private EstimatedSavingsGraphProperties getHeatingRectangleLightProperties () throws InvalidDataException {
		if (null == getNamedAttribute(PropertyAttributeNames.HEATING_COST_POTENTIAL)) {
			String message = "Unable to find value for " + PropertyAttributeNames.HEATING_COST_POTENTIAL.getValue();
			throw new InvalidDataException(message);
		}

		Color color = PdfColorFactory.getHeatingLightColor();
		Float firstResult = MathsHelper
				.getPercentFromStrings(getNamedAttribute(PropertyAttributeNames.HEATING_COST_POTENTIAL).getValue(),
						getNamedAttribute(PropertyAttributeNames.HEATING_COST_CURRENT).getValue());
		// now multiple the width of the line to get the width!
		Float result = MathsHelper.getPercentValue(EstimatedSavingsGraphProperties.fullWidth, firstResult);
		return new EstimatedSavingsGraphProperties(color, result, EstimatedSavingsGraphProperties.column1X,
				EstimatedSavingsGraphProperties.row1Y - EstimatedSavingsGraphProperties.height, "Potential: ",
				getNamedAttribute(PropertyAttributeNames.HEATING_COST_POTENTIAL).getValue());
	}

	private EstimatedSavingsGraphProperties getHotWaterRectangleProperties () throws InvalidDataException {
		if (null == getNamedAttribute(PropertyAttributeNames.HOT_WATER_COST_CURRENT)) {
			String message = "Unable to find value for " + PropertyAttributeNames.HOT_WATER_COST_CURRENT.getValue();
			throw new InvalidDataException(message);
		}
		Color color = PdfColorFactory.getHotWaterColor();
		return new EstimatedSavingsGraphProperties(color, EstimatedSavingsGraphProperties.fullWidth,
				EstimatedSavingsGraphProperties.column1X, EstimatedSavingsGraphProperties.row2Y, "Current: ",
				getNamedAttribute(PropertyAttributeNames.HOT_WATER_COST_CURRENT).getValue());
	}

	private EstimatedSavingsGraphProperties getHotWaterRectangleLightProperties () throws InvalidDataException {
		if (null == getNamedAttribute(PropertyAttributeNames.HOT_WATER_COST_POTENTIAL)) {
			String message = "Unable to find value for " + PropertyAttributeNames.HOT_WATER_COST_POTENTIAL.getValue();
			throw new InvalidDataException(message);
		}
		Color color = PdfColorFactory.getHotWaterLightColor();
		Float firstResult = MathsHelper
				.getPercentFromStrings(getNamedAttribute(PropertyAttributeNames.HOT_WATER_COST_POTENTIAL).getValue(),
						getNamedAttribute(PropertyAttributeNames.HOT_WATER_COST_CURRENT).getValue());
		// now multiple the width of the line to get the width!
		Float result = MathsHelper.getPercentValue(EstimatedSavingsGraphProperties.fullWidth, firstResult);
		return new EstimatedSavingsGraphProperties(color, result, EstimatedSavingsGraphProperties.column1X,
				EstimatedSavingsGraphProperties.row2Y - EstimatedSavingsGraphProperties.height, "Potential: ",
				getNamedAttribute(PropertyAttributeNames.HOT_WATER_COST_POTENTIAL).getValue());
	}

	private EstimatedSavingsGraphProperties getLightingRectangleProperties () throws InvalidDataException {
		if (null == getNamedAttribute(PropertyAttributeNames.LIGHTING_COST_CURRENT)) {
			String message = "Unable to find value for " + PropertyAttributeNames.LIGHTING_COST_CURRENT.getValue();
			throw new InvalidDataException(message);
		}
		Color color = PdfColorFactory.getLightingColor();
		return new EstimatedSavingsGraphProperties(color, EstimatedSavingsGraphProperties.fullWidth,
				EstimatedSavingsGraphProperties.column2X, EstimatedSavingsGraphProperties.row1Y, "Current: ",
				getNamedAttribute(PropertyAttributeNames.LIGHTING_COST_CURRENT).getValue());
	}

	private EstimatedSavingsGraphProperties getLightingRectangleLightProperties () throws InvalidDataException {
		if (null == getNamedAttribute(PropertyAttributeNames.LIGHTING_COST_POTENTIAL)) {
			String message = "Unable to find value for " + PropertyAttributeNames.LIGHTING_COST_POTENTIAL.getValue();
			throw new InvalidDataException(message);
		}
		Color color = PdfColorFactory.getLightingLightColor();
		Float firstResult = MathsHelper
				.getPercentFromStrings(getNamedAttribute(PropertyAttributeNames.LIGHTING_COST_POTENTIAL).getValue(),
						getNamedAttribute(PropertyAttributeNames.LIGHTING_COST_CURRENT).getValue());
		// now multiple the width of the line to get the width!
		Float result = MathsHelper.getPercentValue(EstimatedSavingsGraphProperties.fullWidth, firstResult);
		return new EstimatedSavingsGraphProperties(color, result, EstimatedSavingsGraphProperties.column2X,
				EstimatedSavingsGraphProperties.row1Y - EstimatedSavingsGraphProperties.height, "Potential: ",
				getNamedAttribute(PropertyAttributeNames.LIGHTING_COST_POTENTIAL).getValue());
	}

	private EstimatedSavingsGraphProperties getCO2RectangleProperties () throws InvalidDataException {
		if (null == getNamedAttribute(PropertyAttributeNames.CO2_EMISSIONS_COST_CURRENT)) {
			String message = "Unable to find value for " + PropertyAttributeNames.CO2_EMISSIONS_COST_CURRENT.getValue();
			throw new InvalidDataException(message);
		}
		Color color = PdfColorFactory.getCO2Color();
		return new EstimatedSavingsGraphProperties(color, EstimatedSavingsGraphProperties.fullWidth,
				EstimatedSavingsGraphProperties.column2X, EstimatedSavingsGraphProperties.row2Y, "Current: ",
				getNamedAttribute(PropertyAttributeNames.CO2_EMISSIONS_COST_CURRENT).getValue());
	}

	private EstimatedSavingsGraphProperties getCO2RectangleLightProperties () throws InvalidDataException {
		if (null == getNamedAttribute(PropertyAttributeNames.CO2_EMISSIONS_COST_POTENTIAL)) {
			String message =
					"Unable to find value for " + PropertyAttributeNames.CO2_EMISSIONS_COST_POTENTIAL.getValue();
			throw new InvalidDataException(message);
		}
		Color color = PdfColorFactory.getCO2LightColor();
		Float firstResult = MathsHelper.getPercentFromStrings(
				getNamedAttribute(PropertyAttributeNames.CO2_EMISSIONS_COST_POTENTIAL).getValue(),
				getNamedAttribute(PropertyAttributeNames.CO2_EMISSIONS_COST_CURRENT).getValue());
		// now multiple the width of the line to get the width!		
		Float result = MathsHelper.getPercentValue(EstimatedSavingsGraphProperties.fullWidth, firstResult);
		return new EstimatedSavingsGraphProperties(color, result, EstimatedSavingsGraphProperties.column2X,
				EstimatedSavingsGraphProperties.row2Y - EstimatedSavingsGraphProperties.height, "Potential: ",
				getNamedAttribute(PropertyAttributeNames.CO2_EMISSIONS_COST_POTENTIAL).getValue());
	}

	private PropertyAttribute getNamedAttribute (PropertyAttributeNames name) {
		for (PropertyAttribute attr : attrs) {
			if (attr.getName().equalsIgnoreCase(name.getValue())) {
				return attr;
			}
		}
		return null;
	}
	//	/**
	//	 * @param args
	//	 */
	//	public static void main(String[] args) {
	//		// TODO Auto-generated method stub
	//		Rectangle rectangle = new Rectangle(PageSize.A4);
	//		Document document = new Document(rectangle, 43,43,43,43);
	//
	//		try {
	//			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("c:/mydoc.pdf"));
	//			document.open();
	//
	//			PdfContentByte cbOver = writer.getDirectContent();
	//
	//			PdfContentByte cbUnder = writer.getDirectContentUnder();
	//			Set<PropertyAttribute> attrs = new HashSet<PropertyAttribute>();
	//			PropertyAttribute e = new PropertyAttribute();
	//			e.setName(PropertyAttributeNames.HEATING_COST_CURRENT.getValue());
	//			e.setValue("422");
	//			attrs.add(e);
	//			e = new PropertyAttribute();
	//			e.setName(PropertyAttributeNames.HEATING_COST_POTENTIAL.getValue());
	//			e.setValue("400");
	//			attrs.add(e);
	//			e = new PropertyAttribute();
	//			e.setName(PropertyAttributeNames.LIGHTING_COST_CURRENT.getValue());
	//			e.setValue("88");
	//			attrs.add(e);
	//			e = new PropertyAttribute();
	//			e.setName(PropertyAttributeNames.LIGHTING_COST_POTENTIAL.getValue());
	//			e.setValue("33");
	//			attrs.add(e);
	//			e = new PropertyAttribute();
	//			e.setName(PropertyAttributeNames.HOT_WATER_COST_CURRENT.getValue());
	//			e.setValue("167");
	//			attrs.add(e);
	//			e = new PropertyAttribute();
	//			e.setName(PropertyAttributeNames.HOT_WATER_COST_POTENTIAL.getValue());
	//			e.setValue("101");
	//			attrs.add(e);
	//			e = new PropertyAttribute();
	//			e.setName(PropertyAttributeNames.CO2_EMISSIONS_COST_CURRENT.getValue());
	//			e.setValue("4.4");
	//			attrs.add(e);
	//			e = new PropertyAttribute();
	//			e.setName(PropertyAttributeNames.CO2_EMISSIONS_COST_POTENTIAL.getValue());
	//			e.setValue("3.2");
	//			attrs.add(e);
	//			PdfBuildEstimatedGraph me = new PdfBuildEstimatedGraph(cbUnder, cbOver, attrs);
	//			me.createEstimatedGraph();
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
	////		} catch (InvalidDataException e) {
	////			// TODO Auto-generated catch block
	////			e.printStackTrace();
	////		}
	// catch (ApplicationException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (InvalidDataException e) {
	//	// TODO Auto-generated catch block
	//	e.printStackTrace();
	//}
	//
	//	}

}
