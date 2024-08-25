function calculateOrderAmount() {
    const quantity = document.getElementById('order-quantity').value;
    const costPrice = parseInt(document.getElementById('cost-price').innerText.replace('¥', '').replace(',', ''));
    const orderAmount = quantity * costPrice;
    document.getElementById('order-amount').innerText = '¥' + orderAmount.toLocaleString();
}

function updateOrderState() {
    const quantity = document.getElementById('order-quantity').value;
    const orderButton = document.getElementById('order-button');

    calculateOrderAmount();

    orderButton.disabled = quantity === "0";
}

function confirmOrder() {
    const quantity = document.getElementById('order-quantity').value;
    const orderAmount = document.getElementById('order-amount').innerText;

    document.getElementById('confirm-quantity').innerText = quantity;
    document.getElementById('confirm-amount').innerText = orderAmount;

    $('#confirmOrderModal').modal('show');
}

function completeOrder() {

    const productElement = document.getElementById('product');
    const productId = productElement.getAttribute('data-product-id');

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const quantity = document.getElementById('order-quantity').value;

    const orderData = {
        quantity: quantity,
        productId: productId
    };

    fetch('/api/orders', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify(orderData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('エラーが発生しました。');
            }
            return response;
        })
        .then(data => {
            $('#confirmOrderModal').modal('hide');
            $('#completeOrderModal').modal('show');
        })
        .catch(error => {
            $('#confirmOrderModal').modal('hide');
            alert('発注処理に失敗しました。もう一度お試しください。');
        });
}

function reload() {
    location.reload();
}


// 初期表示時に実行
document.addEventListener('DOMContentLoaded', function () {
    updateOrderState();
});