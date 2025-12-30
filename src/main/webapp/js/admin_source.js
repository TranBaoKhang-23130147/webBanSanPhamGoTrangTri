/**
 * Mở Modal Nguồn Hàng (Source)
 * @param {string} mode - 'edit' hoặc để trống (mặc định là thêm mới)
 * @param {string} id - ID của nguồn hàng (nếu sửa)
 * @param {string} name - Tên nguồn hàng (nếu sửa)
 */
function openSourceModal(mode = 'add', id = '', name = '') {
    const modal = document.getElementById('sourceModal');
    const title = document.getElementById('modalTitle');
    const form = document.getElementById('sourceForm');
    const inputName = document.getElementById('sourceName');
    const inputId = document.getElementById('sourceId');

    if (mode === 'edit') {
        title.innerText = 'Chỉnh Sửa Nguồn Hàng';
        form.action = 'edit-source'; // URL khớp với @WebServlet trong SourceServlet
        inputName.value = name;
        inputId.value = id;
    } else {
        title.innerText = 'Thêm Nguồn Hàng Mới';
        form.action = 'add-source'; // URL khớp với @WebServlet trong SourceServlet
        inputName.value = '';
        inputId.value = '';
    }

    modal.style.display = 'block';
}

function closeSourceModal() {
    document.getElementById('sourceModal').style.display = 'none';
}

// Xử lý tìm kiếm nguồn hàng
const searchInput = document.getElementById('searchInput');

function searchSourceByName() {
    const keyword = searchInput.value.trim();
    const params = new URLSearchParams();

    if (keyword) {
        params.append('search', keyword);
    }

    // Chuyển hướng sang servlet quản lý nguồn hàng
    window.location.href = 'source-manager?' + params.toString();
}

if (searchInput) {
    searchInput.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            searchSourceByName();
        }
    });
}

/**
 * Xóa Nguồn Hàng
 */
function deleteSource(id, name) {
    Swal.fire({
        title: 'Xác nhận xóa?',
        text: "Bạn có chắc muốn xóa nguồn hàng: " + name + "?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Xóa ngay',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            // Chuyển hướng đến servlet xóa nguồn hàng
            window.location.href = "delete-source?id=" + id;
        }
    })
}

// Đóng modal khi click ra ngoài vùng modal-content
window.onclick = function(event) {
    const modal = document.getElementById('sourceModal');
    if (event.target == modal) {
        closeSourceModal();
    }
}
