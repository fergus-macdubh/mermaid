<#include "../header.ftl">
<h1>Доступ запрещен</h1>
<#if user.gender == 'female'>Госпожа<#else>Господин</#if> ${user.familyName}, к сожалению у Вас нет прав для просмотра этой страницы.<br/>
Вы можете запросить доступ у любого из администраторов:
<ul>
<#list admins as admin>
    <li>${admin.name} (${admin.email})</li>
</#list>
</ul>
<#include "../footer.ftl">