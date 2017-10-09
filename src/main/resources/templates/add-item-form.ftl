<#include "header.ftl">
<h1>Новый товар</h1>

<form method="post" action="/storage/item/add">
    <table class="responsive-table" style="width: 20em">
        <tr>
            <th>Товар</th>
            <td>
                <input name="name" />
            </td>
        </tr>
        <tr>
            <th>Производитель</th>
            <td>
                <input name="producer" />
            </td>
        </tr>
        <tr>
            <th>Поставщик</th>
            <td>
                <input name="supplier" />
            </td>
        </tr>
        <tr>
            <th>Единицы измерения</th>
            <td>
                <select name="unit">
                    <option value="KILOGRAM">кг</option>
                    <option value="ITEM">шт</option>
                    <option value="LITER">л</option>
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
<#include "footer.ftl">