// src/main/java/ut/edu/project_skincarebooking/controllers_for_api/AdminUserController.java
package ut.edu.project_skincarebooking.controllers_for_api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ut.edu.project_skincarebooking.enums.Role;
import ut.edu.project_skincarebooking.models.User;
import ut.edu.project_skincarebooking.repositories.ChuyenVienRepository;
import ut.edu.project_skincarebooking.repositories.UserRepository;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepo;
    private final ChuyenVienRepository chuyenVienRepo;

    // Danh sách
    @GetMapping
    public String list(Model model) {
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "admin/users/list";
    }

    // Form tạo
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "admin/users/form";
    }

    // Tạo
    @PostMapping("/new")
    public String create(@ModelAttribute("user") User form, Model model) {
        if (form.getUsername() == null || form.getUsername().isBlank()) {
            model.addAttribute("error", "Username không được trống");
            model.addAttribute("roles", Role.values());
            return "admin/users/form";
        }
        if (userRepo.existsByUsername(form.getUsername())) {
            model.addAttribute("error", "Username đã tồn tại");
            model.addAttribute("roles", Role.values());
            return "admin/users/form";
        }
        if (form.getPassword() == null || form.getPassword().isBlank()) {
            model.addAttribute("error", "Mật khẩu không được trống");
            model.addAttribute("roles", Role.values());
            return "admin/users/form";
        }

        if (form.getRole() == null) form.setRole(Role.USER);
        userRepo.save(form);
        return "redirect:/admin/users";
    }

    // Form sửa
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        User u = userRepo.findById(id).orElseThrow();
        model.addAttribute("user", u);
        model.addAttribute("roles", Role.values());
        return "admin/users/form";
    }

    // Cập nhật
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @ModelAttribute("user") User form,
                         Model model) {
        User u = userRepo.findById(id).orElseThrow();

        // check trùng username với người khác
        User dup = userRepo.findByUsername(form.getUsername());
        if (dup != null && !dup.getId().equals(id)) {
            model.addAttribute("error", "Username đã tồn tại");
            model.addAttribute("roles", Role.values());
            return "admin/users/form";
        }

        u.setUsername(form.getUsername());
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            u.setPassword(form.getPassword()); // nếu dùng PasswordEncoder, encode ở đây
        }
        u.setRole(form.getRole() == null ? Role.USER : form.getRole());

        // Nếu chuyển khỏi role DOCTOR thì xoá bản ghi ChuyenVien (tránh rác/vi phạm FK)
        if (u.getRole() != Role.DOCTOR) {
            chuyenVienRepo.findByUserId(u.getId()).ifPresent(chuyenVienRepo::delete);
        }

        userRepo.save(u);
        return "redirect:/admin/users";
    }

    // Xoá
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        chuyenVienRepo.findByUserId(id).ifPresent(chuyenVienRepo::delete);
        userRepo.deleteById(id);
        return "redirect:/admin/users";
    }
}
