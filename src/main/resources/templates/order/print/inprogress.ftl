Id;Документ;Краска;Производитель;Норма расхода;Выдано краски;Возврат краски;Фактический расход
<#list orders?sort_by(['created']) as order>
<#list order.consumes as consume>
${order.id?c};${order.document};${consume.product.name};${consume.product.producer};${consume.calculatedQuantity};;;
</#list>
</#list>