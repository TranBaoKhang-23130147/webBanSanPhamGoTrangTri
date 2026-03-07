document.addEventListener('DOMContentLoaded', function() {
    const addProductButton = document.querySelector('.add-new-product-btn');
    if (addProductButton) {
        addProductButton.addEventListener('click', function() {
            window.location.href = 'admin_add_products.jsp';
        });
    }
});
document.addEventListener('DOMContentLoaded', function () {

    const promoBtn = document.querySelector('.add-uu-dai');
    const modal = document.getElementById('promotionModal');
    const overlay = document.getElementById('modalOverlay');

    if (promoBtn) {
        promoBtn.addEventListener('click', function () {
            modal.style.display = 'block';
            overlay.style.display = 'block';
        });
    }

    if (overlay) {
        overlay.addEventListener('click', function () {
            modal.style.display = 'none';
            overlay.style.display = 'none';
        });
    }

});


function toggleActive(productId, willBeActive) {

    const status = willBeActive ? 1 : 0;

    Swal.fire({
        title: willBeActive ? 'Kích hoạt sản phẩm?' : 'Tắt sản phẩm?',
        text: willBeActive ? "Sản phẩm sẽ hiển thị cho khách hàng." : "Sản phẩm sẽ ẩn đi.",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Đồng ý'
    }).then((result) => {

        if (result.isConfirmed) {

            fetch(contextPath + '/admin/update-product-status', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'id=' + productId + '&status=' + status
            })
                .then(res => res.json())
                .then(data => {

                    if (data.success) {
                        Swal.fire('Thành công!', data.message, 'success');
                    } else {
                        Swal.fire('Lỗi!', data.message || 'Không xác định', 'error');
                    }

                })
                .catch(err => {

                    console.error(err);

                    Swal.fire('Lỗi!', 'Không thể kết nối server', 'error');

                });

        }

    });

}

document.addEventListener('DOMContentLoaded', function() {

    const addBtn = document.querySelector('.add-new-product-btn');

    if (addBtn) {

        addBtn.addEventListener('click', function() {

            window.location.href = contextPath + '/admin-add-product';

        });

    }

});
function confirmDelete(productId) {

    Swal.fire({
        title: 'Xác nhận xóa?',
        text: "Sản phẩm sẽ bị xóa vĩnh viễn!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Xóa ngay',
        cancelButtonText: 'Hủy'
    }).then((result) => {

        if (result.isConfirmed) {

            fetch(contextPath + '/admin/delete-product', {

                method: 'POST',

                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },

                body: 'id=' + productId

            })

                .then(response => response.json())

                .then(data => {

                    if (data.success) {

                        Swal.fire('Thành công!', data.message, 'success')

                            .then(() => location.reload());

                    } else {

                        Swal.fire('Lỗi!', data.message, 'error');

                    }

                })

                .catch(error => {

                    Swal.fire('Lỗi!', 'Không thể xóa sản phẩm. Lỗi: ' + error, 'error');

                });

        }

    });

}
document.addEventListener('keydown', function (e) {

    if (e.key === 'Escape') {

        const modal = document.getElementById('promotionModal');
        const overlay = document.getElementById('modalOverlay');

        if (modal) modal.style.display = 'none';
        if (overlay) overlay.style.display = 'none';

    }

});