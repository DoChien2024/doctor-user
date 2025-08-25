package ut.edu.project_skincarebooking.controllers_for_api;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ut.edu.project_skincarebooking.enums.BookingStatus;
import ut.edu.project_skincarebooking.enums.Role;
import ut.edu.project_skincarebooking.models.Booking;
import ut.edu.project_skincarebooking.models.ChuyenVien;
import ut.edu.project_skincarebooking.models.User;
import ut.edu.project_skincarebooking.repositories.BookingRepository;
import ut.edu.project_skincarebooking.repositories.ChuyenVienRepository;

import java.util.List;

@Controller
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final BookingRepository bookingRepo;
    private final ChuyenVienRepository chuyenVienRepo;

    // Mặc định: show toàn bộ; nếu ?doctorId=... thì lọc theo bác sĩ
    @GetMapping("/schedule")
    public String schedule(@RequestParam(required = false) Long doctorId,
                           HttpSession session,
                           Model model) {
        User current = (User) session.getAttribute("user");
        if (current == null) return "redirect:/login";
        Role role = (Role) session.getAttribute("role");
        if (role != Role.DOCTOR && role != Role.ADMIN) return "redirect:/access-denied";

        // danh sách bác sĩ cho filter
        model.addAttribute("chuyenViens", chuyenVienRepo.findAll());
        model.addAttribute("doctorId", doctorId); // để set selected trong dropdown

        if (doctorId != null) {
            ChuyenVien cv = chuyenVienRepo.findById(doctorId).orElse(null);
            if (cv == null) {
                model.addAttribute("error", "Không tìm thấy chuyên viên.");
                model.addAttribute("bookings", List.of());
                return "doctor_schedule";
            }
            List<Booking> bookings = bookingRepo.findByChuyenVien_IdOrderByAppointmentTimeAsc(doctorId);
            model.addAttribute("cv", cv); // hiển thị tên bác sĩ đã lọc
            model.addAttribute("bookings", bookings);
        } else {
            // không lọc: lấy toàn bộ
            List<Booking> bookings = bookingRepo.findAllByOrderByAppointmentTimeAsc();
            model.addAttribute("cv", null); // không hiển thị tiêu đề 1 bác sĩ
            model.addAttribute("bookings", bookings);
        }
        return "doctor_schedule";
    }

    // Xác nhận ca: PENDING -> CONFIRMED (giữ nguyên filter nếu có)
    @PostMapping("/bookings/{id}/confirm")
    public String confirm(@PathVariable Long id,
                          @RequestParam(required = false) Long doctorId,
                          HttpSession session) {
        User current = (User) session.getAttribute("user");
        if (current == null) return "redirect:/login";
        Role role = (Role) session.getAttribute("role");
        if (role != Role.DOCTOR && role != Role.ADMIN) return "redirect:/access-denied";

        bookingRepo.findById(id).ifPresent(b -> {
            if (b.getStatus() == BookingStatus.PENDING) {
                b.setStatus(BookingStatus.CONFIRMED);
                bookingRepo.save(b);
            }
        });

        return (doctorId != null)
                ? "redirect:/doctor/schedule?doctorId=" + doctorId
                : "redirect:/doctor/schedule";
    }
    @GetMapping("/results/{id}")
    public String showResultForm(@PathVariable Long id,
                                 @RequestParam(required = false) Long doctorId,
                                 HttpSession session,
                                 Model model) {
        User current = (User) session.getAttribute("user");
        if (current == null) return "redirect:/login";
        Role role = (Role) session.getAttribute("role");
        if (role != Role.DOCTOR && role != Role.ADMIN) return "redirect:/access-denied";

        Booking b = bookingRepo.findById(id).orElse(null);
        if (b == null) return "redirect:/doctor/schedule";

        model.addAttribute("b", b);
        model.addAttribute("doctorId", doctorId); // để quay lại đúng bộ lọc/schedule
        return "doctor_result_form";
    }

    // == SUBMIT KẾT QUẢ (POST) ==
    @PostMapping("/results/{id}/submit")
    public String submitResult(@PathVariable Long id,
                               @RequestParam String resultNote,
                               @RequestParam(required = false) Long doctorId,
                               HttpSession session) {
        User current = (User) session.getAttribute("user");
        if (current == null) return "redirect:/login";
        Role role = (Role) session.getAttribute("role");
        if (role != Role.DOCTOR && role != Role.ADMIN) return "redirect:/access-denied";

        Booking b = bookingRepo.findById(id).orElse(null);
        if (b != null) {
            // cho phép nhập kết quả nếu đã CONFIRMED (hoặc auto-confirm nếu còn PENDING)
            if (b.getStatus() == BookingStatus.PENDING) {
                b.setStatus(BookingStatus.CONFIRMED);
            }
            b.setResultNote(resultNote);
            b.setResultAt(java.time.LocalDateTime.now());
            b.setStatus(BookingStatus.DONE); // trả kết quả xong -> DONE
            bookingRepo.save(b);
        }

        // quay về lịch làm việc, giữ filter nếu có
        return (doctorId != null)
                ? "redirect:/doctor/schedule?doctorId=" + doctorId
                : "redirect:/doctor/schedule";
    }
}
