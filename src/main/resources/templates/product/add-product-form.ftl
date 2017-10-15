<#include "../header.ftl">
<#if product??>
<h1>Новый товар</h1>
<#else>
<h1>Изменение товара</h1>
</#if>

<form method="post">
    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}"/>
    <table class="responsive-table" style="width: 20em">
    <#if product??>
        <tr>
            <th>ID</th>
            <td>
                ${(product.id)!}
            </td>
        </tr>
    </#if>
        <tr>
            <th>Товар</th>
            <td>
                <input name="name" value="${(product.name)!}"/>
            </td>
        </tr>
        <tr>
            <th>Производитель</th>
            <td>
                <input name="producer" value="${(product.producer)!}"/>
            </td>
        </tr>
        <tr>
            <th>Поставщик</th>
            <td>
                <input name="supplier" value="${(product.supplier)!}"/>
            </td>
        </tr>
        <tr>
            <th>Единицы измерения</th>
            <td>
                <select name="unit">
                    <option value="KILOGRAM" <#if (product.unit)! == 'KILOGRAM'>selected</#if>>кг</option>
                    <option value="ITEM" <#if (product.unit)! == 'ITEM'>selected</#if>>шт</option>
                    <option value="LITER" <#if (product.unit)! == 'LITER'>selected</#if>>л</option>
                </select>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Отправить"/>
            </td>
        </tr>
    </table>
</form>
<#include "../footer.ftl">