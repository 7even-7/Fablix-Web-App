/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs three steps:
 *      1. Get parameter from request URL so it know which id to look for
 *      2. Use jQuery to talk to backend API to get the json data.
 *      3. Populate the data to correct html elements.
 */


/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */


function getParameterByName(target) {
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

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function showMsg(){
    window.alert("Sucessfully added to cart!");
}

var startIdx = 0;
var jsonString;
function handleResult(resultData) {
    //console.log("handleResult: populating star info from resultData");
    jsonString = resultData;

    // populate the star info h3
    // find the empty h3 body by id "star_info"
    // let starInfoElement = jQuery("#star_info");
    //
    // // append two html <p> created to the h3 body, which will refresh the page
    // starInfoElement.append("<p>Star Name: " + resultData[0]["star_name"] + "</p>" +
    //     "<p>Date Of Birth: " + resultData[0]["movie_rating"] + "</p>");

    //console.log("handleResult: populating movie table from resultData");

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#movie_table");
    movieTableBodyElement.empty();
    let numPerPage = Number(jQuery("#n").val());
    // Concatenate the html tags with resultData jsonObject to create table rows
    var length = startIdx + numPerPage;
    var resLen = resultData.length;

    console.log("the current length is: " + length);

    for (let i = startIdx; i < Math.min(length, resultData.length); i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" + '<a href="single-movie.html?id=' + resultData[i]["movie_id"] + '">' +
        resultData[i]["movie_title"] + '</a>' + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_genres"] + "</th>";
        const starsArray = resultData[i]["star_name"].split(",");
        let topThreeStars = "";
        for (let j = 0; j < starsArray.length;j++){
            const starsInfo = starsArray[j].split(':');
            topThreeStars +=

                // Add a link to single-star.html with id passed with GET url parameter
                '<a href="single-star.html?id=' + starsInfo[0] + '">'
                + starsInfo[1] +     // display star_name for the link text
                '</a>';
                if (j != 2) topThreeStars += ", ";
        }
        rowHTML += "<th>" + topThreeStars + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";
        rowHTML += '<th><button type="button" onclick="showMsg()" class="btn btn-primary">Add to Cart</button></th>';

        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

//go to next page
function  goNext(){
    let dropDownVal = jQuery("#n").val();
    startIdx = startIdx + Number(dropDownVal);
    handleResult(jsonString);
    console.log("called goNext(), drop down value is: " + dropDownVal + " startIdx: " + startIdx);
}
//go to prev page
function goPrev(){

    let dropDownVal = jQuery("#n").val();

    startIdx = startIdx - Number(dropDownVal);
    if (startIdx < 0){
        startIdx = 0;
    }
    handleResult(jsonString);

    console.log("called goPrev(), drop down value is: " + dropDownVal + " startIdx: " + startIdx);
}
// Get id from URL
let url = window.location.href;
var res;
//if its genres
if (url.includes("genre")){
    res = "genre=" + getParameterByName("genre", url);

}
//if its alpha
if (url.includes("alpha")){
    res = "alpha=" + getParameterByName("alpha", url);

}
console.log(res);
// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/movie-list-ex?" + res, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});