function checkPasswordValidity() {
    var input = document.getElementById("floatingPassword");
    var password = input.value;

    var hasNumber = /\d/.test(password);
    var hasUpper = /[A-Z]/.test(password);
    var hasLower = /[a-z]/.test(password);

    if (!hasNumber || !hasUpper || !hasLower) {
        input.setCustomValidity("Пароль должен содержать цифры, Заглавную и прописную буквы!");
    } else {
        input.setCustomValidity("");
    }
}

$(document).ready(function() {
    $('.table').DataTable();
});

