package com.perpustakaan.digital.service;

import com.perpustakaan.digital.exception.BusinessException;
import com.perpustakaan.digital.exception.ResourceNotFoundException;
import com.perpustakaan.digital.model.Anggota;
import com.perpustakaan.digital.model.StatusPeminjaman;
import com.perpustakaan.digital.repository.AnggotaRepository;
import com.perpustakaan.digital.repository.PeminjamanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class AnggotaService {

    private final AnggotaRepository anggotaRepository;
    private final PeminjamanRepository peminjamanRepository;

    public AnggotaService(AnggotaRepository anggotaRepository, PeminjamanRepository peminjamanRepository) {
        this.anggotaRepository = anggotaRepository;
        this.peminjamanRepository = peminjamanRepository;
    }

    @Transactional(readOnly = true)
    public List<Anggota> semua() {
        return anggotaRepository.findAllByOrderByNamaAsc();
    }

    @Transactional(readOnly = true)
    public List<Anggota> cari(String kata) {
        if (!StringUtils.hasText(kata)) {
            return semua();
        }
        return anggotaRepository.cari(kata.trim());
    }

    @Transactional(readOnly = true)
    public Anggota cariById(Long id) {
        return anggotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anggota dengan id " + id + " tidak ditemukan"));
    }

    public Anggota simpan(Anggota anggota) {
        if (!StringUtils.hasText(anggota.getKodeAnggota())) {
            anggota.setKodeAnggota(generateKode());
        }
        return anggotaRepository.save(anggota);
    }

    public Anggota perbarui(Long id, Anggota data) {
        Anggota anggota = cariById(id);
        anggota.setNama(data.getNama());
        anggota.setEmail(data.getEmail());
        anggota.setTelepon(data.getTelepon());
        anggota.setAlamat(data.getAlamat());
        return anggotaRepository.save(anggota);
    }

    public void hapus(Long id) {
        Anggota anggota = cariById(id);
        if (peminjamanRepository.existsByAnggotaIdAndStatus(id, StatusPeminjaman.DIPINJAM)) {
            throw new BusinessException("Anggota \"" + anggota.getNama()
                    + "\" tidak dapat dihapus karena masih memiliki pinjaman aktif.");
        }
        anggotaRepository.delete(anggota);
    }

    /** Menghasilkan kode anggota unik sederhana, mis. AGT-0007. */
    private String generateKode() {
        long next = anggotaRepository.count() + 1;
        String kode;
        do {
            kode = String.format("AGT-%04d", next++);
        } while (anggotaRepository.existsByKodeAnggota(kode));
        return kode;
    }
}
