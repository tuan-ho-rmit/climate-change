document.addEventListener("DOMContentLoaded", function() {
        fetchData();
});

function fetchData() {
    fetch('http://localhost:8080/applyQuery',{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => response.json())
        .then(data => {
            const section2 = document.querySelector(".Section-2");
            section2.innerHTML = "";
            const table = document.createElement("table");
            table.id = "tempTable";
            section2.appendChild(table);

            const headerRow = table.insertRow();
            headerRow.innerHTML = `
                <th>Name</th>
                <th>Average Temperature Change</th>
                <th>Maximum Temperature Change</th>
                <th>Minimum Temperature Change</th>
            `;

            const tableBody = document.getElementById('tempTable').getElementsByTagName('tbody')[0];
            data.forEach(retrievedData => {
                let row = tableBody.insertRow();
                let cellName = row.insertCell(0);
                let cellAverage = row.insertCell(1);
                let cellMax = row.insertCell(2);
                let cellMin = row.insertCell(3);

                cellName.textContent = retrievedData.name;
                cellAverage.textContent = retrievedData.abs_avg_temperature_change;
                cellMax.textContent = retrievedData.abs_max_temperature_change;
                cellMin.textContent = retrievedData.abs_min_temperature_change;
            });
            applyTableStyling();
        })
        .catch(error => {
            console.error('Error fetching data: ', error);
        });
}

function applyTableStyling() {
    const table = document.getElementById('tempTable');

    table.style.width = '100%';
    table.style.borderCollapse = 'collapse';
    const cells = table.querySelectorAll('th, td');
    cells.forEach(cell => {
        cell.style.border = '1px solid #dddddd';
        cell.style.textAlign = "left";
        cell.style.padding = '8px';
    });

    const thElements = table.querySelectorAll('th')
    thElements.forEach(th => {
        th.style.backgroundColor = "f2f2f2";
    });

    const evenRows = table.querySelectorAll('tr:nth-child(even)');
    evenRows.forEach(row => {
        row.style.backgroundColor = "#f9f9f9";
    })
}
