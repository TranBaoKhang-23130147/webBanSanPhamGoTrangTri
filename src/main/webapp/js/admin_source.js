
function openSourceModal(mode = 'add', id = '', name = '') {
    const modal = document.getElementById('sourceModal');
    const title = document.getElementById('modalTitle');
    const form = document.getElementById('sourceForm');
    const inputName = document.getElementById('sourceName');
    const inputId = document.getElementById('sourceId');

    if (mode === 'edit') {
        title.innerText = 'Chỉnh Sửa Nguồn Hàng';
        form.action = 'edit-source';
        inputName.value = name;
        inputId.value = id;
    } else {
        title.innerText = 'Thêm Nguồn Hàng Mới';
        form.action = 'add-source';
        inputName.value = '';
        inputId.value = '';
    }

    modal.style.display = 'block';
}

function closeSourceModal() {
    document.getElementById('sourceModal').style.display = 'none';
}
const searchInput = document.getElementById('searchInput');

function searchSourceByName() {
    const keyword = searchInput.value.trim();
    const params = new URLSearchParams();

    if (keyword) {
        params.append('search', keyword);
    }
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
            window.location.href = "delete-source?id=" + id;
        }
    })
}

window.onclick = function(event) {
    const modal = document.getElementById('sourceModal');
    if (event.target == modal) {
        closeSourceModal();
    }
}
