<#include "../header.ftl">
<h1>Действия по товару</h1>

<table class="responsive-table">
    <thead>
    <tr>
        <td>ID</td>
        <td>Цена</td>
        <td>Количество</td>
        <td>Тип</td>
        <td>Менеджер</td>
    </tr>
    </thead>
<#list actions as action>
    <tr>
        <td>${action.id}</td>
        <td>${action.price}</a></td>
        <td>${action.quantity}</td>
        <td>${action.type}</td>
        <td>${action.manager!}</td>
    </tr>
</#list>
</table>
<#include "../footer.ftl">