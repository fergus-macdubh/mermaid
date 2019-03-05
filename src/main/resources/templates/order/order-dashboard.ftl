<#include "../header.ftl">
<script type="application/javascript">
    $(document).ready(function () {
        $("#document-filter-input").on("keyup", function () {
            var value = $(this).val().toLowerCase();
            $("#order-table tr td.order-document").filter(function () {
                $(this).parent().toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });
    });
</script>
<h1>Заказы</h1>
<#if currentUser.role! == "ROLE_ADMIN"
|| currentUser.role! == "ROLE_OPERATOR">
    <a href="/storage/product/purchase" class="btn btn-success">Новый заказ</a>
</#if>

<#if isFullList>
    <a href="/order" class="btn btn-info">Текущий месяц</a>
<#else>
    <a href="/order/archive" class="btn btn-info">Архив</a>
</#if>

<span style="margin-left: 1em">Документ: <input id="document-filter-input"/></span>

<table id="order-table" class="responsive-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Статус</th>
        <th>Менеджер</th>
        <th>Площадь</th>
        <th>Документ</th>
        <th>Цена</th>
        <th>Бригада</th>
        <th>Готово</th>
        <th>Краска</th>
        <#if currentUser.role == "ROLE_ADMIN"
        || currentUser.role == "ROLE_OPERATOR"
        || currentUser.role == "ROLE_PAINTER">
            <th>Редактировать</th>
        </#if>
    </tr>
    </thead>
    <#list orders?sort_by('created')?reverse as order>
        <tr>
            <td>${order.id?c}</td>
            <td>${order.status}</td>
            <td>${order.client.manager.name}</td>
            <td>${order.area}</td>
            <td class="order-document">${order.document!}</td>
            <td>${order.price}</td>
            <td>${(order.team.name)!'-'}</td>
            <td>${(order.completed.format('dd MMM yyyy'))!'-'}</td>
            <td>
                <#if order.status == "SHIPPING" || order.status == "CLOSED">
                    <#list order.consumes as consume>
                        ${consume.product.name}: ${consume.actualUsedQuantity} кг<#sep><br/>
                    </#list>
                <#else>
                    <#list order.consumes as consume>
                        ${consume.product.name}: ~${consume.calculatedQuantity} кг<#sep><br/>
                    </#list>
                </#if>

            </td>
            <#if currentUser.role == "ROLE_ADMIN"
            || currentUser.role == "ROLE_OPERATOR"
            || currentUser.role == "ROLE_PAINTER">
                <td><a class="btn btn-info" href="/order/${order.id?c}/edit">Редактировать</a></td>
            </#if>
        </tr>
    </#list>
</table>
<#include "../footer.ftl">