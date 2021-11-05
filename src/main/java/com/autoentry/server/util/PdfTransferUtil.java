package com.autoentry.server.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class PdfTransferUtil
{
	public static void uploadObject(String projectId, String bucketName, String objectName, String filePath) throws IOException
	{
		Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
		BlobId blobId = BlobId.of(bucketName, objectName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

		System.out.println(
				"File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
	}

	public static void downloadObject(String projectId, String bucketName, String objectName, String destFilePath)
	{
		Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

		Blob blob = storage.get(BlobId.of(bucketName, objectName));
		blob.downloadTo(Paths.get(destFilePath));

		System.out.println(
				"Downloaded object "
						+ objectName
						+ " from bucket name "
						+ bucketName
						+ " to "
						+ destFilePath);
	}

	public static PDDocument getDoc(String projectId, String bucketName, String objectName)
	{
		Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
		Blob blob = storage.get(BlobId.of(bucketName, objectName));
		try
		{
			return PDDocument.load(blob.getContent());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;

	}
}
