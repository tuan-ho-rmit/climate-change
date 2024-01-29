function navigateToRegion(selectElement) {
    var selectedRegion = selectElement.value;
    // You can construct the URL based on the selected region and navigate to it
    var url = '/deep-dive/changes-in-regions?region=' + encodeURIComponent(selectedRegion);
    window.location.href = url;
}


function resetFilter (path) {
    var url = '/deep-dive/' +path;
    window.location.href = url;
}



$(function () {
    $("#regionName").autocomplete({
        source: function getListRegions(request, response) {
            var urlParams = new URLSearchParams(window.location.search);
            var regionParam = urlParams.get('region') || 'Country';
            $.ajax({
                url: "/getListRegions",
                dataType: "json",
                data: {
                    region: regionParam,
                    search: request.term

                },
                success: function (data) {
                    response(data);
                },
                error: function (xhr, status, error) {
                    console.error("Error fetching region names:", error);
                }
            });
        },
        open: function () {
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
                "width": "100%"
            });
            widget.on("menucreate", function () {
                $(this).find(".ui-menu .ui-menu-item").hover(
                    function () {
                        $(this).css("background-color", "#EEEEEE !important");
                    },
                    function () {
                        $(this).css("background-color", "#fff");
                    }
                );
            });
        }
    });
});



document.querySelector('.more').innerHTML += '...';
