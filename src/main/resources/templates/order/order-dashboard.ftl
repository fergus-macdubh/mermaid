<#include "../header.ftl">
<h1>Заказы</h1>

<table class="responsive-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Статус</th>
        <th>Менеджер</th>
        <th>Площадь</th>
        <th>Документ</th>
        <th>Цена</th>
    <#if user.role == "ROLE_ADMIN"
    || user.role == "ROLE_OPERATOR"
    || user.role == "ROLE_PAINTER">
        <th>Редактировать</th>
    </#if>
    </tr>
    </thead>
<#list orders as order>
    <tr>
        <td>${order.id}</td>
        <td>${order.status}</td>
        <td>${order.manager.name}</td>
        <td>${order.area}</td>
        <td>${order.document!}</td>
        <td>${order.price}</td>
        <#if user.role == "ROLE_ADMIN"
        || user.role == "ROLE_OPERATOR"
        || user.role == "ROLE_PAINTER">
            <td><a class="btn btn-info" href="/order/${order.id?c}/edit">Редактировать</a></td>
        </#if>
    </tr>
</#list>
</table>
<#include "../footer.ftl">