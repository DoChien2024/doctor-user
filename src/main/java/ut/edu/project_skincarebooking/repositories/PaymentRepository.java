package ut.edu.project_skincarebooking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.project_skincarebooking.models.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
