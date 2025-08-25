package ut.edu.project_skincarebooking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.project_skincarebooking.models.ChuyenVien;
import java.util.Optional;
public interface ChuyenVienRepository extends JpaRepository<ChuyenVien, Long> {
    Optional<ChuyenVien> findByUserId(Long userId);

}
