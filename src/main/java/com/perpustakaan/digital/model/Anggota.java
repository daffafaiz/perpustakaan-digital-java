package com.perpustakaan.digital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Entitas Anggota perpustakaan.
 *
 * <p>Mendemonstrasikan <b>Encapsulation</b>: seluruh field {@code private}
 * dengan akses via getter/setter.</p>
 */
@Entity
@Table(name = "anggota")
public class Anggota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kode_anggota", unique = true, length = 20)
    private String kodeAnggota;

    @NotBlank(message = "Nama tidak boleh kosong")
    @Size(max = 120, message = "Nama maksimal 120 karakter")
    @Column(nullable = false, length = 120)
    private String nama;

    @Email(message = "Format email tidak valid")
    @Column(length = 120)
    private String email;

    @Size(max = 20, message = "Nomor telepon maksimal 20 karakter")
    @Column(length = 20)
    private String telepon;

    @Column(length = 255)
    private String alamat;

    @Column(name = "tanggal_daftar")
    private LocalDate tanggalDaftar;

    public Anggota() {
    }

    public Anggota(String nama, String email, String telepon) {
        this.nama = nama;
        this.email = email;
        this.telepon = telepon;
    }

    @PrePersist
    protected void onCreate() {
        if (tanggalDaftar == null) {
            tanggalDaftar = LocalDate.now();
        }
    }

    // --------------------------- Encapsulation ---------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKodeAnggota() {
        return kodeAnggota;
    }

    public void setKodeAnggota(String kodeAnggota) {
        this.kodeAnggota = kodeAnggota;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public LocalDate getTanggalDaftar() {
        return tanggalDaftar;
    }

    public void setTanggalDaftar(LocalDate tanggalDaftar) {
        this.tanggalDaftar = tanggalDaftar;
    }
}
