<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>
<p>Привет, ${order.manager.givenName}!</p>
<p>${message}</p>

<table>
    <tr>
        <th>ID</th>
        <td class="modal-order-id">${order.id}</td>
    </tr>
    <tr>
        <th>Статус</th>
        <td class="modal-order-status">${order.status}</td>
    </tr>
    <tr>
        <th>Площадь</th>
        <td class="modal-order-area">${order.area}</td>
    </tr>
    <tr>
        <th>Документ</th>
        <td class="modal-order-document">${order.document!}</td>
    </tr>
</table>
<table>
    <thead>
    <th>Расходный материал</th>
    <th>Расчетный расход</th>
    </thead>
    <#list order.consumes as consume>
        <tr>
            <td>${consume.product.name}</td>
            <td>${consume.calculatedQuantity}</td>
        </tr>
    </#list>
</table>
</body>
</html>