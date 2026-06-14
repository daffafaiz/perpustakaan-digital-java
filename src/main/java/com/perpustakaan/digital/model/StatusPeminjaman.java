package com.perpustakaan.digital.model;

/**
 * Status sebuah transaksi peminjaman.
 */
public enum StatusPeminjaman {
    DIPINJAM("Dipinjam"),
    DIKEMBALIKAN("Dikembalikan");

    private final String label;

    StatusPeminjaman(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
