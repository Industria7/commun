<#import "template.ftl" as layout />
<@layout.mainLayout>
    <#if user??><div>Привет [${user.name}]</div></#if>
    <table class="table">
        <thead class="thead-dark">
            <tr>
                <th scope="col">Дата посл. платежа</th>
            </tr>
        </thead>
        <tbody>
            <#if setting??>
                <tr>
                    <td><#if setting.old_pay_Date??>${setting.old_pay_Date}<#else>Нету</#if></td>
                </tr>
            <#else>Настроек нет</#if>
        </tbody>
    </table>

    <table class="table">
    <thead class="thead-dark">
        <tr>
            <th scope="col">Дата</th>
            <th scope="col">Газ</th>
            <th scope="col">Вода</th>
            <th scope="col">Электр. 1</th>
            <th scope="col">Электр. 2</th>
            <th scope="col">Электр. 3</th>
            <th scope="col">Электр. порог 1</th>
            <th scope="col">Электр. порог 2</th>
        </tr>
    </thead>
    <tbody>
    <#if tarif??>
            <tr>
                <td>${tarif.date}</td>
                <td>${tarif.gas}</td>
                <td>${tarif.water}</td>
                <td>${tarif.elec1}</td>
                <td>${tarif.elec2}</td>
                <td>${tarif.elec3}</td>
                <td>${tarif.elec_level1}</td>
                <td>${tarif.elec_level2}</td>
            </tr>
    <#else>Тарифа нет</#if>
</@layout.mainLayout>