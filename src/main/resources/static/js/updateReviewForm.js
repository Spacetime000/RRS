let header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
let token = document.querySelector("meta[name='_csrf']").getAttribute("content");

document.querySelectorAll("a[name='file-delete']").forEach(e => {
    e.addEventListener("click", t => {
        t.preventDefault();

        let id = e.previousElementSibling.value;

        if (confirm("삭제하시겠습니까?")) {
            fetch("/review/delete/" + id, {
                method: "DELETE",
                headers: {[header] : token},
            })
            .then()
            .catch((error) => console.error("에러", error));

            e.parentElement.remove();
        }
    });
});