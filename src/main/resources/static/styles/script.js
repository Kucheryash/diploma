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

// When the user scrolls down 400px from the top of the document, show the button
window.onscroll = function() {scrollFunction()};

function scrollFunction() {
    if (document.body.scrollTop > 400 || document.documentElement.scrollTop > 400) {
        document.getElementById("topBtn").style.display = "block";
    } else {
        document.getElementById("topBtn").style.display = "none";
    }
}
// When the user clicks on the button, scroll to the top of the document
function topFunction() {
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
}

$(document).ready(function() {
    $("#swotButton").click(function() {
        $("#swotContent").slideToggle();
        $("#swotIcon").toggleClass("chevron-compact-down-icon");
    });

    $("#competitorButton").click(function() {
        $("#competitorContent").slideToggle();
        $("#competitorIcon").toggleClass("chevron-compact-down-icon");
    });
});
$(document).ready(function() {
    $(".section-content").hide();
});

// $(document).ready(function() {
//     $('.dataTable').DataTable();
// });