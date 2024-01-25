$(document).ready(function() {
    $.getJSON("displayData", function(data) {
        $("#populationYearRange").text(data.populationYearRange);
        $("#globalTemperatureYearRange").text(data.globalTemperatureYearRange);
        $("#earliestGlobalTemperatureYear").text(data.earliestGlobalTemperatureYear);
        $("#latestGlobalTemperatureYear").text(data.latestGlobalTemperatureYear);
        $("#earliestPopulationYear").text(data.earliestPopulationYear);
        $("#latestPopulationYear").text(data.latestPopulationYear);
        $("#averageTemperatureEarliestYear").text(data.averageTemperatureEarliestYear);
        $("#averageTemperatureLatestYear").text(data.averageTemperatureLatestYear);
    });
});
