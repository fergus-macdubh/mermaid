<#include "../header.ftl">
<h1>Настройки ${month} ${year?c} года</h1>
<form id="month-report-options-form" method="post">
    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}"/>

    <table class="responsive-table" style="width:30em">
           <#list options as option>
               <tr>
                   <th style="min-width: 15em">${option.name.desc}<input type="hidden" name="optionNames" value="${option.name}"/></th>
                   <td><input type="number" step="0.01" placeholder="0.00" name="values" value="${option.value!}"></td>
                   <td style="min-width: 5em">${option.name.unit}</td>
               </tr>
           </#list>
    </table>
    <#if reportIsOpened>
        <input type="submit" value="Закрыть месяц" class="btn btn btn-success"/>
    </#if>
</form>
<#include "../footer.ftl">