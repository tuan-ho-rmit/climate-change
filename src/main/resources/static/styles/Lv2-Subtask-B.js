$(function() {
    $("#tagsCountry").autocomplete({
        minLength: 0,
        source: function(request, response) {
            $.ajax({
                url: "/autocomplete/country",
                type: "GET",
                data: {
                    term: request.term
                },
                dataType: "json",
                success: function(data) {
                    data.sort();
                    response(data);
                }
            });
        },
        open: function(event, ui) {

            var widget = $(this).autocomplete("widget");

            widget.css({
                "max-height": "200px",
                "overflow-y": "auto",
                "overflow-x": "hidden",
                "background-color": "#fff",
                "border-radius": "8px"
            });

            widget.find("li").css({
                "color": "#000",
                "font-family": "Inter",
                "font-size": "14px",
                "font-style": "normal",
                "font-weight": "400",
                "line-height": "normal",
                "cursor": "pointer",
                "padding": "5px",
                "gap": "5px",
                "background-color": "#fff",
            });

    }
    })
    .focus(function() {
        $(this).autocomplete('search', $(this).val())
    });



    $("#tagsYearStart").autocomplete({
        minLength: 0,
        source: function(request, response) {
            $.ajax({
                url: "/autocomplete/year",
                type: "GET",
                data: {
                    term: request.term
                },
                dataType: "json",
                success: function(data) {
                    response(data);
                }
            });
        },
        open: function(event, ui) {

                    var widget = $(this).autocomplete("widget");

                    widget.css({
                        "max-height": "200px",
                        "overflow-y": "auto",
                        "overflow-x": "hidden",
                        "background-color": "#fff",
                        "border-radius": "8px"
                    });

                    widget.find("li").css({
                        "color": "#000",
                        "font-family": "Inter",
                        "font-size": "14px",
                        "font-style": "normal",
                        "font-weight": "400",
                        "line-height": "normal",
                        "cursor": "pointer",
                        "padding": "5px",
                        "gap": "5px",
                        "background-color": "#fff",
                    });

            }
        })
            .focus(function () {
                $(this).autocomplete('search', $(this).val())
            });




    $("#tagsYearEnd").autocomplete({
            minLength: 0,
            source: function(request, response) {
                $.ajax({
                    url: "/autocomplete/year",
                    type: "GET",
                    data: {
                        term: request.term
                    },
                    dataType: "json",
                    success: function(data) {
                        response(data);
                    }
                });
            },
            open: function(event, ui) {

                        var widget = $(this).autocomplete("widget");

                        widget.css({
                            "max-height": "200px",
                            "overflow-y": "auto",
                            "overflow-x": "hidden",
                            "background-color": "#fff",
                            "border-radius": "8px"
                        });

                        widget.find("li").css({
                            "color": "#000",
                            "font-family": "Inter",
                            "font-size": "14px",
                            "font-style": "normal",
                            "font-weight": "400",
                            "line-height": "normal",
                            "cursor": "pointer",
                            "padding": "5px",
                            "gap": "5px",
                            "background-color": "#fff",
                        });

                }
            })
            .focus(function () {
                $(this).autocomplete('search', $(this).val())
            });
            $("#tagsTempOptions").autocomplete({
                minLength: 0,
                source: ["Average", "Minimum", "Maximum"],
                open: function() {
                    var widget = $(this).autocomplete("widget");

                    widget.css({
                        "max-height": "200px",
                        "overflow-y": "auto",
                        "overflow-x": "hidden",
                        "background-color": "#fff",
                        "border-radius": "8px"
                    });

                    widget.find("li").css({
                        "color": "#000",
                        "font-family": "Inter",
                        "font-size": "14px",
                        "font-style": "normal",
                        "font-weight": "400",
                        "line-height": "normal",
                        "cursor": "pointer",
                        "padding": "5px",
                        "gap": "5px",
                        "background-color": "#fff",
                    });
                }
            })
            .focus(function () {
                $(this).autocomplete('search', $(this).val())
            });

    });

document.getElementById('dataSection').style.display = 'none';
document.getElementById('noDataSection').style.display = 'none';
document.getElementById('filterSection').style.display = 'block';

    document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('applyForm').addEventListener('submit', function (event) {
        event.preventDefault();
        if (validateForm()) {
            applyQuery();
        } else {
            return;
        }
    });
});


function applyQuery() {
    var country = document.getElementById('tagsCountry').value;
    var startYear = document.getElementById('tagsYearStart').value;
    var endYear = document.getElementById('tagsYearEnd').value;
    var colorRadio = document.querySelector('input[name="colorRadio"]:checked').value;

    var url = '/applyQuery?Country=' + encodeURIComponent(country) +
              '&StartYear=' + encodeURIComponent(startYear) +
              '&EndYear=' + encodeURIComponent(endYear) +
              '&colorRadio=' + encodeURIComponent(colorRadio);

    loadData(url);
}

    window.onpopstate = function (event) {
        if (event.state) {
            loadData(document.location.href);
        }
    };

function loadData(url) {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var responseData = JSON.parse(xhr.responseText);
            updateTable(responseData);
            history.pushState({}, "Filtered Data", "/high-level/subtask-b");
        }
    };

    xhr.send();
}

function validateForm() {
    var country = document.getElementById('tagsCountry').value;
    var startYear = document.getElementById('tagsYearStart').value;
    var endYear = document.getElementById('tagsYearEnd').value;

    var countryPattern = /^[a-zA-Z\s\-]+$/;
    if (!countryPattern.test(country)) {
        alert('Please enter a valid country name (only characters are allowed).');
        return false;
    }

    var yearPattern = /^[0-9]{4}$/;
    if (!yearPattern.test(startYear) || !yearPattern.test(endYear)) {
        alert('Please enter a valid year (only numbers are allowed).');
        return false;
    }

    startYear = parseInt(startYear);
    endYear = parseInt(endYear);

    if (startYear >= endYear) {
        alert('The start year must be smaller than the end year.');
        return false;
    }

    if (endYear <= startYear) {
        alert('The end year must be larger than the start year.');
        return false;
    }

    return true;
}

$('#resultTable').pagination({
    dataSource: [],
    className: 'paginationjs-theme-grey paginationjs-big',
    pageSize: 10,
    callback: function (data, pagination) {
        var html = '';
        for (var i = 0; i < data.length; i++) {
            html += '<tr class="body-row">' +
                '<td class="body-cell">' + data[i].name + '</td>' +
                '<td class="body-cell">' + formatTemperature(data[i].abs_avg_temperature_change) + '</td>' +
                '<td class="body-cell">' + formatTemperature(data[i].abs_max_temperature_change) + '</td>' +
                '<td class="body-cell">' + formatTemperature(data[i].abs_min_temperature_change) + '</td>' +
                '</tr>';
        }
        $('#resultTable tbody').html(html);
    }
});


function updateTable(data) {
    var table = document.getElementById('resultTable');
    var tbody = table.getElementsByTagName('tbody')[0];
    tbody.innerHTML = '';

        var avgTable = document.getElementById('average');
        var tbodyAvg = avgTable.getElementsByTagName('tbody')[0];
        tbodyAvg.innerHTML = '';

        var minTable = document.getElementById('minimum');
        var tbodyMin = minTable.getElementsByTagName('tbody')[0];
        tbodyMin.innerHTML = '';

        var maxTable = document.getElementById('maximum');
        var tbodyMax = maxTable.getElementsByTagName('tbody')[0];
        tbodyMax.innerHTML = '';

        var option = document.getElementById('tagsTempOptions').value.toLowerCase();
        if (option == null || (option !== 'average' && option !== 'maximum' && option !== 'minimum')) {
            option = 'average';
        }

        var table2 = document.getElementById(option);
        var tbody2 = table2.getElementsByTagName('tbody')[0];

        document.getElementById('average').style.display = 'none';
        document.getElementById('minimum').style.display = 'none';
        document.getElementById('maximum').style.display = 'none';

    if (data.length === 0) {
        document.getElementById('dataSection').style.display = 'none';
        document.getElementById('filterSection').style.display = 'none';
        document.getElementById('noDataSection').style.display = 'block';
        return;
    }

    for (var i = 0; i < Math.min(data.length, 10); i++) {
        var row = tbody.insertRow();
        row.className = 'body-row';

        var cell1 = row.insertCell(0);
        cell1.className = 'body-cell';
        cell1.innerHTML = data[i].name;

        var cell2 = row.insertCell(1);
        cell2.className = 'body-cell';
        cell2.innerHTML = formatTemperature(data[i].abs_avg_temperature_change); // Format and style temperature data

        var cell3 = row.insertCell(2);
        cell3.className = 'body-cell';
        cell3.innerHTML = formatTemperature(data[i].abs_max_temperature_change); // Format and style temperature data

        var cell4 = row.insertCell(3);
        cell4.className = 'body-cell';
        cell4.innerHTML = formatTemperature(data[i].abs_min_temperature_change); // Format and style temperature data
    }

    // Initialize pagination after adding initial 10 rows
    $('#resultTable').pagination({
        dataSource: data,
        className: 'paginationjs-theme-grey paginationjs-big',
        pageSize: 10,
        callback: function (data, pagination) {
            var html = '';
            for (var i = 0; i < data.length; i++) {
                html += '<tr class="body-row">' +
                    '<td class="body-cell">' + data[i].name + '</td>' +
                    '<td class="body-cell">' + formatTemperature(data[i].abs_avg_temperature_change) + '</td>' +
                    '<td class="body-cell">' + formatTemperature(data[i].abs_max_temperature_change) + '</td>' +
                    '<td class="body-cell">' + formatTemperature(data[i].abs_min_temperature_change) + '</td>' +
                    '</tr>';
            }
            $('#resultTable tbody').html(html);
        }
    });

        var dataType = option === "minimum" ? 'abs_min_temperature_change' :
            (option === "maximum" ? 'abs_max_temperature_change' : 'abs_avg_temperature_change')

        var data2 = data;

        data2.sort((a, b) => {
            if (b[dataType] < a[dataType]) return -1;
            if (b[dataType] > a[dataType]) return 1;
            return 0;
        });

        for (var i = 0; i < Math.min(data2.length, 10); i++) {
            var row = tbody2.insertRow();
            row.className = 'body-row';

            var cell1 = row.insertCell(0);
            cell1.className = 'rank';
            cell1.innerHTML = i + 1;

            var cell2 = row.insertCell(0);
            cell2.className = 'body-cell';
            cell2.innerHTML = data2[i].name;

            var cell3 = row.insertCell(1);
            cell3.className = 'body-cell';
            cell3.innerHTML = formatTemperature(data2[i][dataType]);
        }

        $('#' + option).pagination({
            dataSource: data2,
            className: 'paginationjs-theme-grey paginationjs-big',
            pageSize: 10,
            callback: function (data2, pagination) {
                var html = '';
                var pageNumber = pagination.pageNumber;
                for (var i = 0; i < data2.length; i++) {
                    html += '<tr class="body-row">' +
                        '<td class="rank">' + (i+1+(pageNumber - 1) * 10) + '</td>' +
                        '<td class="body-cell">' + data2[i].name + '</td>' +
                        '<td class="body-cell">' + formatTemperature(data2[i][dataType]) + '</td>' +
                        '</tr>';
                }
                $('#' + option + ' tbody').html(html);
            }
        });

        document.getElementById(option).style.display = 'table';

    document.getElementById('dataSection').style.display = 'block';
    document.getElementById('filterSection').style.display = 'none';
    document.getElementById('noDataSection').style.display = 'none';
}




function formatTemperature(value) {
    // Convert the value to a number
    value = parseFloat(value);

    if (value < 0) {
        // If negative, add a "-" symbol and apply red color
        return '<span style="color: red;">-' + Math.abs(value).toFixed(2).padStart(5) + '</span>';
    } else {
        // If positive, add a "+" symbol and apply green color
        return '<span style="color: green;">+' + value.toFixed(2).padStart(6) + '</span>';
    }
}






