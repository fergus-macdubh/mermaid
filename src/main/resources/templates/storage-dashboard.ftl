<#include "header.ftl">
<h1>Склад</h1>

<table class="responsive-table">
    <thead>
    <tr>
        <td>ID</td>
        <td>Наименование</td>
        <td>Количество</td>
    </tr>
    </thead>
    <#list items as item>
        <tr>
            <td>${item.id}</td>
            <td><a href="/storage/item/${item.id}/action">${item.name}</a></td>
            <td>${item.quantity} ${item.unit.abbr}</td>
        </tr>
    </#list>
</table>
<#include "footer.ftl">