package com.perpustakaan.digital.service;

import com.perpustakaan.digital.model.*;
import com.perpustakaan.digital.repository.AnggotaRepository;
import com.perpustakaan.digital.repository.BukuRepository;
import com.perpustakaan.digital.repository.PeminjamanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Kelas <b>Perpustakaan</b> — entitas agregat yang merepresentasikan
 * perpustakaan secara keseluruhan dan menyediakan ringkasan statistik.
 *
 * <p>Kelas ini mengelola koleksi {@link Item} (yang diakses secara
 * <b>polimorfik</b> — lihat {@link #daftarKoleksi()}), {@link Anggota}, dan
 * {@link Peminjaman}, lalu menghitung statistik untuk dashboard.</p>
 */
@Service
public class Perpustakaan {

    private final String nama = "Perpustakaan Digital";

    private final BukuRepository bukuRepository;
    private final AnggotaRepository anggotaRepository;
    private final PeminjamanRepository peminjamanRepository;

    public Perpustakaan(BukuRepository bukuRepository,
                        AnggotaRepository anggotaRepository,
                        PeminjamanRepository peminjamanRepository) {
        this.bukuRepository = bukuRepository;
        this.anggotaRepository = anggotaRepository;
        this.peminjamanRepository = peminjamanRepository;
    }

    public String getNama() {
        return nama;
    }

    /**
     * Mengembalikan seluruh koleksi sebagai {@code List<Item>}. Karena
     * {@link Buku} adalah turunan {@link Item}, koleksi ini dapat diproses
     * secara polimorfik (memanggil {@code getTipe()} / {@code getDeskripsiSingkat()}
     * tanpa mengetahui tipe konkretnya).
     */
    @Transactional(readOnly = true)
    public List<Item> daftarKoleksi() {
        return bukuRepository.findAllByOrderByJudulAsc()
                .stream()
                .map(b -> (Item) b)   // upcasting ke tipe induk
                .collect(Collectors.toList());
    }

    /** Menghitung seluruh statistik perpustakaan. */
    @Transactional(readOnly = true)
    public Statistik hitungStatistik() {
        List<Buku> semuaBuku = bukuRepository.findAll();
        List<Peminjaman> semuaPeminjaman = peminjamanRepository.findAll();

        Statistik s = new Statistik();
        s.setJumlahBuku(semuaBuku.size());
        s.setJumlahEksemplar(semuaBuku.stream().mapToLong(Buku::getJumlahTotal).sum());
        s.setBukuTersedia(semuaBuku.stream().mapToLong(Buku::getJumlahTersedia).sum());
        s.setJumlahAnggota(anggotaRepository.count());
        s.setJumlahPeminjaman(semuaPeminjaman.size());
        s.setPeminjamanAktif(peminjamanRepository.countByStatus(StatusPeminjaman.DIPINJAM));
        s.setTotalDenda(semuaPeminjaman.stream().mapToLong(p -> p.getDenda() != null ? p.getDenda() : 0L).sum());
        s.setBukuPerKategori(hitungPerKategori(semuaBuku));
        s.setBukuTerlaris(hitungTerlaris(semuaPeminjaman));
        return s;
    }

    private Map<String, Long> hitungPerKategori(List<Buku> buku) {
        return buku.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getKategori() != null ? b.getKategori() : "Lainnya",
                        LinkedHashMap::new,
                        Collectors.counting()));
    }

    private Map<String, Long> hitungTerlaris(List<Peminjaman> peminjaman) {
        return peminjaman.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getBuku().getJudul(),
                        Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new));
    }
}
