<#include "../header.ftl">
<script type="application/javascript">
    $(function() {
        $("#purchase-form").validate({
            rules: {
                price: {
                    required: true,
                    commaDotNumber: true},
                quantity: {
                    required: true,
                    commaDotNumber: true
                }
            },
            messages: {
                price: {
                    required: "Поле 'Цена' должно быть заполнено.",
                    commaDotNumber: "'Цена' должна быть числом."
                },
                quantity: {
                    required: "Поле 'Количество' должно быть заполнено.",
                    commaDotNumber: "'Количество' должно быть числом."
                }
            },
            submitHandler: function(form) {
                form.submit();
            }
        });
    });
</script>
<h1>Закупка товара</h1>

<form id="purchase-form" method="post">
    <table class="responsive-table" style="width: 20em">
        <tr>
            <th>Товар</th>
            <td>
                <select name="productId">
                <#list products as product>
                    <option value="${product.id?c}">${product.name}</option>
                </#list>
                </select>
            </td>
        </tr>
        <tr>
            <th>Цена за единицу</th>
            <td>
                <input name="price" />
            </td>
        </tr>
        <tr>
            <th>Количество</th>
            <td>
                <input name="quantity" />
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input class="btn-info btn" type="submit" value="Отправить"/>
            </td>
        </tr>
    </table>
    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}"/>
</form>
<#include "../footer.ftl">