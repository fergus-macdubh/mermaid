<#include "../header.ftl">
<h1>Склад</h1>

<table class="responsive-table">
    <thead>
    <tr>
        <td>ID</td>
        <td>Наименование</td>
        <td>Количество</td>
    </tr>
    </thead>
<#list products as product>
    <tr>
        <td>${product.id}</td>
        <td><a href="/storage/product/${product.id?c}/action">${product.name}</a></td>
        <td>${product.quantity} ${product.unit.abbr}</td>
    </tr>
</#list>
</table>
<#include "../footer.ftl">