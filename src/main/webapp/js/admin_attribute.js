
const colorModal = document.getElementById("colorModal");
const sizeModal = document.getElementById("sizeModal");

const colorId = document.getElementById("colorId");
const colorName = document.getElementById("colorName");
const colorCode = document.getElementById("colorCode");

const sizeId = document.getElementById("sizeId");
const sizeName = document.getElementById("sizeName");

function openColor(){
    colorModal.style.display='flex';
    colorId.value='';
    colorName.value='';
    colorCode.value='';
}

function editColor(id,name,code){
    openColor();
    colorId.value=id;
    colorName.value=name;
    colorCode.value=code;
}

function openSize(){
    sizeModal.style.display='flex';
    sizeId.value='';
    sizeName.value='';
}

function editSize(id,name){
    openSize();
    sizeId.value=id;
    sizeName.value=name;
}

window.onclick=e=>{
    if(e.target===colorModal) colorModal.style.display='none';
    if(e.target===sizeModal) sizeModal.style.display='none';
}

function deleteColor(id){
    Swal.fire({
        title:'Xóa màu?',
        icon:'warning',
        showCancelButton:true
    }).then(r=>{
        if(r.isConfirmed)
            location='admin-attribute?delete=color&id='+id;

    });
}

function deleteSize(id){
    Swal.fire({
        title:'Xóa size?',
        icon:'warning',
        showCancelButton:true
    }).then(r=>{
        if(r.isConfirmed)
            location='admin-attribute?delete=size&id='+id;

    });
}
