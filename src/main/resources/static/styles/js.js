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





