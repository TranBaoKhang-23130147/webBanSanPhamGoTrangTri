function openEditForm() {
    document.getElementById('editProfileModal').style.display = 'flex';
}

function closeEditForm() {
    document.getElementById('editProfileModal').style.display = 'none';
}

function openPassForm() {
    document.getElementById('changePasswordModal').style.display = 'flex';
}

function closePassForm() {
    document.getElementById('changePasswordModal').style.display = 'none';
}

window.onclick = function(event) {
    let editModal = document.getElementById('editProfileModal');
    let passModal = document.getElementById('changePasswordModal');

    if (event.target == editModal) closeEditForm();
    if (event.target == passModal) closePassForm();
};

function selectAvatarWithCKFinder() {

    var finder = new CKFinder();
    finder.basePath = contextPath + '/ckfinder/';

    finder.selectActionFunction = function(fileUrl) {

        var relativeUrl = fileUrl;

        if (contextPath && relativeUrl.startsWith(contextPath)) {
            relativeUrl = relativeUrl.substring(contextPath.length);
        }

        if (relativeUrl && !relativeUrl.startsWith('/')) {
            relativeUrl = '/' + relativeUrl;
        }

        document.getElementById('user-avatar-url').value = relativeUrl;

        document.getElementById('user-avatar-display').src = contextPath + relativeUrl;
    };

    finder.popup();
}

document.addEventListener("DOMContentLoaded", function () {

    let form = document.querySelector('#changePasswordModal form');

    if (form) {
        form.onsubmit = function(e) {

            let pass = document.getElementById('newPassword').value;
            let confirm = document.getElementById('confirmPassword').value;

            if (pass !== confirm) {
                document.getElementById('passError').style.display = 'block';
                e.preventDefault();
                return false;
            }
        };
    }

});