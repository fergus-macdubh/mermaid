<div id="order_${order.id?c}" class="kanban-order" onclick="selectOrder(${order.id?c}, '${order.status}')"
     style="background-image: url(<#if order.client.manager.picture??>${order.client.manager.picture}</#if>);
     <#if order.status=='IN_PROGRESS'>border: 3px solid #${(order.team.color)!}</#if>
             ">
<#if order.status=='BLOCKED'><img src="/img/warning.png" class="warning-icon"
                                  title="Недостаточно материалов"/></#if>
    <div class="kanban-order-id-doc">
        <span class="kanban-order-id">${order.id?c}</span>&nbsp;&nbsp;<span
            class="kanban-order-document">${order.document!}</span>
    </div>
    <div class="kanban-order-client">${order.client.name}</div>
    <div class="kanban-order-consumes">|<#list order.consumes as consume> ${consume.product.name!} ${consume.product.producer}
        |</#list></div>
    <div class="kanban-order-area">
    <#if order.area gt 0>
    ${order.area} м<sup>2</sup> /
    </#if>
    <#if order.clipCount gt 0>
    ${order.clipCount} кл /
    </#if>
    <#if order.furnitureSmallCount gt 0>
    ${order.furnitureSmallCount} сф /
    </#if>
    <#if order.furnitureBigCount gt 0>
    ${order.furnitureBigCount} бф /
    </#if>
         ${order.price} грн
    </div>
    <div class="kanban-order-date">${order.created.format('dd MMM HH:mm')},
    <#if order.status == 'SHIPPING'>
        готов ${order.completed.format('dd MMM')}
    <#else>
        план на ${order.planned.format('dd MMM')}
        <button id="order_${order.id?c}_days_left" class="btn-circle"></button>
    </#if>
    </div>
</div>