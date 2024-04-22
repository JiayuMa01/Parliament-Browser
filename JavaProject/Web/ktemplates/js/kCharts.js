/**
 * @author Kevin Schuff
 */
// Execute this when the document has been loaded
$(document).ready(function () {
    console.log("Im up and running!");
    // on enter the search button should be pressed
    document.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            document.getElementById("buttonOpenOverlay").click();
        }
        // on escape to overlay from the search should be closed
        else if (event.key === "Escape") {
            document.getElementById("buttonCloseOverlay").click();
        }
    });
    // display loading text, while comment sentiment chart is not done, which typically takes the longest time
    $("#loadingText").show();
    // draw charts with some default settings
    drawLineChart(10, 1, 2665525600000, "#speechTokenLineChart", "");
    drawVerticalBarChart(5, 1, 2665525600000, "#speechPosVerticalBar", "");
    drawSpiderChart("speech", 1, 2665525600000, "#speechSentimentSpider", "");
    drawSpiderChart("comment", 1, 2665525600000, "#commentSentimentSpider", "");
    drawMultipleLineChart(10, 1, 2665525600000, "#speechNamedEntityMultipleLine", "");
    drawBarChart("speech", "speaker", 5, 1, 2665525600000, "#speechSpeakerBar", "");
    drawBarChart("speech", "lemma", 5, 1, 2665525600000, "#speechLemmaBar", "");
    drawBarChart("speech", "persons", 5, 1, 2665525600000, "#speechPersonBar", "");
    drawBarChart("comment", "persons", 5, 1, 2665525600000, "#commentPersonBar", "");
    drawBarChart("speech", "organisations", 5, 1, 2665525600000, "#speechOrganisationsBar", "");
    drawBarChart("comment", "organisations", 5, 1, 2665525600000, "#commentOrganisationsBar", "");
    drawBarChart("speech", "locations", 5, 1, 2665525600000, "#speechLocationsBar", "");
    drawBarChart("comment", "locations", 5, 1, 2665525600000, "#commentLocationsBar", "");
})

/**
 * updates all Charts with new input data
 * @author Kevin Schuff
 */
function updateAllChart() {
    $("#loadingText").show();
    // get variables from page
    var resultLimit = $('#limit').val();
    var startDate = new Date(document.getElementById("startDate").value);
    var endDate = new Date(document.getElementById("endDate").value);
    var startDateAsLong = startDate.getTime();
    var endDateAsLong = endDate.getTime();
    // cases where input is empty, or NaN
    if (resultLimit == "") {
        resultLimit = 10;
        alert("das input limit sollte eine natürliche zahl sein.")
    }
    // cases where the input is negative
    if (resultLimit < 0) {
        resultLimit = Math.abs(resultLimit);
        alert("negativ zahlen limits sind nicht erlaubt. Wurde in die entsprechend positiv Zahl gewandelt.")
    }
    console.log(resultLimit);
    console.log(startDateAsLong);
    console.log(endDateAsLong);
    // remove old charts
    $("#speechTokenLineChart").empty();
    $("#speechPosVerticalBar").empty();
    $("#speechSentimentSpider").empty();
    $("#commentSentimentSpider").empty();
    $("#speechNamedEntityMultipleLine").empty();
    $("#speechSpeakerBar").empty();
    $("#speechLemmaBar").empty();
    $("#speechPersonBar").empty();
    $("#commentPersonBar").empty();
    $("#speechOrganisationsBar").empty();
    $("#commentOrganisationsBar").empty();
    $("#speechLocationsBar").empty();
    $("#commentLocationsBar").empty();

    // draws charts again with new set limit and timeframe
    drawLineChart(resultLimit, startDateAsLong, endDateAsLong, "#speechTokenLineChart", "");
    drawVerticalBarChart(resultLimit, startDateAsLong, endDateAsLong, "#speechPosVerticalBar", "");
    drawSpiderChart("speech", startDateAsLong, endDateAsLong, "#speechSentimentSpider", "");
    drawSpiderChart("comment", startDateAsLong, endDateAsLong, "#commentSentimentSpider", "");
    drawMultipleLineChart(resultLimit, startDateAsLong, endDateAsLong, "#speechNamedEntityMultipleLine", "");
    drawBarChart("speech", "speaker", resultLimit, startDateAsLong, endDateAsLong, "#speechSpeakerBar", "");
    drawBarChart("speech", "lemma", resultLimit, startDateAsLong, endDateAsLong, "#speechLemmaBar", "");
    drawBarChart("speech", "persons", resultLimit, startDateAsLong, endDateAsLong, "#speechPersonBar", "");
    drawBarChart("comment", "persons", resultLimit, startDateAsLong, endDateAsLong, "#commentPersonBar", "");
    drawBarChart("speech", "organisations", resultLimit, startDateAsLong, endDateAsLong, "#speechOrganisationsBar", "");
    drawBarChart("comment", "organisations", resultLimit, startDateAsLong, endDateAsLong, "#commentOrganisationsBar", "");
    drawBarChart("speech", "locations", resultLimit, startDateAsLong, endDateAsLong, "#speechLocationsBar", "");
    drawBarChart("comment", "locations", resultLimit, startDateAsLong, endDateAsLong, "#commentLocationsBar", "");
}

/**
 * opens overlay for search
 * @author Kevin Schuff
 */
function openOverlay() {
    document.querySelector(".overlay").style.display = "block";
    var searchInput = $('#searchInput').val();
    document.querySelector("#overlayText").innerHTML = "<h1>Here are the Graphs for your search: <u>" + searchInput + "</u></h1>";
    // set generic values to get all speeches
    var defaultLimit = 10;
    var defaultStartDate = 1;
    var defaultEndDate = 3665525600000;
    // draws Chart in overlay, only displaying results that match the search input
    drawLineChart(defaultLimit, defaultStartDate, defaultEndDate, "#overlaySpeechTokenLineChart", searchInput);
    drawVerticalBarChart(defaultLimit, defaultStartDate, defaultEndDate, "#overlaySpeechPosVerticalBar", searchInput);
    drawSpiderChart("speech", defaultStartDate, defaultEndDate, "#overlaySpeechSentimentSpider", searchInput);
    drawSpiderChart("comment", defaultStartDate, defaultEndDate, "#overlayCommentSentimentSpider", searchInput);
    drawMultipleLineChart(defaultLimit, defaultStartDate, defaultEndDate, "#overlaySpeechNamedEntityMultipleLine", searchInput);
    drawBarChart("speech", "speaker", defaultLimit, defaultStartDate, defaultEndDate, "#overlaySpeechSpeakerBar", searchInput);
    drawBarChart("speech", "lemma", defaultLimit, defaultStartDate, defaultEndDate, "#overlaySpeechLemmaBar", searchInput);
    drawBarChart("speech", "persons", defaultLimit, defaultStartDate, defaultEndDate, "#overlaySpeechPersonBar", searchInput);
    drawBarChart("comment", "persons", defaultLimit, defaultStartDate, defaultEndDate, "#overlayCommentPersonBar", searchInput);
    drawBarChart("speech", "organisations", defaultLimit, defaultStartDate, defaultEndDate, "#overlaySpeechOrganisationsBar", searchInput);
    drawBarChart("comment", "organisations", defaultLimit, defaultStartDate, defaultEndDate, "#overlayCommentOrganisationsBar", searchInput);
    drawBarChart("speech", "locations", defaultLimit, defaultStartDate, defaultEndDate, "#overlaySpeechLocationsBar", searchInput);
    drawBarChart("comment", "locations", defaultLimit, defaultStartDate, defaultEndDate, "#overlayCommentLocationsBar", searchInput);
}

/**
 * closes overlay for search
 * @author Kevin Schuff
 */
function closeOverlay() {
    document.querySelector(".overlay").style.display = "none";
    // remove old charts
    $("#overlaySpeechTokenLineChart").empty();
    $("#overlaySpeechPosVerticalBar").empty();
    $("#overlaySpeechSentimentSpider").empty();
    $("#overlayCommentSentimentSpider").empty();
    $("#overlaySpeechNamedEntityMultipleLine").empty();
    $("#overlaySpeechSpeakerBar").empty();
    $("#overlaySpeechLemmaBar").empty();
    $("#overlaySpeechPersonBar").empty();
    $("#overlayCommentPersonBar").empty();
    $("#overlaySpeechOrganisationsBar").empty();
    $("#overlayCommentOrganisationsBar").empty();
    $("#overlaySpeechLocationsBar").empty();
    $("#overlayCommentLocationsBar").empty();
}


/**
 * Draws a bar chart, Source: https://www.educative.io/blog/d3-js-tutorial-bar-chart
 * @param collection
 * @param subject
 * @param resultLimit
 * @param startDate
 * @param endDate
 * @param selectTarget
 * @param searchInput
 * @author Kevin Schuff
 */
function drawBarChart(collection, subject, resultLimit, startDate, endDate, selectTarget, searchInput) {
    // Set graph margins and dimensions
    var margin = {top: 20, right: 20, bottom: 30, left: 60},
        width = 1500 - margin.left - margin.right,
        height = 500 - margin.top - margin.bottom;
    // Set ranges
    var x = d3.scaleBand()
        .range([0, width])
        .padding(0.1);
    var y = d3.scaleLinear()
        .range([height, 0]);
    // append the svg object to the div
    var svg = d3.select(selectTarget)
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");

    $.ajax({
        // get data
        url: "http://localhost:4567/" + collection + "/" + subject + "?resultLimit=" + resultLimit + "&startDate=" + startDate + "&endDate=" + endDate + "&searchInput=" + searchInput,
        type: 'GET',
        dataType: 'json',
        async: true,
        success: function (data) {
            console.log(data);
            // Scale the range of the data in the domains. We set the x and y axis here
            // We tell the X axis to render the name and we tell the y axis to render the count
            x.domain(data.map(function (d) {
                return d.value;
            }));
            y.domain([0, d3.max(data, function (d) {
                return d.count;
            })]);
            // set bar color
            let thisBarColor;
            const barColors = ["rgb(150,150,233)", "rgb(150,200,150)"];
            if (collection == "speech"){
                thisBarColor = barColors[0];
            }
            else{
                thisBarColor = barColors[1];
            }

            // Now lets actually draw our bars
            // Append rectangles for bar chart
            svg.selectAll(".bar")
                .data(data)
                .enter().append("rect")
                .attr("class", "bar")
                .attr("x", function (d) {
                    return x(d.value);
                })
                .attr("width", x.bandwidth())
                .attr("y", function (d) {
                    return y(d.count);
                })
                .attr("height", function (d) {
                    return height - y(d.count);
                })
                .style("fill", thisBarColor);
            // And now, after we got our bars, lets append the x and y axis
            // Add x axis
            svg.append("g")
                .attr("transform", "translate(0," + height + ")")
                .call(d3.axisBottom(x));
            // Add y axis
            svg.append("g")
                .call(d3.axisLeft(y));
        },
        error: function (ex) {
            alert("Fehler beim laden der Bar Chart" + subject)
        }
    });
}

/**
 * Draws vertical line chart
 * Source: https://d3-graph-gallery.com/graph/barplot_horizontal.html
 * @param resultLimit
 * @param startDate
 * @param endDate
 * @param selectTarget
 * @param searchInput
 * @author Kevin Schuff
 */
function drawVerticalBarChart(resultLimit, startDate, endDate, selectTarget, searchInput) {
    // set the dimensions and margins of the graph
    const margin = {top: 20, right: 30, bottom: 60, left: 60},
        width = 1000 - margin.left - margin.right,
        height = 500 - margin.top - margin.bottom;

    // append the svg object to the body of the page
    const svg = d3.select(selectTarget)
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");

    // Template for any AJAX request. Just change the route below.
    $.ajax({
        url: "http://localhost:4567/speech/pos?resultLimit=" + resultLimit + "&startDate=" + startDate + "&endDate=" + endDate + "&searchInput=" + searchInput,
        type: 'GET',
        dataType: 'json',
        async: true,
        success: function (data) {


            // get max count, for x axis scaling
            var maxCount = d3.max(data, function (d) {
                return d.count;
            });

            // Add X axis
            const x = d3.scaleLinear()
                // change to 4000000
                .domain([0, maxCount])
                .range([0, width]);
            svg.append("g")
                .attr("transform", "translate(0," + height + ")")
                .call(d3.axisBottom(x))
                .selectAll("text")
                .attr("transform", "translate(-10,0)rotate(-45)")
                .style("text-anchor", "end");

            // Y axis
            const y = d3.scaleBand()
                .range([0, height])
                .domain(data.map(d => d.value))
                .padding(.1);
            svg.append("g")
                .call(d3.axisLeft(y))

            //Bars
            svg.selectAll("myRect")
                .data(data)
                .join("rect")
                .attr("x", x(0))
                .attr("y", d => y(d.value))
                .attr("width", d => x(d.count))
                .attr("height", y.bandwidth())
                .attr("fill", "#69b3a2")
        },
        error: function (ex) {
            alert("Fehler beim laden der Vertical Bar Chart Pos")
        }
    });
}

/**
 * Draws Line Chart
 * Source: https://d3-graph-gallery.com/graph/line_basic.html
 * @param resultLimit
 * @param startDate
 * @param endDate
 * @param selectTarget
 * @param searchInput
 * @author Kevin Schuff
 */
function drawLineChart(resultLimit, startDate, endDate, selectTarget, searchInput) {
    // set the dimensions and margins of the graph
    const margin = {top: 10, right: 30, bottom: 30, left: 60},
        width = 1000 - margin.left - margin.right,
        height = 500 - margin.top - margin.bottom;

    // append the svg object to the body of the page
    const svg = d3.select(selectTarget)
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");

    // Template for any AJAX request. Just change the route below.
    $.ajax({
        url: "http://localhost:4567/speech/token?resultLimit=" + resultLimit + "&startDate=" + startDate + "&endDate=" + endDate + "&searchInput=" + searchInput,
        type: 'GET',
        dataType: 'json',
        async: true,
        success: function (data) {

            // Add X axis
            const x = d3.scaleBand()
                .domain(data.map(d => d.value))
                .range([0, width])
                .padding(0.2);
            svg.append("g")
                .attr("transform", "translate(0," + height + ")")
                .call(d3.axisBottom(x));

            // Add Y axis
            const y = d3.scaleLinear()
                .domain([0, d3.max(data, d => d.count)])
                .range([height, 0]);
            svg.append("g")
                .call(d3.axisLeft(y));

            // Add the line
            svg.append("path")
                .datum(data)
                .attr("fill", "none")
                .attr("stroke", "steelblue")
                .attr("stroke-width", 1.5)
                .attr("d", d3.line()
                    .x(function (d) {
                        return x(d.value) + x.bandwidth() / 2;
                    })
                    .y(function (d) {
                        return y(d.count)
                    })
                )
        },
        error: function (ex) {
            alert("Fehler beim laden der Line Chart Token")
        }
    });
}

/**
 * Draws Spider Chart
 * Source: https://github.com/alangrafu/radar-chart-d3
 * @param collection
 * @param startDate
 * @param endDate
 * @param selectTarget
 * @param searchInput
 * @author Kevin Schuff
 */
function drawSpiderChart(collection, startDate, endDate, selectTarget, searchInput) {

    // Template for any AJAX request. Just change the route below.
    $.ajax({
        url: "http://localhost:4567/" + collection + "/sentiment?startDate=" + startDate + "&endDate=" + endDate + "&searchInput=" + searchInput,
        type: 'GET',
        dataType: 'json',
        async: true,
        success: function (data) {

            let features = ["positive", "negative", "neutral"];
            console.log(data);
            let width = 800;
            let height = 700;
            let svg = d3.select(selectTarget).append("svg")
                .attr("width", width)
                .attr("height", height);


            let radialScale = d3.scaleLinear()
                .domain([0, 10])
                .range([0, 250]);
            let ticks = [2, 4, 6, 8, 10];


            svg.selectAll("circle")
                .data(ticks)
                .join(
                    enter => enter.append("circle")
                        .attr("cx", width / 2)
                        .attr("cy", height / 2)
                        .attr("fill", "none")
                        .attr("stroke", "gray")
                        .attr("r", d => radialScale(d))
                );

            function angleToCoordinate(angle, value) {
                let x = Math.cos(angle) * radialScale(value);
                let y = Math.sin(angle) * radialScale(value);
                return {"x": width / 2 + x, "y": height / 2 - y};
            }

            let featureData = features.map((f, i) => {
                let angle = (Math.PI / 2) + (2 * Math.PI * i / features.length);
                return {
                    "name": f,
                    "angle": angle,
                    "line_coord": angleToCoordinate(angle, 10),
                    "label_coord": angleToCoordinate(angle, 10.5)
                };
            });


            // draw axis line
            svg.selectAll("line")
                .data(featureData)
                .join(
                    enter => enter.append("line")
                        .attr("x1", width / 2)
                        .attr("y1", height / 2)
                        .attr("x2", d => d.line_coord.x)
                        .attr("y2", d => d.line_coord.y)
                        .attr("stroke", "black")
                );

            // draw axis label
            svg.selectAll(".axislabel")
                .data(featureData)
                .join(
                    enter => enter.append("text")
                        .attr("x", d => d.label_coord.x)
                        .attr("y", d => d.label_coord.y)
                        .text(d => d.name)
                );

            let line = d3.line()
                .x(d => d.x)
                .y(d => d.y);
            let colors = ["darkorange", "gray", "navy"];

            function getPathCoordinates(data_point) {
                let coordinates = [];
                for (var i = 0; i < features.length; i++) {
                    let ft_name = features[i];
                    let angle = (Math.PI / 2) + (2 * Math.PI * i / features.length);
                    coordinates.push(angleToCoordinate(angle, data_point[ft_name]));
                }
                return coordinates;
            }

            // draw the path element
            svg.selectAll("path")
                .data(data)
                .join(
                    enter => enter.append("path")
                        .datum(d => getPathCoordinates(d))
                        .attr("d", line)
                        .attr("stroke-width", 3)
                        .attr("stroke", (_, i) => colors[i])
                        .attr("fill", (_, i) => colors[i])
                        .attr("stroke-opacity", 1)
                        .attr("opacity", 0.5)
                );
            // this graph typically takes the longest to draw
            if (collection == "comment") {
                $("#loadingText").hide();
            }
        },
        error: function (ex) {
            alert("Fehler beim laden der Spider Chart Sentiment")
        }
    })
}

/**
 * Draws multiple line chart
 * Source: https://d3-graph-gallery.com/graph/line_several_group.html
 * @param resultLimit
 * @param startDate
 * @param endDate
 * @param selectTarget
 * @param searchInput
 * @author Kevin Schuff
 */
function drawMultipleLineChart(resultLimit, startDate, endDate, selectTarget, searchInput) {
    // set the dimensions and margins of the graph
    const margin = {top: 50, right: 100, bottom: 50, left: 60},
        width = 1500 - margin.left - margin.right,
        height = 800 - margin.top - margin.bottom;

    // append the svg object to the body of the page
    const svg = d3.select(selectTarget)
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");

    // get data
    $.ajax({
        url: "http://localhost:4567/speech/namedEntities?resultLimit=" + resultLimit + "&startDate=" + startDate + "&endDate=" + endDate + "&searchInput=" + searchInput,
        type: 'GET',
        dataType: 'json',
        async: true,
        success: function (data) {
            console.log(data)

            // group the data: I want to draw one line per group
            const sumstat = d3.group(data, d => d.name); // nest function allows to group the calculation per level of a factor

            // Add X axis --> it is a date format
            const x = d3.scaleLinear()
                .domain([1, resultLimit])
                .range([0, width]);
            svg.append("g")
                .attr("transform", "translate(0," + height + ")")
                // as many ticks as result limitation
                .call(d3.axisBottom(x).ticks(resultLimit));
            // add text elements to x axis


            // Add Y axis
            const y = d3.scaleLinear()
                .domain([0, d3.max(data, function (d) {
                    return +d.count;
                })])
                .range([height, 0]);
            svg.append("g")
                .call(d3.axisLeft(y));
            // add text elements to y axis


            // color palette
            const color = d3.scaleOrdinal()
                .range(['#e41a1c', '#377eb8', '#4daf4a'])

            // Draw the line
            svg.selectAll(".line")
                .data(sumstat)
                .join("path")
                .attr("fill", "none")
                .attr("stroke", function (d) {
                    return color(d[0])
                })
                .attr("stroke-width", 1.5)
                .attr("d", function (d) {
                    return d3.line()
                        .x(function (d) {
                            return x(d.x);
                        })
                        .y(function (d) {
                            return y(+d.count);
                        })
                        (d[1])
                })
            // add text to points on the lines
            var mappedData = svg.selectAll("text.data")
                .data(data);
            mappedData.enter()
                .append("text")
                .attr("class", "data")
                .text(function(d) { return d.value; })
                .attr("x", function(d) { return x(+d.x); })
                .attr("y", function(d) { return y(+d.count); })
                .style("text-anchor", "start");

        },
        error: function (ex) {
            alert("Fehler beim laden der named Entity multiple Line Chart")
        }
    });
}
