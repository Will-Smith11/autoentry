package com.autoentry.server.interfaces;

import com.autoentry.server.beans.Document;

public interface BaseDocument {	
	
	public static final Document doc = new Document();
	String getSourcePath();
	String getResultPath();
	Object getUploadBucketName();
	Object getProjectId();
	String getGcsSrcPath();
	String getGcsDestPath();
	
	
}
