package org.project.company.greenhomes.service.pdf.templates;

import com.lowagie.text.DocumentException;
import org.project.company.greenhomes.domain.entity.PropertyAddress;
import org.project.company.greenhomes.domain.entity.Schedule;
import org.project.company.greenhomes.exception.ApplicationException;
import org.project.company.greenhomes.exception.InvalidDataException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public interface PdfLetterTemplate {

	/**
	 * Method to generate the pdf.
	 *
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 * @throws ApplicationException
	 * @throws InvalidDataException
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	void generate (final PropertyAddress address, final Schedule schedule)
			throws FileNotFoundException, DocumentException, MalformedURLException, IOException, InvalidDataException,
			ApplicationException;

	ByteArrayOutputStream generateInMemory (final PropertyAddress address, final Schedule schedule)
			throws DocumentException, MalformedURLException, IOException, InvalidDataException, ApplicationException;
	//	PdfWriter getWriter(Document document, PropertyAddress address)throws FileNotFoundException, DocumentException;
	//	Document getDocument();
	//	void addHeaderInfo(Schedule schedule,Document document,PdfWriter writer, PropertyAddress address) throws BadElementException, MalformedURLException, IOException, DocumentException, InvalidDataException, ApplicationException;
	//	void addLetterBody(Document document, PropertyAddress address)throws MalformedURLException, IOException, DocumentException, InvalidDataException, ApplicationException;
	//	void addLogo(PdfWriter writer, Document document) throws DocumentException, MalformedURLException, IOException, ApplicationException;
	//	void addEstimatedGraphs(PdfWriter writer, Document document, PropertyAddress address)throws DocumentException, IOException,InvalidDataException,ApplicationException;
	//	PdfWriter getInMemoryWriter(Document document, ByteArrayOutputStream bao) throws DocumentException;

}
