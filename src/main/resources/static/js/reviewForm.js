const key = '카카오Key';
const inputSearch = document.querySelector('.modal-header input');
const tbody = document.querySelector('tbody');
const inputId = document.getElementById("shopId");
const inputShopName = document.getElementById("shopName");
const inputCategory = document.getElementById("category");
const inputRoad = document.getElementById("roadName");
const modalClose = document.getElementById("modalClose");

document.getElementById('search').addEventListener('click', () => {

    if (inputSearch.value.trim() == '') {
        alert("공백은 허용하지 않습니다.");
        return;
    }

    tbody.textContent = ''; //재검색시 전 검색 제거 역할

    let query = inputSearch.value;
    let categoryGroupCode;
    let headers = {Authorization : key};
    let reData;
    

    document.getElementsByName('rd-category').forEach((node) => {
        if (node.checked) {
            categoryGroupCode = node.value;
        }
    });

    let url = `https://dapi.kakao.com/v2/local/search/keyword.json?query=${query}&category_group_code=${categoryGroupCode}`;
    fetch(url, {headers})
        .then(response => response.json())
        .then(data => {
            reData = data; //수정 가능성 있음.
            reData['documents'].forEach((node) => {
                let categories = node['category_name'].split(' > ');
                let tr = document.createElement('tr');
                tr.setAttribute('name', 'shop');
                
                let td1 = document.createElement('td');
                let td2 = document.createElement('td');
                let td3 = document.createElement('td');
                let hideInput = document.createElement('input');
                hideInput.setAttribute('type', 'hidden');
                hideInput.value = node['id'];
                
                tr.appendChild(hideInput);
                td1.appendChild(document.createTextNode(node['place_name']));
                td2.appendChild(document.createTextNode(categories[1]));
                td3.appendChild(document.createTextNode(node['road_address_name']));

                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tbody.appendChild(tr);
            });
            document.querySelectorAll("tr[name='shop']").forEach(e => {
                e.addEventListener("click", () => {
                    let t = e.querySelectorAll('td');
                    inputId.value = e.querySelector('input').value;
                    inputShopName.value = t[0].innerText;
                    inputCategory.value = t[1].innerText;
                    inputRoad.value = t[2].innerText;
                    modalClose.click();
                });
            });
        })
        .catch(error => {
            console.log(error);
        });
});