<#include "../header.ftl">
<h1>Товары</h1>

<table class="responsive-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Наименование</th>
        <th>Производитель</th>
        <th>Поставщик</th>
        <th>Единицы измерения</th>
    </tr>
    </thead>
<#list products as product>
    <tr>
        <td>${product.id}</td>
        <td><a href="/product/${product.id?c}/edit">${product.name}</a></td>
        <td>${product.producer!}</td>
        <td>${product.supplier!}</td>
        <td>${product.unit.abbr}</td>
    </tr>
</#list>
</table>
<#include "../footer.ftl">