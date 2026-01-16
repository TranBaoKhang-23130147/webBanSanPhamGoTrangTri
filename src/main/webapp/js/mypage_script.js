document.addEventListener('DOMContentLoaded', function() {
    // === 1. XỬ LÝ DROPDOWN (Giữ nguyên) ===
    const dropdownToggle = document.querySelector('.dropdown-toggle');
    const dropdownMenu = document.querySelector('.menu-item-dropdown');
    if (dropdownToggle && dropdownMenu) {
        dropdownToggle.addEventListener('click', function(e) {
            e.preventDefault();
            dropdownMenu.classList.toggle('active');
        });
    }

    // === 2. HÀM CHUYỂN TAB CHUNG ===
    function activeTab(tabId) {
        const targetContent = document.getElementById(tabId);
        const targetLink = document.querySelector(`[data-tab="${tabId}"]`);

        if (targetContent) {
            // Ẩn tất cả tab
            document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
            // Hiện tab mục tiêu
            targetContent.classList.add('active');

            if (targetLink) {
                document.querySelectorAll('.tab-link, .menu-link').forEach(l => l.classList.remove('active'));
                targetLink.classList.add('active');

                const parentDropdown = targetLink.closest('.menu-item-dropdown');
                if (parentDropdown) parentDropdown.classList.add('active');
            }
        }
    }

    // === 3. XỬ LÝ CLICK ĐÃ SỬA ===
    document.querySelectorAll('.menu-link[data-tab], .tab-link[data-tab]').forEach(link => {
        link.addEventListener('click', function (e) {
            const href = this.getAttribute('href');

            // NẾU LINK CÓ CHỨA SERVLET (Hồ sơ, Thanh toán, Đơn hàng)
            if (href && href !== '#' && href.includes('MyPageServlet')) {
                // CHO PHÉP trình duyệt load lại trang để Servlet chạy
                return;
            }

            // NẾU LÀ TAB NỘI BỘ (Địa chỉ, Bảo mật...)
            e.preventDefault();
            const tabId = this.dataset.tab;
            activeTab(tabId);
        });
    });

    // Tự động active tab khi trang load xong dựa trên ?tab=
    const params = new URLSearchParams(window.location.search);
    const tabName = params.get('tab') || 'ho-so';
    activeTab(tabName);
});