// global variables to be accessible for different functions
var wpArray = [];
var indexArray = [];
var speechesArray = [];

/**
 * Function which shows all Protocols as pdf
 * @author Matthias Beck(implemented)
 */
function exportProtocolsAll() {
    $("#printExport").append("<object data=\"/export/all\" type=\"application/pdf\" width=\"1200\" height=\"700\"><div>Vorschau nicht möglich!</div></object>");
}

/**
 * function to add a protocol to the list of protocols for export to pdf
 *
 * @author Matthias Beck
 */
function addProtocol() {
    wpArray.push(document.getElementById('wp').value);
    indexArray.push(document.getElementById('index').value);
    speechesArray.push(document.getElementById('speeches').value);
}

/**
 * function to export the given protocols to pdf
 *
 * @author Matthias Beck
 */
function exportSomeProtocols() {
    var wpString = "";
    var indexString = "";
    var speechesString = "";

    for (let i = 0; i < wpArray.length; i++) {
        wpString += wpArray[i] + "-";
        indexString += indexArray[i] + "-";
        speechesString += speechesArray[i] + "-";
    }

    // create object filled with the data for sending to backend
    var obj = new Object();
    obj.wp = wpString;
    obj.index  = indexString;
    obj.speeches = speechesString;

    // send date to the backend - there the pdf will be created
    fetch("/export/exportSomeProtocols", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(obj)
    }).then(res => {
        console.log("Request complete! response:", res);
        document.getElementById('printExport').innerHTML += "PDF erfolgreich erzeugt.<br>";
    });
}

/**
 * Function which shows some choosen protocols as pdf
 * @author Matthias Beck(implemented)
 */
function displaySomeProtocols() {
    $("#printExport").append("<object data=\"/export/displaySomeProtocols\" type=\"application/pdf\" width=\"1200\" height=\"700\"><div>Vorschau nicht möglich!</div></object>");
}