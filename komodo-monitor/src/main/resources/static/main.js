loader
    .loadJS("plan/plan.js")
    .load("plan-factory")
    .perform(function(){
        var planFactory = loader.get("plan-factory");

        $.getJSON("/plans", function(plans){
            plans.forEach(function(plan){
                console.log(plan);
                var template = planFactory(plan);
                $("#accordion").append(template);
                setInterval(function(){
                    $.getJSON("/plans/" + plan.name, function(plan){
                        template.update(plan);
                    });
                }, 10000);
            })
        });
    });