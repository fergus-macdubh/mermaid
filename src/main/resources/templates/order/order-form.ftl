<#include "../header.ftl">
<script type="application/javascript">
    var consumesCount = <#if order??>${order.consumes?size + 1}<#else>1</#if>;

    function addRow() {
        $('#consumes-table')
                .append('<tr class="consume-form-row" id="consumes-row' + consumesCount + '"><td><select name="productIds"><#list products as product><option value="${product.id?c}">${product.name} ${product.producer}</option></#list>    </select>    </td>    <td>    <input name="quantities"/>                </td>                <td>                <div class="btn btn-warning" onclick="removeConsumesRow(' + consumesCount + ')">Удалить</div> </td> </tr>');
        consumesCount++;
    }

    function removeConsumesRow(i) {
        $('#consumes-row' + i).remove();
    }

    $(function () {
        $("#order-form").validate({
            rules: {
                area: {
                    required: true,
                    commaDotNumber: true
                },
                document: {
                    required: true
                },
                price: {
                    required: true,
                    commaDotNumber: true
                },
                quantities: {
                    required: true,
                    commaDotNumber: true
                }
            },
            messages: {
                area: {
                    required: "Поле 'Площадь' должно быть заполнено.",
                    commaDotNumber: "'Площадь' должна быть числом."
                },
                document: {
                    required: "Поле 'Документ' должно быть заполнено."
                },
                price: {
                    required: "Поле 'Цена' должно быть заполнено.",
                    commaDotNumber: "'Цена' должна быть числом."
                },
                quantities: {
                    required: "Поле 'Количество' должно быть заполнено.",
                    commaDotNumber: "'Количество' должно быть числом."
                }
            },
            submitHandler: function (form) {
                form.submit();
            }
        });
    });

    function calculatePriceAndConsumes() {
        var coeff = $('input[name=price-coeff]:checked').val();
        var area = $('#area-input').val();

        $('input[name=quantities]').val((area * 0.2).toFixed(2));

        if (coeff != 0) {
            $('#price-input').val((coeff * area).toFixed(2));
        }
    }
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

    <table class="responsive-table" style="width:30em">
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
    <#else>
        <tr>
            <th>Авторасчет цены</th>
            <td>
                <div id="priceRadio" class="btn-group" data-toggle="buttons" onchange="calculatePriceAndConsumes()">
                    <label class="btn btn-primary active">
                        <input type="radio" name="price-coeff" checked value="0"> X
                    </label>
                    <label class="btn btn-primary">
                        <input type="radio" name="price-coeff" value="120"> 120
                    </label>
                    <label class="btn btn-primary">
                        <input type="radio" name="price-coeff" value="150"> 150
                    </label>
                    <label class="btn btn-primary">
                        <input type="radio" name="price-coeff" value="180"> 180
                    </label>
                </div>
            </td>
        </tr>
    </#if>
        <tr>
            <th>Площадь</th>
            <td>
                <input id="area-input" name="area" value="${(order.area?c)!}" onchange="calculatePriceAndConsumes()"/>
            </td>
        </tr>
        <tr>
            <th>Документ</th>
            <td>
                <input name="document" value="${(order.document)!}"/>
            </td>
        </tr>
        <tr>
            <th>Цена</th>
            <td>
                <input id="price-input" name="price" value="${(order.price?c)!}" onblur="replaceComma(event.target)"/>
            </td>
        </tr>
        <tr>
            <th>Менеджер</th>
            <td>
                <select name="managerId">
                <#list managers as manager>
                    <option value="${manager.id}"
                            <#if order?? && order.manager.id! == manager.id>selected</#if>>${manager.name}</option>
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
                                    <#if consume.product.id == product.id>selected</#if>>${product.name} ${product.producer}</option>
                        </#list>
                    </select>
                </td>
                <td>
                    <input name="quantities" value="${consume.calculatedQuantity?c}"
                           onblur="replaceComma(event.target)"/>
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
                        <option value="${product.id?c}">${product.name} ${product.producer}</option>
                    </#list>
                </select>
            </td>
            <td>
                <input name="quantities" onblur="replaceComma(event.target)"/>
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