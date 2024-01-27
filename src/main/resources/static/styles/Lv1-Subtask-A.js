$(document).ready(function() {
    $.getJSON("displayData", function(data) {
        $("#globalTemperatureYearRange").text(data.globalTemperatureYearRange);
        $("#earliestGlobalTemperatureYear").text(data.earliestGlobalTemperatureYear);
        $("#latestGlobalTemperatureYear").text(data.latestGlobalTemperatureYear);
        $("#earliestGlobalTempYear").text(data.earliestGlobalTemperatureYear);
        $("#latestGlobalTempYear").text(data.latestGlobalTemperatureYear);
        $("#averageTemperatureEarliestYear").text(data.averageTemperatureEarliestYear);
        $("#averageTemperatureLatestYear").text(data.averageTemperatureLatestYear);
        $("#populationYearRange").text(data.populationYearRange);
        $("#earliestPopulationYear").text(data.earliestPopulationYear);
        $("#latestPopulationYear").text(data.latestPopulationYear);
        $("#earliestPopYear").text(data.earliestPopYear);
        $("#latestPopYear").text(data.latestPopYear);
        $("#earliestPopulationNumber").text(data.earliestPopulationNumber);
        $("#latestPopulationNumber").text(data.latestPopulationNumber);
    });
});
