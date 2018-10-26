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
        var coeff = parseInt($('input[name=price-coeff]:checked').val()) || 0;
        var area = parseFloat($('#area-input').val()) || 0;
        var clips = parseInt($('#clip-input').val()) || 0;
        var furnSmall = parseInt($('#furniture-small-input').val()) || 0;
        var furnBig = parseInt($('#furniture-big-input').val()) || 0;
        var sumArea = clips * ${options['CLIP_TO_AREA']} + furnSmall * ${options['FURNITURE_SMALL_TO_AREA']} + furnBig * ${options['FURNITURE_BIG_TO_AREA']} + area;

        $('#clipsTd').empty().append(clips * ${options['CLIP_PRICE']} +' грн / '
                + clips * ${options['CLIP_TO_AREA']} +' м<sup>2</sup>');
        $('#furnitureSmallTd').empty().append(furnSmall * ${options['FURNITURE_SMALL_PRICE']} +' грн / '
                + furnSmall * ${options['FURNITURE_SMALL_TO_AREA']} +' м<sup>2</sup>');
        $('#furnitureBigTd').empty().append(furnBig * ${options['FURNITURE_BIG_PRICE']} +' грн / '
                + furnBig * ${options['FURNITURE_BIG_TO_AREA']} +' м<sup>2</sup>');
        $('#areaSumTd').empty().append(sumArea + ' м<sup>2</sup>');

        $('input[name=quantities]').val((sumArea * ${options['PAINT_CONSUME']}).toFixed(2));

        if (coeff != 0) {
            var price = coeff * area;
            price += clips * ${options['CLIP_PRICE']};
            price += furnSmall * ${options['FURNITURE_SMALL_PRICE']};
            price += furnBig * ${options['FURNITURE_BIG_PRICE']};
            $('#price-input').val(price.toFixed(2));
        }
    }
    $(function () {
        $.datepicker.setDefaults({
            regional: "ru",
            dateFormat: "dd.mm.y",
            minDate: new Date()
        });
        $("#datepickerCompleteDate").datepicker();
        var complete = new Date();
        complete.setDate(complete.getDate() + 7);
        $("#datepickerCompleteDate").datepicker('setDate', complete);
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

    <table class="responsive-table" style="width:30em">
    <#if order??>
        <tr>
            <th>ID</th>
            <td>
            ${(order.id?c)!}
            </td>
            <td></td>
        </tr>
        <tr>
            <th>Статус</th>
            <td>
            ${(order.status)!}
            </td>
            <td></td>
        </tr>
    <#else>
        <tr>
            <th style="min-width: 15em">Авторасчет цены</th>
            <td style="min-width: 22em">
                <div id="priceRadio" class="btn-group" data-toggle="buttons" onchange="calculatePriceAndConsumes()">
                    <label class="btn btn-primary active">
                        <input type="radio" name="price-coeff" checked value="0"> X
                    </label>
                    <label class="btn btn-primary">
                        <input type="radio" name="price-coeff"
                               value="${options['AUTO_CALC_PRICE_1']}"> ${options['AUTO_CALC_PRICE_1']}
                    </label>
                    <label class="btn btn-primary">
                        <input type="radio" name="price-coeff"
                               value="${options['AUTO_CALC_PRICE_2']}"> ${options['AUTO_CALC_PRICE_2']}
                    </label>
                    <label class="btn btn-primary">
                        <input type="radio" name="price-coeff"
                               value="${options['AUTO_CALC_PRICE_3']}"> ${options['AUTO_CALC_PRICE_3']}
                    </label>
                    <label class="btn btn-primary">
                        <input type="radio" name="price-coeff"
                               value="${options['AUTO_CALC_PRICE_4']}"> ${options['AUTO_CALC_PRICE_4']}
                    </label>
                    <label class="btn btn-primary">
                        <input type="radio" name="price-coeff"
                               value="${options['AUTO_CALC_PRICE_5']}"> ${options['AUTO_CALC_PRICE_5']}
                    </label>
                </div>
            </td>
            <td style="min-width: 10em"></td>
        </tr>
    </#if>
        <tr>
            <th>Площадь</th>
            <td>
                <input id="area-input" name="area" value="${(order.area?c)!}" onchange="calculatePriceAndConsumes()"/>
            </td>
            <td></td>
        </tr>
        <tr>
            <th>Кляймеры</th>
            <td>
                <input id="clip-input" name="clipCount" value="${(order.clipCount?c)!}"
                       onchange="calculatePriceAndConsumes()"/>
            </td>
            <td id="clipsTd"></td>
        </tr>
        <tr>
            <th>Фурнитура мелкая</th>
            <td>
                <input id="furniture-small-input" name="furnitureSmallCount" value="${(order.furnitureSmallCount?c)!}"
                       onchange="calculatePriceAndConsumes()"/>
            </td>
            <td id="furnitureSmallTd"></td>
        </tr>
        <tr>
            <th>Фурнитура крупная</th>
            <td>
                <input id="furniture-big-input" name="furnitureBigCount" value="${(order.furnitureBigCount?c)!}"
                       onchange="calculatePriceAndConsumes()"/>
            </td>
            <td id="furnitureBigTd"></td>
        </tr>
        <tr>
            <th>Суммарная площадь</th>
            <td id="areaSumTd">
            </td>
            <td></td>
        </tr>
        <tr>
            <th>Документ</th>
            <td>
                <input name="document" value="${(order.document)!}"/>
            </td>
            <td></td>
        </tr>
        <tr>
        <th>Цена</th>
        <td>
            <input id="price-input" name="price" value="${(order.price?c)!}" onblur="replaceComma(event.target)"/>
        </td>
        <td></td>
    </tr>
    <tr>
        <th><label for="datepickerCompleteDate">Дата завершения</label></th>
        <td>
            <input id="datepickerCompleteDate" name="complete" type="text" value="<#if complete??>${complete.format('dd.MM.yy')}</#if>">
        </td>
        <td></td>
    </tr>
        <tr>
            <th>Менеджер</th>
            <td>
                <select name="managerId">
                <#list managers as manager>
                    <option value="${manager.id?c}"
                            <#if order?? && order.manager.id! == manager.id>selected</#if>>${manager.name}</option>
                </#list>
                </select>
            </td>
            <td></td>
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