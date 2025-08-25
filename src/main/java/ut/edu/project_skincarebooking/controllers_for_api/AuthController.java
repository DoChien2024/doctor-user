package ut.edu.project_skincarebooking.controllers_for_api;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ut.edu.project_skincarebooking.enums.Role;
import ut.edu.project_skincarebooking.models.User;
import ut.edu.project_skincarebooking.repositories.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // ===== LOGIN =====
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        User user = userRepository.findByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
            return "login";
        }

        session.setAttribute("user", user);
        session.setAttribute("role", user.getRole()); // 👈 lưu role vào session
        return "redirect:/";
    }

    // ===== LOGOUT =====
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ===== REGISTER =====
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu không khớp!");
            return "register";
        }

        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "register";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // đơn giản theo yêu cầu
        newUser.setRole(Role.USER);    // 👈 mặc định USER
        userRepository.save(newUser);

        model.addAttribute("success", "Đăng ký thành công! Mời bạn đăng nhập.");
        return "login";
    }

    // ===== (TÙY CHỌN) Đặt role cho user - chỉ Admin dùng =====
    // Có thể tắt/không expose ở môi trường thật.
    @PostMapping("/admin/set-role")
    public String setRole(@RequestParam String username,
                          @RequestParam Role role,
                          HttpSession session,
                          Model model) {
        // kiểm tra đăng nhập & quyền admin
        User current = (User) session.getAttribute("user");
        if (current == null) return "redirect:/login";
        if (current.getRole() != Role.ADMIN) return "redirect:/access-denied";

        User u = userRepository.findByUsername(username);
        if (u == null) {
            model.addAttribute("error", "Không tìm thấy user: " + username);
            return "admin_user_role";
        }
        u.setRole(role);
        userRepository.save(u);
        model.addAttribute("success", "Đã cập nhật quyền cho " + username + " → " + role);
        return "admin_user_role";
    }
}
