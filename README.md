# 📚 Sistem Perpustakaan Digital

Aplikasi web manajemen perpustakaan modern berbasis **Java Spring Boot** + **Thymeleaf** + **Bootstrap 5**, dibuat sebagai tugas mata kuliah **Pemrograman Berorientasi Objek (PBO)**.

Tampilan ala aplikasi **SaaS** (sidebar, kartu dashboard, tabel data, **Dark/Light mode**, **responsive / mobile friendly**), dengan database **SQLite** untuk pengembangan lokal dan **PostgreSQL** saat di-deploy ke **Railway**.

---

## ✨ Fitur

| Fitur | Keterangan |
|-------|-----------|
| 🔐 **Login** | Autentikasi form dengan Spring Security (password ter-enkripsi BCrypt) |
| 📊 **Dashboard** | Jumlah Buku, Jumlah Anggota, Jumlah Peminjaman, Buku Tersedia |
| 📕 **Manajemen Buku** | Tambah, edit, hapus, dan lihat daftar buku |
| 🔎 **Pencarian Buku** | Cari berdasarkan judul, pengarang, atau kategori |
| 👥 **Manajemen Anggota** | CRUD data anggota (kode anggota dibuat otomatis) |
| 🔁 **Peminjaman Buku** | Catat peminjaman + stok berkurang otomatis |
| ↩️ **Pengembalian Buku** | Hitung denda keterlambatan + stok bertambah otomatis |
| 🕘 **Riwayat Peminjaman** | Seluruh transaksi termasuk yang sudah dikembalikan |
| 📈 **Statistik** | Buku per kategori & buku paling sering dipinjam |
| 🌗 **Dark / Light Mode** | Toggle tema, preferensi tersimpan di browser |

---

## 🧱 Penerapan OOP

| Prinsip | Penerapan dalam kode |
|---------|----------------------|
| **Encapsulation** | Seluruh field entitas bersifat `private` dan diakses lewat getter/setter (lihat semua kelas di `model/`) |
| **Inheritance** | `Buku extends Item` — `Item` adalah kelas abstrak induk dengan strategi JPA `SINGLE_TABLE` |
| **Polymorphism** | `Item` mendefinisikan method abstrak `getTipe()` & `getDeskripsiSingkat()` yang di-*override* oleh `Buku`. Koleksi diproses sebagai `List<Item>` di kelas `Perpustakaan` |

### Kelas wajib

| Kelas | Lokasi |
|-------|--------|
| `Item` (abstract) | `model/Item.java` |
| `Buku extends Item` | `model/Buku.java` |
| `Anggota` | `model/Anggota.java` |
| `Peminjaman` | `model/Peminjaman.java` |
| `Perpustakaan` | `service/Perpustakaan.java` (agregat + statistik) |

---

## 🛠️ Teknologi

- **Backend:** Java 21, Spring Boot 3.4 (Web, Data JPA, Security, Validation)
- **Frontend:** Thymeleaf, Bootstrap 5, Bootstrap Icons, JavaScript
- **Database:** SQLite (dev) · PostgreSQL (production / Railway)
- **Build:** Maven (disertakan Maven Wrapper, tidak perlu install Maven)
- **Deploy:** Docker / Railway

---

## 🚀 Menjalankan Secara Lokal

> Prasyarat: **JDK 17–21** terpasang. Maven **tidak perlu** diinstal (gunakan wrapper).

### Windows (PowerShell / CMD)
```bat
mvnw.cmd spring-boot:run
```

### Linux / macOS
```bash
./mvnw spring-boot:run
```

Lalu buka **http://localhost:8080**

> Database SQLite (`perpustakaan.db`) beserta **data dummy** dan **akun admin** dibuat otomatis saat pertama dijalankan.

### 🔑 Akun Default
```
Username : admin
Password : admin123
```

---

## ☁️ Deploy ke Railway

Aplikasi sudah dilengkapi `Dockerfile` dan `railway.json`, jadi siap deploy.

### Langkah-langkah

1. **Push project ke GitHub.**
2. Buka [railway.app](https://railway.app) → **New Project** → **Deploy from GitHub repo** → pilih repo ini.
3. Tambahkan database: **New** → **Database** → **Add PostgreSQL**.
4. Buka service aplikasi → tab **Variables**, tambahkan:

   | Variable | Value |
   |----------|-------|
   | `SPRING_PROFILES_ACTIVE` | `prod` |
   | `SPRING_DATASOURCE_URL` | `jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}` |
   | `SPRING_DATASOURCE_USERNAME` | `${{Postgres.PGUSER}}` |
   | `SPRING_DATASOURCE_PASSWORD` | `${{Postgres.PGPASSWORD}}` |

   > `${{Postgres.*}}` adalah *variable reference* Railway — ganti `Postgres` bila nama service database Anda berbeda.

5. Railway otomatis membangun `Dockerfile` dan menjalankan aplikasi.
6. Buka tab **Settings → Networking → Generate Domain** untuk mendapatkan **link publik**.
7. Tabel database & data dummy dibuat otomatis saat aplikasi pertama berjalan.

> Tidak perlu instalasi apa pun bagi dosen/teman — cukup buka link yang dihasilkan Railway.

---

## 📁 Struktur Project

```
perpustakaan/
├── Dockerfile                 # Build & runtime untuk Railway
├── railway.json               # Konfigurasi deploy Railway
├── pom.xml                    # Dependensi Maven
├── mvnw / mvnw.cmd            # Maven Wrapper (tanpa install Maven)
├── src/
│   ├── main/
│   │   ├── java/com/perpustakaan/digital/
│   │   │   ├── DigitalLibraryApplication.java
│   │   │   ├── config/        # SecurityConfig, DataInitializer
│   │   │   ├── controller/    # Auth, Dashboard, Buku, Anggota, Peminjaman, Statistik, ErrorHandler
│   │   │   ├── exception/     # BusinessException, ResourceNotFoundException
│   │   │   ├── model/         # Item, Buku, Anggota, Peminjaman, User, StatusPeminjaman
│   │   │   ├── repository/    # Spring Data JPA repositories
│   │   │   ├── security/      # AppUserDetailsService
│   │   │   └── service/       # BukuService, AnggotaService, PeminjamanService, Perpustakaan, Statistik
│   │   └── resources/
│   │       ├── application*.properties
│   │       ├── static/        # css/style.css, js/app.js
│   │       └── templates/     # Halaman Thymeleaf
│   └── test/                  # Unit test logika domain
└── README.md
```

---

## 🧮 Aturan Bisnis

- Lama peminjaman default: **7 hari**.
- Denda keterlambatan: **Rp 1.000 / hari**.
- Buku tidak dapat dihapus bila masih dipinjam.
- Anggota tidak dapat dihapus bila masih punya pinjaman aktif.
- Stok berkurang saat dipinjam dan bertambah saat dikembalikan.

---

## ✅ Error Handling & Validasi

- Validasi input form dengan **Jakarta Bean Validation** (`@NotBlank`, `@Email`, `@Min`, dll.).
- Penanganan error terpusat di `GlobalExceptionHandler` (`@ControllerAdvice`).
- Pesan sukses/error ditampilkan sebagai *flash message* di setiap halaman.
- Halaman error khusus (`error/custom.html`) untuk 404 / 500.

---

> Dibuat untuk keperluan tugas PBO. Selamat belajar! 🎓
