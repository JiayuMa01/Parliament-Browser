/**
 * Function which shows the names of the existing templates
 *
 * @author Matthias Beck
 */
function showTemplateNames() {
    $.ajax({
        url: "http://localhost:4567/export/getTemplateNames",
        method: 'GET',

        success: function(data) {
            var nameList = [];
            var name = "";

            // save the names from the backend in a list
            for (let i = 0; i < data.length; i++) {
                if (data[i] != ",") {
                    name += data[i];
                }
                else {
                    nameList.push(name);
                    name = "";
                }
            }

            // create label and button for every templatename
            for (let i = 0; i < nameList.length; i++) {
                var functionName = nameList[i];
                var html = '<div><label>' + nameList[i] + '</label>' +
                    '<button onclick="editTemplateText(\'' + functionName + '\')">Bearbeiten</button></div>';
                document.getElementsByClassName('templateNames').item(0).innerHTML += html;
            }
            // creates a button to create a new template
            document.getElementsByClassName('templateNames').item(0).innerHTML += "</br>" +
                '<button onclick="editTemplateText(\'einzufuegen\')">Neues Template</button>';
        },

        error: function(error){
            console.log(error);
        }
    });
}

/**
 * Function which is used to show the text of a template in a textarea to be able to change the text
 * Also possible to show a not filled textarea to create a new template
 *
 * @author Matthias Beck
 * @param name
 */
function editTemplateText(name) {

    $.ajax({
        url: "http://localhost:4567/export/getTemplateTextByName/" + name,
        type: 'GET',

        success: function (data) {
            document.getElementsByClassName('templateNames').item(0).innerHTML = "";
            document.getElementsByClassName('editTemplate').item(0).innerHTML +=
                "<input type='text' id='templatename'></br><textarea id=\"templatetext\"></textarea></br></br>" +
                '<button id="savetemplate" onclick="saveTemplate()">Template speichern</button>';
            document.getElementById('templatename').value = name;
            document.getElementById('templatetext').innerHTML += data;
        },

        error: function (error) {
            console.log(error);
        }
    });

}

/**
 * Function which saves the edited or new created template
 *
 * @author Matthias Beck
 */
function saveTemplate() {

    var obj = new Object();
    obj.name = document.getElementById('templatename').value;
    obj.text  = document.getElementById('templatetext').value;

    fetch("/export/saveTemplate", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(obj)
    }).then(res => {
        console.log("Request complete! response:", res);
        document.getElementsByClassName('editTemplate').item(0).innerHTML = "Erfolgreich erzeugt!";
    });
}