$(function() {

    $("#tagsCountry").autocomplete({
        source: function(request, response) {
            $.ajax({
                url: "/autocomplete/country",
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
            
        widget.on("menucreate", function() {
            $(this).find(".ui-menu .ui-menu-item").hover(
                function() {
                    $(this).css("background-color", "#EEEEEE !important"); // Use !important only if necessary
                },
                function() {
                    $(this).css("background-color", "#fff");
                }
            );
        });
        
    }
});
    
    $("#tagsYearStart").autocomplete({
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

                widget.on("menucreate", function() {
                    $(this).find(".ui-menu .ui-menu-item").hover(
                        function() {
                            $(this).css("background-color", "#EEEEEE !important"); // Use !important only if necessary
                        },
                        function() {
                            $(this).css("background-color", "#fff");
                        }
                    );
                });

            }
        });



    $("#tagsYearEnd").autocomplete({
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

                    widget.on("menucreate", function() {
                        $(this).find(".ui-menu .ui-menu-item").hover(
                            function() {
                                $(this).css("background-color", "#EEEEEE !important"); // Use !important only if necessary
                            },
                            function() {
                                $(this).css("background-color", "#fff");
                            }
                        );
                    });

                }
            });

        // Add this JavaScript function to show the dropdown list and fetch data
        $(document).ready(function() {
            // Function to show the dropdown list when the countryDropdownIcon is clicked
            $("#countryDropdownIcon").click(function(event) {
                event.stopPropagation(); // Prevents the click event from bubbling up

                var dropdown = $("#dropdownCountry");
                if (dropdown.css("display") === "none") {
                    // Show the dropdown if it's hidden
                    dropdown.show();

                    // Fetch data from the server
                    $.ajax({
                        url: "/fetch/countries",
                        type: "GET",
                        dataType: "json",
                        success: function(data) {
                            // Populate the dropdown with options
                            dropdown.empty();
                            $.each(data, function(index, value) {
                                dropdown.append($("<div>").text(value).addClass("dropdown-item"));
                            });

                            // Handle click event for dropdown items
                            dropdown.on("click", ".dropdown-item", function() {
                                var selectedOption = $(this).text();
                                $("#tagsCountry").val(selectedOption);
                                dropdown.hide();
                            });
                        },
                        error: function(xhr, status, error) {
                            // Handle errors
                            console.error("Error fetching countries:", error);
                        }
                    });
                } else {
                    // Hide the dropdown if it's already visible
                    dropdown.hide();
                }
            });


            $(document).click(function() {
                $("#dropdownCountry").hide();
            });


            $("#startDropdownIcon").click(function(event) {
                event.stopPropagation();

                var dropdown = $("#dropdownStart");
                if (dropdown.css("display") === "none") {

                    dropdown.show();

                    $.ajax({
                        url: "/fetch/years",
                        type: "GET",
                        dataType: "json",
                        success: function(data) {
                            dropdown.empty();
                            $.each(data, function(index, value) {
                                dropdown.append($("<div>").text(value).addClass("dropdown-item"));
                            });
                            dropdown.on("click", ".dropdown-item", function() {
                                var selectedOption = $(this).text();
                                $("#tagsYearStart").val(selectedOption);
                                dropdown.hide();
                            });
                        },
                        error: function(xhr, status, error) {
                            console.error("Error fetching years:", error);
                        }
                    });
                } else {
                    dropdown.hide();
                }
            });
            $(document).click(function() {
                $("#dropdownStart").hide();
            });

            $("#endDropdownIcon").click(function(event) {
                event.stopPropagation();

                var dropdown = $("#dropdownEnd");
                if (dropdown.css("display") === "none") {

                    dropdown.show();

                    $.ajax({
                        url: "/fetch/years",
                        type: "GET",
                        dataType: "json",
                        success: function(data) {
                            dropdown.empty();
                            $.each(data, function(index, value) {
                                dropdown.append($("<div>").text(value).addClass("dropdown-item"));
                            });
                            dropdown.on("click", ".dropdown-item", function() {
                                var selectedOption = $(this).text();
                                $("#tagsYearEnd").val(selectedOption);
                                dropdown.hide();
                            });
                        },
                        error: function(xhr, status, error) {
                            console.error("Error fetching years:", error);
                        }
                    });
                } else {
                    dropdown.hide();
                }
            });
            $(document).click(function() {
                $("#dropdownEnd").hide();
            });

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

    history.pushState({}, "Filtered Data", url);
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
    className: 'paginationjs-theme-green paginationjs-big',
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
        className: 'paginationjs-theme-green paginationjs-big',
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

    document.getElementById('dataSection').style.display = 'block';
    document.getElementById('filterSection').style.display = 'none';
    document.getElementById('noDataSection').style.display = 'none';
}




function formatTemperature(value) {
    // Convert the value to a number
    value = parseFloat(value);

    // Check if the value is negative
    if (value < 0) {
        // If negative, add a "-" symbol and apply red color
        return '<span style="color: red;">-' + Math.abs(value).toFixed(2).padStart(5) + '</span>';
    } else {
        // If positive, add a "+" symbol and apply green color
        return '<span style="color: green;">+' + value.toFixed(2).padStart(6) + '</span>';
    }
}





