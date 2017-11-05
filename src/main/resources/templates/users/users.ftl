<#include "../header.ftl">
<h1>Пользователи</h1>
<a href="/users/add" class="btn btn-success">Добавить пользователя</a>
<table class="responsive-table">
    <thead>
    <tr>
        <th></th>
        <th>Email</th>
        <th>Полное имя</th>
        <th>Имя</th>
        <th>Фамилия</th>
        <th>Роль</th>
        <th>Бригада</th>
        <th></th>
    </tr>
    </thead>
<#list users as user>
    <tr>
        <td><img src="${user.picture!}" width="40"/></td>
        <td>${user.email!}</a></td>
        <td>${user.name!}</a></td>
        <td>${user.givenName!}</td>
        <td>${user.familyName!}</td>
        <td>
            <#if (user.role)! == 'ROLE_OPERATOR'>
                Оператор
            <#elseif (user.role)! == 'ROLE_PAINTER'>
                Маляр
            <#elseif (user.role)! == 'ROLE_SALES'>
                Менеджер
            <#elseif (user.role)! == 'ROLE_LABORER'>
                Рабочий
            <#elseif (user.role)! == 'ROLE_ADMIN'>
                Админстратор
            <#else>
                Не назначена
            </#if>
        </td>
        <td>
            <#if user.team??>
            ${user.team.name}
            <#else>
                Не назначена
            </#if>
        </td>
        <td>
            <a href="/users/${user.id}/edit" class="btn btn-info">Редактировать</a>
        </td>
    </tr>
</#list>
</table>
<#include "../footer.ftl">