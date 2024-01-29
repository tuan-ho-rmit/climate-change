$(document).ready(function() {
    $.getJSON("displayData", function(data) {
        $("#earliestGlobalTemperatureYear").text(data.earliestGlobalTemperatureYear);
        $("#latestGlobalTemperatureYear").text(data.latestGlobalTemperatureYear);
        $("#earliestGlobalTempYear").text(data.earliestGlobalTempYear);
        $("#latestGlobalTempYear").text(data.latestGlobalTempYear);
        $("#averageTemperatureEarliestYear").text(data.averageTemperatureEarliestYear);
        $("#averageTemperatureLatestYear").text(data.averageTemperatureLatestYear);
        $("#earliestPopulationYear").text(data.earliestPopulationYear);
        $("#latestPopulationYear").text(data.latestPopulationYear);
        $("#earliestPopYear").text(data.earliestPopYear);
        $("#latestPopYear").text(data.latestPopYear);
        $("#earliestPopulationNumber").text(data.earliestPopulationNumber);
        $("#latestPopulationNumber").text(data.latestPopulationNumber);
        $("#totalYearPop").text(data.totalYearPop);
        $("#totalYearGlobal").text(data.totalYearGlobal);
    });
    $("body").css("display", "none");

        // After 3 seconds, show the body
        setTimeout(function() {
            $("body").css("display", "");
        }, 700); // 3 second delay
});
