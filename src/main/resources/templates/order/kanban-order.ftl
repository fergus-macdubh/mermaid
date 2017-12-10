<div id="order_${order.id}" class="kanban-order" onclick="selectOrder(${order.id}, '${order.status}')"
     style="background-image: url(${order.manager.picture});
<#if order.status=='IN_PROGRESS'>border: 3px solid #${(order.team.color)!}</#if>
             ">
<#if order.status=='BLOCKED'><img src="/img/warning.png" class="warning-icon"
                                  title="Недостаточно материалов"/></#if>
    <div class="kanban-order-id-doc">
        <span class="kanban-order-id">${order.id}</span>&nbsp;&nbsp;<span class="kanban-order-document">${order.document!}</span>
    </div>
    <div class="kanban-order-manager">${order.manager.familyName}</div>
    <div class="kanban-order-consumes">|<#list order.consumes as consume> ${consume.product.name!}
        |</#list></div>
    <div class="kanban-order-area">${order.area} м<sup>2</sup> / ${order.price} грн</div>
    <div class="kanban-order-date">${order.created.format('dd MMM HH:mm')},
        <#if order.status == 'SHIPPING'>
            готов ${order.completed.format('dd MMM')}
        <#else>
            план на ${order.planned.format('dd MMM')}
            <button id="order_${order.id}_days_left" class="btn-circle"></button>
        </#if>
        </div>
</div>