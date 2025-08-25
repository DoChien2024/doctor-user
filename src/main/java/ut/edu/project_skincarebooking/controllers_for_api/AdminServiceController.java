// src/main/java/ut/edu/project_skincarebooking/controllers_for_api/AdminServiceController.java
package ut.edu.project_skincarebooking.controllers_for_api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ut.edu.project_skincarebooking.repositories.ServiceRepository;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/services")
@RequiredArgsConstructor
public class AdminServiceController {

    private final ServiceRepository serviceRepo;

    // Danh sách
    @GetMapping
    public String list(Model model) {
        List<ut.edu.project_skincarebooking.models.Service> list = serviceRepo.findAll();
        model.addAttribute("services", list);
        return "admin/services/list";
    }

    // Form tạo
    @GetMapping("/new")
    public String createForm(Model model) {
        ut.edu.project_skincarebooking.models.Service s = new ut.edu.project_skincarebooking.models.Service();
        s.setStatus(true);
        model.addAttribute("service", s);
        return "admin/services/form";
    }

    // Tạo
    @PostMapping("/new")
    public String create(@ModelAttribute("service") ut.edu.project_skincarebooking.models.Service s,
                         Model model) {
        if (s.getName() == null || s.getName().isBlank()) {
            model.addAttribute("error", "Tên dịch vụ không được trống");
            return "admin/services/form";
        }
        if (s.getDescription() == null || s.getDescription().isBlank()) {
            model.addAttribute("error", "Mô tả không được trống");
            return "admin/services/form";
        }
        if (s.getPrice() == null) {
            model.addAttribute("error", "Giá không được trống");
            return "admin/services/form";
        }
        s.setCreatedAt(LocalDateTime.now().toString());
        s.setUpdatedAt(LocalDateTime.now().toString());
        serviceRepo.save(s);
        return "redirect:/admin/services";
    }

    // Form sửa
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ut.edu.project_skincarebooking.models.Service s = serviceRepo.findById(id).orElseThrow();
        model.addAttribute("service", s);
        return "admin/services/form";
    }

    // Cập nhật
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @ModelAttribute("service") ut.edu.project_skincarebooking.models.Service form,
                         Model model) {
        ut.edu.project_skincarebooking.models.Service s = serviceRepo.findById(id).orElseThrow();

        if (form.getName() == null || form.getName().isBlank()) {
            model.addAttribute("error", "Tên dịch vụ không được trống");
            model.addAttribute("service", s);
            return "admin/services/form";
        }
        if (form.getDescription() == null || form.getDescription().isBlank()) {
            model.addAttribute("error", "Mô tả không được trống");
            model.addAttribute("service", s);
            return "admin/services/form";
        }
        if (form.getPrice() == null) {
            model.addAttribute("error", "Giá không được trống");
            model.addAttribute("service", s);
            return "admin/services/form";
        }

        s.setName(form.getName());
        s.setDescription(form.getDescription());
        s.setPrice(form.getPrice());
        s.setImageUrl(form.getImageUrl());
        s.setDuration(form.getDuration());
        s.setBenefits(form.getBenefits());
        s.setSteps(form.getSteps());
        s.setCategory(form.getCategory());
        s.setRating(form.getRating());
        s.setStatus(form.getStatus());
        s.setUpdatedAt(LocalDateTime.now().toString());

        serviceRepo.save(s);
        return "redirect:/admin/services";
    }

    // Xoá
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        serviceRepo.deleteById(id);
        return "redirect:/admin/services";
    }
}
