package com.jai.HireBridge.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jai.HireBridge.exception.BusinessRuleException;
import com.jai.HireBridge.exception.ResourceNotFoundException;
import com.jai.HireBridge.repositories.StudentRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
public class ResumeStorageService {

	private static final long MAX_SIZE_BYTES = 2L * 1024 * 1024; // 2 MB
	private static final String PDF_CONTENT_TYPE = "application/pdf";
	private static final String PDF_HEADER = "%PDF-";
    
	@Value("${app.s3.bucket}")
	private String bucket; 
	
	private final S3Client s3Client;
	private final S3Presigner presigner;

	public ResumeStorageService(S3Client s3Client, S3Presigner presigner) {
		this.s3Client = s3Client;
		this.presigner = presigner;
	}

	public String upload(Long userId, MultipartFile file) {

		
		if (file == null || file.isEmpty()) {
			throw new BusinessRuleException("Please select a resume file");
		}
		if (file.getSize() > MAX_SIZE_BYTES) {
			throw new BusinessRuleException("Resume must be 2 MB or smaller");
		}
		if (!PDF_CONTENT_TYPE.equals(file.getContentType())) {
			throw new BusinessRuleException("Only PDF files are allowed");
		}
		String name = file.getOriginalFilename();
		if (name == null || !name.toLowerCase().endsWith(".pdf")) {
			throw new BusinessRuleException("File name must end with .pdf");
		}
		if (!startsWithPdfHeader(file)) {
			throw new BusinessRuleException("File is not a valid PDF");
		}

		
		String key = "resumes/" + userId + "/" + UUID.randomUUID() + ".pdf";

		
		try (InputStream in = file.getInputStream()) {
			PutObjectRequest request = PutObjectRequest.builder()
					.bucket(bucket)
					.key(key)
					.contentType(PDF_CONTENT_TYPE)
					.build();
			s3Client.putObject(request, RequestBody.fromInputStream(in, file.getSize()));
		} catch (IOException e) {
			throw new RuntimeException("Could not read the uploaded file", e);
		}

		return key;
	}

	public void delete(String key) {
		if (key == null || key.isBlank()) {
			return;
		}
		DeleteObjectRequest request = DeleteObjectRequest.builder()
				.bucket(bucket)
				.key(key)
				.build();
		s3Client.deleteObject(request);
	}

	
	private boolean startsWithPdfHeader(MultipartFile file) {
		try (InputStream in = file.getInputStream()) {
			byte[] head = in.readNBytes(PDF_HEADER.length());
			return new String(head, StandardCharsets.ISO_8859_1).equals(PDF_HEADER);
		} catch (IOException e) {
			return false;
		}
	}
	
	public String presignedUrl(String key, String fileName) {
		String safeName = (fileName == null ? "resume.pdf" : fileName)
				.replaceAll("[^A-Za-z0-9._ -]", "_");

		GetObjectRequest getRequest = GetObjectRequest.builder()
				.bucket(bucket)
				.key(key)
				.responseContentType("application/pdf")
				.responseContentDisposition("inline; filename=\"" + safeName + "\"")
				.build();

		GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
				.signatureDuration(Duration.ofMinutes(5))
				.getObjectRequest(getRequest)
				.build();

		return presigner.presignGetObject(presignRequest).url().toString();
	}
	
	public String copy(String sourceKey, Long userId) {
		String newKey = "resumes/" + userId + "/" + UUID.randomUUID() + ".pdf";
		CopyObjectRequest request = CopyObjectRequest.builder()
				.sourceBucket(bucket).sourceKey(sourceKey)
				.destinationBucket(bucket).destinationKey(newKey)
				.build();
		s3Client.copyObject(request);
		return newKey;
	}
}