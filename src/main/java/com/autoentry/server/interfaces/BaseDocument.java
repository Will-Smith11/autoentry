package com.autoentry.server.interfaces;

import com.autoentry.server.beans.Document;

public interface BaseDocument
{

	public static final Document doc = new Document();

	String getSourcePath();

	String getResultPath();

	String getUploadBucketName();

	String getProjectId();

	String getGcsSrcPath();

	String getGcsDestPath();

	void setHeight(int height);

	void setWidth(int width);

}
