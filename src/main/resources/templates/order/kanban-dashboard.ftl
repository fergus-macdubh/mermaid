<#include "../header.ftl">
<script type="application/javascript">

    var selectedOrderId;
    var orders = ${ordersJson};

    $(function () {
        $("#inProgressForm").validate({
            rules: {
                teamId: {
                    required: true
                }
            },
            messages: {
                teamId: {
                    required: "Нужно выбрать бригаду."
                }
            },
            submitHandler: function (form) {
                form.submit();
            },
            errorPlacement: function(error, element) {
                error.appendTo('#teamRadioContainer');
            }
        });

        var current = new Date();
        for (var orderId in orders) {
            if (!orders.hasOwnProperty(orderId)) continue;
            var order = orders[orderId];
            var planned = new Date(order.planned.year, order.planned.month - 1, order.planned.day);
            var timeDiff = Math.abs(planned.getTime() - current.getTime());
            var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
            $('#order_' + order.id + '_days_left')
                    .addClass(getClassForDaysLeft(diffDays))
                    .text(diffDays);
        }
    });

    function getClassForDaysLeft(daysLeft) {
        if (daysLeft <= 0) return 'btn-danger';
        if (daysLeft <= 2) return 'btn-warning';
        return 'btn-success';
    }

    function selectOrder(id, status) {
        $('#order_' + selectedOrderId)
                .removeClass('kanban-selected-order');
        $('#order_' + id)
                .addClass('kanban-selected-order');
        selectedOrderId = id;

        switch (status) {
            case 'CREATED':
                $('#moveSubmit')
                        .text('В работу')
                        .removeClass('disabled')
                        .attr('data-target', '#modal-inProgress');
                $('#in-progress-submit-button').removeClass('hidden');
                $('#spend-consumes-warning').removeClass('hidden');
                $('#not-enough-consumes-error').addClass('hidden');
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
                        .attr('data-target', '#modal-inProgress')
                        .removeClass('disabled');
                $('#in-progress-submit-button').addClass('hidden');
                $('#spend-consumes-warning').addClass('hidden');
                $('#not-enough-consumes-error').removeClass('hidden');
        }

        $('.cancel-link').attr('href', '/order/' + selectedOrderId + '/cancel');
        $('.modal-order-id').text(selectedOrderId);
        $('.modal-order-area').text(orders[selectedOrderId].area);
        $('.modal-order-document').text(orders[selectedOrderId].document);
        $('.modal-order-id-input').attr('value', selectedOrderId);
        $('.modal-order-status').text(orders[selectedOrderId].status);
        $('#modal-inProgress-consumes-table > tr').remove();
        $('#modal-shipping-consumes-table > tr').remove();
        $('#modal-done-consumes-table > tr').remove();
        for (i = 0; i < orders[selectedOrderId].consumes.length; i++) {
            $('#modal-inProgress-consumes-table').append('<tr><td>' + orders[selectedOrderId].consumes[i].product.name + '</td><td>' + orders[selectedOrderId].consumes[i].calculatedQuantity + ' ' + getUnitAbbr(orders[selectedOrderId].consumes[i].product.unit) + '</td><td></td></tr>');
            $('#modal-shipping-consumes-table').append('<tr><td>' + orders[selectedOrderId].consumes[i].product.name + ' (' + getUnitAbbr(orders[selectedOrderId].consumes[i].product.unit) + ')'
                    + '<input type="hidden" name="consumeIds" value="' + orders[selectedOrderId].consumes[i].id + '"></td><td><input name="actualQuantities" onblur="replaceComma(event.target)" value="' + orders[selectedOrderId].consumes[i].calculatedQuantity + '"></td><td></td></tr>');
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

    function cancelOrder() {
        selectedOrderId
    }
</script>
<h1>Заказы</h1>
<div class="container-fluid">
    <div class="col-sm-12">
        <button id="moveSubmit" class="btn btn-info disabled" data-toggle="modal" data-target="#modal">Заказ не выбран
        </button>
    </div>
</div>
<br/>
<div class="container-fluid">
    <div class="col-sm-4">
        <div class="kanban-column">
            <div class="kanban-column-head">Новые</div>
        <#list orders as order>
            <#if order.status == 'CREATED' || order.status == 'BLOCKED'>
                <#include 'kanban-order.ftl'/>
            </#if>
        </#list>
        </div>
    </div>
    <div class="col-sm-4">
        <div class="kanban-column">
            <div class="kanban-column-head">В работе</div>
        <#list orders as order>
            <#if order.status == 'IN_PROGRESS'>
                <#include 'kanban-order.ftl'/>
            </#if>
        </#list>
        </div>
    </div>
    <div class="col-sm-4">
        <div class="kanban-column">
            <div class="kanban-column-head">К отгрузке</div>
        <#list orders as order>
            <#if order.status == 'SHIPPING'>
                <#include 'kanban-order.ftl'/>
            </#if>
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
                        <th>Документ</th>
                        <td class="modal-order-document"></td>
                    </tr>
                </table>

                <table id="modal-inProgress-consumes-table" class="responsive-table" style="width: 100%">
                    <thead>
                    <th>Расходный материал</th>
                    <th>Количество</th>
                    </thead>
                </table>
                <div id="spend-consumes-warning" class="alert alert-warning"><strong>Расходные материалы будут списаны
                    со склада!</strong></div>
                <div id="not-enough-consumes-error" class="alert alert-danger"><strong>Недостаточно расходных
                    материалов!</strong></div>
                <form id="inProgressForm" method="post">
                <#if user.role == "ROLE_ADMIN"
                || user.role == "ROLE_OPERATOR"
                || user.role == "ROLE_PAINTER">
                    <div id="teamRadioContainer">
                        <div id="teamRadio" class="btn-group" data-toggle="buttons">
                            <#list teams as team>
                                <label class="btn btn-primary <#if user.team?? && user.team.id == team.id>active</#if>">
                                    <input type="radio" name="teamId" value="${team.id}"
                                           <#if user.team?? && user.team.id == team.id>checked</#if>> ${team.name}
                                </label>
                            </#list>
                        </div>
                    </div>
                </#if>
                    <input type="hidden"
                           name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                    <input class="modal-order-id-input" type="hidden" name="orderId"/>
                    <div class="modal-footer">
                    <#if user.role == "ROLE_ADMIN">
                        <div class="btn btn-large btn-danger" data-toggle="modal"
                             data-target="#modal-cancel-new-confirmation">
                            Отменить заказ
                        </div>
                    </#if>
                    <#if user.role == "ROLE_ADMIN"
                    || user.role == "ROLE_OPERATOR"
                    || user.role == "ROLE_PAINTER">
                        <input id="in-progress-submit-button" type="submit" class="btn btn-info" value="В работу">
                    </#if>
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
                        <th>Документ</th>
                        <td class="modal-order-document"></td>
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
                    <#if user.role == "ROLE_ADMIN">
                        <div class="btn btn-large btn-danger" data-toggle="modal"
                             data-target="#modal-cancel-in-progress-confirmation">
                            Отменить заказ
                        </div>
                    </#if>
                    <#if user.role == "ROLE_ADMIN"
                    || user.role == "ROLE_OPERATOR"
                    || user.role == "ROLE_PAINTER">
                        <input type="submit" class="btn btn-info" value="Готово">
                    </#if>
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
                        <th>Документ</th>
                        <td class="modal-order-document"></td>
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
                    <#if user.role == "ROLE_ADMIN"
                    || user.role == "ROLE_OPERATOR"
                    || user.role == "ROLE_PAINTER">
                        <input type="submit" class="btn btn-info" value="Закрыть заказ">
                    </#if>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div id="modal-cancel-new-confirmation" class="modal fade" role="dialog">
    <div class="modal-dialog" style="width: 30em">
        <div class="modal-content">
            <div class="modal-header"><strong>Отменить заказ?</strong></div>
            <div class="modal-body">
                <div class="alert alert-warning"><strong>Заказ будет полностью отменен</strong></div>
                <a class="btn btn-success cancel-link">Отменить заказ</a>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Нееееет!</button>
            </div>
        </div>
    </div>
</div>
<div id="modal-cancel-in-progress-confirmation" class="modal fade" role="dialog">
    <div class="modal-dialog" style="width: 30em">
        <div class="modal-content">
            <div class="modal-header"><strong>Отменить заказ?</strong></div>
            <div class="modal-body">
                <div class="alert alert-warning"><strong>
                    Заказ будет возвращен в "Новые", а материалы будут возвращены на склад
                </strong></div>
                <a class="btn btn-success cancel-link">Отменить заказ</a>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Нееееет!</button>
            </div>
        </div>
    </div>
</div>
<#include "../footer.ftl">