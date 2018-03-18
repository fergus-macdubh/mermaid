<#include "../header.ftl">
<h1>Бригады</h1>

<a href="/teams/add" class="btn btn-success">Создать бригаду</a>

<table class="responsive-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Цвет</th>
        <th>Состав</th>
        <th></th>
    </tr>
    </thead>
<#list teams as team>
    <tr>
        <td>${team.id}</td>
        <td>${team.name}</td>
        <td><span style="background-color: #${team.color!}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> #${team.color!}</td>
        <td>
            <#if users[team.id?string("0")]??>
                |<#list users[team.id?string("0")] as user>
                    ${user.name}
                |</#list>
            </#if>
        </td>
        <td><a class="btn btn-info" href="/teams/${team.id?c}/edit">Редактировать</a></td>
    </tr>
</#list>
</table>
<#include "../footer.ftl">