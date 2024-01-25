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
    
    $("#tagsYearStart, #tagsYearEnd").autocomplete({
        source: function(request, response) {
            $.ajax({
                url: "/autocomplete/year", // URL for city autocomplete
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
        open: function() {

            var widget = $(this).autocomplete("widget");
            
            widget.css ({
                "max-height": "200px",  // Set your desired max-height
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
    });

});
    document.getElementById('dataSection').style.display = 'none';
    document.getElementById('filterSection').style.display = 'block';

    document.addEventListener('DOMContentLoaded', function () {
        document.getElementById('applyForm').addEventListener('submit', function (event) {
            event.preventDefault();
            applyQuery();
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

        history.pushState({ page: 1 }, "Filtered Data", url);
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

    function updateTable(data) {
        var table = document.getElementById('resultTable');
        var tbody = table.getElementsByTagName('tbody')[0];
        tbody.innerHTML = '';

        // Populate data rows
        for (var i = 0; i < data.length; i++) {
            var row = tbody.insertRow();
            row.className = 'body-row';

            var cell1 = row.insertCell(0);
            cell1.className = 'body-cell';
            cell1.innerHTML = data[i].name;

            var cell2 = row.insertCell(1);
            cell2.className = 'body-cell';
            cell2.innerHTML = data[i].abs_avg_temperature_change;
            cell2.style.color = data[i].abs_avg_temperature_change >= 0 ? 'green' : 'red'

            var cell3 = row.insertCell(2);
            cell3.className = 'body-cell';
            cell3.innerHTML = data[i].abs_max_temperature_change;
            cell3.style.color = data[i].abs_max_temperature_change >= 0 ? 'green' : 'red'

            var cell4 = row.insertCell(3);
            cell4.className = 'body-cell';
            cell4.innerHTML = data[i].abs_min_temperature_change;
            cell4.style.color = data[i].abs_min_temperature_change >= 0 ? 'green' : 'red'
        }

        document.getElementById('dataSection').style.display = 'block';
        document.getElementById('filterSection').style.display = 'none';

    }
