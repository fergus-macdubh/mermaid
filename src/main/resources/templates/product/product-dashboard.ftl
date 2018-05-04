<#include "../header.ftl">
<h1>Товары</h1>
<a href="/product/add" class="btn btn-success">Новый товар</a>

<table class="responsive-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Наименование</th>
        <th>Производитель</th>
        <th>Поставщик</th>
        <th>Единицы измерения</th>
        <#if currentUser.role == "ROLE_ADMIN">
            <th>Удалить</th>
        </#if>
    </tr>
    </thead>
<#list products as product>
    <tr>
        <td>${product.id}</td>
        <td><a href="/product/${product.id?c}/edit">${product.name}</a></td>
        <td>${product.producer!}</td>
        <td>${product.supplier!}</td>
        <td>${product.unit.abbr}</td>
        <#if currentUser.role == "ROLE_ADMIN">
            <td>
                <form action="/product/${product.id?c}/delete" method="post">
                    <input type="hidden"
                           name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                    <input type="submit" value="Удалить"
                           <#if product.quantity?string[",##0.00"] == "0,00">class = "btn btn-info"
                           <#else>class = "btn btn-info disabled" disabled="disabled"
                           </#if>/>
                </form>
            </td>
        </#if>
    </tr>
</#list>
</table>
<#include "../footer.ftl">