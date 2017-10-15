<#include "../header.ftl">
<h1>Заказы</h1>

<table class="responsive-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Статус</th>
        <th>Менеджер</th>
        <th>Площадь</th>
        <th>Клиент</th>
        <th>Цена</th>
    </tr>
    </thead>
<#list orders as order>
    <tr>
        <td><a href="/order/${order.id?c}/edit">${order.id}</a></td>
        <td>${order.status}</td>
        <td>${order.manager.name}</td>
        <td>${order.area}</td>
        <td>${order.client!}</td>
        <td>${order.price}</td>
    </tr>
</#list>
</table>
<#include "../footer.ftl">