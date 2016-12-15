loader
    .loadTemplate("plan", "plan/plan.html")
    .perform(function(){

        dom.using('tr', 'td');

        function statusGlyph(plan){
            if (!plan.active){
                return "glyphicon-pause";
            }
            if (plan.lastUpdate == null){
                return "glyphicon-question-sign";
            }
            if (plan.successful){
                return "glyphicon-ok";
            }
            return "glyphicon-remove";
        }

        function statusColor(plan){
            if (!plan.active){
                return "grey";
            }
            if (plan.lastUpdate == null){
                return "#ff7864";
            }
            if (plan.successful){
                return "green";
            }
            return "red";
        }

        return function(plan){
            var template = templates.get("plan");

            template._nameLink.add({href: "#" + plan.name});
            template._nameSpan.add(plan.name);
            template._collapsePanel.add({id : plan.name});

            template.update = function(plan){

                template._statusGlyph.add({
                    'class' : "glyphicon " + statusGlyph(plan),
                    style : "color:" + statusColor(plan)
                })

                if (plan.lastUpdate){
                    var last = plan.lastUpdate;
                    template._updateDate.update(last.substr(0, last.indexOf('T')));
                    template._updateTime.update(last.substr(last.indexOf("T") + 1, last.indexOf(".") - last.indexOf("T") - 1));
                    template._updateMillis.update(last.substr(last.indexOf(".") + 1));
                } else {
                    template._updateDate.empty();
                    template._updateTime.empty();
                    template._updateMillis.empty();
                }

                template._interval.update(plan.interval);

                var tr = template._configBody.empty();
                Object.keys(plan.config).forEach(function(key){
                    var tr = template._configBody.tr();
                    tr.td(key);
                    tr.td(plan.config[key]);
                });
            }


            template.update(plan);
            return template;
        }
    }).as("plan-factory");