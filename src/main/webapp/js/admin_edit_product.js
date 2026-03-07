function selectImagesWithCKFinder(targetInputId, targetPreviewId) {

    var finder = new CKFinder();
    finder.basePath = contextPath + '/ckfinder/';
    finder.selectMultiple = true;

    finder.selectActionFunction = function(fileUrl, data, allFiles) {

        const input = document.getElementById(targetInputId);
        const preview = document.getElementById(targetPreviewId);

        const files = (allFiles && allFiles.length) ? allFiles : [{url: fileUrl}];
        const names = [];

        files.forEach(file => {

            let fullPath = decodeURIComponent(file.url);
            let fileName = fullPath.substring(fullPath.lastIndexOf('/') + 1);
            fileName = fileName.split('?')[0];

            names.push(fileName);
        });

        input.value = names.join(",");

        preview.innerHTML =
            `<div style="margin-top:10px;color:#27ae60;font-weight:600;">
                <i class="fas fa-check-circle"></i>
                Đã cập nhật ${names.length} ảnh thành công
            </div>`;
    };

    finder.popup();
}


function removeVariant(btn) {

    if (confirm('Bạn có chắc chắn muốn xóa biến thể này?')) {

        btn.parentElement.remove();
        checkDuplicateLive();

    }
}
function checkDuplicateLive() {

    const container = document.getElementById('variant-container');
    const items = container.querySelectorAll('.variant-item');
    const seen = new Set();

    items.forEach(item => {

        const colorSel = item.querySelector('select[name="variantColor[]"]');
        const sizeSel = item.querySelector('select[name="variantSize[]"]');

        if (colorSel && sizeSel && colorSel.value && sizeSel.value) {

            const combo = colorSel.value + "-" + sizeSel.value;

            if (seen.has(combo)) {

                item.classList.add('duplicate-error');

            } else {

                seen.add(combo);
                item.classList.remove('duplicate-error');

            }
        }
    });
}

document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById('editProductForm');

    form.addEventListener('submit', function(e) {

        const items = document.querySelectorAll('.variant-item');
        const seen = new Set();
        let hasError = false;

        if (items.length === 0) {

            alert('Phải có ít nhất 1 biến thể!');
            e.preventDefault();
            return;

        }

        items.forEach((item, index) => {

            const colorSel = item.querySelector('select[name="variantColor[]"]');
            const sizeSel = item.querySelector('select[name="variantSize[]"]');

            if (!colorSel.value || !sizeSel.value) {

                alert('Vui lòng chọn Màu và Size cho biến thể #' + (index + 1));
                hasError = true;
                return;

            }

            const combo = colorSel.value + "-" + sizeSel.value;

            if (seen.has(combo)) {

                alert('Biến thể bị trùng Màu và Size!');
                item.classList.add('duplicate-error');
                hasError = true;

            } else {

                seen.add(combo);

            }
        });

        if (hasError) e.preventDefault();

    });


    document.addEventListener('change', function(e) {

        if (e.target.classList.contains('val-change')) {

            checkDuplicateLive();

        }

    });

});