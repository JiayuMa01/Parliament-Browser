
update();

/**
 * Update bars every 0.5s
 * @author Jiayu Ma(implement)
 */
function update() {
    $.ajax({
        url: "http://localhost:4567/uploadStatus",
        method: 'GET',
        dataType: 'json',
        success: function (dataJSON) {
            // Show that server is online
            document.getElementById("upload-status").innerHTML = "API server is online. Updating progress bar every 0.5s";

            //console.log(dataJSON)

            for(var i in dataJSON) {
                // The id of progressbar in HTML is the id of each document in MongoDB
                id = dataJSON[i]._id;

                // the id of each <a> tag
                if(id=="cntDownloadedXML") {
                    count = dataJSON[i].count;
                    document.getElementById("num"+id).innerHTML = count.toString();
                }
                else if(id=="workstatus") {
                    workstatus = dataJSON[i].status;
                    if(workstatus==1){
                        document.getElementById("workStatus").innerHTML = "Start working: Download new XMLs, analyze XMLs, NLP and insert to MongoDB. Please also check your JAVA terminal";
                    }
                    else{
                        document.getElementById("workStatus").innerHTML = "";
                    }
                }
                else {
                    count = dataJSON[i].count;
                    totalCount = dataJSON[i].total;
                    progressBar = document.getElementById(id);
                    progressBar.value = count;
                    progressBar.max = totalCount;
                    document.getElementById("num"+id).innerHTML = count.toString() + "/" + totalCount.toString();
                }
            }

            // refresh the site every 0.5s
            setTimeout(update, 500);
        },
        error: function(dataJSON){
            // server is offline
            document.getElementById("upload-status").innerHTML = "API server is offline";
            console.log(dataJSON);

            // Update again
            setTimeout(update, 500);
        }
    });
}



/**
 * Update bars every 0.5s
 * @author Jiayu Ma(implement)
 */
function addNewData() {
    document.getElementById("workStatus").innerHTML = "Start working: Download new XMLs, analyze XMLs, NLP and insert to MongoDB. Please also check your JAVA terminal";
    $.ajax({
        url: "http://localhost:4567/addNewData",
        method: 'GET',
        dataType: 'json',
        success: function (dataJSON) {
            // Show that server is online
            document.getElementById("workStatus").innerHTML = "Upload completed";
            nameList = "Downloaded XMLs Names: ";
            for (let i = 0; i < dataJSON[0].downloadedXMLsName.length; i++) {
                nameList += dataJSON[0].downloadedXMLsName[i] + " ";
            }
            document.getElementById("nameDownloadedXML").innerHTML = nameList;
            //console.log(dataJSON)


        },
        error: function(dataJSON){
            // server is offline
            document.getElementById("workStatus").innerHTML = "Error. Delete \"XMLResources\" under \"parliament_browser_3_1\", set the \"status\" of \"workstatus\" document in the \"countProgress\" collection in the database to 0. Then try again";
            console.log(dataJSON);
        }
    });
}