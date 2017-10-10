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
        <td>${order.id}</td>
        <td>${order.status}</td>
        <td>${order.manager!}</td>
        <td><a href="/order/${order.id?c}/edit">${order.area}</a></td>
        <td>${order.client!}</td>
        <td>${order.price}</td>
    </tr>
</#list>
</table>
<#include "../footer.ftl">