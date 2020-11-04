<#include "../header.ftl">
<#assign year = yearMonths?last.format('yyyy')>
<h3>${year}</h3>
    <div>
        <#list yearMonths?reverse as yearMonth>
            <#if year != yearMonth.format('yyyy')>
                <#assign year = yearMonth.format('yyyy')>
                <h3 style="clear: both">${year}</h3>
            </#if>
            <a href="/order/archive/${yearMonth.format('yyyy-MM')}">${yearMonth.format('LLLL')}</a></br>
        </#list>
    </div>
<#include "../footer.ftl">