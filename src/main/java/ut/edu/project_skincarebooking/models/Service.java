package ut.edu.project_skincarebooking.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column
    private Integer duration; // Thời gian thực hiện (phút)

    @Column(length = 1000)
    private String benefits; // Lợi ích của dịch vụ

    @Column(length = 1000)
    private String steps; // Các bước thực hiện dịch vụ

    @Column
    private String category; // Danh mục dịch vụ (ví dụ: Massage, Chăm sóc da, v.v.)

    @Column
    private Double rating; // Đánh giá trung bình

    @Column
    private Boolean status = true; // Trạng thái dịch vụ

    @Column
    private String createdAt; // Ngày tạo dịch vụ

    @Column
    private String updatedAt; // Ngày cập nhật dịch vụ
}
