<!--@author Kevin Schuff-->

<html>
<meta charset="utf-8">
<title>${title}</title></meta>
<head>
    <#-- sidebar import -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css"/>
    <#-- awesomefont import for smileys -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <#-- bootstrap import for styling -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <#-- Include jquery for better javascript usage -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
    <!-- Load d3.js -->
    <script src="https://d3js.org/d3.v7.min.js"></script>

    <#-- IMPORT UR CUSTOM .CSS FILES LIKE HERE -->
    <style>
        <#include "css/kSite.css">

    </style>
</head>


<body>

<!--@author Rodiana Koukouzeli-->
<!-- sidebar button -->
<input type="checkbox" id="check" />
<label for="check">
    <i class="fas fa-bars" id="sidebar-button"></i>
</label>
<!--begin of sidebar-->
<div class="sidebar-menue">
    <a href="http://localhost:4567/"><i class="fa-solid fa-house"></i><span>Home</span></a>
    <a href="http://localhost:4567/Volltextvisualisierung"><i class="fa-solid fa-file-lines"></i><span>Volltext Visualisierung</span></a>
    <a href="http://localhost:4567/network/comments/vis"><i
                class="fa-solid fa-circle-nodes"></i><span>Kommentarnetzwerk</span></a>
    <a href="http://localhost:4567/network/categories/vis"><i class="fa-solid fa-circle-nodes"></i><span>Kategorienetzwerk</span></a>
    <a href="http://localhost:4567/network/speeches/vis"><i
                class="fa-solid fa-circle-nodes"></i><span>Redenetzwerk</span></a>
    <a href="http://localhost:4567/export"><i class="fa-solid fa-gears"></i><span>Latex Export</span></a>
    <a href="http://localhost:4567/pageAddData"><i class="fa-solid fa-cloud-arrow-down"></i><span>Protokolle aktualisieren</span></a>
    <a href="http://localhost:4567/login"><i class="fa-solid fa-right-to-bracket"></i><span>Login</span></a>
</div>
<!--end of sidebar-->

<!--@author Kevin Schuff-->
<div class="mainContent">
    <!--Source for Overlay: https://www.w3schools.com/howto/howto_css_overlay.asp-->
    <!--Creates Button to open an overlay for potential search-->
    <input type="text" id="searchInput"/>
    <button id="buttonOpenOverlay" onclick="openOverlay()">[ENTER] zum suchen</button>
    <!-- overlay -->
    <div class="overlay">
        <div class="overlayContent">
            <button id="buttonCloseOverlay" onclick="closeOverlay()">[ESC] zum schließen</button>
            <div id="overlayText"></div>

            <!-- CHARTS -->
            <!-- Draws Top Token Line Chart -->
            <div>
                <h4>Top Token</h4>
                <div id="overlaySpeechTokenLineChart"></div>
            </div>
            <!-- Draws Top Pos Vertical Bar Chart -->
            <div>
                <h4>Top POS</h4>
                <div id="overlaySpeechPosVerticalBar"></div>
            </div>
            <!-- Draws Sentiment Spider Chart speech-->
            <div>
                <h4>Sentiment Verteilung in Reden</h4>
                <div id="overlaySpeechSentimentSpider"></div>
            </div>
            <!-- Draws Sentiment Spider Chart comment-->
            <div>
                <h4>Sentiment Verteilung in Kommentaren</h4>
                <div id="overlayCommentSentimentSpider"></div>
            </div>
            <!-- Draws named Entities multiple Line Chart-->
            <div>
                <h4>Top Named Entities</h4>
                <h5>
                    <span style="text-decoration: underline; color: red;">Personen</span>
                    <span style="text-decoration: underline; color: steelblue;">Organisationen</span>
                    <span style="text-decoration: underline; color: green;">Orte</span>
                </h5>
                <div id="overlaySpeechNamedEntityMultipleLine"></div>
            </div>
            <!-- Draws Top Speaker Bar Chart -->
            <div>
                <h4>Top Redner</h4>
                <div id="overlaySpeechSpeakerBar"></div>
            </div>
            <!-- Draws Top Lemma Bar Chart -->
            <div>
                <h4>Top Lemmas</h4>
                <div id="overlaySpeechLemmaBar"></div>
            </div>
            <!-- Draws Top Persons Bar Chart -->
            <div>
                <h4>Top Personen in Reden</h4>
                <div id="overlaySpeechPersonBar"></div>
            </div>
            <!-- Draws Top Persons Bar Chart comment-->
            <div>
                <h4>Top Personen in Kommentaren</h4>
                <div id="overlayCommentPersonBar"></div>
            </div>
            <!-- Draws Top Organisation Bar Chart -->
            <div>
                <h4>Top Organisationen in Reden</h4>
                <div id="overlaySpeechOrganisationsBar"></div>
            </div>
            <!-- Draws Top Organisation Bar Chart comment-->
            <div>
                <h4>Top Organisationen in Kommentaren</h4>
                <div id="overlayCommentOrganisationsBar"></div>
            </div>
            <!-- Draws Top Locations Bar Chart -->
            <div>
                <h4>Top Orte in Reden</h4>
                <div id="overlaySpeechLocationsBar"></div>
            </div>
            <!-- Draws Top Locations Bar Chart comment-->
            <div>
                <h4>Top Orte in Kommentaren</h4>
                <div id="overlayCommentLocationsBar"></div>
            </div>
        </div>
    </div>

    <!--Creates Button submit Timespan and limit for updating charts-->
    <div>
        <label for="limit">Limit results: </label>
        <input type="number" id="limit">
        <label for="startDate">starting Date: </label>
        <input type="date" id="startDate">
        <label for="endDate">ending Date: </label>
        <input type="date" id="endDate">
        <button onclick="updateAllChart()">submit</button>
    </div>

    <!--display this, during fetching data from db-->
    <div id="loadingText"> Loading Data...</div>


    <!-- CHARTS -->
    <!-- Draws Top Token Line Chart -->
    <div>
        <h4>Top Token</h4>
        <div id="speechTokenLineChart"></div>
    </div>
    <!-- Draws Top Pos Vertical Bar Chart -->
    <div>
        <h4>Top POS</h4>
        <div id="speechPosVerticalBar"></div>
    </div>
    <!-- Draws Sentiment Spider Chart speech-->
    <div>
        <h4>Sentiment Verteilung in Reden</h4>
        <div id="speechSentimentSpider"></div>
    </div>
    <!-- Draws Sentiment Spider Chart comment-->
    <div>
        <h4>Sentiment Verteilung in Kommentaren</h4>
        <div id="commentSentimentSpider"></div>
    </div>
    <!-- Draws named Entities multiple Line Chart-->
    <div>
        <h4>Named Entity Verteilung</h4>
        <h5>
            <span style="text-decoration: underline; color: red;">Personen</span>
            <span style="text-decoration: underline; color: steelblue;">Organisationen</span>
            <span style="text-decoration: underline; color: green;">Orte</span>
        </h5>
        <div id="speechNamedEntityMultipleLine"></div>
    </div>
    <!-- Draws Top Speaker Bar Chart -->
    <div>
        <h4>Top Speakers</h4>
        <div id="speechSpeakerBar"></div>
    </div>
    <!-- Draws Top Lemma Bar Chart -->
    <div>
        <h4>Top Lemmas</h4>
        <div id="speechLemmaBar"></div>
    </div>
    <!-- Draws Top Persons Bar Chart -->
    <div>
        <h4>Top Personen in Reden</h4>
        <div id="speechPersonBar"></div>
    </div>
    <!-- Draws Top Persons Bar Chart Comment-->
    <div>
        <h4>Top Personen in Kommentaren</h4>
        <div id="commentPersonBar"></div>
    </div>
    <!-- Draws Top Organisation Bar Chart -->
    <div>
        <h4>Top Organisationen in Reden</h4>
        <div id="speechOrganisationsBar"></div>
    </div>
    <!-- Draws Top Organisation Bar Chart comment-->
    <div>
        <h4>Top Organisationen in Kommentaren</h4>
        <div id="commentOrganisationsBar"></div>
    </div>
    <!-- Draws Top Locations Bar Chart -->
    <div>
        <h4>Top Orte in Reden</h4>
        <div id="speechLocationsBar"></div>
    </div>
    <!-- Draws Top Locations Bar Chart comment-->
    <div>
        <h4>Top Orte in Kommentaren</h4>
        <div id="commentLocationsBar"></div>
    </div>


</div>


<#-- IMPORT UR CUSTOM .JS FILES LIKE SO -->
<script>
    <#include "js/kCharts.js">

</script>

</body>

</html>