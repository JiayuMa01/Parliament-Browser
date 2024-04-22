<!--@author Fabian Hamid Fazli-->
<!-- this page contains fulltext -->

<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Parliament Browser</title>
    <#include "style.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css"/>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=PT+Serif:wght@400;700&display=swap" rel="stylesheet"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
    <#include "./style.css">
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


<div class="resulttitle"><h2>Volltext-Visualisierung</h2></div>

<!-- Person, Ort, Organisation -->
<div class="legende">Legende:
    <span style="background-color: red;">Person</span>
    <span style="background-color: green;">Ort</span>
    <span style="background-color: yellow;">Organisation</span>

</div>

<#--speech information-->
<div id="text">
    <h2>RedeID: ${speech.get_id()}</h2>
    <h3>Redner: ${speakerName}, Partei: ${speakerParty}, Fraktion: ${speakerFraction}</h3>
    <img src=${speakerPictureURL} alt="keinBild" style="max-height: 500px; max-width: 500px; border-radius: 15px"/>

    <#--speech-->
    <div id="textAfterColor">
        <h1>
            Here will be the text
        </h1>
    </div>
</div>




<script><#include "color.js"></script>

</body>
</html>
