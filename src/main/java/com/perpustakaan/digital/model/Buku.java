package com.perpustakaan.digital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Entitas Buku — turunan dari {@link Item}.
 *
 * <p>Mendemonstrasikan <b>Inheritance</b> ({@code extends Item}) dan
 * <b>Polymorphism</b> dengan meng-override {@link #getTipe()} dan
 * {@link #getDeskripsiSingkat()}.</p>
 */
@Entity
@DiscriminatorValue("BUKU")
public class Buku extends Item {

    @NotBlank(message = "Pengarang tidak boleh kosong")
    @Column(length = 150)
    private String pengarang;

    @Column(length = 150)
    private String penerbit;

    @Pattern(regexp = "^$|^[0-9Xx\\-]{10,17}$", message = "Format ISBN tidak valid")
    @Column(length = 20, unique = true)
    private String isbn;

    @NotBlank(message = "Kategori tidak boleh kosong")
    @Column(length = 80)
    private String kategori;

    @NotNull(message = "Jumlah stok wajib diisi")
    @Min(value = 0, message = "Stok tidak boleh negatif")
    @Column(name = "jumlah_total", nullable = false)
    private Integer jumlahTotal = 0;

    @NotNull
    @Min(value = 0, message = "Stok tersedia tidak boleh negatif")
    @Column(name = "jumlah_tersedia", nullable = false)
    private Integer jumlahTersedia = 0;

    public Buku() {
        super();
    }

    public Buku(String judul, String pengarang, String kategori, Integer tahunTerbit, Integer jumlahTotal) {
        super(judul, tahunTerbit);
        this.pengarang = pengarang;
        this.kategori = kategori;
        this.jumlahTotal = jumlahTotal;
        this.jumlahTersedia = jumlahTotal;
    }

    // ---------------------- Override (Polymorphism) ----------------------

    @Override
    public String getTipe() {
        return "Buku";
    }

    @Override
    public String getDeskripsiSingkat() {
        return getJudul() + " oleh " + (pengarang != null ? pengarang : "Anonim")
                + " (" + (getTahunTerbit() != null ? getTahunTerbit() : "-") + ")";
    }

    // --------------------------- Domain logic ----------------------------

    /** Apakah masih ada eksemplar yang bisa dipinjam. */
    public boolean isTersedia() {
        return jumlahTersedia != null && jumlahTersedia > 0;
    }

    /** Kurangi stok tersedia saat dipinjam. */
    public void kurangiStok() {
        if (!isTersedia()) {
            throw new IllegalStateException("Stok buku \"" + getJudul() + "\" habis");
        }
        this.jumlahTersedia--;
    }

    /** Tambah stok tersedia saat dikembalikan. */
    public void tambahStok() {
        if (jumlahTersedia < jumlahTotal) {
            this.jumlahTersedia++;
        }
    }

    // --------------------------- Encapsulation ---------------------------

    public String getPengarang() {
        return pengarang;
    }

    public void setPengarang(String pengarang) {
        this.pengarang = pengarang;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public Integer getJumlahTotal() {
        return jumlahTotal;
    }

    public void setJumlahTotal(Integer jumlahTotal) {
        this.jumlahTotal = jumlahTotal;
    }

    public Integer getJumlahTersedia() {
        return jumlahTersedia;
    }

    public void setJumlahTersedia(Integer jumlahTersedia) {
        this.jumlahTersedia = jumlahTersedia;
    }
}
