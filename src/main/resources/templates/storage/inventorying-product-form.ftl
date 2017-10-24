<#include "../header.ftl">
<script type="application/javascript">
    $(function() {
        $("#inventorying-form").validate({
            rules: {
                quantity: {
                    required: true,
                    commaDotNumber: true
                }
            },
            messages: {
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

<form id="inventorying-form" method="post">
    <table class="responsive-table" style="width: 20em">
        <tr>
            <th>Товар</th>
            <td>${product.name}</td>
        </tr>
        <tr>
            <th>Количество</th>
            <td><input name="quantity" value="${product.quantity}" onblur="replaceComma(event.target)" /></td>
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