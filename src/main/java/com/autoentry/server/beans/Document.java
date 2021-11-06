package com.autoentry.server.beans;

import java.io.Serializable;
import java.util.List;

public class Document implements Serializable
{
	static final long serialVersionUID = 1L;
	private String sourcePath;
	private String resultPath;
	private String projectId;
	private String uploadBucketName;
	private String gcsSrcPath;
	private String gcsDestPath;
	private boolean isTemplateCopy;
	private Integer height;
	private Integer width;
	private float heightDiv = 1;
	private float widthDiv = 1;
	private double EPS = 3.0;
	private List<String> labels;

	public Document(String srcPath, String outPath, String projectId, String uploadBucketName, String gcsSrcPath, String gcsDestPath,
			boolean isTemplate)
	{
		this.sourcePath = srcPath;
		this.resultPath = outPath;
		this.projectId = projectId;
		this.uploadBucketName = uploadBucketName;
		this.gcsSrcPath = gcsSrcPath;
		this.gcsDestPath = gcsDestPath;
		this.isTemplateCopy = isTemplate;
	}

	public Document(String sourcePath, String resultPath, String projectId, String uploadBucketName, String gcsSrcPath, String gcsDestPath,
			boolean isTemplateCopy, Integer height, Integer width, float heightDiv, float widthDiv)
	{
		super();

		this.sourcePath = sourcePath;
		this.resultPath = resultPath;
		this.projectId = projectId;
		this.uploadBucketName = uploadBucketName;
		this.gcsSrcPath = gcsSrcPath;
		this.gcsDestPath = gcsDestPath;
		this.isTemplateCopy = isTemplateCopy;
		this.height = height;
		this.width = width;
		this.heightDiv = heightDiv;
		this.widthDiv = widthDiv;
	}

	public String getSourcePath()
	{
		return sourcePath;
	}

	public void setSourcePath(String sourcePath)
	{
		this.sourcePath = sourcePath;
	}

	public String getResultPath()
	{
		return resultPath;
	}

	public void setResultPath(String resultPath)
	{
		this.resultPath = resultPath;
	}

	public String getProjectId()
	{
		return projectId;
	}

	public void setProjectId(String projectId)
	{
		this.projectId = projectId;
	}

	public String getUploadBucketName()
	{
		return uploadBucketName;
	}

	public void setUploadBucketName(String uploadBucketName)
	{
		this.uploadBucketName = uploadBucketName;
	}

	public String getGcsSrcPath()
	{
		return gcsSrcPath;
	}

	public void setGcsSrcPath(String gcsSrcPath)
	{
		this.gcsSrcPath = gcsSrcPath;
	}

	public String getGcsDestPath()
	{
		return gcsDestPath;
	}

	public void setGcsDestPath(String gcsDestPath)
	{
		this.gcsDestPath = gcsDestPath;
	}

	public boolean isTemplateCopy()
	{
		return isTemplateCopy;
	}

	public void setTemplateCopy(boolean isTemplateCopy)
	{
		this.isTemplateCopy = isTemplateCopy;
	}

	public Integer getHeight()
	{
		return height;
	}

	public void setHeight(Integer height)
	{
		this.height = height;
	}

	public Integer getWidth()
	{
		return width;
	}

	public void setWidth(Integer width)
	{
		this.width = width;
	}

	public float getHeightDiv()
	{
		return heightDiv;
	}

	public void setHeightDiv(float heightDiv)
	{
		this.heightDiv = heightDiv;
	}

	public float getWidthDiv()
	{
		return widthDiv;
	}

	public void setWidthDiv(float widthDiv)
	{
		this.widthDiv = widthDiv;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gcsDestPath == null) ? 0 : gcsDestPath.hashCode());
		result = prime * result + ((gcsSrcPath == null) ? 0 : gcsSrcPath.hashCode());
		result = prime * result + ((height == null) ? 0 : height.hashCode());
		result = prime * result + Float.floatToIntBits(heightDiv);
		result = prime * result + (isTemplateCopy ? 1231 : 1237);
		result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
		result = prime * result + ((resultPath == null) ? 0 : resultPath.hashCode());
		result = prime * result + ((sourcePath == null) ? 0 : sourcePath.hashCode());
		result = prime * result + ((uploadBucketName == null) ? 0 : uploadBucketName.hashCode());
		result = prime * result + ((width == null) ? 0 : width.hashCode());
		result = prime * result + Float.floatToIntBits(widthDiv);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		if (gcsDestPath == null)
		{
			if (other.gcsDestPath != null)
				return false;
		}
		else if (!gcsDestPath.equals(other.gcsDestPath))
			return false;
		if (gcsSrcPath == null)
		{
			if (other.gcsSrcPath != null)
				return false;
		}
		else if (!gcsSrcPath.equals(other.gcsSrcPath))
			return false;
		if (height == null)
		{
			if (other.height != null)
				return false;
		}
		else if (!height.equals(other.height))
			return false;
		if (Float.floatToIntBits(heightDiv) != Float.floatToIntBits(other.heightDiv))
			return false;
		if (isTemplateCopy != other.isTemplateCopy)
			return false;
		if (projectId == null)
		{
			if (other.projectId != null)
				return false;
		}
		else if (!projectId.equals(other.projectId))
			return false;
		if (resultPath == null)
		{
			if (other.resultPath != null)
				return false;
		}
		else if (!resultPath.equals(other.resultPath))
			return false;
		if (sourcePath == null)
		{
			if (other.sourcePath != null)
				return false;
		}
		else if (!sourcePath.equals(other.sourcePath))
			return false;
		if (uploadBucketName == null)
		{
			if (other.uploadBucketName != null)
				return false;
		}
		else if (!uploadBucketName.equals(other.uploadBucketName))
			return false;
		if (width == null)
		{
			if (other.width != null)
				return false;
		}
		else if (!width.equals(other.width))
			return false;
		if (Float.floatToIntBits(widthDiv) != Float.floatToIntBits(other.widthDiv))
			return false;
		return true;
	}

	public double getEPS()
	{
		return EPS;
	}

	public void setEPS(double ePS)
	{
		EPS = ePS;
	}

	public List<String> getLabels()
	{
		return labels;
	}

	public void setLabels(List<String> labels)
	{
		this.labels = labels;
	}
}
