loader
    .loadJS("//code.jquery.com/jquery-1.11.0.min.js")
    .perform(function(){

        function find(id, callback){
            $.getJSON("/plans/" + id, callback);
        }

        function findAll(callback){
            $.getJSON("/plans/", callback);
        }

        return {
            find : find,
            findAll : findAll
        }
    }).as("plan-service");