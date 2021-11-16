package com.autoentry.server;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.autoentry.server.beans.Document;

@Configuration
public class Config
{
	final static String sourcePathb = "C:\\Users\\Will\\git\\com.autoentry.server\\examples\\CD-00000067Docs202109101643.pdf";
	final static String sourcePathc = "C:\\Users\\Will\\git\\com.autoentry\\examples\\AL-00004546Docs202104301027.pdf";

	final static String resultPathb = "C:\\Users\\Will\\git\\com.autoentry\\results\\";
	final static String projectIdb = "nodal-plexus-325621";
	final static String uploadBucketNameb = "temp-upload-test";
	final static String downloadBucketName = "temp-download-test";

	@Bean
	@Scope("singleton")
	public Document document()
	{
		return multiPageDoc();
	}

	private Document multiPageDoc()
	{
		Document d = new Document(sourcePathc, resultPathb, projectIdb, uploadBucketNameb,
				"gs://temp-upload-test/" + sourcePathc.substring(sourcePathc.lastIndexOf("\\") + 1),
				"gs://temp-download-test/test/multipg", true);
		d.setDocUploadName(sourcePathc.substring(sourcePathc.lastIndexOf("\\") + 1));
		System.out.println(d.getDocUploadName());
		d.setLabels(Arrays.asList("Shippers Name and Address", "Shipper's Account Number", "Not Negotiable", "Air Waybill", "Issued by",
				"Conignee's Name and Address", "Consignee's Account Number", "Accounting Information", "Issuing Carrier's Agent Name and City",
				"Agent's IATA Code", "Account No.", "Airport of Departure (Addr. of First Carrier) and Requested Routing", "Reference Number", "To",
				"By First Carrier", "To", "By", "To", "By", "Currency", "Wt/Vol	PPD", "COLL", "Declared Value for Carrier",
				"Declared Value for Customs", "Air Port of Destination", "Flight/Date", "Flight/Date", "Amount of Insurance",
				"Handling Information", "SCI", "Executed on (date)", "at (place)", "Other Charges", "Signature of Shipper or his Agent"));
		return d;
	}

	private Document singlePageDoc()
	{
		Document d = new Document(sourcePathb, resultPathb, projectIdb, uploadBucketNameb,
				"gs://temp-upload-test/" + sourcePathb.substring(sourcePathb.lastIndexOf("\\") + 1),
				"gs://temp-download-test/singlepg/test", true);
		d.setDocUploadName(sourcePathb.substring(sourcePathb.lastIndexOf("\\") + 1));
		d.setLabels(Arrays.asList("Destination:", "Origin Station:", "Container:", "Master Loader:", "Air/Port of Loading:", "Pier/Terminal:",
				"Ocean B\\L - MAWB no:", "E.T.Arriv.:", "Air/Port of DIScharge:", "Vessel/Airline:", "Master HOBL:", "Departure Date:"));
		return d;
	}
}
