package com.perpustakaan.digital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Kelas dasar (abstract) untuk seluruh koleksi perpustakaan.
 *
 * <p>Mendemonstrasikan prinsip OOP:</p>
 * <ul>
 *   <li><b>Encapsulation</b> — seluruh field bersifat {@code private} dan hanya
 *       dapat diakses melalui getter/setter.</li>
 *   <li><b>Inheritance</b> — menjadi induk dari {@link Buku} (lihat {@code extends Item}).</li>
 *   <li><b>Polymorphism</b> — mendefinisikan method abstrak {@link #getTipe()} dan
 *       {@link #getDeskripsiSingkat()} yang wajib di-override oleh setiap subclass.</li>
 * </ul>
 *
 * Menggunakan strategi pewarisan JPA SINGLE_TABLE: seluruh subclass disimpan pada
 * satu tabel dengan kolom pembeda {@code tipe_item}.
 */
@Entity
@Table(name = "item")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipe_item", discriminatorType = DiscriminatorType.STRING)
public abstract class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Judul tidak boleh kosong")
    @Size(max = 200, message = "Judul maksimal 200 karakter")
    @Column(nullable = false, length = 200)
    private String judul;

    @Column(name = "tahun_terbit")
    private Integer tahunTerbit;

    @Column(name = "dibuat_pada", updatable = false)
    private LocalDateTime dibuatPada;

    protected Item() {
        // wajib ada untuk JPA
    }

    protected Item(String judul, Integer tahunTerbit) {
        this.judul = judul;
        this.tahunTerbit = tahunTerbit;
    }

    @PrePersist
    protected void onCreate() {
        this.dibuatPada = LocalDateTime.now();
    }

    // ---------------------------------------------------------------------
    //  Method polymorphic: setiap subclass WAJIB memberi implementasi sendiri.
    // ---------------------------------------------------------------------

    /** Mengembalikan jenis item (mis. "Buku"). Di-override oleh subclass. */
    public abstract String getTipe();

    /** Mengembalikan deskripsi singkat yang spesifik untuk tiap jenis item. */
    public abstract String getDeskripsiSingkat();

    // --------------------------- Encapsulation ---------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public Integer getTahunTerbit() {
        return tahunTerbit;
    }

    public void setTahunTerbit(Integer tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }

    public LocalDateTime getDibuatPada() {
        return dibuatPada;
    }

    public void setDibuatPada(LocalDateTime dibuatPada) {
        this.dibuatPada = dibuatPada;
    }
}
