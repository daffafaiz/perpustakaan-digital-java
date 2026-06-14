package com.perpustakaan.digital.service;

import com.perpustakaan.digital.exception.BusinessException;
import com.perpustakaan.digital.exception.ResourceNotFoundException;
import com.perpustakaan.digital.model.Buku;
import com.perpustakaan.digital.model.StatusPeminjaman;
import com.perpustakaan.digital.repository.BukuRepository;
import com.perpustakaan.digital.repository.PeminjamanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class BukuService {

    private final BukuRepository bukuRepository;
    private final PeminjamanRepository peminjamanRepository;

    public BukuService(BukuRepository bukuRepository, PeminjamanRepository peminjamanRepository) {
        this.bukuRepository = bukuRepository;
        this.peminjamanRepository = peminjamanRepository;
    }

    @Transactional(readOnly = true)
    public List<Buku> semua() {
        return bukuRepository.findAllByOrderByJudulAsc();
    }

    @Transactional(readOnly = true)
    public List<Buku> cari(String kata) {
        if (!StringUtils.hasText(kata)) {
            return semua();
        }
        return bukuRepository.cari(kata.trim());
    }

    @Transactional(readOnly = true)
    public Buku cariById(Long id) {
        return bukuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Buku dengan id " + id + " tidak ditemukan"));
    }

    public Buku simpan(Buku buku) {
        // Jaga konsistensi stok tersedia tidak melebihi total
        if (buku.getJumlahTersedia() == null || buku.getJumlahTersedia() > buku.getJumlahTotal()) {
            buku.setJumlahTersedia(buku.getJumlahTotal());
        }
        return bukuRepository.save(buku);
    }

    public Buku perbarui(Long id, Buku data) {
        Buku buku = cariById(id);
        int selisihTotal = data.getJumlahTotal() - buku.getJumlahTotal();
        buku.setJudul(data.getJudul());
        buku.setPengarang(data.getPengarang());
        buku.setPenerbit(data.getPenerbit());
        buku.setIsbn(data.getIsbn());
        buku.setKategori(data.getKategori());
        buku.setTahunTerbit(data.getTahunTerbit());
        buku.setJumlahTotal(data.getJumlahTotal());

        // Sesuaikan stok tersedia mengikuti perubahan total, tanpa membuatnya negatif
        int tersediaBaru = buku.getJumlahTersedia() + selisihTotal;
        buku.setJumlahTersedia(Math.max(0, Math.min(tersediaBaru, buku.getJumlahTotal())));
        return bukuRepository.save(buku);
    }

    public void hapus(Long id) {
        Buku buku = cariById(id);
        if (peminjamanRepository.existsByBukuIdAndStatus(id, StatusPeminjaman.DIPINJAM)) {
            throw new BusinessException("Buku \"" + buku.getJudul()
                    + "\" tidak dapat dihapus karena masih dipinjam.");
        }
        bukuRepository.delete(buku);
    }
}
