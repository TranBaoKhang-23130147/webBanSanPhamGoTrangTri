/* mypage_script.js */

document.addEventListener('DOMContentLoaded', function() {

    // 1. Logic cho chức năng đóng/mở menu Tài khoản (Dropdown Toggle)
    const dropdownToggle = document.querySelector('.dropdown-toggle');
    const dropdownMenu = document.querySelector('.menu-item-dropdown');

    if (dropdownToggle && dropdownMenu) {
        dropdownToggle.addEventListener('click', function(e) {
            e.preventDefault();
            dropdownMenu.classList.toggle('active');
        });
    }

    // 2. Logic chuyển tab (Tab Switching Logic)
    const tabLinks = document.querySelectorAll('.tab-link');
    const tabContents = document.querySelectorAll('.tab-content');

    tabLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const tabId = this.getAttribute('data-tab');

            // Xử lý Tab Link:
            tabLinks.forEach(otherLink => otherLink.classList.remove('active'));

            const parentDropdown = this.closest('.menu-item-dropdown');
            if (parentDropdown) {
                parentDropdown.classList.add('active');
            }

            if (!parentDropdown) {
                if(dropdownMenu) dropdownMenu.classList.remove('active');
            }

            this.classList.add('active');


            // Xử lý Tab Content:
            tabContents.forEach(content => content.classList.remove('active'));

            const targetContent = document.getElementById(tabId);
            if(targetContent) {
                targetContent.classList.add('active');
            }
        });
    });
});
// Thêm vào file js/mypage_script.js hoặc cuối trang JSP
window.addEventListener('load', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const tabName = urlParams.get('tab');
    if (tabName) {
        // 1. Ẩn tất cả các tab content hiện tại
        document.querySelectorAll('.tab-content').forEach(tab => {
            tab.classList.remove('active');
        });
        // 2. Hiện tab content tương ứng
        const targetTab = document.getElementById(tabName);
        if (targetTab) {
            targetTab.classList.add('active');
        }
        // 3. Cập nhật trạng thái active cho menu bên trái (tùy chọn)
        document.querySelectorAll('.tab-link').forEach(link => {
            link.classList.remove('active');
            if(link.getAttribute('data-tab') === tabName) {
                link.classList.add('active');
            }
        });
    }
});