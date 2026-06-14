package com.perpustakaan.digital.repository;

import com.perpustakaan.digital.model.Peminjaman;
import com.perpustakaan.digital.model.StatusPeminjaman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeminjamanRepository extends JpaRepository<Peminjaman, Long> {

    List<Peminjaman> findAllByOrderByTanggalPinjamDescIdDesc();

    List<Peminjaman> findByStatusOrderByTanggalPinjamDesc(StatusPeminjaman status);

    long countByStatus(StatusPeminjaman status);

    boolean existsByBukuIdAndStatus(Long bukuId, StatusPeminjaman status);

    boolean existsByAnggotaIdAndStatus(Long anggotaId, StatusPeminjaman status);
}
