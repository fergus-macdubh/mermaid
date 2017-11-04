<#include "../header.ftl">
<h1>Бригады</h1>

<table class="responsive-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Состав</th>
        <th></th>
    </tr>
    </thead>
<#list teams as team>
    <tr>
        <td>${team.id}</a></td>
        <td>${team.name}</a></td>
        <td>
            |<#list users[team.id?string] as user>
        ${user.name}
            |</#list>
        </td>
        <td><a class="btn btn-info" href="/teams/${team.id?c}/edit">Редактировать</a></td>
    </tr>
</#list>
</table>
<#include "../footer.ftl">