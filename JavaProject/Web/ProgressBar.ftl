<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Progress Bar</title>

    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css"
    />

    <style>
        progress {
            width: 1000px;
            height: 35px;
        }
    </style>
    <style>
        .button {
            border: none;
            color: white;
            padding: 15px 32px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            margin: 4px 2px;
            cursor: pointer;
        }
        .button {background-color: #4CAF50;} /* Green */
    </style>
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
        #check:checked ~ .w3-container{
            margin-left: 250px;
        }
        .w3-container{
            padding-right: 0px;
            left: 0;
        }
    </style>

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

    <div class="w3-container">

        <br><br><br>
        <h2>Progress Bar</h2><br>

        <h4 id="upload-status">API server is offline</h4><br><br>

        <button class="button button" onclick="addNewData()">Add New Data</button>
        <a>(Download only new protocols files that have not been processed from Bundestag. Then XML analyze, NLP and upload to MongoDB)</a>
        <br>
        <a>(Only one click until the end of the full upload)</a>
        <br>
        <a>(Try not to interrupt the upload forcibly. If you have to interrupt it, please manually delete "XMLResources" from the "parliament_browser_3_1" folder, and then manually set the "status" of "workstatus" document in the "countProgress" collection in the database to 0.)</a>
        <br>
        <a id="workStatus"></a><br>
        <a id="nameDownloadedXML"></a><br><br>

        <a>Downloaded new XML files</a>
        <a>&nbsp&nbsp&nbsp&nbsp</a>
        <a id="numcntDownloadedXML">NaN</a><br>
        <a id="cntDownloadedXML" value="0" max="100"></a><br><br>

        <a>Analysed XML files</a>
        <a>&nbsp&nbsp&nbsp&nbsp</a>
        <a id="numcntAnalysedXML">NaN</a><br>
        <progress id="cntAnalysedXML" value="0" max="100"></progress><br><br>
        
        <a>Uploaded speaker documents</a>
        <a>&nbsp&nbsp&nbsp&nbsp</a>
        <a id="numcntUploadedSpeaker">NaN</a><br>
        <progress id="cntUploadedSpeaker" value="0" max="100"></progress><br><br>
        
        <a>Uploaded speech documents with NLP</a>
        <a>&nbsp&nbsp&nbsp&nbsp</a>
        <a id="numcntUploadedSpeech">NaN</a><br>
        <progress id="cntUploadedSpeech" value="0" max="100"></progress><br><br>

        <a>Uploaded comment documents with NLP</a>
        <a>&nbsp&nbsp&nbsp&nbsp</a>
        <a id="numcntUploadedComment">NaN</a><br>
        <progress id="cntUploadedComment" value="0" max="100"></progress><br><br>
        
    </div>

<#--    <script>-->
<#--        <#include "https://code.jquery.com/jquery-1.7.1.min.js">-->
<#--    </script>-->
    <script src="https://code.jquery.com/jquery-1.7.1.min.js"></script>
    <script>
        <#include "updateProgressBar.js">
    </script>
    
</body>
</html>
