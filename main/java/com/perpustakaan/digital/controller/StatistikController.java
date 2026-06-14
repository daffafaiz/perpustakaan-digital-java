package com.perpustakaan.digital.controller;

import com.perpustakaan.digital.service.Perpustakaan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistik")
public class StatistikController {

    private final Perpustakaan perpustakaan;

    public StatistikController(Perpustakaan perpustakaan) {
        this.perpustakaan = perpustakaan;
    }

    @GetMapping
    public String statistik(Model model) {
        model.addAttribute("page", "statistik");
        model.addAttribute("statistik", perpustakaan.hitungStatistik());
        return "statistik";
    }
}
