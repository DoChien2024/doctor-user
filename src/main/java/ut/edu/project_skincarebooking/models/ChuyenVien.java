package ut.edu.project_skincarebooking.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chuyenvien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChuyenVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hoTen;

    private String chuyenMon;

    private String soDienThoai;

    private String hinhAnh; // link hình hoặc tên file ảnh nếu có
    // Liên kết với User
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
