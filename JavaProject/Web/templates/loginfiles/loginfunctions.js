/**
 * Functions provided by the login page.
 *
 */


/**
 * gets input from the login field and logs in a user
 * @author Rodiana Koukouzeli
 */
function getInput(){
    var user = $('#username').val();
    var pw = $('#pw').val();

    login(user,pw);
}

/**
 * creates new User from input
 * @author Rodiana Koukouzeli
 */
function createNewUser(){
    var cUser = $('#uName').val();
    var cPw = $('#passwd').val();
    var cRole = $( "#roles option:selected" ).text();
    createUser(cUser,cPw,cRole);
}


/**
 * after user is logged in, depending on the role, different divs show up
 * @param user
 * @param pw
 * @author Rodiana Koukouzeli
 */
function login(user,pw){


    // Let's do some login action
    $.ajax({
        url: "http://localhost:4567/login/users?user=" + user + "&password=" + pw,
        type: 'GET',
        async: true,
        success: function(data) {


            if (data === "false"){
                console.log("hello");
                console.log("login not successful");
                alert("Your username or password is incorrect, please try again.")

            }
            else if (data === "admin"){
                $("#wrapper-login").hide();
                $(".manager").hide();
                $(".user").hide();
                $(".admin").show();
                $(".logout").show();
                alert("You logged in as: "+ user + " with the role: " + data);

            }
            else if (data === "manager"){
                $("#wrapper-login").hide();
                $(".user").hide();
                $(".admin").hide();
                $(".manager").show();
                $(".logout").show();
                alert("You logged in as: "+ user + " with the role: " + data);
            }else {
                $("#wrapper-login").hide();
                $(".admin").hide();
                $(".manager").hide();
                $(".user").show();
                $(".logout").show();
                alert("You logged in as: "+ user + " with the role: " + data);

            }

        },
        error: function(ex){
            alert("Sorry user, this request didnt work.");
        }
    });
}
/**
 * update the speaker of a speech
 * @author Kevin Schuff
 */
function updateSpeaker(spRole){
    var speechID = $("#speechID"+spRole).val();
    var speakerID = $('#speakerID'+spRole).val();

    $.ajax({
        // try speechID:ID192300100, speakerID:99999073
        url: 'http://localhost:4567/loginFeature/update/speaker?speechID='+speechID+'&speakerID='+speakerID,
        type: 'GET',
        dataType: 'text',
        success: function(response) {
            console.log(response);
            if(response=='true'){
                alert('Erfolgreiches update')
            }
            else{
                alert('Es wurde nichts geupdated')
            }
        },
        error: function(xhr, status, error) {
            // code to run if the request fails
            console.log('Request failed: ' + error);
        }
    });
}

/**
 * updates a comment
 * @author Kevin Schuff
 */
function updateComment(cRole){
    var commentID = $('#commentID'+cRole).val();
    var commentatorID = $('#commentatorID'+cRole).val();
    var fraction = $('#fraction'+cRole).val();

    $.ajax({
        // try commentID:ID192300100--1189609602
        url: 'http://localhost:4567/loginFeature/update/comment?commentID='+commentID+'&commentatorID='+commentatorID+'&fraction='+fraction,
        type: 'GET',
        dataType: 'text',
        success: function(response) {
            console.log(response);
            if(response==='true'){
                alert('Erfolgreiches update')
            }
            else{
                alert('Es wurde nichts geupdated')
            }
        },
        error: function(xhr, status, error) {
            // code to run if the request fails
            console.log('Request failed: ' + error);
        }
    });
}




/**
 * Function takes input from the "admin" div and created a new user in the database.
 * @param username
 * @param password
 * @param role
 * @author Rodiana Koukouzeli
 */
function createUser(username, password, role){

    $.ajax({
        url: "http://localhost:4567/login/create?user=" + username + "&password=" + password  + "&role=" + role,
        type: 'GET',
        async: true,
        success: function (data) {
            alert(data);

        },
        error: function (ex){
            alert("Sorry user, this request didnt work.");
        }
    });
}

/**
 * Function takes input from the "admin"/"manager div and creates a new user in the database.
 * @param spRole
 * @author Rodiana Koukouzeli
 */
function addSpeaker(spRole){
    var id = $("#spID"+spRole).val();
    var firstname = $('#name'+spRole).val();
    var lastname = $('#lname'+spRole).val();
    var party = $('#party'+spRole).val();


    $.ajax({
        url: "http://localhost:4567/login/add?id=" + id + "&firstname=" + firstname  + "&lastname=" + lastname+ "&party"+party,
        type: 'GET',
        async: true,
        success: function (data) {
            alert(data);

        },
        error: function (ex){
            alert("Sorry user, this request didnt work.");
        }
    });
}

/**
 * add new speech to the speaker database.
 * @param spRole
 * @author Rodiana Koukouzeli
 */
function addSpeech(spRole){
    var id = $("#addID"+spRole).val();
    var text = $('#text'+spRole).val();
    var speechSPID = $('#addSPID'+spRole).val();


    $.ajax({
        url: "http://localhost:4567/login/addspeech?id=" + id + "&text=" + text  + "&speakerID" + speechSPID,

        type: 'GET',
        async: true,
        success: function (data) {
            alert(data);

        },
        error: function (ex){
            alert("Sorry user, this request didnt work.");
        }
    });
}


/**
 * Function takes input from the "admin"/"manager div and creates a new user in the database.
 * @param spRole
 * @author Rodiana Koukouzeli
 */
function deleteSpeaker(spRole){
    var id = $("#delID"+spRole).val();


    $.ajax({
        url: "http://localhost:4567/login/delete?id=" + id,
        type: 'GET',
        async: true,
        success: function (data) {
            alert(data);

        },
        error: function (ex){
            alert("Sorry user, this request didnt work.");
        }
    });
}

/**
 * changes currrent password of a user
 * @param spRole
 * @author Rodiana Koukouzeli
 */
function changePassword(spRole){
    var user = $("#uName"+spRole).val();
    var oldPassword = $("#oldPw"+spRole).val();
    var newPassword = $("#changePw"+spRole).val();


    $.ajax({
        url: "http://localhost:4567/login/deletepw?user=" + user + "&oldPassword=" + oldPassword + "&newPassword=" + newPassword,
        type: 'GET',
        async: true,
        success: function (data) {
            if (data === "true"){
                alert("Passwort wurde erfolgreich geändert")
            }else{
                alert("Etwas ist schiefgegangen, versuchen Sie es erneut.")
            }

        },
        error: function (ex){
            alert("Sorry user, this request didnt work.");
        }
    });
}

/**
 * deletes a user from the "Users" collection.
 * @author Rodiana Koukouzeli
 */
function deleteUser(){
    var user = $("#delU").val();


    $.ajax({
        url: "http://localhost:4567/login/deleteuser?user=" + user,
        type: 'GET',
        async: true,
        success: function (data) {
            alert(data);

        },
        error: function (ex){
            alert("Sorry user, this request didnt work.");
        }
    });
}

/**
 * Upgrades role of a user.
 * @author Rodiana Koukouzeli
 */
function upgradeUser(){
    var user = $("#userAdmin").val();
    var userpassword = $("#userpassword").val();
    var role = $( "#rolesUpgr option:selected" ).text();



    $.ajax({
        url: "http://localhost:4567/login/upgradeuser?user=" + user+ "&userpassword=" + userpassword + "&role=" + role,
        type: 'GET',
        async: true,
        success: function (data) {
            if (data === "true"){
                alert("User upgraded;")
            }else {
                alert("Something went wrong, try again.")
            }

        },
        error: function (ex){
            alert("Sorry user, this request didnt work.");
        }
    });
}



