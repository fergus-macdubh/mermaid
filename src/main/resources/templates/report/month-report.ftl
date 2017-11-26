<#include "../header.ftl">
<h1>Отчет за ${month} ${year?c} года</h1>

<table class="responsive-table" style="width:30em">
    <tr>
        <th>Общая площадь покраски</th>
        <td>
            <#assign sumArea = 0>
            <#list orders as order><#assign sumArea += order.area></#list>
            ${sumArea} м<sup>2</sup>
        </td>
    </tr>
    <tr>
        <th>Общий валовый доход</th>
        <td>
            <#assign sumPrice = 0>
            <#list orders as order><#assign sumPrice += order.price></#list>
            ${sumPrice?string["0.##"]} грн
        </td>
    </tr>
    <tr>
        <th>Сумма затрат на краску</th>
        <td>
            <#assign sumPriceConsumes = 0>
            <#list orders as order><#assign sumPriceConsumes += order.consumes[0].product.price></#list>
            ${sumPriceConsumes?string["0.##"]} грн
        </td>
    </tr>
    <tr>
        <th>Премия маляров</th>
        <td>${(sumArea * 3)?string["0.##"]} грн</td>
    </tr>
    <tr>
        <th>Премия помощников</th>
        <td>${(sumArea * 2)?string["0.##"]} грн</td>
    </tr>
</table>

<#include "../footer.ftl">