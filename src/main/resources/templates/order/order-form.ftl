<#include "../header.ftl">
<script type="application/javascript">
    function addRow() {
        $('#consumes-table')
                .append($('#consumes-row-source').clone());
    }
</script>
<#if order??>
<h1>Изменение заказа</h1>
<#else>
<h1>Новый заказ</h1>
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
    </table>

    <table id="consumes-table" class="responsive-table">
        <thead>
        <th>Расходный материал</th>
        <th>Количество</th>
        <th></th>
        </thead>
        <tr class="consume-form-row">
            <td>
                <select name="orderConsume">
                <#list products as product>
                    <option value="${product.id?c}">${product.name}</option>
                </#list>
                </select>
            </td>
            <td>
                <input name="quantity"/>
            </td>
            <td>
                <button class="btn btn-warning">Удалить</button>
            </td>
        </tr>
    </table>

    <div class="btn btn-default" onclick="addRow()">Добавить расходный материал</div>

    <input type="submit" value="Отправить" class="btn btn-info"/>
</form>

<tr class="consume-form-row" id="consumes-row-source">
    <td>
        <select name="orderConsume">
        <#list products as product>
            <option value="${product.id?c}">${product.name}</option>
        </#list>
        </select>
    </td>
    <td>
        <input name="quantity"/>
    </td>
    <td>
        <button class="btn btn-warning">Удалить</button>
    </td>
</tr>
<#include "../footer.ftl">