package com.dpap.bookingapp.storage.files;

public record UploadFileResponse(String fileName, String fileDownloadUri, String fileType, long size) {
}