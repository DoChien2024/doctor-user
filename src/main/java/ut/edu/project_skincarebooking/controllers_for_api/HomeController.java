package ut.edu.project_skincarebooking.controllers_for_api;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ut.edu.project_skincarebooking.models.User;
import ut.edu.project_skincarebooking.models.ChuyenVien;
import ut.edu.project_skincarebooking.models.Service;
import ut.edu.project_skincarebooking.repositories.ChuyenVienRepository;
import ut.edu.project_skincarebooking.repositories.ServiceRepository;

import java.util.List;

@Controller
public class HomeController {

    private final ChuyenVienRepository chuyenVienRepository;
    private final ServiceRepository serviceRepository;

    public HomeController(ChuyenVienRepository chuyenVienRepository, ServiceRepository serviceRepository) {
        this.chuyenVienRepository = chuyenVienRepository;
        this.serviceRepository = serviceRepository;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        // Lấy user từ session (nếu có)
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("username", user.getUsername());
        }

        // Lấy danh sách chuyên viên và dịch vụ từ database
        List<ChuyenVien> chuyenVienList = chuyenVienRepository.findAll();
        List<Service> serviceList = serviceRepository.findAll();

        // Truyền vào model để hiển thị ở giao diện
        model.addAttribute("chuyenVienList", chuyenVienList);
        model.addAttribute("serviceList", serviceList);

        return "index"; // index.html trong thư mục templates/
    }
}
