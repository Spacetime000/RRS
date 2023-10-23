const reg = /(.*?)\.(jpg|jpeg|png|gif|bmp)$/;
const maxFileSize = 10 * 1024 * 1024; //10MB
const inputFile = document.querySelector("input[type=file]");
const img = document.querySelector(".my-profile img");

document.querySelector(".my-profile").addEventListener("click", () => {
    document.querySelector("input[type=file]").click();
});

inputFile.addEventListener("change", () => {
    var file = inputFile.files[0];
    var fileName = file.name;

    if (file > maxFileSize) {
        inputFile.value = null;
        alert("10MB이하 이미지만 가능합니다.");
        return;
    }

    if (!fileName.match(reg)) {
        inputFile.value = null;
        alert("이미지 파일만 가능합니다.");
        return;
    }

    const imageSrc = URL.createObjectURL(file);
    img.src = imageSrc;
});

