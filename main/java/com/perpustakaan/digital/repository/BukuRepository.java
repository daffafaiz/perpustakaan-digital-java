package com.perpustakaan.digital.repository;

import com.perpustakaan.digital.model.Buku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BukuRepository extends JpaRepository<Buku, Long> {

    /** Pencarian buku berdasarkan judul, pengarang, atau kategori (case-insensitive). */
    @Query("""
            SELECT b FROM Buku b
            WHERE LOWER(b.judul) LIKE LOWER(CONCAT('%', :kata, '%'))
               OR LOWER(b.pengarang) LIKE LOWER(CONCAT('%', :kata, '%'))
               OR LOWER(b.kategori) LIKE LOWER(CONCAT('%', :kata, '%'))
            ORDER BY b.judul ASC
            """)
    List<Buku> cari(String kata);

    List<Buku> findAllByOrderByJudulAsc();

    long countByJumlahTersediaGreaterThan(int batas);

    boolean existsByIsbn(String isbn);
}
