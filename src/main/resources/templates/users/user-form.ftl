<#include "../header.ftl">
<script type="application/javascript">
    $(function () {
        $("#user-form").validate({
            rules: {
                name: {
                    required: true
                },
                email: {
                    required: true
                },
                givenName: {
                    required: true
                },
                familyName: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "Поле 'Полное имя' должно быть заполнено."
                },
                email: {
                    required: "Поле 'Email' должно быть заполнено."
                },
                givenName: {
                    required: "Поле 'Имя' должно быть заполнено."
                },
                familyName: {
                    required: "Поле 'Фамилия' должно быть заполнено."
                }
            },
            submitHandler: function (form) {
                form.submit();
            }
        });
    });
</script>
<#if targetUser??>
<h1>Редактирование данных пользователя</h1>
<#else>
<h1>Добавление пользователя</h1>
</#if>

<form id="user-form" method="post">
    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}"/>

    <table class="responsive-table" style="width:30em">
    <#if targetUser??>
        <tr>
            <th>ID</th>
            <td>
            ${(targetUser.id)!}
            </td>
        </tr>
    </#if>
        <tr>
            <th>Полное имя</th>
            <td>
                <input name="name" value="${(targetUser.name)!}"/>
            </td>
        </tr>
        <tr>
            <th>Email</th>
            <td>
            <#if (targetUser.role)! == 'ROLE_ADMIN'>
            ${(targetUser.email)!}
                <input type="hidden" name="email" value="${(targetUser.email)!}"/>
            <#else>
                <input name="email" value="${(targetUser.email)!}"/>
            </#if>
            </td>
        </tr>
        <tr>
            <th>Имя</th>
            <td>
                <input name="givenName" value="${(targetUser.givenName)!}"/>
            </td>
        </tr>
        <tr>
            <th>Фамилия</th>
            <td>
                <input name="familyName" value="${(targetUser.familyName)!}"/>
            </td>
        </tr>
        <tr>
            <th>Роль</th>
            <td>
            <#if (targetUser.role)! == 'ROLE_ADMIN'>
                Администратор
                <input type="hidden" name="role" value="${(targetUser.role)!}"/>
            <#else>
                <select name="role">
                    <option value="">Не назначена</option>
                    <option value="ROLE_OPERATOR"
                            <#if (targetUser.role)! == 'ROLE_OPERATOR'>selected</#if>>
                        Оператор
                    </option>
                    <option value="ROLE_PAINTER"
                            <#if (targetUser.role)! == 'ROLE_PAINTER'>selected</#if>>
                        Маляр
                    </option>
                    <option value="ROLE_SALES"
                            <#if (targetUser.role)! == 'ROLE_SALES'>selected</#if>>
                        Менеджер
                    </option>
                    <option value="ROLE_LABORER"
                            <#if !targetUser?? || (targetUser.role)! == 'ROLE_LABORER'>selected</#if>>
                        Рабочий
                    </option>
                </select>
            </#if>
            </td>
        </tr>
        <tr>
            <th>Бригада</th>
            <td>
                <select name="teamId">
                    <option value="">Не назначена</option>
                <#list teams as team>
                    <option value="${team.id?c}"
                            <#if targetUser?? && targetUser.team?? && targetUser.team.id! == team.id>selected</#if>>${team.name}</option>
                </#list>
                </select>
            </td>
        </tr>
    </table>
<#if targetUser?? && (targetUser.role)! != 'ROLE_ADMIN'>
    <div class="btn btn-danger" data-toggle="modal"
         data-target="#modal-delete-confirmation">Удалить пользователя
    </div>
</#if>
    <input type="submit" value="Отправить" class="btn btn-info"/>
</form>

<div id="modal-delete-confirmation" class="modal fade" role="dialog">
    <div class="modal-dialog" style="width: 30em">
        <div class="modal-content">
            <div class="modal-header"><strong>Удалить пользователя?</strong></div>
            <div class="modal-body">
                <a class="btn btn-success" href="/users/${(targetUser.id?c)!}/delete">Удалить пользователя</a>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Нееееет!</button>
            </div>
        </div>
    </div>
</div>
<#include "../footer.ftl">