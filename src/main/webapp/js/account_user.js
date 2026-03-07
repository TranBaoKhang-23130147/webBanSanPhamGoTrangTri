document.addEventListener("DOMContentLoaded", () => {
    const editPopup = document.getElementById("edit-popup");
    const passwordPopup = document.getElementById("password-popup");
    const editBtn = document.querySelector(".edit-btn");
    const passBtn = document.querySelector(".pass-btn");
    const cancelBtns = document.querySelectorAll(".cancel-btn");
    editBtn.addEventListener("click", () => {
        editPopup.classList.add("show");
    });
    passBtn.addEventListener("click", () => {
        passwordPopup.classList.add("show");
    });
    cancelBtns.forEach(btn => {
        btn.addEventListener("click", () => {
            editPopup.classList.remove("show");
            passwordPopup.classList.remove("show");
        });
    });
    window.addEventListener("click", (e) => {
        if (e.target === editPopup || e.target === passwordPopup) {
            editPopup.classList.remove("show");
            passwordPopup.classList.remove("show");
        }
    });
});
