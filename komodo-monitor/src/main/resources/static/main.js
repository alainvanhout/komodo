loader
    .loadJS("plan/plan-component.js")
    .loadJS("plan/plan-service.js")
    .load("plan-component")
    .load("plan-service")
    .perform(function(){
        var planComponent = loader.get("plan-component");
        var planService = loader.get("plan-service");
        var accordion = dom(document.getElementById("plan-list"));

        planService.findAll(function(plans){
            plans.forEach(function(plan){
                accordion.add(planComponent(plan));
            })
        });
    });