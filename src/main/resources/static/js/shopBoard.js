document.getElementById("search-btn").addEventListener("click", e => {
    e.preventDefault();
    page(0);
});

function page(page) {
    var searchCategory = document.getElementById("searchCategory").value;
    var searchBy = document.getElementById("searchBy").value;
    var searchQuery = document.getElementById("searchQuery").value;

    location.href = "/shop/board/?page="+ page + "&searchCategory=" + searchCategory + "&searchBy=" + searchBy + "&searchQuery=" + searchQuery;
}