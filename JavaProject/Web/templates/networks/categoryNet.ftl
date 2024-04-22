<!--basic structure of the category network-->
<#--@author Rodiana Koukouzeli-->
<html lang="en">
<head>
    <meta charset="UTF-8" />

    <script src="https://d3js.org/d3.v6.js"></script>

    <#--    <script src="https://d3js.org/d3.v7.min.js"></script>-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css"
    />
    <style><#include "css/categoryStyle.css"></style>
    <title>${title}</title>
</head>
<body>


<input type="checkbox" id="check" />

<label for="check">
    <i class="fas fa-bars" id="sidebar-button"></i>
</label>

<!--begin of sidebar-->
<div class="sidebar-menue">
    <a href="http://localhost:4567/"><i class="fa-solid fa-house"></i><span>Home</span></a>
    <a href="http://localhost:4567/Volltextvisualisierung"><i class="fa-solid fa-file-lines"></i><span>Volltext Visualisierung</span></a>
    <a href="http://localhost:4567/network/comments/vis"><i class="fa-solid fa-circle-nodes"></i><span>Kommentarnetzwerk</span></a>
    <a href="http://localhost:4567/network/categories/vis"><i class="fa-solid fa-circle-nodes"></i><span>Kategorienetzwerk</span></a>
    <a href="http://localhost:4567/network/speeches/vis"><i class="fa-solid fa-circle-nodes"></i><span>Redenetzwerk</span></a>
    <a href="http://localhost:4567/export"><i class="fa-solid fa-gears"></i><span>Latex Export</span></a>
    <a href="http://localhost:4567/pageAddData"><i class="fa-solid fa-cloud-arrow-down"></i><span>Protokolle aktualisieren</span></a>
    <a href="http://localhost:4567/login"><i class="fa-solid fa-right-to-bracket"></i><span>Login</span></a>
</div>
<!--end of sidebar-->


<!--Creates Button submit Timespan and limit for updating charts-->
<!--@author Kevin Schuff-->
<!--begin of filter-->
<div class="mainContentCateg">
    <div class="setFilter">
        <label for="limit">Limit: </label>
        <input type="number"id="limit">
        <label for="startDate">starting Date: </label>
        <input type="date" id="startDate">
        <label for="endDate">ending Date: </label>
        <input type="date" id="endDate">
        <button onclick="updateGraphTwo()">submit</button>
        <label for="protocol">Protokolltitel</label>
        <input type="text" id="protocol" placeholder="Plenarprotokoll XX/YY">
        <button onclick="getProtocolCateg()">submit</button>
    </div>
    <div class="filtercheck">
        <button id="wp19" onclick="getSpecificOne(1000,1508796000000,1635199200000)">Wahlperiode19</button>
        <button id="wp20" onclick="getSpecificOne(1000,1635199200000,2665525600000)">Wahlperiode20</button>
        <button id="SPD" onclick="">SPD</button>
        <button id="CDU" onclick="">CDU</button>
        <button id="FDP" onclick="">FDP</button>
        <button id="GRÜNEN" onclick="">GRÜNEN</button>
        <button id="LINKE" onclick="">DIE LINKE</button>
        <button id="AfD" onclick="">AfD</button>
    </div>
    <!--end of filter-->
</div>

<div class="graph">
    <div id="my_dataviztwo"></div>
</div>
<div id="protocolGraphTwo"></div>

</body>


<script><#include "js/categoryVis.js"></script>
<script>
    //This method is called when the page is fully loaded.
    $(document).ready(function () {
        loadCategoryNet(500,1,2665525600000);
    });
</script>
</html>

