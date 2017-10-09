<#include "header.ftl">
<h1>Закупка товара</h1>

<form method="post" action="/storage/item/purchase">
    <table class="responsive-table" style="width: 20em">
        <tr>
            <th>Товар</th>
            <td>
                <select name="itemId">
                <#list items as item>
                    <option value="${item.id?c}">${item.name}</option>
                </#list>
                </select>
            </td>
        </tr>
        <tr>
            <th>Цена</th>
            <td>
                <input name="price" />
            </td>
        </tr>
        <tr>
            <th>Количество</th>
            <td>
                <input name="quantity" />
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Отправить"/>
            </td>
        </tr>
    </table>
</form>
<#include "footer.ftl">