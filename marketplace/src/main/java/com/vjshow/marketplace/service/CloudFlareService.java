package com.vjshow.marketplace.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Service
@RequiredArgsConstructor
public class CloudFlareService {

	private final S3Client s3Client;

	private final S3Presigner presigner;

	@Value("${storage.r2.bucket}")
	private String bucket;

	@Value("${storage.r2.public-url}")
	private String publicUrl;

	public String upload(MultipartFile file, String key) {
		try {
			PutObjectRequest request = PutObjectRequest.builder().bucket(bucket).key(key)
					.contentType(file.getContentType()).build();

			byte[] bytes = file.getBytes();

			s3Client.putObject(request, RequestBody.fromBytes(bytes));

			return publicUrl + "/" + key;

		} catch (Exception e) {
			throw new RuntimeException("Upload failed", e);
		}
	}

	public String generatePresignedUrl(String key) {
		PutObjectRequest request = PutObjectRequest.builder().bucket(bucket).key(key).build();

		PresignedPutObjectRequest presignedRequest = presigner
				.presignPutObject(r -> r.signatureDuration(Duration.ofMinutes(5)).putObjectRequest(request));

		return presignedRequest.url().toString();
	}
}
