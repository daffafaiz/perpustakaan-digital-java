package com.perpustakaan.digital.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Entitas transaksi peminjaman buku oleh anggota.
 *
 * <p>Menghubungkan {@link Buku} dan {@link Anggota} (relasi ManyToOne) serta
 * menyimpan status, tanggal, dan denda keterlambatan.</p>
 */
@Entity
@Table(name = "peminjaman")
public class Peminjaman {

    /** Denda per hari keterlambatan (Rupiah). */
    public static final long DENDA_PER_HARI = 1000L;

    /** Lama peminjaman default (hari). */
    public static final int LAMA_PINJAM_HARI = 7;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "buku_id", nullable = false)
    private Buku buku;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "anggota_id", nullable = false)
    private Anggota anggota;

    @Column(name = "tanggal_pinjam", nullable = false)
    private LocalDate tanggalPinjam;

    @Column(name = "tanggal_jatuh_tempo", nullable = false)
    private LocalDate tanggalJatuhTempo;

    @Column(name = "tanggal_kembali")
    private LocalDate tanggalKembali;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPeminjaman status = StatusPeminjaman.DIPINJAM;

    @Column(nullable = false)
    private Long denda = 0L;

    public Peminjaman() {
    }

    public Peminjaman(Buku buku, Anggota anggota) {
        this.buku = buku;
        this.anggota = anggota;
        this.tanggalPinjam = LocalDate.now();
        this.tanggalJatuhTempo = this.tanggalPinjam.plusDays(LAMA_PINJAM_HARI);
        this.status = StatusPeminjaman.DIPINJAM;
    }

    /**
     * Proses pengembalian: tandai tanggal kembali, hitung denda keterlambatan,
     * dan ubah status menjadi DIKEMBALIKAN.
     */
    public void kembalikan(LocalDate tanggal) {
        this.tanggalKembali = tanggal;
        this.status = StatusPeminjaman.DIKEMBALIKAN;
        long hariTerlambat = hariKeterlambatan(tanggal);
        this.denda = hariTerlambat > 0 ? hariTerlambat * DENDA_PER_HARI : 0L;
    }

    /** Jumlah hari keterlambatan terhadap suatu tanggal acuan. */
    public long hariKeterlambatan(LocalDate acuan) {
        LocalDate pembanding = acuan != null ? acuan : LocalDate.now();
        if (status == StatusPeminjaman.DIKEMBALIKAN && tanggalKembali != null) {
            pembanding = tanggalKembali;
        }
        if (pembanding.isAfter(tanggalJatuhTempo)) {
            return ChronoUnit.DAYS.between(tanggalJatuhTempo, pembanding);
        }
        return 0;
    }

    /** Apakah peminjaman ini sedang terlambat (belum dikembalikan & lewat jatuh tempo). */
    public boolean isTerlambat() {
        return status == StatusPeminjaman.DIPINJAM
                && LocalDate.now().isAfter(tanggalJatuhTempo);
    }

    // --------------------------- Encapsulation ---------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Buku getBuku() {
        return buku;
    }

    public void setBuku(Buku buku) {
        this.buku = buku;
    }

    public Anggota getAnggota() {
        return anggota;
    }

    public void setAnggota(Anggota anggota) {
        this.anggota = anggota;
    }

    public LocalDate getTanggalPinjam() {
        return tanggalPinjam;
    }

    public void setTanggalPinjam(LocalDate tanggalPinjam) {
        this.tanggalPinjam = tanggalPinjam;
    }

    public LocalDate getTanggalJatuhTempo() {
        return tanggalJatuhTempo;
    }

    public void setTanggalJatuhTempo(LocalDate tanggalJatuhTempo) {
        this.tanggalJatuhTempo = tanggalJatuhTempo;
    }

    public LocalDate getTanggalKembali() {
        return tanggalKembali;
    }

    public void setTanggalKembali(LocalDate tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }

    public StatusPeminjaman getStatus() {
        return status;
    }

    public void setStatus(StatusPeminjaman status) {
        this.status = status;
    }

    public Long getDenda() {
        return denda;
    }

    public void setDenda(Long denda) {
        this.denda = denda;
    }
}
