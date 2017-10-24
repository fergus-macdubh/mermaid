<html>
<head>
    <style>
        td, th {
            border: 1px solid darkblue;
            padding: 3px;
        }

        th {
            background-color: lightblue;
        }
    </style>
</head>
<body>
<p>Привет, ${order.manager.givenName}!</p>
<p>${message}</p>

<table style="border: 1px solid black;">
    <tr>
        <th style="150px;">ID</th>
        <td style="150px;">${order.id}</td>
    </tr>
    <tr>
        <th>Статус</th>
        <td>${order.status}</td>
    </tr>
    <tr>
        <th>Площадь</th>
        <td>${order.area} м<sup>2</sup></td>
    </tr>
    <tr>
        <th>Документ</th>
        <td class="modal-order-document">${order.document!}</td>
    </tr>
    <tr>
        <th>Сумма заказа</th>
        <td class="modal-order-document">${order.price!} грн</td>
    </tr>
</table>
</body>
</html>