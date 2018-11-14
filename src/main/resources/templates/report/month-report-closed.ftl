<div style="float: left">
    <div><a href="/reports/month/${year?c}-${monthNum?string["00"]}/options" class="btn btn-success">Настройки</a></div>
    <h3>Расходы</h3>
    <table class="responsive-table" style="width: 30em">
        <tr>
            <th>Сумма затрат на краску</th>
            <td>${reportCommon.sumPriceConsumes?string[",##0.##"]} грн</td>
        </tr>
        <tr>
            <th>Электроэнергия</th>
            <td>${reportCommon.electricity?string[",##0.##"]} грн</td>
        </tr>
        <tr>
            <th>Налог ФЛП</th>
            <td>${reportCommon.enterprenuerTax?string[",##0.##"]} грн</td>
        </tr>
        <tr>
            <th>Заработная плата</th>
            <td>${reportCommon.sumSalary?string[",##0.##"]} грн</td>
        </tr>
        <tr>
            <th>Премии</th>
            <td>${reportCommon.sumBonuses?string[",##0.##"]} грн</td>
        </tr>
        <tr>
            <th>Аренда помещения</th>
            <td>${reportCommon.buildingRent?string[",##0.##"]} грн</td>
        </tr>
        <tr>
            <th>Амортизация линии</th>
            <td>${reportCommon.lineAmortization?string[",##0.##"]} грн</td>
        </tr>
        <tr>
            <th>Расходные материалы</th>
            <td>${reportCommon.otherConsumes?string[",##0.##"]} грн</td>
        </tr>
        <tr>
            <th>Прочие расходы</th>
            <td>${reportCommon.otherExpenses?string[",##0.##"]} грн</td>
        </tr>
    </table>
</div>
    <div style="float: left; margin-left: 2em">
        <h3>Доходы</h3>
        <table class="responsive-table" style="width: 30em">
            <tr>
                <th>Общая площадь покраски</th>
                <td>${reportCommon.sumArea} м<sup>2</sup></td>
            </tr>
            <tr>
                <th>Общий валовый доход</th>
                <td>${reportCommon.sumPrice?string[",##0.##"]} грн</td>
            </tr>
        </table>
    </div>
    <br style="clear: both">
    <h4><strong>Прибыль: ${reportCommon.totalIncome}</strong></h4>
    <h3>По бригадам</h3>
    <table class="responsive-table">
        <tr>
            <thead>
            <th>Бригада</th>
            <th>Площадь</th>
            <th>Доход</th>
            <th>Расход краски</th>
            <th>Цена краски</th>
            <th>Зарплата</th>
            <th>Премия</th>
            </thead>
        </tr>
    <#list reportTeamList as reportTeam>
        <tr>
            <td><a href="/reports/month/${year?c}-${monthNum?c}/team/${reportTeam.teamId?c}">${reportTeam.teamName}</a></td>
            <td>${reportTeam.area?string[",##0.##"]} м<sup>2</sup></td>
            <td>${reportTeam.income?string[",##0.##"]} грн</td>
            <td>${reportTeam.paintExpenses?string[",##0.##"]} кг</td>
            <td>${reportTeam.paintPrice?string[",##0.##"]} грн</td>
            <td>${reportTeam.salary?string[",##0.##"]} грн</td>
            <td>${reportTeam.bonus?string[",##0.##"]} грн</td>
        </tr>
    </#list>
    </table>

    <h3>По сотрудникам</h3>
    <table class="responsive-table">
        <tr>
            <thead>
            <th>Сотрудник</th>
            <th>Площадь</th>
            <th>Зарплата</th>
            <th>Премия</th>
            <th>К выплате</th>
            </thead>
        </tr>
    <#list reportEmployeeList as reportEmployee>
        <tr>
            <td><a href="/reports/month/${year?c}-${monthNum?c}/user/${reportEmployee.employeeId?c}">${reportEmployee.employeeName}</a></td>
            <td>${reportEmployee.area?string[",##0.##"]} м<sup>2</sup></td>
            <td>${reportEmployee.salary?string[",##0.##"]} грн</td>
            <td>${reportEmployee.bonus?string[",##0.##"]} грн</td>
            <td>${(reportEmployee.salary + reportEmployee.bonus)?string[",##0.##"]} грн</td>
        </tr>
    </#list>
    </table>