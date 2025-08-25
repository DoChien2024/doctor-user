document.addEventListener("DOMContentLoaded", function () {
    // Smooth scroll for buttons
    document.querySelector(".btn-light").addEventListener("click", function (event) {
        event.preventDefault();
        document.querySelector("#services").scrollIntoView({ behavior: "smooth" });
    });

    // Navbar shrink effect on scroll
    window.addEventListener("scroll", function () {
        let navbar = document.querySelector(".navbar");
        if (window.scrollY > 50) {
            navbar.classList.add("shadow-sm", "bg-white");
        } else {
            navbar.classList.remove("shadow-sm", "bg-white");
        }
    });
});
