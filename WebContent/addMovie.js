let add_movie_form = $("#add_movie_form");

/**
 * Handle the data returned by AddMovieServlet
 * @param resultDataString jsonObject
 */
function handleAddMovieResult(resultDataString) {

    let resultDataJson = JSON.parse(resultDataString);
    console.log(resultDataString["message"]);
    let resultMessage = $("#result_message");

    resultMessage.empty();

    if (resultDataJson["status"] === "success") {
        resultMessage.text("Star added successfully!");
    } else {
        console.log("add movie failed");
        resultMessage.text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitAddMovieForm(formSubmitEvent) {
    console.log("submit add movie form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/add_movie", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: add_movie_form.serialize(),
            success: handleAddMovieResult
        }
    );
}
add_movie_form.submit(submitAddMovieForm);