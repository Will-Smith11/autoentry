package com.autoentry.server;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.autoentry.server.beans.Document;

@Configuration
public class Config
{
	final static String sourcePathb = "/com.autoentry.server/src/main/resources/CD-00000067Docs202109101643.pdf";
	final static String resultPathb = "C:\\Users\\Will\\git\\com.autoentry\\results\\";
	final static String projectIdb = "nodal-plexus-325621";
	final static String uploadBucketNameb = "temp-upload-test";
	final static String downloadBucketName = "temp-download-test";

	@Bean
	public Document document()
	{
		Document d = new Document(sourcePathb, resultPathb, projectIdb, uploadBucketNameb, "gs://temp-upload-test/test",
				"gs://temp-download-test/test", true);
		d.setLabels(Arrays.asList("Destination:", "Origin Station:", "Container:", "Master Loader:", "Air/Port of Loading:", "Pier/Terminal:",
				"Ocean B\\L - MAWB no:", "E.T.Arriv.:", "Air/Port of DIScharge:", "Vessel/Airline:", "Master HOBL:", "Departure Date:"));
		return d;
	}
}
