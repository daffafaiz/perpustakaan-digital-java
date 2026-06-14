package com.perpustakaan.digital.controller;

import com.perpustakaan.digital.model.StatusPeminjaman;
import com.perpustakaan.digital.service.AnggotaService;
import com.perpustakaan.digital.service.BukuService;
import com.perpustakaan.digital.service.PeminjamanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/peminjaman")
public class PeminjamanController {

    private final PeminjamanService peminjamanService;
    private final BukuService bukuService;
    private final AnggotaService anggotaService;

    public PeminjamanController(PeminjamanService peminjamanService,
                                BukuService bukuService,
                                AnggotaService anggotaService) {
        this.peminjamanService = peminjamanService;
        this.bukuService = bukuService;
        this.anggotaService = anggotaService;
    }

    /** Daftar peminjaman aktif (sedang dipinjam). */
    @GetMapping
    public String daftar(Model model) {
        model.addAttribute("page", "peminjaman");
        model.addAttribute("daftarPeminjaman", peminjamanService.aktif());
        return "peminjaman/daftar";
    }

    /** Form peminjaman baru. */
    @GetMapping("/baru")
    public String formBaru(Model model) {
        model.addAttribute("page", "peminjaman");
        model.addAttribute("daftarBuku", bukuService.semua().stream().filter(b -> b.isTersedia()).toList());
        model.addAttribute("daftarAnggota", anggotaService.semua());
        return "peminjaman/form";
    }

    @PostMapping("/pinjam")
    public String pinjam(@RequestParam Long bukuId,
                         @RequestParam Long anggotaId,
                         RedirectAttributes ra) {
        peminjamanService.pinjam(bukuId, anggotaId);
        ra.addFlashAttribute("sukses", "Peminjaman berhasil dicatat.");
        return "redirect:/peminjaman";
    }

    @PostMapping("/kembalikan/{id}")
    public String kembalikan(@PathVariable Long id, RedirectAttributes ra) {
        var p = peminjamanService.kembalikan(id);
        String pesan = "Buku berhasil dikembalikan.";
        if (p.getDenda() != null && p.getDenda() > 0) {
            pesan += " Denda keterlambatan: Rp " + String.format("%,d", p.getDenda());
        }
        ra.addFlashAttribute("sukses", pesan);
        return "redirect:/peminjaman";
    }

    /** Riwayat seluruh peminjaman (termasuk yang sudah dikembalikan). */
    @GetMapping("/riwayat")
    public String riwayat(Model model) {
        model.addAttribute("page", "riwayat");
        model.addAttribute("daftarPeminjaman", peminjamanService.semua());
        model.addAttribute("STATUS_DIKEMBALIKAN", StatusPeminjaman.DIKEMBALIKAN);
        return "peminjaman/riwayat";
    }
}
