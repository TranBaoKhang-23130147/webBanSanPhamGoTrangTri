/**
 * Quản lý logic cho trang Loại sản phẩm
 */

// Hàm mở Modal để thêm mới
function openProductTypeModal() {
    const modal = document.getElementById('productTypeModal');

    // Reset các trường input trong form
    const idInput = document.getElementById('productTypeId');
    const nameInput = document.getElementById('productTypeName');
    const categoryInput = document.getElementById('categoryId');

    if(idInput) idInput.value = '';
    if(nameInput) nameInput.value = '';
    // Nếu là thẻ select thì reset về option đầu tiên, nếu là input thì xóa trống
    if(categoryInput) {
        if(categoryInput.tagName === 'SELECT') categoryInput.selectedIndex = 0;
        else categoryInput.value = '';
    }

    // Đổi tiêu đề và action form sang ADD
    document.getElementById('modalTitle').innerText = "Thêm Loại Sản Phẩm Mới";
    document.getElementById('productTypeForm').action = "add-product-type";

    modal.style.display = 'block';
}

// Hàm mở Modal để chỉnh sửa (Edit) - Nhận dữ liệu từ nút bấm trong bảng
function editProductType(id, name, parentCategoryId) {
    const modal = document.getElementById('productTypeModal');

    // Đổi tiêu đề và action form sang UPDATE
    document.getElementById('modalTitle').innerText = "Chỉnh Sửa Loại Sản Phẩm";
    document.getElementById('productTypeForm').action = "update-product-type";

    // Điền dữ liệu cũ vào form để sửa
    document.getElementById('productTypeId').value = id;
    document.getElementById('productTypeName').value = name;
    document.getElementById('categoryId').value = parentCategoryId;

    modal.style.display = 'block';
}

// Hàm đóng Modal
function closeProductTypeModal() {
    document.getElementById('productTypeModal').style.display = 'none';
}

// Xử lý Tìm kiếm
const searchInput = document.getElementById('searchInput');

function searchProductTypeByName() {
    if (!searchInput) return;
    const keyword = searchInput.value.trim();

    // Chuyển hướng về servlet quản lý loại sản phẩm kèm tham số search
    // Ví dụ: product-type-manager?search=BanGhe
    window.location.href = 'product-type-manager?search=' + encodeURIComponent(keyword);
}

// Lắng nghe sự kiện phím Enter trên ô tìm kiếm
if (searchInput) {
    searchInput.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            searchProductTypeByName();
        }
    });
}

// Đóng modal khi click ra ngoài vùng nội dung modal-content
window.onclick = function(event) {
    const modal = document.getElementById('productTypeModal');
    if (event.target === modal) {
        closeProductTypeModal();
    }
}