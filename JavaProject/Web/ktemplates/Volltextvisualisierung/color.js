$(document).ready(function () {
    color()

});

/**
 * these Function are to colorize the persons, organisations and locations
 * @author Fabian Hamid Fazli (implemented) Kevin Schuff(modified)
 */
function color() {
    var myDiv = document.querySelector("#textAfterColor");
    // list indices of locations, organisations and persons in the speech
    const locationIndices = ${locationIndices};
    const organisationIndices = ${organisationIndices};
    const personIndices = ${personIndices};
    const speechText = "${speechText}";
    // object to array
    const locationIndicesArray = Array.from(locationIndices);
    const organisationIndicesArray = Array.from(organisationIndices);
    const personIndicesArray = Array.from(personIndices);
    // piece string together
    let resultString = "";
    for (let i = 0; i < speechText.length; i++) {
        if (locationIndicesArray.includes(i)) {
            resultString += "<span style='color:green'>" + speechText.charAt(i) + "</span>";
        }
        else if (organisationIndicesArray.includes(i)){
            resultString += "<span style='color:yellow'>" + speechText.charAt(i) + "</span>";
        }
        else if (personIndicesArray.includes(i)){
            resultString += "<span style='color:red'>" + speechText.charAt(i) + "</span>";
        }
        else {
            resultString += speechText.charAt(i);
        }

    }
    // add string to ftl
    myDiv.innerHTML = resultString;
}

