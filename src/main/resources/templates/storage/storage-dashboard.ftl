<#include "../header.ftl">
<script type="application/javascript">
    $(document).ready(function(){
        $("#name-filter-input").on("keyup", function() {
            var value = $(this).val().toLowerCase();
            $("#products-table tr td.product-name").filter(function() {
                $(this).parent().toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });
    });
</script>
<div class="table-info">
    <h1 class="storage-title">Склад</h1>
    <h4 class="storage-price">Товара на сумму ${storagePrice?string[",##0.00"]} грн</h4>
</div>

<#if currentUser.role! == "ROLE_ADMIN"
|| currentUser.role! == "ROLE_OPERATOR">
    <a href="/storage/product/purchase" class="btn btn-success">Пополнить склад</a>
</#if>

<span style="margin-left: 1em">Наименование продукта: <input id="name-filter-input"/></span>

<table id="products-table" class="responsive-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Наименование</th>
        <th>Цена последней закупки</th>
        <th>Количество</th>
        <th>Стоимость</th>
    <#if currentUser.role == "ROLE_ADMIN">
        <th>Инвентаризация</th>
    </#if>
    </tr>
    </thead>
<#list products as product>
    <tr>
        <td>${product.id}</td>
        <td class="product-name"><a href="/storage/product/${product.id?c}/action">${product.name} ${product.producer}</a></td>
        <td>${product.price} грн / ${product.unit.abbr}</td>
        <td>${product.quantity} ${product.unit.abbr}</td>
        <td>${(product.quantity * product.price)?string[",##0.00"]} грн</td>
        <#if currentUser.role == "ROLE_ADMIN">
            <td>
                <a href="/storage/product/${product.id?c}/inventorying" class="btn btn-info">Инвентаризация</a>
            </td>
        </#if>
    </tr>
</#list>
</table>
<#include "../footer.ftl">