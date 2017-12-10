<#include "../header.ftl">
<script type="application/javascript">
    $(function () {
        $("#team-form").validate({
            rules: {
                name: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "Поле 'Название' должно быть заполнено."
                }
            },
            submitHandler: function (form) {
                form.submit();
            }
        });
    });
</script>
<#if team??>
<h1>Редактирование бригады</h1>
<#else>
<h1>Новая бригада</h1>
</#if>

<form id="team-form" method="post">
    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}"/>

    <table class="responsive-table" style="width:30em">
    <#if team??>
        <tr>
            <th>ID</th>
            <td>
            ${(team.id)!}
            </td>
        </tr>
    </#if>
        <tr>
            <th>Название</th>
            <td>
            <input name="name" value="${(team.name)!}"/>
            </td>
        </tr>
        <tr>
            <th>Цвет</th>
            <td>
                <select name="color">
                <#assign colors = ['6495ED', 'B0171F', '32CD32', 'EE7600', '388E8E']>
                    <#list colors as color>
                        <option value="${color}" style="color: #${color}" <#if team?? && team.color! == '${color}'>selected</#if>>
                            <span >&#9632;</span> #${color}</option>
                    </#list>
                </select>
            </td>
        </tr>
    </table>
    <#if team??>
    <div class="btn btn-danger" data-toggle="modal"
         data-target="#modal-delete-confirmation">Удалить бригаду</div>
    </#if>
    <input type="submit" value="Отправить" class="btn btn-info"/>
</form>

<div id="modal-delete-confirmation" class="modal fade" role="dialog">
    <div class="modal-dialog" style="width: 30em">
        <div class="modal-content">
            <div class="modal-header"><strong>Удалить бригаду?</strong></div>
            <div class="modal-body">
                <div class="alert alert-warning"><strong>
                    Все работники останутся без бригады!
                </strong></div>
                <a class="btn btn-success" href="/teams/${team.id?c}/delete">Удалить бригаду</a>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Нееееет!</button>
            </div>
        </div>
    </div>
</div>
<#include "../footer.ftl">