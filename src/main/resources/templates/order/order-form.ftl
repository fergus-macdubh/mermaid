<#include "../header.ftl">
<script type="application/javascript">
    <#if order??>
    var consumesCount = ${order.consumes?size + 1};
    <#else>
    var consumesCount = 1;
    </#if>
    function addRow() {
        $('#consumes-table')
                .append('<tr class="consume-form-row" id="consumes-row' + consumesCount + '"><td><select name="productIds"><#list products as product><option value="${product.id?c}">${product.name}</option></#list>    </select>    </td>    <td>    <input name="quantities"/>                </td>                <td>                <div class="btn btn-warning" onclick="removeConsumesRow(' + consumesCount + ')">Удалить</div> </td> </tr>');
        consumesCount++;
    }

    function removeConsumesRow(i) {
        $('#consumes-row' + i).remove();
    }

    $(function() {
        $("#order-form").validate({
            rules: {
                area: {
                    required: true,
                    number: true,
                    min: 1},
                client: {
                    required: true
                },
                price: {
                    required: true,
                    number: true,
                    min: 1},
                quantities: {
                    required: true,
                    number: true,
                    min: 0
                }
            },
            messages: {
                area: {
                    required: "Поле 'Площадь' должно быть заполнено.",
                    min: "Площадь не может быть отрицательной.",
                    number: "Поле 'Площадь' может содержать только цифры."
                },
                client: {
                    required: "Поле 'Клиент' должно быть заполнено."
                },
                price: {
                    required: "Поле 'Цена' должно быть заполнено.",
                    min: "Цена не может быть отрицательной.",
                    number: "Поле 'Цена' может содержать только цифры."
                },
                quantities: {
                    required: "Поле 'Количество' должно быть заполнено.",
                    min: "Количество не может быть отрицательным.",
                    number: "Поле 'Количество' может содержать только цифры."
                }
            },
            submitHandler: function(form) {
                form.submit();
            }
        });
    });
</script>
<#if order??>
<h1>Изменение заказа</h1>
<#else>
<h1>Новый заказ</h1>
</#if>

<form id="order-form" method="post">
    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}"/>
    <table class="responsive-table" style="width:20em">
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
            <th>Менеджер</th>
            <td>
                <select name="managerId">
                    <#list managers as manager>
                        <option value="${manager.id}" <#if order?? && order.manager.id! == manager.id>selected</#if>>${manager.name}</option>
                    </#list>
                </select>
            </td>
        </tr>
    </table>

    <table id="consumes-table" class="responsive-table" style="width: 20em">
        <thead>
        <th>Расходный материал</th>
        <th>Количество</th>
        <th></th>
        </thead>
    <#if order?? >
        <#list order.consumes as consume>
            <tr class="consume-form-row" id="consumes-row${consume?index}">
                <td>
                    <select name="productIds">
                        <#list products as product>
                            <option value="${product.id?c}"
                                    <#if consume.product.id == product.id>selected</#if>>${product.name}</option>
                        </#list>
                    </select>
                </td>
                <td>
                    <input name="quantities" value="${consume.calculatedQuantity?c}"/>
                </td>
                <td>
                    <#if consume?index == 0>
                        <div class="btn" style="opacity: 0">Удалить</div>
                    <#else>
                        <div class="btn btn-warning" onclick="removeConsumesRow(${consume?index})">Удалить</div>
                    </#if>

                </td>
            </tr>
        </#list>
    <#else>
        <tr class="consume-form-row">
            <td>
                <select name="productIds">
                    <#list products as product>
                        <option value="${product.id?c}">${product.name}</option>
                    </#list>
                </select>
            </td>
            <td>
                <input name="quantities"/>
            </td>
            <td>
                <div class="btn" style="opacity: 0">Удалить</div>
            </td>
        </tr>
    </#if>
    </table>

    <div class="btn btn-default" onclick="addRow()">Добавить расходный материал</div>

    <input type="submit" value="Отправить" class="btn btn-info"/>
</form>
<#include "../footer.ftl">