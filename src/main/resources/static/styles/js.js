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


document.getElementById("submitButton").addEventListener('click', function(event) {

function applyQuery(event) {
    event.preventDefault();
    const colorRadio = document.querySelector('input[name="colorRadio"]:checked').value;
    const formData = new FormData(document.querySelector(".Section-1"));

    console.log('Submitting request...');

    fetch("/applyQuery", {
        method: "POST",
        body: formData.toString(),
    })
    .then(response => {
        console.log('Received response:', response);

        return response.json();
    })
    .then(data => {
        console.log('Received data:', data); // Log the received data

        // Create the table header
        const table = document.createElement("table");
        const thead = document.createElement("thead");
        const headerRow = document.createElement("tr");

        // Assuming your data has four columns:
        const headers = ["Name", "Average Temperature", "Minimum Temperature", "Maximum Temperature"];
        headers.forEach(header => {
            const th = document.createElement("th");
            th.textContent = header;
            headerRow.appendChild(th);
        });

        thead.appendChild(headerRow);
        table.appendChild(thead);

        // Create table body
        const tbody = document.createElement("tbody");

        // Iterate through the retrieved data and create table rows
        data.forEach(rowData => {
            const row = document.createElement("tr");

            // Assuming each rowData has four values in an array
            const cells = rowData;
            cells.forEach(cellData => {
                const td = document.createElement("td");
                td.textContent = cellData;
                row.appendChild(td);
            });

            tbody.appendChild(row);
        });

        table.appendChild(tbody);

        // Create a new div to hold the table
        const tableContainer = document.createElement("div");

        // Append the table to the new div
        tableContainer.appendChild(table);

        // Clear existing content and append the new div to the main container
        const container = document.querySelector(".Section-2"); // Select a suitable container
        container.innerHTML = "";
        container.appendChild(tableContainer);
    })
}


})




