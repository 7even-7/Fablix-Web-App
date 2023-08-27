/*let search = $("#search");

/!**
 * Submit form content with POST method
 * @param cartEvent
 *!/
//gets the input field value then goes to the movie matched page
function handleSearchRes(resultData){
    let query = $("#query").val();
    let url = "fullTextSearchRes.html?query=" + query;
    window.location.href= url;
}
function displayMovies(formSubmitEvent){
    formSubmitEvent.preventDefault();

    $.ajax({
        method: "GET",
        success: handleSearchRes
    });
}*/
document.getElementById("searchForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Prevent form submission

    let query = document.getElementById("autocomplete").value;
    let encodedQuery = encodeURIComponent(query); // Encode the query

    // Redirect the user to the search results page with the query as a parameter
    window.location.href = "fullTextSearchRes.html?query=" + encodedQuery;
});
/*

// Bind the submit action of the form to a event handler function
search.submit(displayMovies);*/
