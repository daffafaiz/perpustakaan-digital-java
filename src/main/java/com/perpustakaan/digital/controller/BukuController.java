package com.perpustakaan.digital.controller;

import com.perpustakaan.digital.model.Buku;
import com.perpustakaan.digital.service.BukuService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/buku")
public class BukuController {

    private final BukuService bukuService;

    public BukuController(BukuService bukuService) {
        this.bukuService = bukuService;
    }

    @GetMapping
    public String daftar(@RequestParam(value = "q", required = false) String q, Model model) {
        model.addAttribute("page", "buku");
        model.addAttribute("daftarBuku", bukuService.cari(q));
        model.addAttribute("q", q);
        return "buku/daftar";
    }

    @GetMapping("/tambah")
    public String formTambah(Model model) {
        model.addAttribute("page", "buku");
        model.addAttribute("buku", new Buku());
        model.addAttribute("mode", "tambah");
        return "buku/form";
    }

    @PostMapping("/simpan")
    public String simpan(@Valid @ModelAttribute("buku") Buku buku,
                         BindingResult result, Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("page", "buku");
            model.addAttribute("mode", "tambah");
            return "buku/form";
        }
        bukuService.simpan(buku);
        ra.addFlashAttribute("sukses", "Buku \"" + buku.getJudul() + "\" berhasil ditambahkan.");
        return "redirect:/buku";
    }

    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Long id, Model model) {
        model.addAttribute("page", "buku");
        model.addAttribute("buku", bukuService.cariById(id));
        model.addAttribute("mode", "edit");
        return "buku/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("buku") Buku buku,
                         BindingResult result, Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("page", "buku");
            model.addAttribute("mode", "edit");
            return "buku/form";
        }
        bukuService.perbarui(id, buku);
        ra.addFlashAttribute("sukses", "Buku berhasil diperbarui.");
        return "redirect:/buku";
    }

    @PostMapping("/hapus/{id}")
    public String hapus(@PathVariable Long id, RedirectAttributes ra) {
        bukuService.hapus(id);
        ra.addFlashAttribute("sukses", "Buku berhasil dihapus.");
        return "redirect:/buku";
    }
}
