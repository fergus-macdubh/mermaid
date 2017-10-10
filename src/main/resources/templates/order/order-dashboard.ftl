<#include "../header.ftl">
<h1>Заказы</h1>

<table class="responsive-table">
    <thead>
    <tr>
        <td>ID</td>
        <td>Статус</td>
        <td>Менеджер</td>
        <td>Площадь</td>
        <td>Клиент</td>
        <td>Цена</td>
    </tr>
    </thead>
<#list orders as order>
    <tr>
        <td><a href="/order/${order.id?c}/edit">${order.id}</a></td>
        <td>${order.status}</td>
        <td>${order.manager!}</td>
        <td>${order.area}</td>
        <td>${order.client!}</td>
        <td>${order.price}</td>
    </tr>
</#list>
</table>
<#include "../footer.ftl">