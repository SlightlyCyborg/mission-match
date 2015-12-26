(function(){
    $("#submit-button").click(function(e){
        e.preventDefault();
        var data = $("form").serialize();
        $.post("submit", data, function(d){
            console.log(d);
        });
    });
})();
