<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="shortcut icon" href="#">
    <script src="https://d3js.org/d3.v6.js"></script>

    <#--    <script src="https://d3js.org/d3.v7.min.js"></script>-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
            href="https://fonts.googleapis.com/css2?family=PT+Serif:wght@400;700&display=swap"
            rel="stylesheet"
    />
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css"
    />
    <title>Protokoll-Export</title>
    <style>
        body{
            margin-top: 0;
            padding: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: #fdfeff;

        }

        .sidebar-menue{
            background: #34495e;
            margin-top: 0;
            padding-top: 120px;
            position: fixed;
            left: -250px;
            width: 250px;
            height: 100%;
            transition: 0.2s;
        }

        .sidebar-menue a{
            color: #cccccc;
            display: block;
            width: 100%;
            line-height: 50px;
            text-decoration: none; /* no lines under the icons */
            padding-left: 40px;
            box-sizing: border-box;

        }

        .sidebar-menue a:hover{
            background:  #cccccc;
            color: #34495e;
        }

        .sidebar-menue i{
            padding-right: 10px;

        }

        label #sidebar-button{
            z-index: 1;
            left: 40px;
            top: 25px;
            font-size: 25px;
            position: absolute;
            cursor: pointer;
            border-radius: 3px;
            padding: 4px 8px;
            color: white;
            background: #34495e;
        }

        label #sidebar-button:hover{
            color: #cccccc;
            background: #34495e;

        }

        #check{
            display: none;
        }

        #check:checked ~ .sidebar-menue{
            left: 0;
        }

        #check:checked ~ .mainContent{
            margin-left: 250px;
        }


        .mainContent{
            padding-right: 0px;
            left: 0;

        }
    </style>
</head>

<body>

<!-- Begin of the sidebar menu -->
<input type="checkbox" id="check" />

<label for="check">
    <i class="fas fa-bars" id="sidebar-button"></i>
</label>

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

<!-- end of sidebar -->

<div class="mainContent">

<br><br><br>

<h1>Protokoll-Export</h1>

<!-- this part represents the show of the pdf and its generation -->
<h4>Vorschau</h4>
<div id="printExport"></div>
<label>Wahlperiode:</label><input type="text" id="wp"><br>
<label>Protokollindex:</label><input type="text" id="index"><br>
<label>Reden:</label><input type="text" id="speeches"><br><br>
<button id="addProtocol" onclick="addProtocol()">Protokoll hinzufügen</button><br><br>

<button id="generateSomeProtocols" onclick="exportSomeProtocols()">PDF für ausgewählte Protokolle erzeugen</button>
<button id="printSomeProtocols" onclick="displaySomeProtocols()">Nur ausgewählte Protokolle anzeigen lassen (erst PDF erzeugen!)</button><br><br>

<button id="printAllProtocols" onclick="exportProtocolsAll()">Alle Protokolle anzeigen</button>

<!-- this part represents the edit/creation of templates -->

<h4>Bearbeiten/Hinzufügen von Templates</h4>
<button id="showTemplateNames" onclick="showTemplateNames()">Templatenamen anzeigen</button>
</br></br>
<div class="templateNames"></div>
<div class="editTemplate"></div>

</div>

</body>

<script><#include "ExportProtocols.js"></script>
<script><#include "editLatexTemplates.js"></script>

</html>