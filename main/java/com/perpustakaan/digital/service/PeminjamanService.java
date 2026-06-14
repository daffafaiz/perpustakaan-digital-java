package com.perpustakaan.digital.service;

import com.perpustakaan.digital.exception.BusinessException;
import com.perpustakaan.digital.exception.ResourceNotFoundException;
import com.perpustakaan.digital.model.*;
import com.perpustakaan.digital.repository.BukuRepository;
import com.perpustakaan.digital.repository.PeminjamanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PeminjamanService {

    private final PeminjamanRepository peminjamanRepository;
    private final BukuRepository bukuRepository;
    private final BukuService bukuService;
    private final AnggotaService anggotaService;

    public PeminjamanService(PeminjamanRepository peminjamanRepository,
                             BukuRepository bukuRepository,
                             BukuService bukuService,
                             AnggotaService anggotaService) {
        this.peminjamanRepository = peminjamanRepository;
        this.bukuRepository = bukuRepository;
        this.bukuService = bukuService;
        this.anggotaService = anggotaService;
    }

    @Transactional(readOnly = true)
    public List<Peminjaman> semua() {
        return peminjamanRepository.findAllByOrderByTanggalPinjamDescIdDesc();
    }

    @Transactional(readOnly = true)
    public List<Peminjaman> aktif() {
        return peminjamanRepository.findByStatusOrderByTanggalPinjamDesc(StatusPeminjaman.DIPINJAM);
    }

    @Transactional(readOnly = true)
    public Peminjaman cariById(Long id) {
        return peminjamanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Peminjaman dengan id " + id + " tidak ditemukan"));
    }

    /** Buat transaksi peminjaman baru sekaligus mengurangi stok buku. */
    public Peminjaman pinjam(Long bukuId, Long anggotaId) {
        Buku buku = bukuService.cariById(bukuId);
        Anggota anggota = anggotaService.cariById(anggotaId);

        if (!buku.isTersedia()) {
            throw new BusinessException("Stok buku \"" + buku.getJudul() + "\" sedang habis.");
        }

        buku.kurangiStok();
        bukuRepository.save(buku);

        Peminjaman peminjaman = new Peminjaman(buku, anggota);
        return peminjamanRepository.save(peminjaman);
    }

    /** Proses pengembalian: hitung denda dan kembalikan stok. */
    public Peminjaman kembalikan(Long peminjamanId) {
        Peminjaman peminjaman = cariById(peminjamanId);
        if (peminjaman.getStatus() == StatusPeminjaman.DIKEMBALIKAN) {
            throw new BusinessException("Peminjaman ini sudah dikembalikan sebelumnya.");
        }

        peminjaman.kembalikan(LocalDate.now());

        Buku buku = peminjaman.getBuku();
        buku.tambahStok();
        bukuRepository.save(buku);

        return peminjamanRepository.save(peminjaman);
    }
}
