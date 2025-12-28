function openCategoryModal() {
    const modal = document.getElementById('categoryModal');
    // Chú ý ID này phải khớp với id="categoryName" trong thẻ <input>
    const input = document.getElementById('categoryName');
    if(input) input.value = '';
    modal.style.display = 'block';
}

function closeCategoryModal() {
    document.getElementById('categoryModal').style.display = 'none';
}
const searchInput = document.getElementById('searchInput');

function searchCategoryByName() {
    const keyword = searchInput.value.trim();

    const params = new URLSearchParams();
    if (keyword) params.append('search', keyword);

    window.location.href = 'category-manager?' + params.toString();
}

if (searchInput) {
    searchInput.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            searchCategoryByName();
        }
    });
}
function deleteCategory(id, name) {
    Swal.fire({
        title: 'Xác nhận xóa?',
        text: "Bạn có chắc muốn xóa danh mục: " + name + "?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Xóa ngay',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            // Chuyển hướng đến servlet xóa
            window.location.href = "delete-category?id=" + id;
        }
    })
}