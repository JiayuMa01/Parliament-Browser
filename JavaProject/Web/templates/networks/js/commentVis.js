/**
 * methods for retrieving and processing the data for the network visualization
 * graph from https://d3-graph-gallery.com/graph/network_basic.html
 * modified by Rodiana Koukouzeli
 */


/**
 * updates graph after filter parameters have been set
 * @author Rodiana Koukouzeli
 */
function updateGraph(){
    var limit = $('#limit').val();
    var startDate = new Date(document.getElementById("startDate").value);
    var endDate = new Date(document.getElementById("endDate").value);
    var startDateAsLong = startDate.getTime();
    var endDateAsLong = endDate.getTime();
    console.log(limit);
    console.log(startDateAsLong);
    console.log(endDateAsLong);
    $("#my_dataviz").empty();
    $("#protocolGraph").empty();

    loadCommentNet(limit,startDateAsLong,endDateAsLong);
}

/**
 * build graph with user conrtolles variables
 * @param limit
 * @param startDate
 * @param endDate
 * @author Rodiana Koukouzeli
 */
function getSpecific(limit, startDate, endDate){
    $("#my_dataviz").empty();
    $("#protocolGraph").empty();
    loadCommentNet(limit, startDate,endDate);
}


/**
 * buils graph the same way as loadCategoryNet only for a specified protocol
 * @author Rodiana Koukouzeli
 */
function getProtocol(){
    alert("This may take a little while.");
    var protocol = document.getElementById("protocol").value;
    console.log(protocol);
    $("#protocolGraph").empty();
    $("#my_dataviz").empty();
    loadProtocolNet(protocol);
}

/**
 * colors all category nodes blue
 * @param party
 * @returns {string}
 * @author Rodiana Koukouzeli
 */
function colorParty(party){
    if (party == "SPD"){
        return "#EA0909FF";
    }else if(party == "CDU"){
        return "#000000FF";
    }else if(party == "BÜNDNIS 90/DIE GRÜNEN"){
        return "#209534"
    }else if(party=="FDP"){
        return "#e6ff00";
    }else if(party == "DIE LINKE."){
        return "#7846a1";
    }else if(party == "AfD"){
        return "#0278fc";
    }else {
        return "#7c8692";
    }
}


/**
 * colors all links depending on what sentiment they have
 * @param sentiment
 * @returns {string}
 * @author Rodiana Koukouzeli
 */
function colorLine(sentiment){
    //let sValue = parseFloat(sentiment);
    if (sentiment < 0.0){
        return "#c23030";
    }else if(sentiment > 0.0){
        return "#3E9D3CFF";
    }else{
        return "#73797EFF";
    }
}



/**
 * buidls force graph with variable parameters
 * @param limit
 * @param startDate
 * @param endDate
 * @author Rodiana Koukouzeli
 */

function loadCommentNet(limit, startDate, endDate){
// set the dimensions and margins of the graph
    const margin = {top: 10, right: 30, bottom: 30, left: 40},
        width = 1500 - margin.left - margin.right,
        height = 1500 - margin.top - margin.bottom;

// append the svg object to the body of the page
    const svg = d3.select("#my_dataviz")
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");




    $.ajax({
        url: "http://localhost:4567/network/comments?limit=" + limit + "&startDate=" + startDate + "&endDate=" + endDate,
        type: 'GET',
        dataType: 'json',
        async: true,
        success: function (data) {

            console.log(data);
            // Initialize the links
            const link = svg
                .selectAll("line")
                .data(data.links)
                .join("line")
                .attr("stroke", function (d) {
                    return colorLine(d.sentiment);
                });


            // Initialize the nodes
            const node = svg
                .selectAll("circle")
                .data(data.nodes)
                .join("circle")
                .attr("r", 3)
                .attr("fill", function (d) {
                    return colorParty(d.party);
                });


            var text = svg.selectAll(".text")
                .data(data.nodes)
                .enter()
                .append("text")
                .attr("dx", 5)
                .attr("dy", 4)
                .attr("fill", "black")
                .attr("font-family", "sans-serif")
                .attr("font-size", "5px")
                .text(function (d) {
                    return d.id;
                });


            // Let's list the force we want to apply on the network
            const simulation = d3.forceSimulation(data.nodes)                 // Force algorithm is applied to data.nodes
                .force("link", d3.forceLink()                               // This force provides links between nodes
                    .id(function (d) {
                        return d.id;
                    })                     // This provide  the id of a node
                    .links(data.links)                                    // and this the list of links
                )
                .force("charge", d3.forceManyBody().strength(-2))         // This adds repulsion between nodes. Play with the -400 for the repulsion strength
                .force("center", d3.forceCenter(width / 2, height / 2))
                .force("collision", d3.forceCollide().radius(5))    // This force attracts nodes to the center of the svg area
                .on("end", ticked);

            // This function is run at each iteration of the force algorithm, updating the nodes position.
            function ticked() {
                link
                    .attr("x1", function (d) {
                        return d.source.x;})
                    .attr("y1", function (d) {
                        return d.source.y;})
                    .attr("x2", function (d) {
                        return d.target.x;})
                    .attr("y2", function (d) {
                        return d.target.y;});


                node
                    .attr("cx", function (d) {
                        return d.x;})
                    .attr("cy", function (d) {
                        return d.y;});

                text.attr("x", function (d) {
                    return d.x;})
                    .attr("y", function (d) {
                        return d.y;});
            }
        },
        error: function(ex){
            alert("Fehler beim laden des Graphen, versuchen Sie es erneut.")
        }
    });
}


/**
 * also build a force graph but only for a specific protocol
 * @param protocol
 * @author Rodiana Koukouzeli
 */
function loadProtocolNet(protocol){

    const margin = {top: 10, right: 30, bottom: 30, left: 40},
        width = 1500 - margin.left - margin.right,
        height = 1500 - margin.top - margin.bottom;

// append the svg object to the body of the page
    const svg = d3.select("#protocolGraph")
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");



    $.ajax({
        url: "http://localhost:4567/network/comments/protocol?protocol=" + protocol,
        type: 'GET',
        dataType: 'json',
        async: true,
        success: function (data) {

            console.log(data);
            // Initialize the links
            const link = svg
                .selectAll("line")
                .data(data.links)
                .join("line")
                .attr("stroke", function (d) {
                    return colorLine(d.sentiment);
                });


            // Initialize the nodes
            const node = svg
                .selectAll("circle")
                .data(data.nodes)
                .join("circle")
                .attr("r", 3)
                .attr("fill", function (d) {
                    return colorParty(d.party);
                });


            var text = svg.selectAll(".text")
                .data(data.nodes)
                .enter()
                .append("text")
                .attr("dx", 5)
                .attr("dy", 4)
                .attr("fill", "black")
                .attr("font-family", "sans-serif")
                .attr("font-size", "5px")
                .text(function (d) {
                    return d.id;
                });


            // Let's list the force we want to apply on the network
            const simulation = d3.forceSimulation(data.nodes)                 // Force algorithm is applied to data.nodes
                .force("link", d3.forceLink()                               // This force provides links between nodes
                    .id(function (d) {
                        return d.id;
                    })                     // This provide  the id of a node
                    .links(data.links)                                    // and this the list of links
                )
                .force("charge", d3.forceManyBody().strength(-2))         // This adds repulsion between nodes. Play with the -400 for the repulsion strength
                .force("center", d3.forceCenter(width / 2, height / 2))
                .force("collision", d3.forceCollide().radius(5))    // This force attracts nodes to the center of the svg area
                .on("end", ticked);

            // This function is run at each iteration of the force algorithm, updating the nodes position.
            function ticked() {
                link
                    .attr("x1", function (d) {
                        return d.source.x;})
                    .attr("y1", function (d) {
                        return d.source.y;})
                    .attr("x2", function (d) {
                        return d.target.x;})
                    .attr("y2", function (d) {
                        return d.target.y;});


                node
                    .attr("cx", function (d) {
                        return d.x;})
                    .attr("cy", function (d) {
                        return d.y;});

                text.attr("x", function (d) {
                    return d.x;})
                    .attr("y", function (d) {
                        return d.y;});
            }
        },
        error: function(ex){
            alert("Fehler beim laden des Graphen, versuchen Sie es erneut.")
        }
    });

}