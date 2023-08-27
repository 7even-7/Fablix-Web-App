function handleResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");
    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");

    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < resultData.length; i++) {
        console.log(resultData[i]["movieRating"]);
        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" + '<a href="single-movie.html?id=' + resultData[i]["movieId"] + '">' +
            resultData[i]["movieTitle"] + '</a>' + "</th>";
        rowHTML += "<th>" + resultData[i]["movieYear"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movieDirector"] + "</th>";
        const starsArray = resultData[i]["movieStar"].split(",");
        let topThreeStars = "";
        for (let j = 0; j < Math.min(3, starsArray.length);j++){
            topThreeStars +=

                // Add a link to single-star.html with id passed with GET url parameter
                '<a href="single-star.html?id=' + starsArray[j] + '">'
                + starsArray[j] +     // display star_name for the link text
                '</a>';
            if (j != 2) topThreeStars += ", ";
        }
        rowHTML += "<th>" + topThreeStars + "</th>";
        rowHTML += "<th>" + resultData[i]["movieRating"] + "</th>";
        rowHTML += '<th><button type="button" onclick="showMsg()" class="btn btn-primary">Add to Cart</button></th>';
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}

$(document).ready(function() {
    // Extract the query from the URL parameter
    var query = getQueryFromURL();

    // Make an AJAX POST request to the backend
    $.ajax({
        url: "api/full_text_search",
        method: "GET",
        data: { query: query },
        success: function(response) {
            // Handle the response data
            handleResult(response);
        },
        error: function(xhr, textStatus, errorThrown) {
            // Handle the error condition
            console.log("Error:", errorThrown);
        }
    });
});

// Function to extract the query from the URL parameter
function getQueryFromURL() {
    var queryString = window.location.search;
    var urlParams = new URLSearchParams(queryString);
    return urlParams.get("query");
}