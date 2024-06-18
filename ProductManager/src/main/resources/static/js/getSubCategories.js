document.addEventListener("DOMContentLoaded", function () {
    const largeCategory = document.getElementById('largeCategory');
    const middleCategory = document.getElementById('middleCategory');
    const smallCategory = document.getElementById('smallCategory');

    function getSubCategories(parentId, selectElement, selectedId) {
        if (parentId) {
            fetch(`/category/getSubCategories/${parentId}`)
                .then(response => response.json())
                .then(data => {
                    selectElement.innerHTML = '<option value="">選択してください</option>';
                    data.forEach(category => {
                        const option = document.createElement('option');
                        option.value = category.id;
                        option.textContent = category.name;
                        if (category.id === selectedId) {
                            option.selected = true;
                        }
                        selectElement.appendChild(option);
                    });
                })
                .catch(error => console.error('カテゴリの取得に失敗しました。', error));
        } else {
            selectElement.innerHTML = '<option value="">選択してください</option>';
        }
    }

    largeCategory.addEventListener('change', function () {
        const parentId = largeCategory.value;
        getSubCategories(parentId, middleCategory);
        smallCategory.innerHTML = '<option value="">選択してください</option>';
    });

    middleCategory.addEventListener('change', function () {
        const parentId = middleCategory.value;
        getSubCategories(parentId, smallCategory);
    });
});
