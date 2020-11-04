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
<p>Привет, ${order.client.manager.givenName}!</p>
<p>${message}</p>

<table style="border: 1px solid black;">
    <tr>
        <th style="150px;">ID</th>
        <td style="150px;">${order.id?c}</td>
    </tr>
    <tr>
        <th>Статус</th>
        <td>${order.status.title}</td>
    </tr>
    <tr>
        <th>Площадь</th>
        <td>${order.area} м<sup>2</sup></td>
    </tr>
    <tr>
        <th>Документ</th>
        <td>${order.document!}</td>
    </tr>
    <tr>
        <th>Сумма заказа</th>
        <td>${order.price!} грн</td>
    </tr>
    <tr>
        <th>Клиент</th>
        <td>${order.client.name!}</td>
    </tr>
</table>
<p>Перейти на KANBAN: <a href="${url}" target="_blank">${url}</a></p>
<br/>
${signature!}<br/>
</body>
</html>