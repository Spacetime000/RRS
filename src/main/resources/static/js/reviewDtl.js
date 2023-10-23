const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
const reviewId = document.getElementById("review-id").value;


document.getElementById("del-review").addEventListener("click", () => {
    

    if (confirm("삭제하시겠습니까?")) {
        fetch("/review/del/" + reviewId, {
            method: "DELETE",
            headers: {[header] : token}
        })
        .then(res => {
            window.location.replace("/review/board");
        })
        .catch(err => {
            window.location.replace("/review/board");
        });
    }
});