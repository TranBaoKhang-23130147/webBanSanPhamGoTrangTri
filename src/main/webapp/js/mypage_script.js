document.addEventListener('DOMContentLoaded', function() {
    const dropdownToggle = document.querySelector('.dropdown-toggle');
    const dropdownMenu = document.querySelector('.menu-item-dropdown');
    if (dropdownToggle && dropdownMenu) {
        dropdownToggle.addEventListener('click', function(e) {
            e.preventDefault();
            dropdownMenu.classList.toggle('active');
        });
    }

    function activeTab(tabId) {
        const targetContent = document.getElementById(tabId);
        const targetLink = document.querySelector(`[data-tab="${tabId}"]`);

        if (targetContent) {
            document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
            targetContent.classList.add('active');

            if (targetLink) {
                document.querySelectorAll('.tab-link, .menu-link').forEach(l => l.classList.remove('active'));
                targetLink.classList.add('active');

                const parentDropdown = targetLink.closest('.menu-item-dropdown');
                if (parentDropdown) parentDropdown.classList.add('active');
            }
        }
    }

    document.querySelectorAll('.menu-link[data-tab], .tab-link[data-tab]').forEach(link => {
        link.addEventListener('click', function (e) {
            const href = this.getAttribute('href');

            if (href && href !== '#' && href.includes('MyPageServlet')) {
                return;
            }

            e.preventDefault();
            const tabId = this.dataset.tab;
            activeTab(tabId);
        });
    });

    const params = new URLSearchParams(window.location.search);
    const tabName = params.get('tab') || 'ho-so';
    activeTab(tabName);
});
function openEditModal(id, type, number, duration) {
    document.getElementById('edit-id').value = id;
    document.getElementById('edit-type').value = type;
    document.getElementById('edit-number').value = number;
    document.getElementById('edit-duration').value = duration;
    document.getElementById('editCardModal').style.display = 'block';
}
function openAddAddressModal() {
    document.getElementById("modalTitle").innerHTML = '<i class="fas fa-plus-circle"></i> Thêm địa chỉ';
    document.getElementById("addr-action").value = "add";

    document.getElementById("addr-id").value = "";
    document.getElementById("addr-name").value = "";
    document.getElementById("addr-phone").value = "";
    document.getElementById("addr-detail").value = "";
    document.getElementById("addr-commune").value = "";
    document.getElementById("addr-district").value = "";
    document.getElementById("addr-province").value = "";

    document.getElementById("addressModal").style.display = "block";
}

function openEditAddressModal(id, name, phone, detail, commune, district, province) {
    document.getElementById("modalTitle").innerHTML = '<i class="fas fa-edit"></i> Cập nhật địa chỉ';
    document.getElementById("addr-action").value = "update";

    document.getElementById("addr-id").value = id;
    document.getElementById("addr-name").value = name;
    document.getElementById("addr-phone").value = phone;
    document.getElementById("addr-detail").value = detail;
    document.getElementById("addr-commune").value = commune;
    document.getElementById("addr-district").value = district;
    document.getElementById("addr-province").value = province;

    document.getElementById("addressModal").style.display = "block";
}

function closeAddressModal() {
    document.getElementById("addressModal").style.display = "none";
}

