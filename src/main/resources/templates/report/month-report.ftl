<#include "../header.ftl">
<h1>Отчет за ${month} ${year?c} года</h1>

<table class="responsive-table" style="width:30em">
    <tr>
        <thead>
        <th style="min-width: 15em"></th>
        <#list teams?keys as teamId>
        <th style="min-width: 10em">
        ${teams[teamId].name}
        </th>
        </#list>
        <th style="min-width: 10em">Итого</th>
        </thead>
    </tr>
    <tr>
        <th>Общая площадь покраски</th>
    <#list ordersByTeam?keys as teamId>
        <td>
        ${areaByTeamId[teamId]?string["0.##"]} м<sup>2</sup>
        </td>
    </#list>
        <td>
        <#assign totalArea = 0>
        <#list areaByTeamId?keys as teamId><#assign totalArea += areaByTeamId[teamId]></#list>
        ${totalArea?string["0.##"]} м<sup>2</sup>
        </td>
    </tr>
    <tr>
        <th>Общий валовый доход</th>
    <#assign totalPrice = 0>
    <#list ordersByTeam?keys as teamId>
        <td>
            <#assign orders = ordersByTeam[teamId]>
            <#assign sumPrice = 0>
            <#list orders as order><#assign sumPrice += order.price></#list>
            <#assign totalPrice += sumPrice>
        ${sumPrice?string["0.##"]} грн
        </td>
    </#list>
        <td>
        ${totalPrice?string["0.##"]} грн
        </td>
    </tr>
    <tr>
        <th>Потраченная краска</th>
    <#assign totalQuantity = 0>
    <#list ordersByTeam?keys as teamId>
        <td>
            <#assign orders = ordersByTeam[teamId]>
            <#assign sumConsumesQuantity = 0>
            <#list orders as order><#assign sumConsumesQuantity += order.consumes[0].actualUsedQuantity></#list>
            <#assign totalQuantity += sumConsumesQuantity>
        ${sumConsumesQuantity?string["0.##"]} кг
        </td>
    </#list>
        <td>
        ${totalQuantity?string["0.##"]} кг
        </td>
    </tr>
    <tr>
        <th>Сумма затрат на краску</th>
    <#assign totalConsumesPrice = 0>
    <#list ordersByTeam?keys as teamId>
        <td>
            <#assign orders = ordersByTeam[teamId]>
            <#assign sumPriceConsumes = 0>
            <#list orders as order><#assign sumPriceConsumes += order.consumes[0].product.price * order.consumes[0].actualUsedQuantity></#list>
            <#assign totalConsumesPrice += sumPriceConsumes>
        ${sumPriceConsumes?string["0.##"]} грн
        </td>
    </#list>
        <td>
        ${totalConsumesPrice?string["0.##"]} грн
        </td>
    </tr>
    <tr>
        <th>Средняя стоимость м<sup>2</sup></th>
    <#list ordersByTeam?keys as teamId>
        <td>
            <#assign orders = ordersByTeam[teamId]>
            <#assign sumPrice = 0>
            <#list orders as order><#assign sumPrice += order.price></#list>
        ${(sumPrice / areaByTeamId[teamId])?string["0.##"]} грн
        </td>
    </#list>
        <td>-</td>
    </tr>
    <tr>
        <th>Премия маляров</th>
    <#list ordersByTeam?keys as teamId>
        <td>
        ${(areaByTeamId[teamId] * 3)?string["0.##"]} грн
        </td>
    </#list>
        <td>
        ${(totalArea * 3)?string["0.##"]}  грн
        </td>
    </tr>
    <tr>
        <th>Премия помощников</th>
    <#list ordersByTeam?keys as teamId>
        <td>
        ${(areaByTeamId[teamId] * 2)?string["0.##"]} грн
        </td>
    </#list>
        <td>
        ${(totalArea * 2)?string["0.##"]}  грн
        </td>
    </tr>
</table>

<#include "../footer.ftl">