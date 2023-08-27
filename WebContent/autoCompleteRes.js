/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function getParameterByName(target) {
    console.log(target);
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}
function showMsg(){
    window.alert("Sucessfully added to cart!");
}
function handleStarResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");
    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let starTableBodyElement = jQuery("#star_table_body");

    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < resultData.length; i++) {
        console.log(resultData[i]["movie_title"]);
        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" +  resultData[i]["movie_title"]  +"</th>";
        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_genres"] + "</th>";
        let starsArray = [];
        if (resultData[i]["movie_stars"] != null){
            starsArray = resultData[i]["movie_stars"].split(',');
        }

        let topStars = "";
        for (let j = 0; j < starsArray.length;j++){
            const starsInfo = starsArray[j].split(':');
            topStars +=

                // Add a link to single-star.html with id passed with GET url parameter
                '<a href="single-star.html?id=' + starsInfo[0] + '">'
                + starsInfo[1] +     // display star_name for the link text
                '</a>';
            if (j != starsArray.length -1) topStars += ", ";
        }
        rowHTML += "<th>" + topStars + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";
        rowHTML += '<th><button type="button" onclick="showMsg()" class="btn btn-primary">Add to Cart</button></th>';
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        starTableBodyElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
let movieName = getParameterByName('query');
console.log(movieName);
// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
     url: "api/searchname?query=" + movieName, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});