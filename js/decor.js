document.addEventListener("DOMContentLoaded", () => {
    const banners = document.querySelectorAll(".banner > div");
    let current = 0;
    const total = banners.length;

    // Khởi tạo banner đầu tiên
    banners[current].classList.add("active");

    // Tạo chấm điều hướng
    const controls = document.createElement("div");
    controls.classList.add("slider-controls");
    banners[0].parentElement.appendChild(controls);

    for (let i = 0; i < total; i++) {
        const dot = document.createElement("div");
        dot.classList.add("control-dot");
        if (i === 0) dot.classList.add("active");
        dot.addEventListener("click", () => goToBanner(i));
        controls.appendChild(dot);
    }

    const dots = document.querySelectorAll(".control-dot");

    // Nút trái/phải
    const leftArrow = document.createElement("div");
    leftArrow.classList.add("arrow", "left");
    leftArrow.innerHTML = "&#10094;";
    const rightArrow = document.createElement("div");
    rightArrow.classList.add("arrow", "right");
    rightArrow.innerHTML = "&#10095;";
    banners[0].parentElement.appendChild(leftArrow);
    banners[0].parentElement.appendChild(rightArrow);

    leftArrow.addEventListener("click", prevBanner);
    rightArrow.addEventListener("click", nextBanner);

    function goToBanner(index) {
        banners[current].classList.remove("active");
        dots[current].classList.remove("active");
        current = index;
        banners[current].classList.add("active");
        dots[current].classList.add("active");
    }

    function nextBanner() {
        goToBanner((current + 1) % total);
    }

    function prevBanner() {
        goToBanner((current - 1 + total) % total);
    }

    // Tự động chuyển slide
    setInterval(nextBanner, 6000);
});
