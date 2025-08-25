package ut.edu.project_skincarebooking.controllers_for_api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ut.edu.project_skincarebooking.models.Booking;
import ut.edu.project_skincarebooking.models.Service;
import ut.edu.project_skincarebooking.models.User;
import ut.edu.project_skincarebooking.repositories.BookingRepository;
import ut.edu.project_skincarebooking.repositories.ChuyenVienRepository;
import ut.edu.project_skincarebooking.repositories.ServiceRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    private final ServiceRepository serviceRepo;
    private final BookingRepository bookingRepo;
    private final ChuyenVienRepository chuyenVienRepo; // ✅ thêm repo chuyên viên

    @GetMapping("/new")
    public String showBookingForm(@RequestParam("serviceId") Long serviceId, HttpSession session, Model model) {
        Service service = serviceRepo.findById(serviceId).orElseThrow();
        Booking booking = new Booking();
        booking.setService(service);

        User user = (User) session.getAttribute("user");
        if (user != null) {
            booking.setUser(user);
        }

        model.addAttribute("booking", booking);
        model.addAttribute("chuyenViens", chuyenVienRepo.findAll()); // ✅ đưa list chuyên viên ra view
        return "booking_form";
    }

    @PostMapping("/save")
    public String saveBooking(@ModelAttribute Booking booking, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Bạn cần đăng nhập để thực hiện đặt lịch.");
            return "redirect:/login";
        }

        if (booking.getUser() == null) {
            booking.setUser(user);
        }

        bookingRepo.save(booking);
        return "redirect:/";
    }

    @GetMapping("/list")
    public String listBookings(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("bookings", bookingRepo.findByUser(user));
        return "booking_list";
    }
}

