package com.autoentry.server;

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
		return new Document(sourcePathb, resultPathb, projectIdb, uploadBucketNameb, "gs://temp-upload-test/test",
				"gs://temp-download-test/test", true);
	}
}
