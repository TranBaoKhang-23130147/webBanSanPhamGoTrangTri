// --- XỬ LÝ ĐƠN HÀNG ---
const buttons = document.querySelectorAll('.status-btn');
const orderList = document.querySelector('.order-list');

const ordersData = {
    "Chờ xác nhận": [
        { id: "#DH001", date: "04/11/2025", total: "350.000đ" },
        { id: "#DH002", date: "05/11/2025", total: "520.000đ" }
    ],
    "Chờ lấy hàng": [
        { id: "#DH003", date: "03/11/2025", total: "720.000đ" }
    ],
    "Đang vận chuyển": [
        { id: "#DH004", date: "02/11/2025", total: "430.000đ" },
        { id: "#DH005", date: "01/11/2025", total: "610.000đ" }
    ],
    "Đã giao": [
        { id: "#DH006", date: "28/10/2025", total: "990.000đ" },
        { id: "#DH007", date: "27/10/2025", total: "275.000đ" }
    ],
    "Đã hủy": [
        { id: "#DH008", date: "25/10/2025", total: "120.000đ" }
    ]
};

// ✅ Gộp tất cả đơn hàng lại
const allOrders = Object.values(ordersData).flat();
ordersData["Tất cả đơn hàng"] = allOrders;

function renderOrders(status) {
    const orders = ordersData[status] || [];
    if (!orders.length) {
        orderList.innerHTML = `<p style="text-align:center;color:#777;">Không có đơn hàng ở trạng thái "${status}"</p>`;
        return;
    }

    orderList.innerHTML = orders.map(order => `
    <div class="order-item">
      <div class="order-left">
        <img src="../img/sample_product.jpg" alt="Sản phẩm">
        <div class="order-center">
          <p><strong>${order.name || "Bộ bình hoa gốm sứ"} </strong></p>
          <p>Số lượng: ${order.quantity || 2}</p>
        </div>
      </div>
      <div class="order-right">
        <p>${order.date}</p>
        <p class="price">Tổng thanh toán: ${order.total}</p>
        <button class="detail-btn">Xem chi tiết</button>
      </div>
    </div>
  `).join('');
}

buttons.forEach(btn => {
    btn.addEventListener('click', () => {
        document.querySelector('.status-btn.active')?.classList.remove('active');
        btn.classList.add('active');
        renderOrders(btn.textContent.trim());
    });
});

// ✅ Mặc định hiển thị "Tất cả đơn hàng"
renderOrders("Tất cả đơn hàng");

// --- XỬ LÝ CẤP ĐỘ THÀNH VIÊN ---
const rankItems = document.querySelectorAll('.rank-item');
const rankLine = document.querySelector('.rank-line');

const rankData = {
    "Partner": "Cấp độ cơ bản cho thành viên mới.",
    "Partner Silver": "Chi tiêu 2 triệu, giảm 5% đơn hàng.",
    "Partner Gold": "Chi tiêu 5 triệu, giảm 10% & giao nhanh.",
    "Partner Platinum": "Chi tiêu 10 triệu, giảm 15% + quà sinh nhật.",
    "Partner Diamond": "Cao nhất! Giảm 20% & sự kiện VIP."
};

const rankInfo = document.createElement('div');
rankInfo.className = 'rank-info';
rankInfo.innerHTML = `<p>${rankData["Partner"]}</p>`;
rankLine.after(rankInfo);

rankItems.forEach(item => {
    item.addEventListener('click', () => {
        document.querySelector('.rank-item.active')?.classList.remove('active');
        item.classList.add('active');
        const rankName = item.textContent.trim();
        rankInfo.innerHTML = `<p><strong>${rankName}</strong></p><p>${rankData[rankName]}</p>`;
    });
});
