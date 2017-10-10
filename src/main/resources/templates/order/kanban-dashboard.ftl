<#include "../header.ftl">
<script type="application/javascript">
    var selectedOrder;

    function selectOrder(id) {
        $('#orderIdInput').attr('value', id);
        $('#order_'+selectedOrder)
                .removeClass('kanban-selected-order')
                .addClass('kanban-order');
        $('#order_'+id)
                .removeClass('kanban-order')
                .addClass('kanban-selected-order');
        selectedOrder = id;
    }
</script>
<h1>Заказы</h1>
<div class="kanban-action">
    <form method="post">
        <input type="hidden" name="orderId" id="orderIdInput"/>
        <input type="submit" class="btn btn-info" value="Move"/>
    </form>
</div>
<div class="container-fluid">
    <div class="col-sm-4">
        <div class="kanban-column">
            <div class="kanban-column-head">Новые</div>
        <#list createdOrders as order>
            <div id="order_${order.id}" class="kanban-order" onclick="selectOrder(${order.id})">
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
            <div id="order_${order.id}" class="kanban-order" onclick="selectOrder(${order.id})">
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
            <div id="order_${order.id}" class="kanban-order" onclick="selectOrder(${order.id})">
                <div class="kanban-order-id">${order.id}</div>
                <div class="kanban-order-manager">${order.manager!}</div>
                <div class="kanban-order-client">${order.client!}</div>
            </div>
        </#list>
        </div>
    </div>
</div>
<#include "../footer.ftl">