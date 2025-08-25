document.addEventListener("DOMContentLoaded", function () {
    const serviceSelect = document.getElementById("service");
    const specialistSelect = document.getElementById("specialist");
    const bookingForm = document.getElementById("bookingForm");

    // Danh sách dịch vụ tại KAMOI Spa
    const services = [
        "Chăm sóc da mặt ", "Massage toàn thân", "Massage trị liệu", "Điều trị laser",
         "Giảm béo", "Gội đầu dưỡng sinh",
        "Nâng cơ trẻ hóa", "Cấy collagen", "Trị nám tàn nhang"
    ];

    // Danh sách chuyên viên tại KAMOI Spa
    const specialists = [
        "Đinh Thị Thu Trà", "Nguyễn Ngọc Như Ý", "Trần Thị Nguyệt Anh",
        "Hà Thành", "Nguyễn Hoàng Tâm Minh"
    ];

    // Cập nhật danh sách dịch vụ
    function populateSelect(selectElement, items, defaultText) {
        selectElement.innerHTML = '';
        const defaultOption = document.createElement("option");
        defaultOption.value = "";
        defaultOption.textContent = defaultText;
        selectElement.appendChild(defaultOption);

        items.forEach(item => {
            let option = document.createElement("option");
            option.value = item;
            option.textContent = item;
            selectElement.appendChild(option);
        });
    }

    // Hiển thị dịch vụ và chuyên viên khi trang tải xong
    populateSelect(serviceSelect, services, "-- Chọn dịch vụ --");
    populateSelect(specialistSelect, specialists, "-- Chọn chuyên viên --");

    // Xử lý đặt lịch
    bookingForm.addEventListener("submit", function (event) {
        event.preventDefault();

        // Hiển thị modal Bootstrap
        const successModal = new bootstrap.Modal(document.getElementById("successModal"));
        successModal.show();

        // Xóa nội dung form
        bookingForm.reset();
    });

    console.log("✅ File datlich.js đã được tải thành công!");
});
