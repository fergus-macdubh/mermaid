<#include "../header.ftl">
<script type="application/javascript">
    $(document).ready(function(){
        $("#client-filter-input").on("keyup", function() {
            var value = $(this).val().toLowerCase();
            $("#client-table tr td.client-name").filter(function() {
                $(this).parent().toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });
    });
</script>
<h1>Клиенты</h1>
<a href="/clients/add" class="btn btn-success">Добавить клиента</a>
<span style="margin-left: 1em">Имя клиента: <input id="client-filter-input"/></span>
<table id="client-table" class="responsive-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Имя</th>
        <th>Email</th>
        <th>Менеджер</th>
        <th></th>
    </tr>
    </thead>
<#list clients as client>
    <tr>
        <td>${client.id}</td>
        <td  class="client-name">${client.name!}</a></td>
        <td>${client.email!}</a></td>
        <td>${client.manager.name!}</a></td>
        <td>
            <a href="/clients/${client.id?c}/edit" class="btn btn-info">Редактировать</a>
        </td>
    </tr>
</#list>
</table>
<#include "../footer.ftl">