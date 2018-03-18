<#include "../header.ftl">
<h1>Отчет по краске за ${month} ${year?c} года</h1>

<table class="responsive-table">
    <tr>
        <thead>
        <th>Название</th>
        <th>Количество заказов</th>
        <th>Площадь</th>
        <th>Кляймеры</th>
        <th>Мелкая фурнитура</th>
        <th>Крупная фурнитура</th>
        <th>Количество</th>
        </thead>
    </tr>
<#list paints?sort_by('sumQuantity')?reverse as paint>
    <tr>
        <td><a href="/storage/product/${paint.productId?c}/action">${paint.name} ${paint.producer}</a></td>
        <td>${paint.orderCount}</td>
        <td>${paint.sumArea}</td>
        <td>${paint.sumClips}</td>
        <td>${paint.sumSmallFurniture}</td>
        <td>${paint.sumBigFurniture}</td>
        <td>${paint.sumQuantity?string[",##0.00"]} кг</td>
</#list>
</table>

<#include "../footer.ftl">