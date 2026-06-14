package com.perpustakaan.digital.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pengujian unit untuk logika domain (tanpa konteks Spring).
 * Menguji encapsulation stok buku, polymorphism Item, dan perhitungan denda.
 */
class DomainLogicTest {

    @Test
    void bukuMengelolaStokDenganBenar() {
        Buku buku = new Buku("Test", "Penulis", "Umum", 2020, 2);
        assertEquals(2, buku.getJumlahTersedia());
        assertTrue(buku.isTersedia());

        buku.kurangiStok();
        assertEquals(1, buku.getJumlahTersedia());

        buku.kurangiStok();
        assertFalse(buku.isTersedia());

        // Stok habis -> harus melempar exception
        assertThrows(IllegalStateException.class, buku::kurangiStok);

        buku.tambahStok();
        assertEquals(1, buku.getJumlahTersedia());
    }

    @Test
    void bukuMengimplementasikanPolymorphismDariItem() {
        Item item = new Buku("Judul", "Penulis", "Novel", 2021, 1);
        assertEquals("Buku", item.getTipe());
        assertTrue(item.getDeskripsiSingkat().contains("Penulis"));
    }

    @Test
    void peminjamanMenghitungDendaKeterlambatan() {
        Buku buku = new Buku("Test", "Penulis", "Umum", 2020, 1);
        Anggota anggota = new Anggota("Budi", "budi@mail.com", "0812");
        Peminjaman peminjaman = new Peminjaman(buku, anggota);

        // Kembali 3 hari setelah jatuh tempo
        LocalDate terlambat = peminjaman.getTanggalJatuhTempo().plusDays(3);
        peminjaman.kembalikan(terlambat);

        assertEquals(StatusPeminjaman.DIKEMBALIKAN, peminjaman.getStatus());
        assertEquals(3 * Peminjaman.DENDA_PER_HARI, peminjaman.getDenda());
    }

    @Test
    void peminjamanTanpaKeterlambatanTidakKenaDenda() {
        Buku buku = new Buku("Test", "Penulis", "Umum", 2020, 1);
        Anggota anggota = new Anggota("Ani", "ani@mail.com", "0813");
        Peminjaman peminjaman = new Peminjaman(buku, anggota);

        peminjaman.kembalikan(peminjaman.getTanggalPinjam());
        assertEquals(0L, peminjaman.getDenda());
    }
}
