function openCategoryModal(mode = 'add', id = '', name = '') {
    const modal = document.getElementById('categoryModal');
    const categoryNameInput = document.getElementById('categoryName');
    const categoryIdInput = document.getElementById('categoryId');
    const categoryForm = document.getElementById('categoryForm');

    if (mode === 'edit') {
        document.getElementById('modalTitle').textContent = 'Chỉnh Sửa Danh Mục';
        categoryNameInput.value = name;
        categoryIdInput.value = id;
        // Nếu sửa, bạn có thể đổi action sang servlet update
        categoryForm.action = "update-category";
    } else {
        document.getElementById('modalTitle').textContent = 'Thêm Danh Mục Mới';
        categoryNameInput.value = '';
        categoryIdInput.value = '';
        // Đảm bảo action là add-category khi thêm mới
        categoryForm.action = "add-category";
    }
    modal.style.display = 'block';
}

// QUAN TRỌNG: Xóa bỏ đoạn categoryForm.addEventListener('submit', ...) cũ
// Để trình duyệt tự gửi dữ liệu về Servlet theo action của Form.