package com.learning.backend.fun;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FunController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/fun")

    public String getFunPage() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        // Redirect to the static HTML file in resources/static
        return "redirect:/fun.html";
    }
}
