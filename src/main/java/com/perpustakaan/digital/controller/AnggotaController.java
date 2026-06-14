package com.perpustakaan.digital.controller;

import com.perpustakaan.digital.model.Anggota;
import com.perpustakaan.digital.service.AnggotaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/anggota")
public class AnggotaController {

    private final AnggotaService anggotaService;

    public AnggotaController(AnggotaService anggotaService) {
        this.anggotaService = anggotaService;
    }

    @GetMapping
    public String daftar(@RequestParam(value = "q", required = false) String q, Model model) {
        model.addAttribute("page", "anggota");
        model.addAttribute("daftarAnggota", anggotaService.cari(q));
        model.addAttribute("q", q);
        return "anggota/daftar";
    }

    @GetMapping("/tambah")
    public String formTambah(Model model) {
        model.addAttribute("page", "anggota");
        model.addAttribute("anggota", new Anggota());
        model.addAttribute("mode", "tambah");
        return "anggota/form";
    }

    @PostMapping("/simpan")
    public String simpan(@Valid @ModelAttribute("anggota") Anggota anggota,
                         BindingResult result, Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("page", "anggota");
            model.addAttribute("mode", "tambah");
            return "anggota/form";
        }
        anggotaService.simpan(anggota);
        ra.addFlashAttribute("sukses", "Anggota \"" + anggota.getNama() + "\" berhasil ditambahkan.");
        return "redirect:/anggota";
    }

    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Long id, Model model) {
        model.addAttribute("page", "anggota");
        model.addAttribute("anggota", anggotaService.cariById(id));
        model.addAttribute("mode", "edit");
        return "anggota/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("anggota") Anggota anggota,
                         BindingResult result, Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("page", "anggota");
            model.addAttribute("mode", "edit");
            return "anggota/form";
        }
        anggotaService.perbarui(id, anggota);
        ra.addFlashAttribute("sukses", "Data anggota berhasil diperbarui.");
        return "redirect:/anggota";
    }

    @PostMapping("/hapus/{id}")
    public String hapus(@PathVariable Long id, RedirectAttributes ra) {
        anggotaService.hapus(id);
        ra.addFlashAttribute("sukses", "Anggota berhasil dihapus.");
        return "redirect:/anggota";
    }
}
