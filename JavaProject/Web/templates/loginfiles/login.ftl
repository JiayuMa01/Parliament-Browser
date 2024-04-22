<#--This code snippet downloaded from CodeHim.-->
<#--For more web design code & scripts visit now:-->
<#--https://www.codehim.com-->
<#--modified and extended by Rodiana Koukouzeli and @Kevin Schuff-->


<html lang="en" >
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>

    <style><#include "loginstyle.css"></style>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css"
    />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
            href="https://fonts.googleapis.com/css2?family=PT+Serif:wght@400;700&display=swap"
            rel="stylesheet"
    />


    <title>${title}</title>

</head>
<body>


<div class="homepage"><a href="http://localhost:4567/">Home</a></div>
<div class="logout"><a href="http://localhost:4567/login">Logout</a></div>


<!-- partial:index.partial.html -->
<div id="wrapper-login">
    <h2>Login</h2>
    <form id="login-form">
        <p>
            <input type="text" id="username" placeholder="Username">
        </p>
        <p>
            <input type="password" id="pw" placeholder="Password">
        </p>
        <p>
            <button type="button" onclick="getInput()">Login</button>

        </p>
    </form>
    <div id="create-account-wrap">
        <p><p>
    </div><!--create-account-wrap-->

</div><!--login-form-wrap-->
<#----------------------------------start of admin div----------------------------------------->
<div class="admin"><h3>Sie verfügen über folgende features:</h3>
<#--displays all features of an admin after successful login-->
    <#--    @author Kevin Schuff-->

    <div id="featuresAfterLogin">

        <button id="changeSpeakerButton">Rede zuweisen</button>

        <!--Div to be displayed for assigning speeches-->
        <div id="updateSpeakerDivAdmin" class ="featureDivAdmin">
            <label for="speechIDAdmin">Reden ID: </label>
            <input type="text" id="speechIDAdmin">
            <label for="speakerIDAdmin">Redner ID: </label>
            <input type="text" id="speakerIDAdmin">
            <button onclick="updateSpeaker('Admin')">zuweisen</button>
        </div>
        <!--Div to displayed for assigning comments-->
        <button id="assignCommentButton">Kommentar zuweisen</button>
        <div id="updateCommentDiv" class ="featureDiv">
            <label for="commentIDAdmin">Kommentar ID: </label>
            <input type="text" id="commentIDAdmin">
            <label for="commentatorIDAdmin">Kommentator ID: </label>
            <input type="text" id="commentatorIDAdmin">
            <label for="fraction">Fraktion: </label>
            <input type="text" id="fractionAdmin">
            <button onclick="updateComment('Admin')">zuweisen</button>
        </div>
    </div>

    <#--@author Rodiana Koukouzeli-->

    <div class="addSpeechAdmin">
        <p id="pAddAdmin"> <strong> Legen Sie eine neue Rede an:</strong></p>
        <div class="addSpeechAdmin">
            <form>
                <label for="addIDAdmin">RedeID:</label>
                <input type="text" id="addIDAdmin">
                <label for="textAdmin">Text:</label>
                <textarea id="textAdmin"></textarea>
                <label for="addSPIDAdmin">Redner ID:</label>
                <input type="text" id="addSPIDAdmin">
                <button type="button" onclick="addSpeech('Admin')">submit</button>
            </form>
        </div>
    </div>

    <div class="addSpeaker">
        <p id="padd"> <strong> Legen Sie einen neuen Redner an:</strong></p>
        <div class="addSP"> <form>
                <label for="nameAdmin">Vorname:</label>
                <input type="text" id="nameAdmin">
                <label for="lnameAdmin">Nachname:</label>
                <input type="text" id="lnameAdmin">
                <label for="partyAdmin">Partei:</label>
                <input type="text" id="partyAdmin">
                <label for="spIDAdmin">RednerID:</label>
                <input type="text" id="spIDAdmin">
                <button type="button" onclick="addSpeaker('Admin')">submit</button>
            </form>
        </div>
    </div>

    <p> <strong>Legen Sie einen neuen Nutzer an:</strong> </p>
    <div class="createUser"> <form>
            <label for="uName">Benutzername:</label>
            <input type="text" id="uName">
            <label for="passwd">Passwort:</label>
            <input type="password" id="passwd">
            <label for="allRoles">Rolle auswählen:</label>
            <select name="roles" id="roles">
                <option value="user">user</option>
                <option value="manager">manager</option>
                <option value="admin">admin</option>
            </select>
            <button type="button" onclick="createNewUser()">submit</button>
        </form>
    </div>

    <div class="deleteSpeakerAdmin">
        <p id="pdelete"> <strong>Löschen Sie einen Redner:</strong> </p>
        <div class="delSP">
            <form>
                <label for="delIDAdmin">RednerID:</label>
                <input type="text" id="delIDAdmin">
                <button type="button" onclick="deleteSpeaker('Admin')">submit</button>
            </form>
        </div>
    </div>

    <div class="changePWAdmin">
        <p id="pwchange"><strong>Passwort ändern:</strong> </p>
        <div class="changePWAdmin">
            <form>
                <label for="uNameAdmin">Benutzername:</label>
                <input type="text" id="uNameAdmin">
                <label for="oldPwAdmin">aktuelles Passwort:</label>
                <input type="text" id="oldPwAdmin">
                <label for="changePwAdmin">neues Passwort:</label>
                <input type="text" id="changePwAdmin">
                <button type="button" onclick="changePassword('Admin')">submit</button>
            </form>
        </div>
    </div>

    <div class="upgradeUserAdmin">
        <p id="upgrdUserAdmin"> <strong>Einen Benutzer upgraden/downgraden:</strong> </p>
        <div class="upgradeAUserAdmin">
            <form>
                <label for="userAdmin">Benutzername:</label>
                <input type="text" id="userAdmin">
                <label for="userpassword">aktuelles Passwort:</label>
                <input type="text" id="userpassword">
                <label for="allRolesNew">Rolle auswählen:</label>
                <select name="rolesUpgr" id="rolesUpgr">
                    <option value="user">user</option>
                    <option value="manager">manager</option>
                    <option value="admin">admin</option>
                </select>
                <button type="button" onclick="upgradeUser()">submit</button>
            </form>
        </div>
    </div>



    <div class="deleteUser">
        <p><strong>Löschen Sie einen Nutzer:</strong></p>
        <form>
            <label for="delU">Benutzername:</label>
            <input type="text" id="delU">
            <button type="button" onclick="deleteUser()">submit</button>
        </form>
    </div>


</div>
<#----------------------------------end of admin div--------------------------------------------->

<#----------------------------------start of manager div----------------------------------------->

<#--displays all features of a manager after successful login-->

<div class="manager"><h3> Sie verfügen über folgende features:</h3>
    <#--    @author Kevin Schuff-->
    <div id="featuresAfterLogin">

        <button id="changeSpeakerButton">Rede zuweisen</button>

        <!--Div to be displayed for assigning speeches-->
        <div id="updateSpeakerDiv" class ="featureDiv">
            <label for="speechIDMng">Reden ID: </label>
            <input type="text" id="speechIDMng">
            <label for="speakerIDMng">Redner ID: </label>
            <input type="text" id="speakerIDMng">
            <button onclick="updateSpeaker('Mng')">zuweisen</button>
        </div>
        <!--Div to displayed for assigning comments-->
        <button id="assignCommentButton">Kommentar zuweisen</button>
        <div id="updateCommentDiv" class ="featureDiv">
            <label for="commentIDMng">Kommentar ID: </label>
            <input type="text" id="commentIDMng">
            <label for="commentatorIDMng">Kommentator ID: </label>
            <input type="text" id="commentatorIDMng">
            <label for="fractionMng">Fraktion: </label>
            <input type="text" id="fractionMng">
            <button onclick="updateComment('Mng')">zuweisen</button>
        </div>
    </div>

    <#--    @author Rodiana Koukouzeli-->
    <div class="addSpeechMng">
        <strong> Legen Sie eine neue Rede an: </strong>
        <div class="addSpeechMng">
            <form>
                <label for="addIDMng">RedeID:</label>
                <input type="text" id="addIDMng">
                <label for="textMng">Text:</label>
                <textarea id="textMng"></textarea>
                <label for="addSPIDMng">Redner ID:</label>
                <input type="text" id="addSPIDMng">
                <button type="button" onclick="addSpeech('Mng')">submit</button>
            </form>
        </div>
    </div>

    <div class="addSpeakerMng">
        <p id="pAdd"> <strong>Legen Sie einen neuen Redner an:</strong> </p>
        <div class="addSP">
            <form>
                <label for="nameMng">Vorname:</label>
                <input type="text" id="nameMng">
                <label for="lnameMng">Nachname:</label>
                <input type="text" id="lnameMng">
                <label for="partyMng">Partei:</label>
                <input type="text" id="partyMng">
                <label for="spIDMng">RednerID:</label>
                <input type="text" id="spIDMng">
                <button type="button" onclick="addSpeaker('Mng')">submit</button>
            </form>
        </div>
    </div>

    <div class="deleteSpeakerMng">
        <p id="pdelete"> <strong>Löschen Sie einen Redner:</strong> </p>
        <div class="delSP">
            <form>

                <label for="delIDMng">RednerID:</label>
                <input type="text" id="delIDMng">
                <button type="button" onclick="deleteSpeaker('Mng')">submit</button>
            </form>
        </div>
    </div>

    <div class="changePWMng">
        <p id="pwchange"> <strong>Passwort ändern:</strong> Passwort ändern:</p>
        <div class="changePWMng">
            <form>
                <label for="uNameMng">Benutzername:</label>
                <input type="text" id="uNameMng">
                <label for="oldPwMng">aktuelles Passwort:</label>
                <input type="text" id="oldPwMng">
                <label for="changePwMng">neues Passwort:</label>
                <input type="text" id="changePwMng">
                <button type="button" onclick="changePassword('Mng')">submit</button>
            </form>
        </div>
    </div>

</div>
<#----------------------------------end of manager div----------------------------------------->

<#----------------------------------start of user div------------------------------------------>
<#--displays all features of a user  after successful login-->

<div class="user"><h3> Sie verfügen über folgende features:</h3>

    <#--    @author Kevin Schuff-->
    <div id="featuresAfterLogin">

        <button id="changeSpeakerButton">Rede zuweisen</button>
        <!--Div to be displayed for assigning speeches-->
        <div id="updateSpeakerDiv" class ="featureDiv">
            <label for="speechIDU">Reden ID: </label>
            <input type="text" id="speechIDU">
            <label for="speakerIDU">Redner ID: </label>
            <input type="text" id="speakerIDU">
            <button onclick="updateSpeaker('U')">zuweisen</button>
        </div>
        <!--Div to displayed for assigning comments-->
        <button id="assignCommentButton">Kommentar zuweisen</button>
        <div id="updateCommentDiv" class ="featureDiv">
            <label for="commentIDU">Kommentar ID: </label>
            <input type="text" id="commentIDU">
            <label for="commentatorIDU">Kommentator ID: </label>
            <input type="text" id="commentatorIDU">
            <label for="fractionU">Fraktion: </label>
            <input type="text" id="fractionU">
            <button onclick="updateComment('U')">zuweisen</button>
        </div>
    </div>


    <div class="addSpeechUser">
        <p id="pAdd"><strong>Legen Sie eine neue Rede an:</strong></p>
        <div class="addSpeechUser">
            <form>
                <label for="addIDUser">RedeID:</label>
                <input type="text" id="addIDUser">
                <label for="textUser">Text:</label>
                <textarea id="textUser"></textarea>
                <label for="addSPIDUser">Redner ID:</label>
                <input type="text" id="addSPIDUser">
                <button type="button" onclick="addSpeech('User')">submit</button>
            </form>
        </div>
    </div>

    <div class="changePWUser">
        <p id="pwchange">  <strong>Passwort ändern:</strong> </p>
        <div class="delPWUser">
            <form>
                <label for="uNameUser">Benutzername:</label>
                <input type="text" id="uNameUser">
                <label for="oldPwUser">aktuelles Passwort:</label>
                <input type="text" id="oldPwUser">
                <label for="changePwUser">neues Passwort:</label>
                <input type="text" id="changePwUser">
                <button type="button" onclick="changePassword('User')">submit</button>
            </form>
        </div>
    </div>

</div>
<#----------------------------------end of user div----------------------------------------->

</body>
<script><#include "loginfunctions.js"></script>

<script>
    //This method is called when the page is fully loaded.
    $(document).ready(function () {
        $(".admin").hide();
        $(".manager").hide();
        $(".user").hide();
        $(".logout").hide();
        console.log("Hi");
    });
</script>
</html>
