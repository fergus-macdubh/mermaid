<#include "../header.ftl">
<script type="application/javascript">
    $(document).ready(function () {
        $("#document-filter-input").on("keyup", function () {
            var value = $(this).val().toLowerCase();
            $("#order-table tr td.order-document").filter(function () {
                $(this).parent().toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });

        $("#manager-filter-input").on("keyup", function () {
            var value = $(this).val().toLowerCase();
            $("#order-table tr td.order-manager").filter(function () {
                $(this).parent().toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });

        $("#client-filter-input").on("keyup", function () {
            var value = $(this).val().toLowerCase();
            $("#order-table tr td.order-client").filter(function () {
                $(this).parent().toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });

        $("#paint-filter-input").on("keyup", function () {
            var value = $(this).val().toLowerCase();
            $("#order-table tr td.order-paint").filter(function () {
                $(this).parent().toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });
    });
</script>
<h1>Заказы</h1>
<#if currentUser.role! == "ROLE_ADMIN"
|| currentUser.role! == "ROLE_OPERATOR">
    <a href="/order/add" class="btn btn-success">Новый заказ</a>
</#if>

<#if isFullList>
    <a href="/order" class="btn btn-info">Текущий месяц</a>
<#else>
    <a href="/order/archive" class="btn btn-info">Архив</a>
</#if>

<span style="margin-left: 1em">Менеджер: <input id="manager-filter-input"/></span>
<span style="margin-left: 1em">Документ: <input id="document-filter-input"/></span>
<#if currentUser.role == "ROLE_ADMIN"|| currentUser.role == "ROLE_OPERATOR">
    <span style="margin-left: 1em">Краска: <input id="paint-filter-input"/></span>
    <span style="margin-left: 1em">Клиент: <input id="client-filter-input"/></span>
</#if>

<table id="order-table" class="responsive-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Статус</th>
        <th>Готово</th>
        <th>Дни</th>
        <th>Менеджер</th>
        <th>Площадь</th>
        <th>Документ</th>
        <th>Цена</th>
        <#if currentUser.role == "ROLE_ADMIN"
        || currentUser.role == "ROLE_OPERATOR">
            <th>Бригада</th>
            <th>Краска</th>
            <th>Клиент</th>
            <th></th>
        </#if>
    </tr>
    </thead>
    <#list orders?sort_by('created')?reverse as order>
        <tr>
            <td>${order.id?c}</td>
            <td>${order.status.title}</td>
            <td>${(order.completed.format('dd MMM yyyy'))!'-'}</td>
            <td>${((order.completed.format('dd MMM yyyy')?date('dd MMM yyyy')?long - order.created.format('dd MMM yyyy')?date('dd MMM yyyy')?long)/(1000*60*60*24))!'-'}</td>
            <td class="order-manager">${order.client.manager.name}</td>
            <td>${order.area
                + order.clipCount * options['CLIP_TO_AREA']?eval
                + order.furnitureSmallCount * options['FURNITURE_SMALL_TO_AREA']?eval
                + order.furnitureBigCount * options['FURNITURE_BIG_TO_AREA']?eval}</td>
            <td class="order-document">${order.document!}</td>
            <td>${order.price}</td>
            <#if currentUser.role == "ROLE_ADMIN"
            || currentUser.role == "ROLE_OPERATOR">
                <td>${(order.team.name)!'-'}</td>
                <td class="order-paint">
                    <#if order.status == "SHIPPING" || order.status == "CLOSED">
                        <#list order.consumes as consume>
                            ${consume.product.producer} ${consume.product.name}: ${consume.actualUsedQuantity} кг<#sep>
                            <br/>
                        </#list>
                    <#else>
                        <#list order.consumes as consume>
                            ${consume.product.producer} ${consume.product.name}: ~${consume.calculatedQuantity} кг<#sep>
                            <br/>
                        </#list>
                    </#if>
                </td>
                <td class="order-client">${order.client.name}</td>
                <td><a href="/order/${order.id?c}/edit"><img src="/img/edit.png"/></a></td>
            </#if>
        </tr>
    </#list>
</table>
<#include "../footer.ftl">