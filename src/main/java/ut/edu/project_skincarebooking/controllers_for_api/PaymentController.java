package ut.edu.project_skincarebooking.controllers_for_api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ut.edu.project_skincarebooking.models.Booking;
import ut.edu.project_skincarebooking.models.Payment;
import ut.edu.project_skincarebooking.repositories.BookingRepository;
import ut.edu.project_skincarebooking.repositories.PaymentRepository;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @PostMapping("/pay/{bookingId}")
    public String payBooking(
            @PathVariable Long bookingId,
            Model model              // <-- đây là nơi bạn truyền dữ liệu sang view
    ) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid booking ID: " + bookingId)
                );

        // Nếu đã có payment, chuyển thẳng sang page list với thông báo lỗi
        if (booking.getPayment() != null) {
            model.addAttribute("error", "Lịch hẹn này đã được thanh toán!");
            // bạn cần chắc controller danh sách booking trả về view "booking_list"
            return "booking_list";
        }

        // Tạo mới Payment
        Payment p = new Payment();
        p.setBooking(booking);
        p.setAmount(booking.getService().getPrice());
        p.setPaymentTime(LocalDateTime.now());
        p.setMethod("CASH");  // hoặc lấy từ form nếu bạn thêm select
        p.setStatus(true);

        paymentRepository.save(p);

        // Gắn vào booking và lưu lại
        booking.setPayment(p);
        bookingRepository.save(booking);

        // Truyền payment sang view payment_success.html
        model.addAttribute("payment", p);
        return "payment_success";
    }
}
