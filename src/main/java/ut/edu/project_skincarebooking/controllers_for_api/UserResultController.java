// ut.edu.project_skincarebooking.controllers_for_api.UserResultController
package ut.edu.project_skincarebooking.controllers_for_api;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ut.edu.project_skincarebooking.enums.BookingStatus;
import ut.edu.project_skincarebooking.models.Booking;
import ut.edu.project_skincarebooking.models.User;
import ut.edu.project_skincarebooking.repositories.BookingRepository;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserResultController {

    private final BookingRepository bookingRepo;

    // Danh sách kết quả của CHÍNH user (mặc định: DONE)
    // /user/results
    @GetMapping("/results")
    public String myResults(@RequestParam(required = false) String scope,
                            HttpSession session,
                            Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Booking> bookings = ("all".equalsIgnoreCase(scope))
                ? bookingRepo.findByUserOrderByAppointmentTimeDesc(user)
                : bookingRepo.findByUserAndStatusOrderByAppointmentTimeDesc(user, BookingStatus.DONE);

        model.addAttribute("bookings", bookings);
        model.addAttribute("scope", scope == null ? "done" : scope.toLowerCase());
        model.addAttribute("active", "results"); // <<-- thêm dòng này
        return "user_results";
    }

    @GetMapping("/results/{id}")
    public String myResultDetail(@PathVariable Long id,
                                 HttpSession session,
                                 Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Booking b = bookingRepo.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("b", b);
        model.addAttribute("active", "results"); // <<-- thêm dòng này
        return "user_result_detail";
    }

}
