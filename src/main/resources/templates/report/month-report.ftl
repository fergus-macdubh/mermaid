<#include "../header.ftl">
<h1>Отчет за ${month} ${year?c} года</h1>
<div style="float: left">
<#assign sumArea = 0, totalExpenses = 0>
<#list orders as order>
    <#assign sumArea += (order.area
    + order.clipCount * options['CLIP_TO_AREA']?eval
    + order.furnitureSmallCount * options['FURNITURE_SMALL_TO_AREA']?eval
    + order.furnitureBigCount * options['FURNITURE_BIG_TO_AREA']?eval)>
</#list>
<#assign totalExpenses += options['ELECTRICITY']?eval>

    <h3>Расходы</h3>
    <table class="responsive-table" style="width: 30em">
        <tr>
            <th>Сумма затрат на краску</th>
            <td>
            <#assign sumPriceConsumes = 0>
            <#list orders as order><#assign sumPriceConsumes += order.consumes[0].product.price * order.consumes[0].actualUsedQuantity></#list>
            ${sumPriceConsumes?string[",##0.##"]} грн
            <#assign totalExpenses += sumPriceConsumes>
            </td>
        </tr>
        <tr>
            <th>Электроэнергия</th>
            <td>${(sumArea * options['ELECTRICITY']?eval)?string[",##0.##"]} грн</td>
        <#assign totalExpenses += sumArea * options['ELECTRICITY']?eval>
        </tr>
        <tr>
            <th>Налог ФЛП</th>
            <td>${options['ENTERPRENUER_TAX']?eval?string[",##0.##"]} грн</td>
        <#assign totalExpenses += options['ENTERPRENUER_TAX']?eval>
        </tr>
        <tr>
            <th>Заработная плата</th>
            <td>
            <#assign sumSalary = 0>
            <#list users?values as user>
                <#if user.role == 'ROLE_PAINTER'>
                    <#assign sumSalary += options['SALARY_PAINTER']?eval>
                </#if>
                <#if user.role == 'ROLE_LABORER'>
                    <#assign sumSalary += options['SALARY_LABORER']?eval>
                </#if>
            </#list>
            <#assign sumSalary += options['SALARY_MANAGER']?eval>
            ${sumSalary?string[",##0.##"]} грн
            <#assign totalExpenses += sumSalary>
            </td>
        </tr>
        <tr>
            <th>Премии</th>
            <td>
            <#assign sumBonus = 0>
            <#list teams?keys as teamId>
                <#if usersByTeamId[teamId]??>
                    <#list usersByTeamId[teamId] as user>
                        <#if user.role == 'ROLE_PAINTER'>
                            <#assign sumBonus += options['BONUS_PAINTER']?eval * areaByTeamId[teamId]>
                        </#if>
                        <#if user.role == 'ROLE_LABORER'>
                            <#assign sumBonus += options['BONUS_LABORER']?eval * areaByTeamId[teamId]>
                        </#if>
                    </#list>
                </#if>
            </#list>
            ${sumBonus?string[",##0.##"]} грн
            <#assign totalExpenses += sumBonus>
            </td>
        </tr>
        <tr>
            <th>Аренда помещения</th>
            <td>${options['BUILDING_RENT']?eval?string[",##0.##"]} грн</td>
        <#assign totalExpenses += options['BUILDING_RENT']?eval>
        </tr>
        <tr>
            <th>Амортизация линии</th>
            <td>${options['LINE_AMORTIZATION']?eval?string[",##0.##"]} грн</td>
        <#assign totalExpenses += options['LINE_AMORTIZATION']?eval>
        </tr>
        <tr>
            <th>Расходные материалы</th>
            <td>${(sumArea * options['OTHER_CONSUMES']?eval)?string[",##0.##"]} грн</td>
        <#assign totalExpenses += sumArea * options['OTHER_CONSUMES']?eval>
        </tr>
        <tr>
            <th>Прочие расходы</th>
            <td>${options['OTHER_EXPENSES']?eval?string[",##0.##"]} грн</td>
        <#assign totalExpenses += options['OTHER_EXPENSES']?eval>
        </tr>
    </table>
</div>
<div style="float: left; margin-left: 2em">
    <h3>Доходы</h3>
    <table class="responsive-table" style="width: 30em">
        <tr>
            <th>Общая площадь покраски</th>
            <td>
            ${sumArea} м<sup>2</sup>
            </td>
        </tr>
        <tr>
            <th>Общий валовый доход</th>
            <td>
            <#assign sumPrice = 0>
            <#list orders as order><#assign sumPrice += order.price></#list>
            ${sumPrice?string[",##0.##"]} грн
            </td>
        </tr>
    </table>
</div>
<br style="clear: both">
<h4><strong>Прибыль: ${sumPrice - totalExpenses}</strong></h4>
<h3>По бригадам</h3>
<table class="responsive-table">
    <tr>
        <thead>
        <th>Бригада</th>
        <th>Площадь</th>
        <th>Доход</th>
        <th>Расход краски</th>
        <th>Цена краски</th>
        <th>Зарплата</th>
        <th>Премия</th>
        </thead>
    </tr>
<#list teams?keys as teamId>
    <tr>
        <td><a href="/reports/month/${year?c}-${monthNum?c}/team/${teamId}">${teams[teamId].name}</a></td>
        <td>${areaByTeamId[teamId]?string[",##0.##"]} м<sup>2</sup></td>
        <td>
            <#assign orders = ordersByTeam[teamId]>
            <#assign sumPrice = 0>
            <#list orders as order><#assign sumPrice += order.price></#list>
        ${sumPrice?string[",##0.##"]} грн
        </td>
        <td>
            <#assign orders = ordersByTeam[teamId]>
            <#assign sumConsumesQuantity = 0>
            <#list orders as order><#assign sumConsumesQuantity += order.consumes[0].actualUsedQuantity></#list>
        ${sumConsumesQuantity?string[",##0.##"]} кг
        </td>
        <td>
            <#assign orders = ordersByTeam[teamId]>
            <#assign sumPriceConsumes = 0>
            <#list orders as order><#assign sumPriceConsumes += order.consumes[0].product.price * order.consumes[0].actualUsedQuantity></#list>
        ${sumPriceConsumes?string[",##0.##"]} грн
        </td>
        <td>
            <#assign sumSalary = 0>
            <#if usersByTeamId[teamId]??>
                <#list usersByTeamId[teamId] as user>
                    <#if user.role == 'ROLE_PAINTER'>
                        <#assign sumSalary += options['SALARY_PAINTER']?eval>
                    </#if>
                    <#if user.role == 'ROLE_LABORER'>
                        <#assign sumSalary += options['SALARY_LABORER']?eval>
                    </#if>
                </#list>
            </#if>
        ${sumSalary?string[",##0.##"]} грн
        </td>
        <td>
            <#assign sumBonus = 0>
            <#if usersByTeamId[teamId]??>
                <#list usersByTeamId[teamId] as user>
                    <#if user.role == 'ROLE_PAINTER'>
                        <#assign sumBonus += options['BONUS_PAINTER']?eval * areaByTeamId[teamId]>
                    </#if>
                    <#if user.role == 'ROLE_LABORER'>
                        <#assign sumBonus += options['BONUS_LABORER']?eval * areaByTeamId[teamId]>
                    </#if>
                </#list>
            </#if>
        ${sumBonus?string[",##0.##"]}
            грн
        </td>
    </tr>
</#list>
</table>

<h3>По сотрудникам</h3>
<table class="responsive-table">
    <tr>
        <thead>
        <th>Сотрудник</th>
        <th>Площадь</th>
        <th>Зарплата</th>
        <th>Премия</th>
        <th>К выплате</th>
        </thead>
    </tr>
<#list users?keys as userId>
    <tr>
        <td><a href="/reports/month/${year?c}-${monthNum?c}/user/${userId}">${users[userId].name}</a></td>
        <td>${areaByUserId[userId]?string[",##0.##"]} м<sup>2</sup></td>
        <td>
            <#if users[userId].role == 'ROLE_PAINTER'>
            ${options['SALARY_PAINTER']}
            </#if>
            <#if users[userId].role == 'ROLE_LABORER'>
            ${options['SALARY_LABORER']}
            </#if>
            грн
        </td>
        <td>
            <#if users[userId].role == 'ROLE_PAINTER'>
            ${(options['BONUS_PAINTER']?eval * areaByUserId[userId])?string[",##0.##"]}
            </#if>
            <#if users[userId].role == 'ROLE_LABORER'>
            ${(options['BONUS_LABORER']?eval * areaByUserId[userId])?string[",##0.##"]}
            </#if>
            грн
        </td>
        <td>
            <#if users[userId].role == 'ROLE_PAINTER'>
            ${(options['BONUS_PAINTER']?eval * areaByUserId[userId] + options['SALARY_PAINTER']?eval)?string[",##0.##"]}
            </#if>
            <#if users[userId].role == 'ROLE_LABORER'>
        ${(options['BONUS_LABORER']?eval * areaByUserId[userId] + options['SALARY_LABORER']?eval)?string[",##0.##"]}
        </#if>
        </td>
    </tr>
</#list>
</table>

<#include "../footer.ftl">