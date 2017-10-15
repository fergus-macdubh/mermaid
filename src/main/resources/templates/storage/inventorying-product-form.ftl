<#include "../header.ftl">
<h1>Закупка товара</h1>

<form method="post">
    <table class="responsive-table" style="width: 20em">
        <tr>
            <th>Товар</th>
            <td>${product.name}</td>
        </tr>
        <tr>
            <th>Количество</th>
            <td><input name="quantity" value="${product.quantity}" /></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Отправить"/>
            </td>
        </tr>
    </table>
    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}"/>
</form>
<#include "../footer.ftl">