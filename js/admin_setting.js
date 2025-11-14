// document.addEventListener('DOMContentLoaded', function () {
//     const sections = document.querySelectorAll('.settings-card');
//     const navItems = document.querySelectorAll('.settings-menu a');
//
//     // Hàm gỡ bỏ trạng thái active khỏi tất cả các mục
//     function removeActiveClass() {
//         navItems.forEach(item => {
//             // Gỡ bỏ class 'active' khỏi thẻ <li>
//             item.closest('li').classList.remove('active');
//         });
//     }
//
//     // Hàm theo dõi việc cuộn trang (Intersection Observer)
//     const observer = new IntersectionObserver(entries => {
//         entries.forEach(entry => {
//             if (entry.isIntersecting) {
//                 removeActiveClass();
//                 const targetId = entry.target.getAttribute('id');
//
//                 // Tìm thẻ <a> có href khớp với ID
//                 const activeLink = document.querySelector(`.settings-menu a[href="#${targetId}"]`);
//
//                 // Thêm class 'active' vào thẻ <li> chứa thẻ <a> đó
//                 if (activeLink) {
//                     activeLink.closest('li').classList.add('active');
//                 }
//             }
//         });
//     }, {
//         rootMargin: "0px 0px -70% 0px", // Đánh dấu active khi mục đạt đến 30% từ đỉnh màn hình
//         threshold: 0.1
//     });
//
//     // Quan sát từng phần nội dung
//     sections.forEach(section => {
//         observer.observe(section);
//     });
//
//     // Xử lý sự kiện nhấp chuột để cuộn mượt mà
//     navItems.forEach(item => {
//         item.addEventListener('click', function(e) {
//             e.preventDefault();
//             const targetId = this.getAttribute('href').substring(1);
//             const targetElement = document.getElementById(targetId);
//
//             if (targetElement) {
//                 // Cuộn mượt mà đến phần tử
//                 window.scrollTo({
//                     top: targetElement.offsetTop - 70, // Trừ 70px cho header cố định
//                     behavior: 'smooth'
//                 });
//             }
//         });
//     });
// });
// document.addEventListener('DOMContentLoaded', function () {
//     // Tự động tìm tất cả các thẻ settings-card còn lại
//     const sections = document.querySelectorAll('.settings-card');
//     // Tự động tìm tất cả các liên kết trong menu
//     const navItems = document.querySelectorAll('.settings-menu a');
//
//     // Hàm gỡ bỏ trạng thái active khỏi tất cả các mục
//     function removeActiveClass() {
//         navItems.forEach(item => {
//             item.closest('li').classList.remove('active');
//         });
//     }
//
//     // Khởi tạo trạng thái active đầu tiên
//     if (navItems.length > 0) {
//         navItems[0].closest('li').classList.add('active');
//     }
//
//     // Hàm theo dõi việc cuộn trang (Intersection Observer)
//     const observer = new IntersectionObserver(entries => {
//         entries.forEach(entry => {
//             // Kiểm tra xem phần tử có đang giao với viewport không
//             if (entry.isIntersecting) {
//                 removeActiveClass();
//                 const targetId = entry.target.getAttribute('id');
//
//                 // Tìm thẻ <a> có href khớp với ID
//                 const activeLink = document.querySelector(`.settings-menu a[href="#${targetId}"]`);
//
//                 // Thêm class 'active' vào thẻ <li> chứa thẻ <a> đó
//                 if (activeLink) {
//                     activeLink.closest('li').classList.add('active');
//                 }
//             }
//         });
//     }, {
//         // Định nghĩa khu vực quan sát:
//         // Đánh dấu active khi mục đạt đến 30% từ đỉnh màn hình, tránh header
//         rootMargin: "0px 0px -70% 0px",
//         threshold: 0.1
//     });
//
//     // Quan sát từng phần nội dung
//     sections.forEach(section => {
//         observer.observe(section);
//     });
//
//     // Xử lý sự kiện nhấp chuột để cuộn mượt mà
//     navItems.forEach(item => {
//         item.addEventListener('click', function(e) {
//             e.preventDefault();
//             const targetId = this.getAttribute('href').substring(1);
//             const targetElement = document.getElementById(targetId);
//
//             if (targetElement) {
//                 // Cuộn mượt mà đến phần tử, trừ đi chiều cao header cố định (60px)
//                 window.scrollTo({
//                     top: targetElement.offsetTop - 70, // Đảm bảo nội dung không bị header che
//                     behavior: 'smooth'
//                 });
//             }
//         });
//     });

    document.addEventListener('DOMContentLoaded', function () {
    // Tự động tìm tất cả các thẻ settings-card còn lại
    const sections = document.querySelectorAll('.settings-card');
    // Tự động tìm tất cả các liên kết trong menu
    const navItems = document.querySelectorAll('.settings-menu a');

    // Hàm gỡ bỏ trạng thái active khỏi tất cả các mục
    function removeActiveClass() {
    navItems.forEach(item => {
    item.closest('li').classList.remove('active');
});
}

    // Khởi tạo trạng thái active đầu tiên
    if (navItems.length > 0) {
    navItems[0].closest('li').classList.add('active');
}

    // Hàm theo dõi việc cuộn trang (Intersection Observer)
    const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => {
    // Kiểm tra xem phần tử có đang giao với viewport không
    if (entry.isIntersecting) {
    removeActiveClass();
    const targetId = entry.target.getAttribute('id');

    // Tìm thẻ <a> có href khớp với ID
    const activeLink = document.querySelector(`.settings-menu a[href="#${targetId}"]`);

    // Thêm class 'active' vào thẻ <li> chứa thẻ <a> đó
    if (activeLink) {
    activeLink.closest('li').classList.add('active');
}
}
});
}, {
    // Định nghĩa khu vực quan sát:
    // Đánh dấu active khi mục đạt đến 30% từ đỉnh màn hình, tránh header
    rootMargin: "0px 0px -70% 0px",
    threshold: 0.1
});

    // Quan sát từng phần nội dung
    sections.forEach(section => {
    observer.observe(section);
});

    // Xử lý sự kiện nhấp chuột để cuộn mượt mà
    navItems.forEach(item => {
    item.addEventListener('click', function(e) {
    e.preventDefault();
    const targetId = this.getAttribute('href').substring(1);
    const targetElement = document.getElementById(targetId);

    if (targetElement) {
    // Cuộn mượt mà đến phần tử, trừ đi chiều cao header cố định (60px)
    window.scrollTo({
    top: targetElement.offsetTop - 70, // Đảm bảo nội dung không bị header che
    behavior: 'smooth'
});
}
});
});
});


