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

    $("#mailButton").click(function() {
        $("#mailContent").slideToggle();
        $("#mailIcon").toggleClass("chevron-compact-down-icon");
    });
});
$(document).ready(function() {
    $(".section-content").hide();
});


function showNotification() {
    var notification = document.getElementById("notification");
    notification.style.display = "block";
    setTimeout(function() {
        notification.style.display = "none";
    }, 10000);
}
function closeNotification() {
    var notification = document.getElementById("notification");
    notification.style.display = "none";
}


function initializeCharts(forecastRevComp, forecastRevMarket, forecastMarketShare) {
    function getMonthName(monthNumber) {
        var months = [
            'Январь',
            'Февраль',
            'Март',
            'Апрель',
            'Май',
            'Июнь',
            'Июль',
            'Август',
            'Сентябрь',
            'Октябрь',
            'Ноябрь',
            'Декабрь'
        ];
        return months[monthNumber - 1];
    }
    var monthNumbers = Array.from({length: 12}, (_, i) => i + 1);
    // Создание графика выручки компании
    var revCompChartCtx = document.getElementById('revCompChart').getContext('2d');
    new Chart(revCompChartCtx, {
        type: 'line',
        data: {
            labels: monthNumbers,
            datasets: [{
                label: 'Выручка компании',
                data: forecastRevComp,
                borderColor: '#EC6A32',
                fill: false
            }, {
                label: 'Выручка рынка',
                data: forecastRevMarket,
                borderColor: '#4F5D75',
                fill: false
            }]
        },
        options: {
            responsive: false,
            maintainAspectRatio: false,
            scales: {
                y: {
                    suggestedMin: Math.min(Math.min.apply(null, forecastRevComp), Math.min.apply(null, forecastRevMarket)), // Минимальное значение из обоих списков
                    suggestedMax: Math.max(Math.max.apply(null, forecastRevComp), Math.max.apply(null, forecastRevMarket)) // Максимальное значение из обоих списков
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        title: function(context) {
                            // Получение названия месяца для заголовка подсказки
                            var monthNumber = context[0].parsed.x;
                            return getMonthName(monthNumber);
                        },
                        label: function(context) {
                            var label = context.dataset.label || '';
                            if (label) {
                                label += ': ';
                            }
                            if (context.parsed.y !== null) {
                                // Добавление символа процента к значению и форматирование подсказки
                                label += '$' + context.parsed.y.toLocaleString();
                            }
                            return label;
                        }
                    }
                }
            }
        }
    });

    // Создание графика рыночной доли
    var marketShareChartCtx = document.getElementById('marketShareChart').getContext('2d');
    new Chart(marketShareChartCtx, {
        type: 'line',
        data: {
            labels: monthNumbers,
            datasets: [{
                label: 'Рыночная доля',
                data: forecastMarketShare,
                borderColor: '#2d3142',
                fill: false
            }]
        },
        options: {
            responsive: false,
            maintainAspectRatio: false,
            scales: {
                y: {
                    suggestedMin: Math.min.apply(null, forecastMarketShare)-0.1,
                    suggestedMax: Math.max.apply(null, forecastMarketShare)+0.1
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        title: function(context) {
                            // Получение названия месяца для заголовка подсказки
                            var monthNumber = context[0].parsed.x;
                            return getMonthName(monthNumber);
                        },
                        label: function(context) {
                            var label = context.dataset.label || '';
                            if (label) {
                                label += ': ';
                            }
                            if (context.parsed.y !== null) {
                                // Добавление символа процента к значению и форматирование подсказки
                                label += context.parsed.y.toFixed(2) + '%';
                            }
                            return label;
                        }
                    }
                }
            }
        }
    });
}


// $(document).ready(function() {
//     $('.dataTable').DataTable();
// });
