package ut.edu.project_skincarebooking.controllers_for_api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ut.edu.project_skincarebooking.repositories.ChuyenVienRepository;

@Controller
@RequiredArgsConstructor
public class ChuyenVienController {

    private final ChuyenVienRepository chuyenVienRepo;

    @GetMapping("/chuyenvien")
    public String danhSachChuyenVien(Model model) {
        model.addAttribute("danhSachChuyenVien", chuyenVienRepo.findAll());
        return "chuyenvien_list"; // trang HTML để hiển thị
    }
}
