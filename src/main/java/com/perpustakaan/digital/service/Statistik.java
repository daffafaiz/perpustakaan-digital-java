package com.perpustakaan.digital.service;

import java.util.Map;

/**
 * Objek nilai (value object) yang merangkum statistik perpustakaan
 * untuk ditampilkan pada dashboard dan halaman statistik.
 */
public class Statistik {

    private long jumlahBuku;
    private long jumlahEksemplar;
    private long jumlahAnggota;
    private long jumlahPeminjaman;
    private long peminjamanAktif;
    private long bukuTersedia;
    private long totalDenda;
    private Map<String, Long> bukuPerKategori;
    private Map<String, Long> bukuTerlaris;

    public long getJumlahBuku() {
        return jumlahBuku;
    }

    public void setJumlahBuku(long jumlahBuku) {
        this.jumlahBuku = jumlahBuku;
    }

    public long getJumlahEksemplar() {
        return jumlahEksemplar;
    }

    public void setJumlahEksemplar(long jumlahEksemplar) {
        this.jumlahEksemplar = jumlahEksemplar;
    }

    public long getJumlahAnggota() {
        return jumlahAnggota;
    }

    public void setJumlahAnggota(long jumlahAnggota) {
        this.jumlahAnggota = jumlahAnggota;
    }

    public long getJumlahPeminjaman() {
        return jumlahPeminjaman;
    }

    public void setJumlahPeminjaman(long jumlahPeminjaman) {
        this.jumlahPeminjaman = jumlahPeminjaman;
    }

    public long getPeminjamanAktif() {
        return peminjamanAktif;
    }

    public void setPeminjamanAktif(long peminjamanAktif) {
        this.peminjamanAktif = peminjamanAktif;
    }

    public long getBukuTersedia() {
        return bukuTersedia;
    }

    public void setBukuTersedia(long bukuTersedia) {
        this.bukuTersedia = bukuTersedia;
    }

    public long getTotalDenda() {
        return totalDenda;
    }

    public void setTotalDenda(long totalDenda) {
        this.totalDenda = totalDenda;
    }

    public Map<String, Long> getBukuPerKategori() {
        return bukuPerKategori;
    }

    public void setBukuPerKategori(Map<String, Long> bukuPerKategori) {
        this.bukuPerKategori = bukuPerKategori;
    }

    public Map<String, Long> getBukuTerlaris() {
        return bukuTerlaris;
    }

    public void setBukuTerlaris(Map<String, Long> bukuTerlaris) {
        this.bukuTerlaris = bukuTerlaris;
    }
}
