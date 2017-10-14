<#include "../header.ftl">
<h1>Действия по товару</h1>

<table class="responsive-table">
    <thead>
    <tr>
        <td>ID</td>
        <td>Цена</td>
        <td>Количество</td>
        <td>Цена за единицу</td>
        <td>Тип</td>
        <td>Менеджер</td>
    </tr>
    </thead>
<#list actions as action>
    <tr>
        <td>${action.id}</td>
        <td>${action.price} грн</a></td>
        <td>${action.quantity} ${action.product.unit.abbr}</td>
        <td>${action.price / action.quantity} грн / ${action.product.unit.abbr}</a></td>
        <td>${action.type}</td>
        <td>${action.actor.name}</td>
    </tr>
</#list>
</table>
<#include "../footer.ftl">