<div id="order_${order.id}" class="kanban-order" onclick="selectOrder(${order.id}, '${order.status}')"
     style="background-image: url(${order.manager.picture}); ">
<#if order.status=='BLOCKED'><img src="/img/warning.png" class="warning-icon"
                                  title="Недостаточно материалов"/></#if>
    <div class="kanban-order-id">${order.id} : ${order.document!}</div>
    <div class="kanban-order-area">${order.area} м<sup>2</sup> / ${order.price} грн</div>
    <div class="kanban-order-consumes">|<#list order.consumes as consume> ${consume.product.name!}
        |</#list></div>
    <div class="kanban-order-manager">${order.manager.familyName}</div>
</div>