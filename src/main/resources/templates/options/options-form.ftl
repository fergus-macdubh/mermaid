<#include "../header.ftl">
<h1>Настройки</h1>

<form id="options-form" method="post">
    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}"/>

    <table class="responsive-table" style="width:30em">
       <#list options as option>
           <tr>
               <th>${option.name.desc}<input type="hidden" name="optionNames" value="${option.name}"/></th>
               <td><input name="values" value="${option.value!}"></td>
               <td>${option.name.unit}</td>
           </tr>
       </#list>
    </table>
    <input type="submit" value="Отправить" class="btn btn-info"/>
</form>
<#include "../footer.ftl">