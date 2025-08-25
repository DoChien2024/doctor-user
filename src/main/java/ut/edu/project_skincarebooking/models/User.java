package ut.edu.project_skincarebooking.models;
import jakarta.persistence.*;
import lombok.*;
import ut.edu.project_skincarebooking.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER; // mặc định USER
    // Quan hệ 1-1 với ChuyenVien (nếu role = DOCTOR)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ChuyenVien chuyenVien;
}
