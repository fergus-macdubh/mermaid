<#include "../header.ftl">
<script type="application/javascript">
    var selectedOrderId;
    var orders = ${ordersJson};

    function selectOrder(id, status) {
        $('#order_' + selectedOrderId)
                .removeClass('kanban-selected-order')
                .addClass('kanban-order');
        $('#order_' + id)
                .removeClass('kanban-order')
                .addClass('kanban-selected-order');
        selectedOrderId = id;

        switch (status) {
            case 'CREATED':
                $('#moveSubmit')
                        .text('В работу')
                        .removeClass('disabled')
                        .attr('data-target', '#modal-inProgress');
                break;
            case 'SHIPPING':
                $('#moveSubmit')
                        .text('Закрыть')
                        .attr('data-target', '#modal-done')
                        .removeClass('disabled');
                break;
            case 'IN_PROGRESS':
                $('#moveSubmit')
                        .text('Готово')
                        .attr('data-target', '#modal-shipping')
                        .removeClass('disabled');
                break;
            case 'BLOCKED':
                $('#moveSubmit')
                        .text('Недостаточно материалов')
                        .attr('data-target', '')
                        .addClass('disabled');
        }

        $('.modal-order-id').text(selectedOrderId);
        $('.modal-order-area').text(orders[selectedOrderId].area);
        $('.modal-order-client').text(orders[selectedOrderId].client);
        $('.modal-order-id-input').attr('value', selectedOrderId);
        $('.modal-order-status').text(orders[selectedOrderId].status);
        $('#modal-inProgress-consumes-table > tr').remove();
        $('#modal-shipping-consumes-table > tr').remove();
        $('#modal-done-consumes-table > tr').remove();
        for (i = 0; i < orders[selectedOrderId].consumes.length; i++) {
            $('#modal-inProgress-consumes-table').append('<tr><td>' + orders[selectedOrderId].consumes[i].product.name + '</td><td>' + orders[selectedOrderId].consumes[i].calculatedQuantity + ' ' + getUnitAbbr(orders[selectedOrderId].consumes[i].product.unit) + '</td><td></td></tr>');
            $('#modal-shipping-consumes-table').append('<tr><td>' + orders[selectedOrderId].consumes[i].product.name + ' (' + getUnitAbbr(orders[selectedOrderId].consumes[i].product.unit) + ')'
                    + '<input type="hidden" name="productIds" value="' + orders[selectedOrderId].consumes[i].product.id + '"></td><td><input name="actualQuantities" value="' + orders[selectedOrderId].consumes[i].calculatedQuantity + '"></td><td></td></tr>');
            $('#modal-done-consumes-table').append('<tr><td>' + orders[selectedOrderId].consumes[i].product.name + '</td><td>' + orders[selectedOrderId].consumes[i].calculatedQuantity + ' ' + getUnitAbbr(orders[selectedOrderId].consumes[i].product.unit) + '</td><td>' + orders[selectedOrderId].consumes[i].actualUsedQuantity + ' ' + getUnitAbbr(orders[selectedOrderId].consumes[i].product.unit) + '</td></tr>');
        }
    }

    function getUnitAbbr(unit) {
        switch (unit) {
            case "KILOGRAM":
                return "кг";
            case "LITER":
                return "л";
            case "ITEM":
                return "шт";
        }
    }
</script>
<h1>Заказы</h1>
<div class="container-fluid">
    <div class="col-sm-12">
        <button id="moveSubmit" class="btn btn-info disabled" data-toggle="modal" data-target="#modal">Заказ не выбран
        </button>
        </form>
    </div>
</div>
<br/>
<div class="container-fluid">
    <div class="col-sm-4">
        <div class="kanban-column">
            <div class="kanban-column-head">Новые</div>
        <#list createdOrders as order>
            <div id="order_${order.id}" class="kanban-order" onclick="selectOrder(${order.id}, '${order.status}')">
                <#if order.status=='BLOCKED'><img src="/img/warning.png" class="warning-icon" title="Недостаточно материалов"/></#if>
                <div class="kanban-order-id">${order.id} : ${order.client!}</div>
                <div class="kanban-order-area">${order.area} м<sup>2</sup></div>
                <div class="kanban-order-consumes">|<#list order.consumes as consume> ${consume.product.name!}
                    |</#list></div>
                <div class="kanban-order-manager">${order.manager.familyName}</div>
            </div>
        </#list>
        </div>
    </div>
    <div class="col-sm-4">
        <div class="kanban-column">
            <div class="kanban-column-head">В работе</div>
        <#list inProgressOrders as order>
            <div id="order_${order.id}" class="kanban-order" onclick="selectOrder(${order.id}, '${order.status}')">
                <div class="kanban-order-id">${order.id} : ${order.client!}</div>
                <div class="kanban-order-area">${order.area} м<sup>2</sup></div>
                <div class="kanban-order-consumes">|<#list order.consumes as consume> ${consume.product.name!}
                    |</#list></div>
                <div class="kanban-order-manager">${order.manager.familyName}</div>
            </div>
        </#list>
        </div>
    </div>
    <div class="col-sm-4">
        <div class="kanban-column">
            <div class="kanban-column-head">К отгрузке</div>
        <#list shippingOrders as order>
            <div id="order_${order.id}" class="kanban-order" onclick="selectOrder(${order.id}, '${order.status}')">
                <div class="kanban-order-id">${order.id} : ${order.client!}</div>
                <div class="kanban-order-area">${order.area} м<sup>2</sup></div>
                <div class="kanban-order-consumes">|<#list order.consumes as consume> ${consume.product.name!}
                    |</#list></div>
                <div class="kanban-order-manager">${order.manager.familyName}</div>
            </div>
        </#list>
        </div>
    </div>
</div>
<div id="modal-inProgress" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header"><strong>Начать работу над заказом</strong></div>
            <div class="modal-body">
                <table class="responsive-table" style="width:20em">
                    <tr>
                        <th>ID</th>
                        <td class="modal-order-id"></td>
                    </tr>
                    <tr>
                        <th>Статус</th>
                        <td class="modal-order-status"></td>
                    </tr>
                    <tr>
                        <th>Площадь</th>
                        <td class="modal-order-area"></td>
                    </tr>
                    <tr>
                        <th>Клиент</th>
                        <td class="modal-order-client"></td>
                    </tr>
                </table>

                <table id="modal-inProgress-consumes-table" class="responsive-table" style="width: 100%">
                    <thead>
                    <th>Расходный материал</th>
                    <th>Количество</th>
                    </thead>
                </table>
                <div class="alert alert-warning"><strong>Расходные материалы будут списаны со склада!</strong></div>
                <form method="post">
                    <input type="hidden"
                           name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                    <input class="modal-order-id-input" type="hidden" name="orderId"/>
                    <div class="modal-footer">
                        <input type="submit" class="btn btn-info" value="В работу">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div id="modal-shipping" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header"><strong>Закончить работу над заказом</strong></div>
            <div class="modal-body">
                <table class="responsive-table" style="width:20em">
                    <tr>
                        <th>ID</th>
                        <td class="modal-order-id"></td>
                    </tr>
                    <tr>
                        <th>Статус</th>
                        <td class="modal-order-status"></td>
                    </tr>
                    <tr>
                        <th>Площадь</th>
                        <td class="modal-order-area"></td>
                    </tr>
                    <tr>
                        <th>Клиент</th>
                        <td class="modal-order-client"></td>
                    </tr>
                </table>
                <form method="post">
                    <input type="hidden"
                           name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                    <table id="modal-shipping-consumes-table" class="responsive-table" style="width: 100%">
                        <thead>
                        <th>Расходный материал</th>
                        <th>Фактичский расход</th>
                        </thead>
                    </table>
                    <div class="alert alert-warning"><strong>Остатки материалов будут возвращены на склад</strong></div>
                    <input class="modal-order-id-input" type="hidden" name="orderId"/>
                    <div class="modal-footer">
                        <input type="submit" class="btn btn-info" value="Готово">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div id="modal-done" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header"><strong>Закрыть заказ</strong></div>
            <div class="modal-body">
                <table class="responsive-table" style="width:20em">
                    <tr>
                        <th>ID</th>
                        <td class="modal-order-id"></td>
                    </tr>
                    <tr>
                        <th>Статус</th>
                        <td class="modal-order-status"></td>
                    </tr>
                    <tr>
                        <th>Площадь</th>
                        <td class="modal-order-area"></td>
                    </tr>
                    <tr>
                        <th>Клиент</th>
                        <td class="modal-order-client"></td>
                    </tr>
                </table>
                <form method="post">
                    <input type="hidden"
                           name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                    <table id="modal-done-consumes-table" class="responsive-table" style="width: 100%">
                        <thead>
                        <th>Расходный материал</th>
                        <th>Расчетный расход</th>
                        <th>Фактический расход</th>
                        </thead>
                    </table>
                    <input class="modal-order-id-input" type="hidden" name="orderId"/>
                    <div class="modal-footer">
                        <input type="submit" class="btn btn-info" value="Закрыть заказ">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<#include "../footer.ftl">