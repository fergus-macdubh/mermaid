<#include "../header.ftl">
<script type="application/javascript">
    $(function() {
        $("#inventorying-form").validate({
            rules: {
                quantity: {
                    required: true,
                    commaDotNumber: true,
                    min: 0
                },
                price: {
                    required: true,
                    commaDotNumber: true,
                    min: true
                }
            },
            messages: {
                quantity: {
                    required: "Поле 'Количество' должно быть заполнено.",
                    commaDotNumber: "'Количество' должно быть числом.",
                    min: "'Количество' должно быть положительным."
                },
                price: {
                    required: "Поле 'Цена' должно быть заполнено.",
                    commaDotNumber: "'Цена' должна быть числом.",
                    min: "'Цена' должна быть положительной."
                }
            },
            submitHandler: function(form) {
                form.submit();
            }
        });
    });
</script>

<h1>Инвентаризация</h1>

<form id="inventorying-form" method="post">
    <table class="responsive-table" style="width: 20em">
        <tr>
            <th>Товар</th>
            <td>${product.name} ${product.producer}</td>
        </tr>
        <tr>
            <th>Количество фактическое</th>
            <td><input name="quantity" value="${product.quantity}" onblur="replaceComma(event.target)" /></td>
        </tr>
        <tr>
            <th>Цена за 1 ${product.unit.abbr}</th>
            <td><input name="price" value="${product.price}" onblur="replaceComma(event.target)" /></td>
        </tr>
        <tr>
            <td colspan="2">
                <input class="btn btn-info" type="submit" value="Отправить"/>
            </td>
        </tr>
    </table>
    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}"/>
</form>
<#include "../footer.ftl">