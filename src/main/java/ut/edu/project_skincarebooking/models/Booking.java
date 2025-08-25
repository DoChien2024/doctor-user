package ut.edu.project_skincarebooking.models;

import jakarta.persistence.*;
import lombok.*;
import ut.edu.project_skincarebooking.enums.BookingStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service; // Liên kết với Service

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Liên kết với User

    @ManyToOne
    @JoinColumn(name = "chuyenvien_id")
    private ChuyenVien chuyenVien;
    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerPhone;

    @Column(nullable = false)
    private LocalDateTime appointmentTime; // Thời gian hẹn

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;
    // 🆕 Thêm cột trạng thái
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status = BookingStatus.PENDING; // mặc định là PENDING

    // === NEW: kết quả bác sĩ ===
    @Column(name = "result_note", columnDefinition = "TEXT")
    private String resultNote;          // nội dung kết quả / dặn dò

    @Column(name = "result_at")
    private LocalDateTime resultAt;     // thời điểm trả kết quả
}
