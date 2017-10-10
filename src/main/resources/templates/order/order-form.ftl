<#include "../header.ftl">
<#if order??>
<h1>Новый заказ</h1>
<#else>
<h1>Изменение заказа</h1>
</#if>

<form method="post">
    <table class="responsive-table" style="width: 20em">
    <#if order??>
        <tr>
            <th>ID</th>
            <td>
                ${(order.id)!}
            </td>
        </tr>
        <tr>
            <th>Статус</th>
            <td>
                ${(order.status)!}
            </td>
        </tr>
    </#if>
        <tr>
            <th>Площадь</th>
            <td>
                <input name="area" value="${(order.area?c)!}"/>
            </td>
        </tr>
        <tr>
            <th>Клиент</th>
            <td>
                <input name="client" value="${(order.client)!}"/>
            </td>
        </tr>
        <tr>
            <th>Цена</th>
            <td>
                <input name="price" value="${(order.price?c)!}"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Отправить"/>
            </td>
        </tr>
    </table>
</form>
<#include "../footer.ftl">