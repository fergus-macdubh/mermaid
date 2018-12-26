<#include "../header.ftl">
<script type="application/javascript">

    $(function () {
        $("#client-form").validate({
            rules: {
                name: {
                    required: true
                },
                phone: {
                    required: true
                },
                email: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "Поле 'Имя клиента' должно быть заполнено."
                },
                phone: {
                    required: "Поле 'Телефон клиента' должно быть заполнено."
                },
                email: {
                    required: "Поле 'Email' должно быть заполнено.",
                    email: "Поле 'Email' должно соответствовать email формату."
                }
            },
            submitHandler: function (form) {
                form.submit();
            }
        });
    });

    $(document).ready(function() {
        if (${activeOrdersCount} != 0) {
            $("#deleteButton").addClass("disabled");
            $("#deleteButton").prop("disabled", true)
        }
    });

</script>
<#if targetClient??>
<h1>Редактирование данных клиента</h1>
<#else>
<h1>Добавление клиента</h1>
</#if>

<form id="client-form" method="post">
    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}"/>

    <table class="responsive-table" style="width:30em">
    <#if targetClient??>
        <tr>
            <th>ID</th>
            <td>
            ${(targetClient.id)!}
            </td>
        </tr>
    </#if>
        <tr>
            <th>Имя клиента</th>
            <td>
                <input name="name" value="${(targetClient.name)!}"/>
            </td>
        </tr>
        <tr>
            <th>Телефон клиента</th>
            <td>
                <input name="phone" value="${(targetClient.phone)!}"/>
            </td>
        </tr>
        <tr>
            <th>Контакт клиента</th>
            <td>
                <textarea name="contact" rows="4">${(targetClient.contact)!}</textarea>
            </td>
        </tr>
        <tr>
            <th>Email</th>
            <td>
                <input type="email" name="email" value="${(targetClient.email)!}"/>
            </td>
        </tr>
        <tr>
            <th>Менеджер</th>
            <td>
                <select name="managerId">
                <#list managers as manager>
                    <option value="${manager.id?c}"
                            <#if targetClient?? && targetClient.manager.id! == manager.id>selected</#if>>${manager.name}</option>
                </#list>
                </select>
            </td>
        </tr>
    </table>
    <#if targetClient??>
        <div id="deleteButton" class="btn btn-danger" data-toggle="modal"
             data-target="#modal-delete-confirmation">Удалить клиента
        </div>
    </#if>
    <input type="submit" value="Отправить" class="btn btn-info"/>
</form>

<div id="modal-delete-confirmation" class="modal fade" role="dialog">
    <div class="modal-dialog" style="width: 30em">
        <div class="modal-content">
            <div class="modal-header"><strong>Удалить клиента?</strong></div>
            <div class="modal-body">
                <a class="btn btn-success" href="/clients/${(targetClient.id?c)!}/delete">Удалить клиента</a>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Нееееет!</button>
            </div>
        </div>
    </div>
</div>
<#include "../footer.ftl">