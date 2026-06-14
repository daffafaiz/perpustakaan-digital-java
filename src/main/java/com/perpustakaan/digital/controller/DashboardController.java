package com.perpustakaan.digital.controller;

import com.perpustakaan.digital.service.Perpustakaan;
import com.perpustakaan.digital.service.PeminjamanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final Perpustakaan perpustakaan;
    private final PeminjamanService peminjamanService;

    public DashboardController(Perpustakaan perpustakaan, PeminjamanService peminjamanService) {
        this.perpustakaan = perpustakaan;
        this.peminjamanService = peminjamanService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("page", "dashboard");
        model.addAttribute("statistik", perpustakaan.hitungStatistik());
        model.addAttribute("peminjamanAktif", peminjamanService.aktif());
        return "dashboard";
    }
}
