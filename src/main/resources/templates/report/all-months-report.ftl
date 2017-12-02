<#include "../header.ftl">
<h1>Отчет по месяцам</h1>

<table class="responsive-table">
    <thead>
    <tr>
        <th>Месяц</th>
        <th>Количество заказов</th>
        <th>Площадь</th>
        <th>Цена</th>
    </tr>
    </thead>
<#list ordersByMonth?keys?reverse as month>
    <tr>
        <td><a href="/reports/month/${month}">${month}</a></td>
        <td>${ordersByMonth[month]?size}</td>
        <td>
            <#assign sumArea = 0>
            <#list ordersByMonth[month] as order><#assign sumArea += order.area></#list>
            ${sumArea} м<sup>2</sup>
        </td>
        <td>
            <#assign sumPrice = 0>
            <#list ordersByMonth[month] as order><#assign sumPrice += order.price></#list>
            ${sumPrice} грн
        </td>
    </tr>
</#list>
</table>

<#include "../footer.ftl">