<#include "../header.ftl">
<script type="application/javascript">
    $(function () {
        $.datepicker.setDefaults({
            regional: "ru",
            dateFormat: "dd.mm.y"
        });
        $("#datepickerStart").datepicker();
        $("#datepickerEnd").datepicker();
    });
</script>
<#if paint??>
<h1>Отчет по краске <b>${paint.name} ${paint.producer}</b> за период с <b>${start.format('dd MMM yy')}</b> по
    <b>${end.format('dd MMM yy')}</b></h1>
<#else>
<h1>Отчет по краске за период</h1>
</#if>
<form method="get">
    <label for="productSelect">Краска:</label>
    <select id="productSelect" name="productId">
        <#list products as product>
            <option value="${product.id?c}"
                    <#if paint?? && paint.id == product.id>selected</#if>>${product.name} ${product.producer}</option>
        </#list>
    </select>
    &nbsp; &nbsp; &nbsp;
    <label for="datepickerStart">Начало:</label>
    <input id="datepickerStart" name="start" type="text" value="<#if start??>${start.format('dd.MM.yy')}</#if>">
    &nbsp; &nbsp; &nbsp;
    <label for="datepickerEnd">Конец:</label>
    <input id="datepickerEnd" name="end" type="text" value="<#if end??>${end.format('dd.MM.yy')}</#if>">

    <input type="submit" value="Отправить" class="btn btn-info"/>
</form>

<#if paint??>
    <#if orders?size == 0>
    В указанный период краска <b>${paint.name} ${paint.producer}</b> не использовалась.
    <#else>
<h3>За период: <b>${sumArea} м<sup>2</sup></b>, <b>${sumClips} кляймеров</b>,
    <b>${sumFurnitureBig} фурнитуры крупной</b>, <b>${sumFurnitureSmall} фурнитуры мелкой</b></h3>


<table class="responsive-table">
    <tr>
        <thead>
        <th>ID</th>
        <th>Создан</th>
        <th>Документ</th>
        <th>Менеджер</th>
        <th>Площадь</th>
        <th>Кляймеры</th>
        <th>Крупная фурнитура</th>
        <th>Мелкая фурнитура</th>
        </thead>
    </tr>
<#list orders?sort_by('created') as order>
    <tr>
        <td>${order.id}</td>
        <td>${order.created.format('dd MMM HH:mm')}</td>
        <td>${order.document}</td>
        <td>${order.manager.name}</td>
        <td>${order.area} м<sup>2</sup></td>
        <td>${order.clipCount}</td>
        <td>${order.furnitureBigCount}</td>
        <td>${order.furnitureSmallCount}</td>
    </tr>
</#list>
</table>
    </#if>
</#if>
<#include "../footer.ftl">