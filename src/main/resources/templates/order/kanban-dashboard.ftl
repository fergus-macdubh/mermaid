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
            case 'COMPLETED':
                $('#moveSubmit')
                        .text('Недоступно')
                        .attr('data-target', '')
                        .addClass('disabled');
                break;
            case 'IN_PROGRESS':
                $('#moveSubmit')
                        .text('Готово')
                        .attr('data-target', '#modal-completed')
                        .removeClass('disabled');
        }

        $('#modal-inProgress-order-id').text(selectedOrderId);
        $('#modal-inProgress-order-area').text(orders[selectedOrderId].area);
        $('#modal-inProgress-order-client').text(orders[selectedOrderId].client);
        $('#modal-inProgress-order-id-input').attr('value', selectedOrderId);
        $('#modal-inProgress-order-status').text(orders[selectedOrderId].status);
        $('#modal-completed-order-id').text(selectedOrderId);
        $('#modal-completed-order-area').text(orders[selectedOrderId].area);
        $('#modal-completed-order-client').text(orders[selectedOrderId].client);
        $('#modal-completed-order-id-input').attr('value', selectedOrderId);
        $('#modal-completed-order-status').text(orders[selectedOrderId].status);
        $('#modal-inProgress-consumes-table > tr').remove();
        $('#modal-completed-consumes-table > tr').remove();
        for (i = 0; i < orders[selectedOrderId].consumes.length; i++) {
            $('#modal-inProgress-consumes-table').append('<tr><td>' + orders[selectedOrderId].consumes[i].product.name + '</td><td>' + orders[selectedOrderId].consumes[i].calculatedQuantity + ' ' + getUnitAbbr(orders[selectedOrderId].consumes[i].product.unit) + '</td><td></td></tr>');
            $('#modal-completed-consumes-table').append('<tr><td>' + orders[selectedOrderId].consumes[i].product.name + ' (' + getUnitAbbr(orders[selectedOrderId].consumes[i].product.unit) + ')'
                    + '<input type="hidden" name="productIds" value="' + orders[selectedOrderId].consumes[i].product.id + '"></td><td><input name="remains" value="0"></td><td></td></tr>');
        }
    }

    function getUnitAbbr(unit) {
        switch (unit) {
            case "KILOGRAM": return "кг";
            case "LITER": return "л";
            case "ITEM": return "шт";
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
                <div class="kanban-order-id">${order.id}</div>
                <div class="kanban-order-manager">${order.manager!}</div>
                <div class="kanban-order-client">${order.client!}</div>
            </div>
        </#list>
        </div>
    </div>
    <div class="col-sm-4">
        <div class="kanban-column">
            <div class="kanban-column-head">В работе</div>
        <#list inProgressOrders as order>
            <div id="order_${order.id}" class="kanban-order" onclick="selectOrder(${order.id}, '${order.status}')">
                <div class="kanban-order-id">${order.id}</div>
                <div class="kanban-order-manager">${order.manager!}</div>
                <div class="kanban-order-client">${order.client!}</div>
            </div>
        </#list>
        </div>
    </div>
    <div class="col-sm-4">
        <div class="kanban-column">
            <div class="kanban-column-head">Готовы</div>
        <#list completedOrders as order>
            <div id="order_${order.id}" class="kanban-order" onclick="selectOrder(${order.id}, '${order.status}')">
                <div class="kanban-order-id">${order.id}</div>
                <div class="kanban-order-manager">${order.manager!}</div>
                <div class="kanban-order-client">${order.client!}</div>
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
                        <td id="modal-inProgress-order-id"></td>
                    </tr>
                    <tr>
                        <th>Статус</th>
                        <td id="modal-inProgress-order-status"></td>
                    </tr>
                    <tr>
                        <th>Площадь</th>
                        <td id="modal-inProgress-order-area"></td>
                    </tr>
                    <tr>
                        <th>Клиент</th>
                        <td id="modal-inProgress-order-client"></td>
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
                    <input id="modal-inProgress-order-id-input" type="hidden" name="orderId"/>
                    <input type="submit" class="btn btn-info" value="В работу">
                </form>
            </div>
        </div>
    </div>
</div>
<div id="modal-completed" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header"><strong>Закончить работу над заказом</strong></div>
            <div class="modal-body">
                <table class="responsive-table" style="width:20em">
                    <tr>
                        <th>ID</th>
                        <td id="modal-completed-order-id"></td>
                    </tr>
                    <tr>
                        <th>Статус</th>
                        <td id="modal-completed-order-status"></td>
                    </tr>
                    <tr>
                        <th>Площадь</th>
                        <td id="modal-completed-order-area"></td>
                    </tr>
                    <tr>
                        <th>Клиент</th>
                        <td id="modal-completed-order-client"></td>
                    </tr>
                </table>
                <form method="post">
                    <table id="modal-completed-consumes-table" class="responsive-table" style="width: 100%">
                        <thead>
                        <th>Расходный материал</th>
                        <th>Остаток</th>
                        </thead>
                    </table>
                    <div class="alert alert-warning"><strong>Остатки материалов будут возвращены на склад</strong></div>
                    <input id="modal-completed-order-id-input" type="hidden" name="orderId"/>
                    <input type="submit" class="btn btn-info" value="Готово">
                </form>
            </div>
        </div>
    </div>
</div>
<#include "../footer.ftl">