package org.project.company.greenhomes.service.pdf;

import com.lowagie.text.DocumentException;
import org.project.company.greenhomes.common.enums.PropertyAttributeNames;
import org.project.company.greenhomes.domain.entity.*;
import org.project.company.greenhomes.exception.ApplicationException;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.service.pdf.templates.PdfLetterTemplateA;
import org.project.company.greenhomes.service.pdf.templates.PdfTemplateAText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Class just for calling main method to generate pdfs
 *
 *
 */

@Deprecated
public class PdfLetterGenerator {

	private final static String imagesFolder = "C:/dev/workspace/GreenHome/src/pdf-images/";
	//private final static String imagesFolder = "/home/fergus/pdf-images/";
	//private final static String outputDir = "/home/fergus/classes/";
	private final static String outputDir = "C:\\dev\\workspace\\GreenHome\\build\\";

	public static void main (String[] args) {
		try {

			PropertyEPC address = new PropertyEPC(UUID.randomUUID());
			address.setEnergyRatingCurrent(19);
			address.setEnergyRatingPotential(70);
			Date date = new Date();
			address.setRrn(date.getTime() + "");
			// set address
			address.setAddressLine2("2");
			address.setAddressLine3("DEAN WOOD VIE");
			address.setTown("Bildingford");
			address.setPostcodeOutcode("WC1");
			address.setPostcodeIncode("4GH");
			Centre centre = new Centre();
			centre.setCentreName("Energy Savings Trust Advice Centre");
			centre.setAddress1("18B Manor Way");
			centre.setAddress2("Belasis Hall Technology Park");
			centre.setAddress3("Billingham");
			centre.setAddress4("East Suffolk");
			//centre.setAddress5("Suffolk");
			centre.setPostcode("TS23 4HN");
			centre.setCentreCode(3);
			address.setCentre(centre);
			centre.setContactName("Jo the Plumber");
			centre.setContactJobTitle("Manager");

			// need to add some of the energy attributes
			// savings
			// need to add some of the energy attributes
			// savings

			address.setMeasuresSet(createPropertyMeasures());
			address.setPropertyAddressAttributeSet(createPropertyAttributes());
			//address.setRating("G");
			PdfLetterTemplateA templateA = new PdfLetterTemplateA();
			templateA.setPdfOutputDirectory(outputDir);
			templateA.setImagesFolder(imagesFolder);
			templateA.setEpcURL("energysavingtrust.org.uk/epcghghgfhgh");
			PdfTemplateAText pdfTemplateAText = getTemplatetext();
			templateA.setPdfTemplateAText(pdfTemplateAText);
			Schedule schedule = new Schedule();
			schedule.setScheduleId(22l);
			schedule.setStartDate(new Date());
			templateA.setChangeCaseAddress(true);
			templateA.generate(address, schedule);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// finally close the document
		catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("finished");
	}

	private static PdfTemplateAText getTemplatetext () {
		PdfTemplateAText pdfTemplateAText = new PdfTemplateAText();
		pdfTemplateAText.setGreeting("To the resident");
		pdfTemplateAText.setSignOff("Yours faithfully");
		;
		pdfTemplateAText.setLetterHeadline("Save up to $potential-saving-text$ and reduce your carbon footprint.");
		pdfTemplateAText.setParagraph1(
				"We are writing to you from the Energy Saving Trust. We are an independent, non-profit organisation, who provide free and impartial advice tailored to help you save energy in the home.");
		pdfTemplateAText.setParagraph2(
				"Before you bought your new home, you received an Energy Performance Certificate as part of your Home Information Pack. Energy Performance Certificates give energy efficiency ratings on homes: 'A' being the most energy efficient and 'G' the least efficient. Homes which score poor energy ratings of F or G have higher running costs and have more impact on the environment.");
		pdfTemplateAText.setParagraph3a(
				"By simply following the energy saving measures recommended in your Energy Performance Certificate you could save up to potential-saving-text a year and reduce your carbon footprint by carbon-saving-text tonnes of carbon dioxide (CO");
		pdfTemplateAText.setParagraph3b(
				"). As well as advising you on the measures in your Energy Performance Certificate, we can tell you about other ways to save energy and any grants or offers available to help towards the costs of measures including loft and cavity wall insulation.");
		pdfTemplateAText.setParagraph4("To start saving today call 0800 512 012 or visit");
		pdfTemplateAText.setRatingText("Your home was rated: ");
		pdfTemplateAText.setEstimatedHeadlinePart1("Estimated annual fuel costs and CO");
		pdfTemplateAText.setEstimatedHeadlinePart2("emissions for your home");
		pdfTemplateAText.setEstimatedText(
				"The costs and savings shown here and on your Energy Performance Certificate are based on standardised assumptions on how many people live in a home, how it is heated and where it is located. They are a way of comparing different homes. The savings only take into account the actual cost of fuel - not the cost of any associated services, maintenance or safety inspection. Remember fuel prices may have increased since your certificate was created.");
		pdfTemplateAText.setMeasuresHeadline("Here are the measures you can take to improve the rating of your home");
		pdfTemplateAText.setLowCostMeasures(
				"Lower cost effective measures to improve your home$inverted-commas$s ratings (typically up to $pound-sign$500 each)");
		pdfTemplateAText.setHighCostMeasures(
				"Higher cost measures to improve your home$inverted-commas$s ratings (typically over $pound-sign$500 each)");
		pdfTemplateAText.setFurtherMeasuresPart1(
				"There are also further measures that you could consider to reduce your bills and CO");
		pdfTemplateAText.setFurtherMeasuresPart2("emissions");
		return pdfTemplateAText;
	}

	private static Set<PropertyAttribute> createPropertyAttributes () {
		Set<PropertyAttribute> attrs = new HashSet<PropertyAttribute>();
		PropertyAttribute e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.HEATING_COST_CURRENT.getValue());
		e.setValue("422");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.HEATING_COST_POTENTIAL.getValue());
		e.setValue("400");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.LIGHTING_COST_CURRENT.getValue());
		e.setValue("88");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.LIGHTING_COST_POTENTIAL.getValue());
		e.setValue("33");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.HOT_WATER_COST_CURRENT.getValue());
		e.setValue("167");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.HOT_WATER_COST_POTENTIAL.getValue());
		e.setValue("101");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.CO2_EMISSIONS_COST_CURRENT.getValue());
		e.setValue("4.4");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.CO2_EMISSIONS_COST_POTENTIAL.getValue());
		e.setValue("3.2");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.TYPICAL_SAVING.getValue());
		e.setValue("117");
		attrs.add(e);
		return attrs;
	}

	private static Set<PropertyMeasure> createPropertyMeasures () {
		Set<PropertyMeasure> attrs = new LinkedHashSet<PropertyMeasure>();
		PropertyMeasure e = new PropertyMeasure();
		e.setCategoryId(1);
		e.setDescription(
				"Fit thermostatic valves to radiators to control heat in each room. You'll reduce energy use and heating bills.");
		e.setHeading("Upgrade heating controls");
		e.setSortOder(1);
		e.setTypicalSaving(25);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(1);
		e.setDescription(
				"A room thermostat tells the boiler to switch off when no more heat is needed. This reduces energy use and heating bills.");
		e.setHeading("Heating controls (room thermostat)");
		e.setSortOder(2);
		e.setTypicalSaving(22);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(1);
		e.setDescription(
				"Fit a programmer and room thermostat to tell the warm air heating system to switch off when no more heat is needed. You'll reduce energy use and heating bills.");
		e.setHeading("Heating controls (programmer and room thermostat)");
		e.setSortOder(3);
		e.setTypicalSaving(20);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(1);
		e.setDescription(
				"Installing a 160mm thick jacket around the hot water cylinder will keep water hot, reduce the energy used and lower fuel bills.");
		e.setHeading("Hot water cylinder insulation");
		e.setSortOder(4);
		e.setTypicalSaving(20);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(1);
		e.setDescription(
				"A thicker hot water cylinder jacket up to 160mm - will keep water hotter, reduce the energy used and lower fuel bills.");
		e.setHeading("Hot water cylinder insulation");
		e.setSortOder(5);
		e.setTypicalSaving(20);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(1);
		e.setDescription(
				"Add an 80 mm cylinder jacket to your existing hot water cylinder insulation will keep water hotter, reduce the energy used and lower fuel bills.");
		e.setHeading("Hot water cylinder insulation");
		e.setSortOder(6);
		e.setTypicalSaving(20);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(1);
		e.setDescription("Minimise energy use and lower fuel bills by fixing a thermostat to your hot water cylinder.");
		e.setHeading("Cylinder thermostat");
		e.setSortOder(7);
		e.setTypicalSaving(20);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(1);
		e.setDescription(
				"Insulate your loft space, or between roof rafters to dramatically reduce heat loss through the roof, reducing your energy use and your fuel bills.");
		e.setHeading("Loft insulation");
		e.setSortOder(8);
		e.setTypicalSaving(20);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(1);
		e.setDescription(
				"Swapping traditional light bulbs for energy saving ones will cut lighting costs over the bulb's lifetime. They last up to 10 times longer, and with the average cost only, you'll usually recoup the outlay within a year.");
		e.setHeading("Low energy lighting");
		e.setSortOder(9);
		e.setTypicalSaving(20);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(1);
		e.setDescription(
				"Fit draught proofing - strips of insulation around windows and doors - to improve comfort and warmth.");
		e.setHeading("Draughtproofing");
		e.setSortOder(10);
		e.setTypicalSaving(20);
		attrs.add(e);

		e = new PropertyMeasure();
		e.setCategoryId(2);
		e.setDescription(
				"Change to a biomass stove and boiler, which burns 'cleaner', renewable fuel such as wood pellets and therefore is less damaging to the environment..");
		e.setHeading("Biomass stove with boiler");
		e.setSortOder(1);
		e.setTypicalSaving(18);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(2);
		e.setDescription(
				"Change to a condensing boiler that heats rooms and water to save money. A condensing boiler will burn less fuel to heat the property than other types of boiler.");
		e.setHeading("Band A condensing boiler");
		e.setSortOder(2);
		e.setTypicalSaving(19);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(2);
		e.setDescription(
				"Change to a biomass stove and boiler, which burns 'cleaner', renewable fuel such as wood pellets and therefore is less damaging to the environment..");
		e.setHeading("Biomass boiler");
		e.setSortOder(3);
		e.setTypicalSaving(13);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(2);
		e.setDescription("Change to a high-efficiency condensing unit to heat your home, and save fuel and money.");
		e.setHeading("Install Band A condensing heating unit");
		e.setSortOder(4);
		e.setTypicalSaving(13);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(2);
		e.setDescription(
				"Change to a high-efficiency condensing unit to heat your home, independent of your range cooker. It will save fuel and money.");
		e.setHeading("Install Band A condensing boiler (separate from the range cooker)");
		e.setSortOder(5);
		e.setTypicalSaving(13);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(2);
		e.setDescription(
				"Fan-assisted storage heaters with automatic charge control are smaller and easier to control than the ones you've got.");
		e.setHeading("Fan assisted storage heaters");
		e.setSortOder(6);
		e.setTypicalSaving(13);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(2);
		e.setDescription(
				"Switch to a modern, more efficient warm air unit, which will burn less fuel for heating and hot water.");
		e.setHeading("Replacement warm air unit");
		e.setSortOder(7);
		e.setTypicalSaving(13);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(2);
		e.setDescription(
				"Fan-assisted storage heaters with automatic charge control are smaller and easier to control than the ones you have got.");
		e.setHeading("Fan assisted storage heaters");
		e.setSortOder(8);
		e.setTypicalSaving(13);
		attrs.add(e);

		e = new PropertyMeasure();
		e.setCategoryId(3);
		e.setDescription("A wind turbine provides electricity from wind energy.");
		e.setHeading("Wind turbine");
		e.setSortOder(1);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(3);
		e.setDescription(
				"Change to a condensing boiler that heats rooms and water. It will burn less fuel to heat your home.");
		e.setHeading("Band A condensing gas boiler");
		e.setSortOder(2);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(3);
		e.setDescription(
				"Solar PV panels on your roof will convert light directly into electricity with no waste and no emissions.");
		e.setHeading("Solar photovoltaic (PV) panels");
		e.setSortOder(3);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(3);
		e.setDescription(
				"Add secondary glazing to reduce draughts and cold spots, and potentially reduce noise and combat condensation.");
		e.setHeading("Secondary glazing");
		e.setSortOder(4);
		attrs.add(e);
		return attrs;
	}

	public void createPDF () {

	}

	//	private static Set<PropertyMeasure> createPropertyMeasures()
	//	{
	//		Set<PropertyMeasure> attrs = new LinkedHashSet<PropertyMeasure>();
	//		PropertyMeasure e = new PropertyMeasure();
	//		e.setCategoryId(1);
	//		e.setDescription("Double glazing is the term given to a system where two panes of glass are made up into a sealed unit. Replacing existing single-glazed windows with double glazing will improve comfort in the home by reducing draughts and cold spots near windows. Double-glazed windows may also reduce noise, improve security and combat problems with condensation. Building Regulations apply to this work, so either use a contractor who is registered with a competent persons scheme1 or obtain advice from your local authority building control department.");
	//		e.setHeading("Double glazing");
	//		e.setSortOder(1);
	//		e.setTypicalSaving(25);
	//		attrs.add(e);
	//		e = new PropertyMeasure();
	//		e.setCategoryId(1);
	//		e.setDescription("Heating controls (programmer, room thermostat and thermostatic radiator valves)");
	//		e.setHeading("Heating controls (programmer, room thermostat and thermostatic radiator valves)");
	//		e.setSortOder(2);
	//		e.setTypicalSaving(22);
	//		attrs.add(e);
	//		e = new PropertyMeasure();
	//		e.setCategoryId(1);
	//		e.setDescription("Fitting draughtproofing, strips of insulation around windows and doors, will improve the comfort in the home. A contractor can be employed but draughtproofing can be installed by a competent DIY enthusiast");
	//		e.setHeading("Draughtproofing");
	//		e.setSortOder(3);
	//		e.setTypicalSaving(20);
	//		attrs.add(e);
	//
	//		e = new PropertyMeasure();
	//		e.setCategoryId(2);
	//		e.setDescription("A condensing boiler is capable of much higher efficiencies than other types of boiler, meaning it will burn less fuel to heat this property. This improvement is most appropriate when the existing central heating boiler needs repair or replacement, but there may be exceptional circumstances making this impractical. Condensing boilers need a drain for the condensate which limits their location; remember this when considering remodelling the room containing the existing boiler even if the latter is to be retained for the time being (for example a kitchen makeover). Building Regulations apply to this work, so your local authority building control department should be informed, unless the installer is registered with a competent persons scheme1, and can therefore self-certify the work for Building Regulation compliance. Ask a qualified heating engineer to explain the options.");
	//		e.setHeading("Band A condensing boiler");
	//		e.setSortOder(1);
	//		e.setTypicalSaving(18);
	//		attrs.add(e);
	//		e = new PropertyMeasure();
	//		e.setCategoryId(2);
	//		e.setDescription("Increasing the thickness of existing insulation by adding an 80 mm cylinder jacket around the hot water cylinder will help maintain the water at the required temperature; this will reduce the amount of energy used and lower fuel bills. The jacket should be fitted over the top of the existing foam insulation and over any thermostat clamped to the cylinder. Hot water pipes from the hot water cylinder should also be insulated, using pre-formed pipe insulation of up to 50 mm thickness, or to suit the space available, for as far as they can be accessed to reduce losses in summer. All these materials can be purchased from DIY stores and installed by a competent DIY enthusiast.");
	//		e.setHeading("Hot water cylinder insulation");
	//		e.setSortOder(2);
	//		e.setTypicalSaving(19);
	//		attrs.add(e);
	//		e = new PropertyMeasure();
	//		e.setCategoryId(2);
	//		e.setDescription("Cavity wall insulation, to fill the gap between the inner and outer layers of external walls with an insulating material, reduces heat loss; this will improve levels of comfort, reduce energy use and lower fuel bills. The insulation material is pumped into the gap through small holes that are drilled into the outer walls, and the holes are made good afterwards. As specialist machinery is used to fill the cavity, a professional installation company should carry out this work, and they should carry out a thorough survey before commencing work to ensure that this type of insulation is suitable for this home. They should also provide a guarantee for the work and handle any building control issues. Further information about cavity wall insulation and details of local installers can be obtained from the National Insulation Association (www.nationalinsulationassociation.org.uk).");
	//		e.setHeading("Cavity wall insulation");
	//		e.setSortOder(3);
	//		e.setTypicalSaving(13);
	//		attrs.add(e);
	//
	//		e = new PropertyMeasure();
	//		e.setCategoryId(3);
	//		e.setDescription("Modern storage heaters are smaller and easier to control than the older type in the property. Ask for a quotation for new, fan-assisted heaters with automatic charge control. A dual-immersion cylinder, which can be installed at the same time, will provide cheaper hot water than the system currently installed. As installations should be in accordance with the current regulations covering electrical wiring, only a qualified electrician should carry out the installation. Building Regulations apply to this work, so your local authority building control department should be informed, unless the installer is registered with a competent persons scheme1, and can therefore self-certify the work for Building Regulation compliance. Ask a qualified electrical heating engineer to explain the options, which might also include switching to other forms of electric heating.");
	//		e.setHeading("Fan assisted storage heaters");
	//		e.setSortOder(1);
	//		attrs.add(e);
	//		e = new PropertyMeasure();
	//		e.setCategoryId(3);
	//		e.setDescription("A wind turbine provides electricity from wind energy. This electricity is used throughout the home in the same way as the electricity purchased from an energy supplier. The British Wind Energy Association has up-to-date information on suppliers of small-scale wind systems and any grant that may be available. Planning restrictions may apply and you should check this with the local authority. Building Regulations apply to this work, so your local authority building control department should be informed, unless the installer is appropriately qualified and registered as such with a competent persons scheme1, and can therefore self-certify the work for Building Regulation compliance. Wind turbines are not suitable for all properties. The system�s effectiveness depends on local wind speeds and the presence of nearby obstructions, and a site survey should be undertaken by an accredited installer.");
	//		e.setHeading("Wind turbine");
	//		e.setSortOder(2);
	//		attrs.add(e);
	//		e = new PropertyMeasure();
	//		e.setCategoryId(3);
	//		e.setDescription("A solar PV system is one which converts light directly into electricity via panels placed on the roof with no waste and no emissions. This electricity is used throughout the home in the same way as the electricity purchased from an energy supplier. The British Photovoltaic Association has up-to-date information on local installers who are qualified electricians and on any grant that may be available. Planning restrictions may apply in certain neighbourhoods and you should check this with the local authority. Building Regulations apply to this work, so your local authority building control department should be informed, unless the installer is appropriately qualified and registered as such with a competent persons scheme1, and can therefore self-certify the work for Building Regulation compliance.");
	//		e.setHeading("Solar ddphotovoltaic (PV) panels");
	//		e.setSortOder(3);
	//		attrs.add(e);
	//		e = new PropertyMeasure();
	//		e.setCategoryId(3);
	//		e.setDescription("A solar PV system is one which converts light directly into electricity via panels placed on the roof with no waste and no emissions. This electricity is used throughout the home in the same way as the electricity purchased from an energy supplier. The British Photovoltaic Association has up-to-date information on local installers who are qualified electricians and on any grant that may be available. Planning restrictions may apply in certain neighbourhoods and you should check this with the local authority. Building Regulations apply to this work, so your local authority building control department should be informed, unless the installer is appropriately qualified and registered as such with a competent persons scheme1, and can therefore self-certify the work for Building Regulation compliance.");
	//		e.setHeading("Solar ddphotovoltaic (PV) panels");
	//		e.setSortOder(4);
	//		attrs.add(e);
	//		e = new PropertyMeasure();
	//		e.setCategoryId(3);
	//		e.setDescription("A xsolar PV system is one which converts light directly into electricity via panels placed on the roof with no waste and no emissions. This electricity is used throughout the home in the same way as the electricity purchased from an energy supplier. The British Photovoltaic Association has up-to-date information on local installers who are qualified electricians and on any grant that may be available. Planning restrictions may apply in certain neighbourhoods and you should check this with the local authority. Building Regulations apply to this work, so your local authority building control department should be informed, unless the installer is appropriately qualified and registered as such with a competent persons scheme1, and can therefore self-certify the work for Building Regulation compliance.");
	//		e.setHeading("Solar xphotovoltaic (PV) panels");
	//		e.setSortOder(5);
	//		attrs.add(e);
	//		return attrs;
	//	}
}
