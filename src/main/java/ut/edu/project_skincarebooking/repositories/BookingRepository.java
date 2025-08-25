package ut.edu.project_skincarebooking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.project_skincarebooking.enums.BookingStatus;
import ut.edu.project_skincarebooking.models.Booking;
import ut.edu.project_skincarebooking.models.ChuyenVien;
import ut.edu.project_skincarebooking.models.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user); // Tìm tất cả booking của một user
    List<Booking> findByChuyenVien_IdOrderByAppointmentTimeAsc(Long chuyenVienId);
    List<Booking> findAllByOrderByAppointmentTimeAsc();
    // Danh sách lịch của 1 user (mới nhất trước)
    List<Booking> findByUserOrderByAppointmentTimeDesc(User user);

    // Chỉ các ca đã DONE của 1 user (mới nhất trước)
    List<Booking> findByUserAndStatusOrderByAppointmentTimeDesc(User user, BookingStatus status);

    // LẤY 1 CA THEO ID + CHỦ SỞ HỮU (ngăn xem trộm)
    Optional<Booking> findByIdAndUser(Long id, User user);
}
