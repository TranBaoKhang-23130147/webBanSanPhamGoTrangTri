function showDetail(id) {

    const sourceData = document.getElementById('data-order-' + id);

    if (sourceData) {
        document.getElementById('modalContainer').innerHTML = sourceData.innerHTML;

        document.getElementById('orderModal').style.display = 'block';

        document.body.style.overflow = 'hidden';
    }

}

function closeModal() {

    document.getElementById('orderModal').style.display = 'none';

    document.body.style.overflow = 'auto';

}

window.onclick = function(event) {

    const modal = document.getElementById('orderModal');

    if (event.target == modal) {
        closeModal();
    }

}