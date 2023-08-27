let search = $("#search");

/**
 * Submit form content with POST method
 * @param cartEvent
 */
//gets the input field value then goes to the movie matched page
function handleSearchRes(resultData){
    let title = $("#title").val();
    let year = $("#year").val();
    let director = $("#director").val();
    let star = $("#starName").val();
    let url = "movieSearchRes.html?title=" + title + "/year=" + year + "/director=" + director + "/star="+star;
    window.location.href= url;
}
function displayMovies(formSubmitEvent){
    formSubmitEvent.preventDefault();

    $.ajax({
        method: "GET",
        success: handleSearchRes
    });
}



// Bind the submit action of the form to a event handler function
search.submit(displayMovies);
