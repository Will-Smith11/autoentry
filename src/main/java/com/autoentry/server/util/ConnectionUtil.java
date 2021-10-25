package com.autoentry.server.util;

import java.io.FileInputStream;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;

public class ConnectionUtil
{
	private static final String jsonPath = "C:\\Users\\Will\\git\\googleKey\\nodal-plexus-325621-b78c1f8fafde.json";

	static void authImplicit() throws Exception
	{
		// If you don't specify credentials when constructing the client, the client library will
		// look for credentials via the environment variable GOOGLE_APPLICATION_CREDENTIALS.
		//		StorageOptions storageOptions = StorageOptions.newBuilder()
		//				.setProjectId("nodal-plexus-325621")
		//				.setCredentials(
		//						GoogleCredentials.fromStream(new FileInputStream("C:\\Users\\Will\\git\\googleKey\\nodal-plexus-325621-b78c1f8fafde.json"))) //change to env path 
		//				.build();
		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
				.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
		Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

		System.out.println("Buckets:");
		Page<Bucket> buckets = storage.list();
		for (Bucket bucket : buckets.iterateAll())
		{
			System.out.println(bucket.toString());
		}
		System.out.println("completed auth");
	}
}
