function selectImagesWithCKFinder(targetInputId, targetPreviewId) {
    var finder = new CKFinder();
    finder.basePath = '${pageContext.request.contextPath}/ckfinder/';
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
        preview.innerHTML = `
            <span style="color:green;font-weight:600;">
                ✔ Đã thêm ${names.length} ảnh thành công
            </span>
        `;
    };

    finder.popup();
}


function addVariant() {

    const container = document.getElementById("variant-container");

    const template = document.getElementById("variant-template").innerHTML;

    const wrapper = document.createElement("div");

    wrapper.innerHTML = template;

    container.appendChild(wrapper.firstElementChild);

}
function validateVariants() {
    const items = document.querySelectorAll('.variant-item');
    const seen = new Set();
    let errorMsg = "";

    for (let i = 0; i < items.length; i++) {
        const colorId = items[i].querySelector('select[name="colorId[]"]').value;
        const sizeId = items[i].querySelector('select[name="sizeId[]"]').value;

        if (!colorId || !sizeId) {
            alert("Vui lòng chọn đầy đủ Màu và Size cho biến thể số " + (i + 1));
            return false;
        }

        const combo = colorId + "-" + sizeId;
        if (seen.has(combo)) {
            const colorName = items[i].querySelector('select[name="colorId[]"] option:checked').text;
            const sizeName = items[i].querySelector('select[name="sizeId[]"] option:checked').text;
            errorMsg += `- Dòng ${i+1}: Trùng cặp [${colorName} - ${sizeName}]\n`;
        }
        seen.add(combo);
    }

    if (errorMsg) {
        alert("Trùng biến tể :\n" + errorMsg);
        return false;
    }
    return true;
}
function checkDuplicateLive() {
    const items = document.querySelectorAll('.variant-item');
    const seen = new Set();
    items.forEach(item => {
        const cId = item.querySelector('select[name="colorId[]"]').value;
        const sId = item.querySelector('select[name="sizeId[]"]').value;
        if (cId && sId) {
            const key = cId + "-" + sId;
            if (seen.has(key)) {
                item.style.border = "2px solid red";
            } else {
                item.style.border = "1px solid #eee";
                seen.add(key);
            }
        }
    });
}

document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    form.onsubmit = function() {
        return validateVariants();
    };
});