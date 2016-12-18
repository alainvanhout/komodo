loader
    .loadJS("plan/plan-template.js")
    .loadJS("plan/plan-service.js")
    .load("plan-template")
    .load("plan-service")
    .perform(function(){
        var planService = loader.get("plan-service");
        var planTemplate = loader.get("plan-template");

        return function(plan){
            var template = planTemplate(plan);
            setInterval(function(){
                planService.find(plan.name, function(plan){
                    template.set(plan);
                });
            }, 10000);
            return template;
        }
    }).as("plan-component");
