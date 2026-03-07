
function openProductTypeModal() {
    const modal = document.getElementById('productTypeModal');
    const idInput = document.getElementById('productTypeId');
    const nameInput = document.getElementById('productTypeName');
    const categoryInput = document.getElementById('categoryId');

    if(idInput) idInput.value = '';
    if(nameInput) nameInput.value = '';
    if(categoryInput) {
        if(categoryInput.tagName === 'SELECT') categoryInput.selectedIndex = 0;
        else categoryInput.value = '';
    }

    document.getElementById('modalTitle').innerText = "Thêm Loại Sản Phẩm Mới";
    document.getElementById('productTypeForm').action = "add-product-type";

    modal.style.display = 'block';
}
function editProductType(id, name, parentCategoryId) {
    const modal = document.getElementById('productTypeModal');
    document.getElementById('modalTitle').innerText = "Chỉnh Sửa Loại Sản Phẩm";
    document.getElementById('productTypeForm').action = "update-product-type";
    document.getElementById('productTypeId').value = id;
    document.getElementById('productTypeName').value = name;
    document.getElementById('categoryId').value = parentCategoryId;

    modal.style.display = 'block';
}
function closeProductTypeModal() {
    document.getElementById('productTypeModal').style.display = 'none';
}
const searchInput = document.getElementById('searchInput');

function searchProductTypeByName() {
    if (!searchInput) return;
    const keyword = searchInput.value.trim();

    window.location.href = 'product-type-manager?search=' + encodeURIComponent(keyword);
}

if (searchInput) {
    searchInput.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            searchProductTypeByName();
        }
    });
}

window.onclick = function(event) {
    const modal = document.getElementById('productTypeModal');
    if (event.target === modal) {
        closeProductTypeModal();
    }

}
function deleteProductType(id, name) {
    Swal.fire({
        title: 'Xác nhận xóa?',
        text: "Bạn có chắc muốn xóa loại sản phẩm: " + name + " không?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Xóa ngay',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = "delete-product-type?id=" + id;
        }
    })
}

