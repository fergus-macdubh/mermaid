<#include "../header.ftl">
<h1 class="storage-title">Склад</h1>
<h4 class="storage-price">Товара на сумму ${storagePrice} грн</h4>

<table class="responsive-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Наименование</th>
        <th>Цена последней закупки</th>
        <th>Количество</th>
    <#if user.role == "ROLE_ADMIN">
        <th>Инвентаризация</th>
    </#if>
    </tr>
    </thead>
<#list products as product>
    <tr>
        <td>${product.id}</td>
        <td><a href="/storage/product/${product.id?c}/action">${product.name} ${product.producer}</a></td>
        <td>${product.price} грн / ${product.unit.abbr}</td>
        <td>${product.quantity} ${product.unit.abbr}</td>
        <#if user.role == "ROLE_ADMIN">
            <td>
                <a href="/storage/product/${product.id}/inventorying" class="btn btn-info">Инвентаризация</a>
            </td>
        </#if>
    </tr>
</#list>
</table>
<#include "../footer.ftl">