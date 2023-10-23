document.addEventListener("DOMContentLoaded", () => {
    let isEmail = false;
    let isNickname = false;
    // let email = '';
    // let nickname = '';
    let key;
    let header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
    let token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const modal = document.getElementById('modal');
    const modalBtnSearch = document.getElementById("modal-btn-search");
    const modalValid = document.getElementById('modal-valid');
    const modalInvalid = document.getElementById('modal-invalid');
    const modalInput = document.getElementById('modal-input');
    const modalBtnUse = document.getElementById('modal-btn-use');
    
    document.getElementById('btn-addr').addEventListener('click', () => {
        new daum.Postcode({
            oncomplete: function(data) {
                document.getElementById('zoneCode').value = data.zonecode;
                document.getElementById('roadName').value = data.roadAddress;
                document.getElementById("detailedAddress").focus();
            }
        }).open();
    });

    function reset() {
        modalInput.classList.remove('is-invalid');
        modalInput.classList.remove('is-valid');
        modalInput.value="";
        modalInput.removeAttribute('readonly');
        modalBtnUse.style.display = "none";
        modalBtnSearch.style.display = "";
    }

    /**
     * 국제화
     * @param {string} langCode 국가 코드, 한국(ko)을 제외한 다른 나라 영어로 나옴.
     * @param {string} msgCode 메시지 코드
     * @returns 국가 코드와 메시지 코드를 보고 해당하는 문자열을 출력.
     */
    function worldMsg(langCode, msgCode) {

    }

    modal.addEventListener('show.bs.modal', e => {
        reset();
        const button = e.relatedTarget;
        const modalTitle = modal.querySelector('.modal-title');
        key = button.getAttribute("data-key");
        if (key === "email") {
            modalTitle.textContent = "이메일 중복 검사";
            modalInput.placeholder = "이메일 입력";
        } else if(key === "nickname") {
            modalTitle.textContent = "닉네임 중복 검사";
            modalInput.placeholder = "닉네임 입력(3글자 이상)";
        }
    });

    //초기화 클릭
    document.getElementById('modal-btn-cancel').addEventListener('click', () => {
        reset();
    });

    //사용
    modalBtnUse.addEventListener('click', () => {
 
        switch(key) {
            case "email":
                document.getElementById('email').value = modalInput.value;;
                // email = data;
                isEmail = true;
                break;
            case "nickname":
                document.getElementById('nickname').value = modalInput.value;;
                // nickname = data;
                isNickname = true;
                break;
            default :
                alert("에러");
                break;
        }
        document.getElementById('modal-close').click();
    });

    modalBtnSearch.addEventListener("click", () => {
        let regEmail = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+$/;
        let regNick = /^[A-Za-z0-9가-힣]{3,}$/;

        //표현식
        if (key === "email" && !regEmail.test(modalInput.value)) {
            modalInput.classList.add('is-invalid');
            modalInput.classList.remove('is-valid');
            modalInvalid.textContent = "올바른 형식으로 입력해주세요.";
            return;
        } else if(key === "nickname" && !regNick.test(modalInput.value)) {
            modalInput.classList.add('is-invalid');
            modalInput.classList.remove('is-valid');
            modalInvalid.textContent = "특수문자 없이 3글자 이상 입력해주세요.";
            return;
        }

        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) { //응답완료
                    if (xhr.responseText === 'true') { //중복 없음.
                        modalInput.classList.remove('is-invalid');
                        modalInput.classList.add('is-valid');
                        modalInput.setAttribute('readonly', true);
                        modalValid.textContent = "사용 가능합니다.";
                        modalBtnUse.style.display = "";
                        modalBtnSearch.style.display = "none";
                    } else { //중복 있음.
                        modalInput.classList.add('is-invalid');
                        modalValid.textContent = "이미 사용중입니다.";
                    }
                } else { //응답실패
                    modalInput.classList.add('is-invalid');
                    modalValid.textContent = "문제가 발생했습니다. 재시도 해주세요.";
                }
            }
        }

        let paramData = {
            source : modalInput.value,
            key : key
        };

        let param = JSON.stringify(paramData);
        xhr.open("POST", "/members/vd", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.setRequestHeader(header, token);
        xhr.send(param);

    });

    //회원가입 버튼 동작
    document.getElementsByClassName("btn-register")[0].addEventListener('click', () => {
        console.log("회원가입 버튼 동작!");

        let pw = document.querySelectorAll("input[type='password']");

        if (pw[0].value != pw[1].value) {
            pw[0].classList.add('is-invalid');
            pw[1].classList.add('is-invalid');
            alert("비밀번호가 서로 다릅니다.")
            return;
        }

        let regTel = /^[0-9]{2,3}-[0-9]{3,4}-[0-9]{3,4}$/;
        let tel = document.getElementById('tel');
        if (!regTel.test(tel.value)) {
            tel.classList.add('is-invalid');
            alert("연락처를 올바르게 작성해주세요.")
            return;
        }

        if (isEmail == true && isNickname == true) {
            document.getElementById("form").submit();
        } else {
            alert("이메일 및 닉네임을 입력해주세요.")
        }
    });
 
})