<#include "../header.ftl">
<h1>Действия по товару</h1>

<table class="responsive-table">
    <thead>
    <tr>
        <th>Дата</th>
        <th>Цена</th>
        <th>Количество</th>
        <th>Цена за единицу</th>
        <th>Тип</th>
        <th>Менеджер</th>
    </tr>
    </thead>
<#list actions as action>
    <tr>
        <td>${action.created.format('dd MMM yyyy')}</td>
        <td><#if action.type == 'PURCHASE'>${action.price} грн<#else>-</#if></td>
        <td><#if action.type == 'SPEND'>-</#if>${action.quantity} ${action.product.unit.abbr}</td>
        <td><#if action.type == 'PURCHASE'>${action.price / action.quantity} грн / ${action.product.unit.abbr}<#else>-</#if></td>
        <td>
            <#switch action.type>
                <#case 'SPEND'>
                    <a href="/order/${action.order.id}/edit">Списано по заказу #${action.order.id}</a>
                    <#break>
                <#case 'PURCHASE'>
                    Покупка
                    <#break>
                <#case 'INVENTORYING'>
                    Инвентаризация
                    <#break>
                <#case 'RETURN'>
            <a href="/order/${action.order.id}/edit">Возврат по заказу #${action.order.id}</a>
                    <#break>
            </#switch>
        </td>
        <td>${action.actor.name}</td>
    </tr>
</#list>
</table>
<#include "../footer.ftl">