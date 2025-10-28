// === Xử lý điều hướng sidebar + load nội dung trang ===
const nav = document.getElementById('nav');
const mainContent = document.getElementById('main-content');

// Hàm load nội dung HTML con
async function loadPage(page) {
  try {
    // ensure we load the admin variant if caller passed a short route
    const fileName = page.endsWith('_admin') ? page : `${page}_admin`;
    const res = await fetch(`../html/${fileName}.html`);
    if (!res.ok) throw new Error("Không thể tải trang");
    const html = await res.text();
    mainContent.innerHTML = html;
    // Run page-specific initializers when needed
    if (fileName === 'overview_admin') {
      initOverviewPage();
    }
  } catch (err) {
    mainContent.innerHTML = `<p style="color:red;">Lỗi khi tải trang: ${err.message}</p>`;
  }
}

// Mặc định load trang Tổng quan
loadPage('overview_admin');

// Initializer for the overview fragment (runs after overview_admin.html is injected)
function initOverviewPage() {
  // Scoped query inside the loaded fragment
  try {
    const q = mainContent.querySelector('#q');
    const ordersTable = mainContent.querySelector('.table-section table');
    const rows = ordersTable ? ordersTable.querySelectorAll('tbody tr') : [];

    if (q && rows.length) {
      q.addEventListener('input', function () {
        const val = this.value.trim().toLowerCase();
        if (!val) { rows.forEach(r => r.style.display = ''); return; }
        rows.forEach(r => {
          r.style.display = (r.innerText.toLowerCase().includes(val)) ? '' : 'none';
        });
      });
    }
  } catch (e) {
    // swallow errors to avoid breaking app when fragment structure differs
    console.warn('initOverviewPage error:', e);
  }
}

// Sự kiện click đổi trang
nav.addEventListener('click', (e) => {
  const item = e.target.closest('.nav-item');
  if (!item) return;

  // cập nhật active
  document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
  item.classList.add('active');

  // load nội dung tương ứng
  const route = item.dataset.route;
  loadPage(route);
});
