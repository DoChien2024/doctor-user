// src/main/java/ut/edu/project_skincarebooking/controllers_for_api/AdminDoctorController.java
package ut.edu.project_skincarebooking.controllers_for_api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ut.edu.project_skincarebooking.enums.Role;
import ut.edu.project_skincarebooking.models.ChuyenVien;
import ut.edu.project_skincarebooking.models.User;
import ut.edu.project_skincarebooking.repositories.ChuyenVienRepository;
import ut.edu.project_skincarebooking.repositories.UserRepository;

import java.util.List;

@Controller
@RequestMapping("/admin/doctors")
@RequiredArgsConstructor
public class AdminDoctorController {

    private final UserRepository userRepo;
    private final ChuyenVienRepository chuyenVienRepo;

    // Danh sách bác sĩ (chuyên viên)
    @GetMapping
    public String list(Model model) {
        List<ChuyenVien> doctors = chuyenVienRepo.findAll();
        model.addAttribute("doctors", doctors);
        return "admin/doctors/list";
    }

    // Form tạo
    @GetMapping("/new")
    public String createForm() {
        return "admin/doctors/form";
    }

    // Tạo mới bác sĩ
    @PostMapping("/new")
    public String create(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String hoTen,
                         @RequestParam(required = false) String chuyenMon,
                         @RequestParam(required = false) String soDienThoai,
                         @RequestParam(required = false) String hinhAnh,
                         Model model) {

        if (username == null || username.isBlank()) {
            model.addAttribute("error", "Username không được trống");
            return "admin/doctors/form";
        }
        if (userRepo.existsByUsername(username)) {
            model.addAttribute("error", "Username đã tồn tại");
            return "admin/doctors/form";
        }
        if (password == null || password.isBlank()) {
            model.addAttribute("error", "Mật khẩu không được trống");
            return "admin/doctors/form";
        }
        if (hoTen == null || hoTen.isBlank()) {
            model.addAttribute("error", "Họ tên không được trống");
            return "admin/doctors/form";
        }

        // Tạo user role DOCTOR
        User u = new User();
        u.setUsername(username);
        u.setPassword(password); // encode nếu có PasswordEncoder
        u.setRole(Role.DOCTOR);
        u = userRepo.save(u);

        // Tạo chuyên viên gắn user
        ChuyenVien cv = new ChuyenVien();
        cv.setHoTen(hoTen);
        cv.setChuyenMon(chuyenMon);
        cv.setSoDienThoai(soDienThoai);
        cv.setHinhAnh(hinhAnh);
        cv.setUser(u);
        chuyenVienRepo.save(cv);

        return "redirect:/admin/doctors";
    }

    // Form sửa (id là id của ChuyenVien)
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ChuyenVien cv = chuyenVienRepo.findById(id).orElseThrow();
        model.addAttribute("cv", cv);
        return "admin/doctors/form";
    }

    // Cập nhật
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam String username,
                         @RequestParam(required = false) String password,
                         @RequestParam String hoTen,
                         @RequestParam(required = false) String chuyenMon,
                         @RequestParam(required = false) String soDienThoai,
                         @RequestParam(required = false) String hinhAnh,
                         Model model) {
        ChuyenVien cv = chuyenVienRepo.findById(id).orElseThrow();
        User u = cv.getUser();

        // Check username trùng với user khác
        User dup = userRepo.findByUsername(username);
        if (dup != null && !dup.getId().equals(u.getId())) {
            model.addAttribute("cv", cv);
            model.addAttribute("error", "Username đã tồn tại");
            return "admin/doctors/form";
        }

        u.setUsername(username);
        if (password != null && !password.isBlank()) {
            u.setPassword(password); // encode nếu có
        }
        u.setRole(Role.DOCTOR); // đảm bảo đúng role
        userRepo.save(u);

        cv.setHoTen(hoTen);
        cv.setChuyenMon(chuyenMon);
        cv.setSoDienThoai(soDienThoai);
        cv.setHinhAnh(hinhAnh);
        chuyenVienRepo.save(cv);

        return "redirect:/admin/doctors";
    }

    // Xoá (xoá ChuyenVien và User liên kết)
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        ChuyenVien cv = chuyenVienRepo.findById(id).orElseThrow();
        Long userId = cv.getUser().getId();
        chuyenVienRepo.delete(cv);
        userRepo.deleteById(userId);
        return "redirect:/admin/doctors";
    }
}
