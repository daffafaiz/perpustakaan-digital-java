package com.perpustakaan.digital.repository;

import com.perpustakaan.digital.model.Anggota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnggotaRepository extends JpaRepository<Anggota, Long> {

    @Query("""
            SELECT a FROM Anggota a
            WHERE LOWER(a.nama) LIKE LOWER(CONCAT('%', :kata, '%'))
               OR LOWER(a.email) LIKE LOWER(CONCAT('%', :kata, '%'))
               OR LOWER(a.kodeAnggota) LIKE LOWER(CONCAT('%', :kata, '%'))
            ORDER BY a.nama ASC
            """)
    List<Anggota> cari(String kata);

    List<Anggota> findAllByOrderByNamaAsc();

    boolean existsByKodeAnggota(String kodeAnggota);
}
