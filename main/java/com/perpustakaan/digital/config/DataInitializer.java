package com.perpustakaan.digital.config;

import com.perpustakaan.digital.model.Anggota;
import com.perpustakaan.digital.model.Buku;
import com.perpustakaan.digital.model.User;
import com.perpustakaan.digital.repository.AnggotaRepository;
import com.perpustakaan.digital.repository.BukuRepository;
import com.perpustakaan.digital.repository.UserRepository;
import com.perpustakaan.digital.service.PeminjamanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Membuat akun admin default dan data dummy secara otomatis saat aplikasi
 * pertama kali dijalankan (jika database masih kosong).
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final BukuRepository bukuRepository;
    private final AnggotaRepository anggotaRepository;
    private final PeminjamanService peminjamanService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           BukuRepository bukuRepository,
                           AnggotaRepository anggotaRepository,
                           PeminjamanService peminjamanService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bukuRepository = bukuRepository;
        this.anggotaRepository = anggotaRepository;
        this.peminjamanService = peminjamanService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        buatAdminDefault();
        buatDataDummy();
    }

    private void buatAdminDefault() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", passwordEncoder.encode("admin123"), "Administrator", "ADMIN");
            userRepository.save(admin);
            log.info("Akun admin default dibuat (username: admin / password: admin123)");
        }
    }

    private void buatDataDummy() {
        if (bukuRepository.count() > 0) {
            return; // data sudah ada, jangan duplikasi
        }

        List<Buku> bukuList = List.of(
                buku("Laskar Pelangi", "Andrea Hirata", "Bentang Pustaka", "9789793062792", "Novel", 2005, 5),
                buku("Bumi Manusia", "Pramoedya Ananta Toer", "Hasta Mitra", "9789799731240", "Novel", 1980, 4),
                buku("Filosofi Teras", "Henry Manampiring", "Kompas", "9786024125189", "Pengembangan Diri", 2018, 6),
                buku("Atomic Habits", "James Clear", "Gramedia", "9786020633176", "Pengembangan Diri", 2018, 8),
                buku("Algoritma & Pemrograman", "Rinaldi Munir", "Informatika", "9789791153270", "Teknologi", 2016, 3),
                buku("Clean Code", "Robert C. Martin", "Prentice Hall", "9780132350884", "Teknologi", 2008, 4),
                buku("Pemrograman Berorientasi Objek", "Budi Raharjo", "Informatika", "9786022392019", "Teknologi", 2019, 5),
                buku("Sapiens", "Yuval Noah Harari", "Pustaka Alvabet", "9786029193183", "Sejarah", 2011, 4),
                buku("Negeri 5 Menara", "Ahmad Fuadi", "Gramedia", "9789792248616", "Novel", 2009, 3),
                buku("Pulang", "Tere Liye", "Republika", "9786020822015", "Novel", 2015, 6)
        );
        bukuRepository.saveAll(bukuList);

        List<Anggota> anggotaList = List.of(
                anggota("AGT-0001", "Siti Nurhaliza", "siti@example.com", "081234567801", "Jl. Melati No. 1"),
                anggota("AGT-0002", "Budi Santoso", "budi@example.com", "081234567802", "Jl. Mawar No. 2"),
                anggota("AGT-0003", "Dewi Lestari", "dewi@example.com", "081234567803", "Jl. Anggrek No. 3"),
                anggota("AGT-0004", "Ahmad Rizki", "ahmad@example.com", "081234567804", "Jl. Kenanga No. 4"),
                anggota("AGT-0005", "Putri Maharani", "putri@example.com", "081234567805", "Jl. Dahlia No. 5")
        );
        anggotaRepository.saveAll(anggotaList);

        // Beberapa contoh transaksi peminjaman
        try {
            List<Buku> buku = bukuRepository.findAllByOrderByJudulAsc();
            List<Anggota> anggota = anggotaRepository.findAllByOrderByNamaAsc();
            peminjamanService.pinjam(buku.get(0).getId(), anggota.get(0).getId());
            peminjamanService.pinjam(buku.get(1).getId(), anggota.get(1).getId());
            peminjamanService.pinjam(buku.get(2).getId(), anggota.get(2).getId());
            // satu transaksi langsung dikembalikan untuk mengisi riwayat
            var p = peminjamanService.pinjam(buku.get(3).getId(), anggota.get(0).getId());
            peminjamanService.kembalikan(p.getId());
        } catch (Exception e) {
            log.warn("Gagal membuat peminjaman dummy: {}", e.getMessage());
        }

        log.info("Data dummy berhasil dibuat: {} buku, {} anggota",
                bukuRepository.count(), anggotaRepository.count());
    }

    private Buku buku(String judul, String pengarang, String penerbit, String isbn,
                      String kategori, int tahun, int stok) {
        Buku b = new Buku(judul, pengarang, kategori, tahun, stok);
        b.setPenerbit(penerbit);
        b.setIsbn(isbn);
        return b;
    }

    private Anggota anggota(String kode, String nama, String email, String telepon, String alamat) {
        Anggota a = new Anggota(nama, email, telepon);
        a.setKodeAnggota(kode);
        a.setAlamat(alamat);
        return a;
    }
}
