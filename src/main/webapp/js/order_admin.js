
document.addEventListener('DOMContentLoaded', function() {
    const rows = document.querySelectorAll('.clickable-row');

    rows.forEach(row => {
        row.addEventListener('click', function(event) {
            const detailUrl = this.getAttribute('data-href');
            const target = event.target;
            if (target.matches('input[type="checkbox"]') || target.closest('input[type="checkbox"]')) {
                return;
            }
            if (target.matches('.action-menu') || target.closest('.action-menu')) {
                return;
            }

            if (detailUrl) {
                window.location.href = detailUrl;
            }
        });
    });
});

function showDetail(id) {
    const data = document.getElementById('data-order-' + id).innerHTML;
    document.getElementById('modalContainer').innerHTML = data;
    document.getElementById('orderModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('orderModal').style.display = 'none';
}
function filterOrders() {
    let searchValue = document.getElementById('orderSearch').value.toUpperCase();
    let statusValue = document.getElementById('statusFilter').value;

    let rows = document.querySelectorAll('.order-row');

    rows.forEach(row => {
        let orderId = row.getElementsByTagName('td')[0].textContent.toUpperCase();
        let orderStatus = row.getAttribute('data-status');
        let matchId = orderId.indexOf(searchValue) > -1;
        let matchStatus = (statusValue === "") || (orderStatus === statusValue);
        if (matchId && matchStatus) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}