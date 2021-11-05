package com.autoentry.server.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autoentry.server.interfaces.BaseDocument;
import com.autoentry.server.service.DocumentOcrService;
import com.autoentry.server.util.ConnectionUtil;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.vision.v1.AnnotateFileResponse;
import com.google.cloud.vision.v1.AnnotateFileResponse.Builder;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.AsyncAnnotateFileRequest;
import com.google.cloud.vision.v1.AsyncAnnotateFileResponse;
import com.google.cloud.vision.v1.AsyncBatchAnnotateFilesResponse;
import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.GcsDestination;
import com.google.cloud.vision.v1.GcsSource;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.InputConfig;
import com.google.cloud.vision.v1.OperationMetadata;
import com.google.cloud.vision.v1.OutputConfig;
import com.google.cloud.vision.v1.Page;
import com.google.cloud.vision.v1.Paragraph;
import com.google.cloud.vision.v1.TextAnnotation;
import com.google.cloud.vision.v1.Word;
import com.google.protobuf.util.JsonFormat;

import io.reactivex.rxjava3.core.Completable;

@Service
public class PdfDocumentOcrServiceImpl implements DocumentOcrService
{
	@Autowired
	private BaseDocument doc;

	@Override
	public Completable run() throws Exception
	{
		return Completable.fromAction(() -> {
			//			runOcr(); //TODO dont make calls everytime (for testing)
			buildDoc();
		});
	}

	public void runOcr() throws Exception
	{
		ConnectionUtil.authImplicit();
		//		PdfTransferUtil.uploadObject(doc.getProjectId(), doc.getUploadBucketName(), "test", doc.getSourcePath());

		try (ImageAnnotatorClient client = ImageAnnotatorClient.create())
		{
			List<AsyncAnnotateFileRequest> requests = new ArrayList<>();

			// Set the GCS source path for the remote file.
			GcsSource gcsSource = GcsSource.newBuilder().setUri(doc.getGcsSrcPath()).build();

			// Create the configuration with the specified MIME (Multipurpose Internet Mail Extensions)
			// types
			InputConfig inputConfig = InputConfig.newBuilder()
					.setMimeType(
							"application/pdf") // Supported MimeTypes: "application/pdf", "image/tiff"
					.setGcsSource(gcsSource)
					.build();

			// Set the GCS destination path for where to save the results.
			GcsDestination gcsDestination = GcsDestination.newBuilder().setUri(doc.getGcsDestPath()).build();

			// Create the configuration for the System.output with the batch size.
			// The batch size sets how many pages should be grouped into each json System.output file.
			OutputConfig outputConfig = OutputConfig.newBuilder().setBatchSize(2).setGcsDestination(gcsDestination).build();

			// Select the Feature required by the vision API

			Feature feature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();

			// Build the OCR request
			AsyncAnnotateFileRequest request = AsyncAnnotateFileRequest.newBuilder()
					.addFeatures(feature)
					.setInputConfig(inputConfig)
					.setOutputConfig(outputConfig)
					.build();

			requests.add(request);

			// Perform the OCR request
			OperationFuture<AsyncBatchAnnotateFilesResponse, OperationMetadata> response = client.asyncBatchAnnotateFilesAsync(requests);

			System.out.println("Waiting for the operation to finish.");

			// Wait for the request to finish. (The result is not used, since the API saves the result to
			// the specified location on GCS.)
			List<AsyncAnnotateFileResponse> result = response.get(180, TimeUnit.SECONDS).getResponsesList();
			System.out.println("Completed");
		}
	}

	public void buildDoc() throws Exception
	{
		// Once the request has completed and the System.output has been
		// written to GCS, we can list all the System.output files.
		Storage storage = StorageOptions.getDefaultInstance().getService();

		// Get the destination location from the gcsDestinationPath
		Pattern pattern = Pattern.compile("gs://([^/]+)/(.+)");
		Matcher matcher = pattern.matcher(doc.getGcsDestPath());

		if (matcher.find())
		{
			String bucketName = matcher.group(1);
			String prefix = matcher.group(2);

			// Get the list of objects with the given prefix from the GCS bucket
			Bucket bucket = storage.get(bucketName);
			com.google.api.gax.paging.Page<Blob> pageList = bucket.list(BlobListOption.prefix(prefix));

			// List objects with the given prefix.
			System.out.println("Output files:");

			for (Blob blob : pageList.iterateAll())
			{
				System.out.println(blob.getName());

				String jsonContents = new String(blob.getContent());
				Builder builder = AnnotateFileResponse.newBuilder();
				JsonFormat.parser().merge(jsonContents, builder);
				AnnotateFileResponse annotateFileResponse = builder.build();
				//				AnnotateImageResponse annotateImageResponse = annotateFileResponse.getResponsesList();
				int pgNum = 0;
				for (AnnotateImageResponse res : annotateFileResponse.getResponsesList())
				{
					TextAnnotation annotation = res.getFullTextAnnotation();
					pgNum++;
					for (Page page : annotation.getPagesList())
					{
						doc.setHeight(page.getHeight());
						doc.setWidth(page.getWidth());
						//						doc.blocks = page.getBlocksList();

						//					String pageText = "";
						for (Block block : page.getBlocksList())
						{
							//						BoundingPoly poly = block.getBoundingBox();
							//						BlockVertices v = new BlockVertices(poly.getVerticesList(), 1);
							//						d.addBox(v);
							//						String blockText = "";

							//							doc.paragraphs = block.getParagraphsList();

							for (Paragraph para : block.getParagraphsList())
							{
								//							BoundingPoly poly = para.getBoundingBox();
								//							BlockVertices v = new BlockVertices(poly.getVerticesList(), 1);
								//							d.addBox(v);
								//								doc.words = para.getWordsList();
								for (Word word : para.getWordsList())
								{
									//									doc.smybols = word.getSymbolsList();
									//								BoundingPoly polyW = word.getBoundingBox();
									//								BlockVertices vW = new BlockVertices(polyW.getVerticesList(), 0);
									//								d.addBox(vW);

									//								for (Symbol symbol : word.getSymbolsList())
									//								{
									//									//																		BoundingPoly polyS = symbol.getBoundingBox();
									//									//																		BlockVertices vS = new BlockVertices(polyS.getVerticesList(), 1);
									//									//																		d.setSmybols(smybols);
									//								}
								}

							}

						}

					}

				}

			}
		}
		else
		{
			System.out.println("No MATCH");
		}
	}
}
