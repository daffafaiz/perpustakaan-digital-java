package com.perpustakaan.digital.controller;

import com.perpustakaan.digital.exception.BusinessException;
import com.perpustakaan.digital.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;

/**
 * Penanganan error global untuk seluruh controller.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Pelanggaran aturan bisnis: kembali ke halaman sebelumnya dengan pesan error. */
    @ExceptionHandler(BusinessException.class)
    public String handleBusiness(BusinessException ex, HttpServletRequest request, RedirectAttributes ra) {
        ra.addFlashAttribute("error", ex.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (StringUtils.hasText(referer) ? referer : "/dashboard");
    }

    /** Data tidak ditemukan: tampilkan halaman error 404. */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("statusCode", 404);
        model.addAttribute("pesan", ex.getMessage());
        return "error/custom";
    }

    /** Error tak terduga lainnya. */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneral(Exception ex, Model model) {
        log.error("Terjadi kesalahan tak terduga", ex);
        model.addAttribute("statusCode", 500);
        model.addAttribute("pesan", "Terjadi kesalahan pada server. Silakan coba lagi.");
        return "error/custom";
    }
}
