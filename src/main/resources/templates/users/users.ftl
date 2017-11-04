<#include "../header.ftl">
<h1>Пользователи</h1>

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
            <#if user.role! == 'ROLE_ADMIN'>
                Администратор
            <#else>
                <form method="post" action="/users/role">
                    <input type="hidden" value="${user.id}" name="userId"/>
                    <input type="hidden"
                           name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                    <select name="role">
                        <option value="">Не назначена</option>
                        <option value="ROLE_OPERATOR"
                                <#if user.role! == 'ROLE_OPERATOR'>selected</#if>>
                            Оператор
                        </option>
                        <option value="ROLE_PAINTER"
                                <#if user.role! == 'ROLE_PAINTER'>selected</#if>>
                            Маляр
                        </option>
                        <option value="ROLE_SALES"
                                <#if user.role! == 'ROLE_SALES'>selected</#if>>
                            Менеджер
                        </option>
                        <option value="ROLE_LABORER"
                                <#if user.role! == 'ROLE_LABORER'>selected</#if>>
                            Рабочий
                        </option>
                    </select>
                    <button class="btn btn-info">Сохранить</button>
                </form>
            </#if>
        </td>
        <td>
            <form method="post" action="/users/team">
                <input type="hidden" value="${user.id}" name="userId"/>
                <input type="hidden"
                       name="${_csrf.parameterName}"
                       value="${_csrf.token}"/>
                <select name="teamId">
                    <option value="">Не назначена</option>
                        <#list teams as team>
                            <option value="${team.id}"
                                    <#if user.team?? && user.team.id! == team.id>selected</#if>>${team.name}</option>
                        </#list>
                </select>
                <button class="btn btn-info">Сохранить</button>
            </form>
        </td>
    </tr>
</#list>
</table>
<#include "../footer.ftl">