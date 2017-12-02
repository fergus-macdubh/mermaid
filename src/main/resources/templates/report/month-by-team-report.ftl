<#include "../header.ftl">
<h1>Отчет по бригаде "${team.name}" за ${month} ${year?c} года</h1>

<table class="responsive-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Менеджер</th>
        <th>Площадь</th>
        <th>Документ</th>
        <th>Цена</th>
        <th>Закрыт</th>
    </tr>
    </thead>
<#list orders?sort_by('completed') as order>
    <tr>
        <td>${order.id}</td>
        <td>${order.manager.name}</td>
        <td>${order.area} м<sup>2</sup></td>
        <td>${order.document!}</td>
        <td>${order.price} грн</td>
        <td>${order.completed.format('dd MMMM')}</td>
    </tr>
</#list>
</table>

<#include "../footer.ftl">