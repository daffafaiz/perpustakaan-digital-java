package com.perpustakaan.digital.exception;

/** Dilempar ketika aturan bisnis dilanggar (mis. stok habis, masih ada pinjaman aktif). */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
