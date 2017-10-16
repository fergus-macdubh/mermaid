<#include "../header.ftl">
<script type="application/javascript">
    $(function() {
        $("#inventorying-form").validate({
            rules: {
                quantity: {
                    required: true,
                    number: true,
                    min: 0
                }
            },
            messages: {
                quantity: {
                    required: "Поле 'Количество' должно быть заполнено.",
                    min: "Количество не может быть отрицательным.",
                    number: "Поле 'Количество' может содержать только цифры."
                }
            },
            submitHandler: function(form) {
                form.submit();
            }
        });
    });
</script>

<h1>Закупка товара</h1>

<form id="inventorying-form" method="post">
    <table class="responsive-table" style="width: 20em">
        <tr>
            <th>Товар</th>
            <td>${product.name}</td>
        </tr>
        <tr>
            <th>Количество</th>
            <td><input name="quantity" value="${product.quantity}" /></td>
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