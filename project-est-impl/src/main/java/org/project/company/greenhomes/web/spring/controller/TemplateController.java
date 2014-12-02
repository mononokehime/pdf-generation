package org.project.company.greenhomes.web.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class TemplateController {

	/**
	 * the logger
	 */
	//private static final Logger log = LoggerFactory.getLogger(TemplateController.class);

	private String pdfTemplateDirectory;

	@RequestMapping(value = "/template/index", method = RequestMethod.GET)
	public void indexHandler () {
		// displays a lsit of the templates
	}

	@RequestMapping(value = "/template/template-display", method = RequestMethod.GET)
	public void templateDisplay (HttpServletResponse response, @RequestParam("template") String template)
			throws IOException {
		// displays a template
		// get the pdf

		File file = new File(pdfTemplateDirectory + template + ".pdf");
		//log.debug("getting from folder:"+name);
		FileInputStream inputStr = new FileInputStream(file);
		//InputStream inputFile = getClass().getResourceAsStream(name);
		//inputFile.
		BufferedInputStream input = new BufferedInputStream(inputStr);

		BufferedOutputStream output = null;
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=\"" + template + ".pdf\"");
		response.setContentLength((int)inputStr.getChannel().size());
		output = new BufferedOutputStream(response.getOutputStream());

		byte[] buffer = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = input.read(buffer, 0, buffer.length))) {
			output.write(buffer, 0, bytesRead);
		}
		if (null != input) {
			input.close();
		}
		if (null != output) {
			output.close();
		}

	}

	public void setPdfTemplateDirectory (String pdfTemplateDirectory) {
		this.pdfTemplateDirectory = pdfTemplateDirectory;
	}

}
