package com.perpustakaan.digital.exception;

/** Dilempar ketika data yang diminta tidak ditemukan. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
