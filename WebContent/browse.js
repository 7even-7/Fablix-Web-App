
/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function displayGenres(result){
    let starTableBodyElement = jQuery("#genre_table");
    let table = document.getElementById("genre_table");
    table.innerHTML = "";
    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < result.length; i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th style='text-align: center;'><a href='home.html?genre=" + result[i]["genre"] + "'>" + result[i]["genre"] + "</a></th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        starTableBodyElement.append(rowHTML);
    }
}

function displayMovies(){
    let movieTitleElement = jQuery("#genre_table");
    let table = document.getElementById("genre_table");
    table.innerHTML = "";


    // Iterate through resultData, no more than 10 entries
    let alphaNumArray = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "*"];

    for (let i = 0; i < alphaNumArray.length; i++) {
        let rowHTML = "";
        // Concatenate the html tags with resultData jsonObject
        rowHTML += "<tr>";
        rowHTML += "<th style='text-align: center;'><a href='home.html?alpha=" + alphaNumArray[i] + "'>" + alphaNumArray[i] + "</a></th>";
        rowHTML += "</tr>";
        movieTitleElement.append(rowHTML);

        // Append the row created to the table body, which will refresh the page
    }

}
function showGenres() {
    console.log("showing genres");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */

    $.ajax(
        "api/genres", {
            method: "GET",
            success: (resultData) => displayGenres(resultData)
        }
    );
}
function showTitles() {
    console.log("showing movie titles");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    displayMovies()
}

