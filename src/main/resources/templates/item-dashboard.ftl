<#include "header.ftl">
<h1>Товары</h1>

<table class="responsive-table">
    <thead>
    <tr>
        <td>ID</td>
        <td>Наименование</td>
        <td>Производитель</td>
        <td>Поставщик</td>
        <td>Единицы измерения</td>
    </tr>
    </thead>
<#list items as item>
    <tr>
        <td>${item.id}</td>
        <td><a href="/storage/item/${item.id?c}/edit">${item.name}</a></td>
        <td>${item.producer!}</td>
        <td>${item.supplier!}</td>
        <td>${item.unit.abbr}</td>
    </tr>
</#list>
</table>
<#include "footer.ftl">