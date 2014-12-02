package org.project.company.greenhomes.test.common;

import org.project.company.greenhomes.common.enums.PropertyAttributeNames;
import org.project.company.greenhomes.domain.entity.PropertyAttribute;
import org.project.company.greenhomes.domain.entity.PropertyMeasure;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TestDriver {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main (String[] args) throws IOException {

		System.out.println("Listing Font properties");
		File pdfFolder = new File("C:\\temp\\output");
		File[] fileList = pdfFolder.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			System.out.println("try to delete" + file.getName());
			if (!file.delete()) {
				System.out.println("Couldn't delete files" + file.getName());
			}
		}
		//		for (File file: fileList)
		//		{
		//			System.out.println("try to delete"+file.getName());
		//			if (!file.delete())
		//			{
		//				System.out.println("Couldn't delete files"+file.getName());
		//			}
		//		}
		//		try {
		//			BufferedWriter out = new BufferedWriter(new FileWriter("encodings.txt"));
		//			//BaseFont bfComic = BaseFont.createFont("c:\\windows\\fonts\\comicbd.ttf", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		//			BaseFont bfComic = PdfFontFactory.getFrutiger65Bold();
		//			out.write("postscriptname: " + bfComic.getPostscriptFontName());
		//			out.write("\r\n\r\n");
		//	        String[] codePages = bfComic.getCodePagesSupported();
		//	        out.write("All available encodings:\n\n");
		//	        for (int i = 0; i < codePages.length; i++) {
		//	        	out.write(codePages[i]);
		//	        	out.write("\r\n");
		//	        }
		//	        out.flush();
		//	        out.close();
		//		}  catch (IOException e) {
		//			e.printStackTrace();
		//		}
		//		String word = "MYWORD-HANDSS";
		//		word = WordUtils.capitalizeFully(word);
		//		System.out.println("word is:"+word);
		//		File file = new File("C:\\temp\\uploads\\landmark\\RdSAP EPC-Recommendations_SAMPLE1.csv");
		//		System.out.println("file:"+file.getName());
		//		long startTime = 123103100000l;
		//		long endTime = 123123100000l;
		//	    long timeMillis = endTime -startTime;
		//	    long time = timeMillis / 1000;
		//	    String seconds = Integer.toString((int)(time % 60));
		//	    String minutes = Integer.toString((int)((time % 3600) / 60));
		//	    String hours = Integer.toString((int)(time / 3600));
		//		    for (int i = 0; i < 2; i++) {
		//		    if (seconds.length() < 2) {
		//		    seconds = "0" + seconds;
		//		  }
		//		   if (minutes.length() < 2) {
		//		   minutes = "0" + minutes;
		//		  }
		//		  if (hours.length() < 2) {
		//		  hours = "0" + hours;
		//		  	}
		//		  }
		// generate a series of random number based on number of results
		//		RandomData data = new RandomDataImpl();
		//		int numberOfResults = 10;
		//		//int randomNumber = data.nextInt(0, numberOfResults-1);
		//		Set<String> set = new HashSet<String>();
		//		while (set.size() < numberOfResults){
		//			set.add(data.nextInt(0, numberOfResults-1)+"");
		//		}
		//		System.out.println("set:"+set.size());
		//		System.out.println("minutes:"+minutes);
		//		System.out.println("seconds:"+seconds);
		//		String fileName = "text.out";
		//		FileWriter fout = new FileWriter(fileName);
		//
		//		String text = "3333-2846-6690-0791-9999,4,3,\"Solar photovoltaic panels, 2.5 kWp\",\"Solar photovoltaic panels, 2.5 kWp\",\"A solar PV system is one which converts light directly into electricity via panels placed on the roof with no waste and no emissions. This electricity is used throughout the home in the same way as the electricity purchased from an energy supplier. The British Photovoltaic Association has up-to-date information on local installers who are qualified electricians and on any grant that may be available. Planning restrictions may apply in certain neighbourhoods and you should check this with the local authority. Building Regulations apply to this work, so your local authority building control department should be informed, unless the installer is appropriately qualified and registered as such with a competent persons scheme?, and can therefore self-certify the work for Building Regulation compliance.\",150,60,52";
		//		fout.write(text);
		//		fout.close();
		//		FileReader fin = new FileReader(fileName);
		//
		//
		//
		//		// Parse the data
		//		String[][] values = CSVParser.parse(
		//				fin
		//		);
		//
		//		// Display the parsed data
		//		for (int i=0; i<values.length; i++){
		//			System.out.println("i"+i);
		//		    for (int j=0; j<values[i].length; j++){
		//		        System.out.println(values[i][j]);
		//		    }
		//		    System.out.println("-----");
		//		}
		//		catch (ApplicationException e)
		//		{
		//		// TODO Auto-generated catch block
		//		e.printStackTrace();
		//		}

		//		Scanner src = new Scanner(fin);
		//		src.useDelimiter(",");
		//		while(src.hasNext())
		//		{
		//			System.out.println("value:"+src.next());
		//		}
		//		InputStream is = TestDriver.class.getResourceAsStream("config/Frutiger/FrutigerLTStd-Bold.otf");
		//		System.out.println("is:"+is);
		//		//String font = "C:/dev/workspace/GreenHome/src/java/uk/org/est/greenhomes/fonts/Frutiger/FrutigerLTStd-Bold.otf";
		//		String font = "Frutiger/FrutigerLTStd-Bold.otf";
		//		try {
		//			BaseFont bf = BaseFont.createFont(font, BaseFont.CP1252, BaseFont.EMBEDDED);
		//		} catch (DocumentException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		System.out.println("finished");

		//		try{
		//			DBUtil.initaliseMockJNDI();
		//		}
		//		catch(Exception e){
		//			e.printStackTrace();
		//		}
		//       // setAutowireMode(AUTOWIRE_NO);
		//        // create a new file input stream with the input file specified
		//		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(BaseSpringTestCase.APPLICATION_CONTEXT);
		//		// get the list of valid sales
		//		PropertyService propertyService = (PropertyService)ctx.getBean(BeanNames.PROPERTY_SALES_SERVICE.getValue());
		//		ReferenceDataService referenceDataService = (ReferenceDataService)ctx.getBean(BeanNames.REFERENCE_DATA_SERVICE.getValue());
		//		BatchSummaryAndScheduleService batchSummaryService = (BatchSummaryAndScheduleService)ctx.getBean(BeanNames.BATCH_SUMMARY_SERVICE.getValue());
		//		// generate some epc results - create the criteria for finding adderss sales
		//		Map<String, Object> holder = new HashMap<String, Object>();
		//		holder.put("workFlowStatus", WorkFlowStatus.RECEIVED.getValue());
		//
		//		List<PropertyAddress> results = propertyService.findByCriteria(holder);
		//		//List<PropertyAddress> toSave = new ArrayList<PropertyAddress>();
		//		UploadSummary summary = new UploadSummary();
		//		summary.setUploadType(BatchTypes.EPC_XML.getValue());
		//		long start = System.currentTimeMillis();
		//		summary.setStartTime(new Date(start));
		//		summary = batchSummaryService.save(summary);
		//		PropertyEPC epc = null;
		//		PropertyAddress add = null;
		//		PropertySale sale = null;
		//		// now for each one create an epc
		//		for(PropertyAddress address: results)
		//		{
		//			// this is the data that needs to be copied
		//			epc = new PropertyEPC(UUID.randomUUID());
		//			//System.out.println("upload id:"+summary.getUploadSummaryId());
		//			epc.setUploadId(summary.getUploadSummaryId());
		//			epc.setPostcodeOutcode(address.getPostcodeOutcode());
		//			epc.setPostcodeIncode(address.getPostcodeIncode());
		//			epc.setAddressLine1(address.getAddressLine1());
		//			epc.setAddressLine2(address.getAddressLine2());
		//			epc.setAddressLine3(address.getAddressLine3());
		//			epc.setCounty(address.getCounty());
		//			epc.setDistrict(address.getDistrict());
		//			epc.setTown(address.getTown());
		//			sale = (PropertySale)address;
		//			epc.setInspectionDate(sale.getSaleDate());
		//			// now create the things that will come from the report
		//			Random random = new Random();
		//			epc.setRrn(random.nextInt()+"");
		//			epc.setEnergyRatingCurrent(18);
		//			epc.setEnergyRatingPotential(38);
		//			epc.setPropertyAddressAttributeSet(createPropertyAttributes());
		//			// now we need to add the 4 required measures
		//			epc.setMeasuresSet(createPropertyMeasures());
		//			// now the location data
		//			add = (PropertyAddress)epc;
		//			add = referenceDataService.populateLocationData(add);
		//			propertyService.insertPropertyAddress(add);
		//			System.out.println("saved");
		//		}
		//		Integer numberOfRows = batchSummaryService.findNumberOfResultsByUploadId(summary.getUploadSummaryId());
		//		summary.setNumberOfRows(numberOfRows);
		//		batchSummaryService.save(summary);
		//		System.out.println("finished");

		//        UploadSummary summary = new UploadSummary();
		//        BufferedReader reader = null;
		//        try
		//        {
		//			summary.setUploadType(BatchTypes.PROPERTY_SALES.getValue());
		//			long start = System.currentTimeMillis();
		//			summary.setStartTime(new Date(start));
		//			summary = batchSummaryService.save(summary);
		//			if (log.isDebugEnabled())
		//			{
		//				log.debug("summary.getUploadSummaryId()"+summary.getUploadSummaryId());
		//			}
		//
		//	        long finish = System.currentTimeMillis();
		//	        log.error("time taken = "+(finish-start));
		//			summary.setEndTime(new Date(finish));
		//			summary.setErrorCount(errors);
		//			// finally get the number of records for that batch
		//			Integer numberOfRows = batchSummaryService.findNumberOfResultsByUploadId(summary.getUploadSummaryId());
		//	        summary.setNumberOfRows(numberOfRows.intValue());
		//			// finally, update the schedule info
		//			batchSummaryService.save(summary);

		// at the command line
		//		try {
		//			FileInputStream fin = null;
		//			String file = "C:/dev/workspace/GreenHomes/docs/land-registry/land-registry.xls";
		//			fin = new FileInputStream(file);
		//
		//	        // create a new org.apache.poi.poifs.filesystem.Filesystem
		//	        POIFSFileSystem poifs = new POIFSFileSystem(fin);
		//	        // get the Workbook (excel part) stream in a InputStream
		//	        InputStream din = poifs.createDocumentInputStream("Workbook");
		//	        // construct out HSSFRequest object
		//	        HSSFRequest req = new HSSFRequest();
		//	        // lazy listen for ALL records with the listener shown above
		//	        req.addListenerForAllRecords(new ExcelRowProcessor());
		//	        // create our event factory
		//	        HSSFEventFactory factory = new HSSFEventFactory();
		//	        // process our events based on the document input stream
		//	        factory.processEvents(req, din);
		//	        // once all the events are processed close our file input stream
		//	        fin.close();
		//	        // and our document input stream (don't want to leak these!)
		//	        din.close();
		//        System.out.println("done.");
		//		} catch (FileNotFoundException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}

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
		e.setValue("175");
		attrs.add(e);
		return attrs;
	}

	private static Set<PropertyMeasure> createPropertyMeasures () {
		Set<PropertyMeasure> attrs = new HashSet<PropertyMeasure>();
		PropertyMeasure e = new PropertyMeasure();
		e.setCategoryId(1);
		e.setDescription(
				"Double glazing is the term given to a system where two panes of glass are made up into a sealed unit. Replacing existing single-glazed windows with double glazing will improve comfort in the home by reducing draughts and cold spots near windows. Double-glazed windows may also reduce noise, improve security and combat problems with condensation. Building Regulations apply to this work, so either use a contractor who is registered with a competent persons scheme1 or obtain advice from your local authority building control department.");
		e.setHeading("Double glazing");
		e.setSortOder(1);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(1);
		e.setDescription("Heating controls (programmer, room thermostat and thermostatic radiator valves)");
		e.setHeading("Heating controls (programmer, room thermostat and thermostatic radiator valves)");
		e.setSortOder(2);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(1);
		e.setDescription(
				"Fitting draughtproofing, strips of insulation around windows and doors, will improve the comfort in the home. A contractor can be employed but draughtproofing can be installed by a competent DIY enthusiast");
		e.setHeading("Draughtproofing");
		e.setSortOder(3);
		attrs.add(e);

		e = new PropertyMeasure();
		e.setCategoryId(2);
		e.setDescription(
				"A condensing boiler is capable of much higher efficiencies than other types of boiler, meaning it will burn less fuel to heat this property. This improvement is most appropriate when the existing central heating boiler needs repair or replacement, but there may be exceptional circumstances making this impractical. Condensing boilers need a drain for the condensate which limits their location; remember this when considering remodelling the room containing the existing boiler even if the latter is to be retained for the time being (for example a kitchen makeover). Building Regulations apply to this work, so your local authority building control department should be informed, unless the installer is registered with a competent persons scheme1, and can therefore self-certify the work for Building Regulation compliance. Ask a qualified heating engineer to explain the options.");
		e.setHeading("Band A condensing boiler");
		e.setSortOder(1);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(2);
		e.setDescription(
				"Increasing the thickness of existing insulation by adding an 80 mm cylinder jacket around the hot water cylinder will help maintain the water at the required temperature; this will reduce the amount of energy used and lower fuel bills. The jacket should be fitted over the top of the existing foam insulation and over any thermostat clamped to the cylinder. Hot water pipes from the hot water cylinder should also be insulated, using pre-formed pipe insulation of up to 50 mm thickness, or to suit the space available, for as far as they can be accessed to reduce losses in summer. All these materials can be purchased from DIY stores and installed by a competent DIY enthusiast.");
		e.setHeading("Hot water cylinder insulation");
		e.setSortOder(2);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(2);
		e.setDescription(
				"Cavity wall insulation, to fill the gap between the inner and outer layers of external walls with an insulating material, reduces heat loss; this will improve levels of comfort, reduce energy use and lower fuel bills. The insulation material is pumped into the gap through small holes that are drilled into the outer walls, and the holes are made good afterwards. As specialist machinery is used to fill the cavity, a professional installation company should carry out this work, and they should carry out a thorough survey before commencing work to ensure that this type of insulation is suitable for this home. They should also provide a guarantee for the work and handle any building control issues. Further information about cavity wall insulation and details of local installers can be obtained from the National Insulation Association (www.nationalinsulationassociation.org.uk).");
		e.setHeading("Cavity wall insulation");
		e.setSortOder(3);
		attrs.add(e);

		e = new PropertyMeasure();
		e.setCategoryId(3);
		e.setDescription(
				"Modern storage heaters are smaller and easier to control than the older type in the property. Ask for a quotation for new, fan-assisted heaters with automatic charge control. A dual-immersion cylinder, which can be installed at the same time, will provide cheaper hot water than the system currently installed. As installations should be in accordance with the current regulations covering electrical wiring, only a qualified electrician should carry out the installation. Building Regulations apply to this work, so your local authority building control department should be informed, unless the installer is registered with a competent persons scheme1, and can therefore self-certify the work for Building Regulation compliance. Ask a qualified electrical heating engineer to explain the options, which might also include switching to other forms of electric heating.");
		e.setHeading("Fan assisted storage heaters");
		e.setSortOder(1);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(3);
		e.setDescription(
				"A wind turbine provides electricity from wind energy. This electricity is used throughout the home in the same way as the electricity purchased from an energy supplier. The British Wind Energy Association has up-to-date information on suppliers of small-scale wind systems and any grant that may be available. Planning restrictions may apply and you should check this with the local authority. Building Regulations apply to this work, so your local authority building control department should be informed, unless the installer is appropriately qualified and registered as such with a competent persons scheme1, and can therefore self-certify the work for Building Regulation compliance. Wind turbines are not suitable for all properties. The system's effectiveness depends on local wind speeds and the presence of nearby obstructions, and a site survey should be undertaken by an accredited installer.");
		e.setHeading("Wind turbine");
		e.setSortOder(2);
		attrs.add(e);
		e = new PropertyMeasure();
		e.setCategoryId(3);
		e.setDescription(
				"A solar PV system is one which converts light directly into electricity via panels placed on the roof with no waste and no emissions. This electricity is used throughout the home in the same way as the electricity purchased from an energy supplier. The British Photovoltaic Association has up-to-date information on local installers who are qualified electricians and on any grant that may be available. Planning restrictions may apply in certain neighbourhoods and you should check this with the local authority. Building Regulations apply to this work, so your local authority building control department should be informed, unless the installer is appropriately qualified and registered as such with a competent persons scheme1, and can therefore self-certify the work for Building Regulation compliance.");
		e.setHeading("Solar photovoltaic (PV) panels");
		e.setSortOder(3);
		attrs.add(e);
		return attrs;
	}
}
