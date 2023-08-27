let add_star_form = $("#add_star_form");

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleAddStarResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);
    let resultMessage = $("#result_message");

    resultMessage.empty();

    if (resultDataJson["status"] === "success") {
        resultMessage.text("Star added successfully!");
    } else {
        resultMessage.text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitAddStarForm(formSubmitEvent) {
    console.log("submit add star form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/add_star", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: add_star_form.serialize(),
            success: handleAddStarResult
        }
    );
}
add_star_form.submit(submitAddStarForm);

$(document).ready(function() {
  $("#metadataButton").click(function() {
    console.log("Button clicked!");
    var databaseName = "moviedb";
    getMetadata(databaseName);
  });
});

function getMetadata(databaseName) {
  $.ajax({
    url: "api/get_metadata", // the URL of the servlet
    type: "POST",
    data: {database: databaseName},
    dataType: "json",
    success: function(response) {
        // Create HTML elements based on the metadata and append them to the page
        var $metadataDiv = $("<div>").attr("id", "metadata");
        $.each(response, function(tableName, tableData) {
          console.log(tableData);
          var $tableDiv = $("<div>").addClass("table");
          $tableDiv.append("<strong>" + tableName + "</strong><br>");
          for (const key in tableData) {
              if (key != "table") {
                  var $columnDiv = $("<div>").addClass("column");
                  $columnDiv.append(`${key}: ${tableData[key].type}`);
                  $tableDiv.append($columnDiv);
              }
          }
/*          $.each(tableData.columns, function(columnName, columnType) {
            var $columnDiv = $("<div>").addClass("column");
            $columnDiv.append(columnName + " (" + columnType + ")");
            $tableDiv.append($columnDiv);
          });*/
          $metadataDiv.append($tableDiv);
        });
        $("body").append($metadataDiv);
    },
    error: function(xhr, status, error) {
      // Handle the error case
      console.error("Error retrieving metadata:", error);
    }
  });
}

