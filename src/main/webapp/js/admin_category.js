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
//2