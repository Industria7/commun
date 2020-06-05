<#import "template.ftl" as layout />
<@layout.mainLayout title="Калькулятор">
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
        </tr></#if>

    <form action="/calc" method="post">
        <div class="form-group1">
            <label for="gas">Газ</label>
            <input type="number" class="form-control" id="gas" name="gas" placeholder="Введите количество кубов" value="">
        </div>
        <div class="form-group2">
            <label for="water">Вода</label>
            <input type="number" class="form-control" id="water" name="water" placeholder="Введите количество кубов" value="">
        </div>
        <div class="form-group3">
            <label for="elec">Электричество</label>
            <input type="number" class="form-control" id="elec" name="elec" placeholder="Введите количество киловат" value="">
        </div>
        <input type="hidden" id="action" name="action" value="">
        <input type="hidden" id="id" name="id" value="<#if tarif??>${tarif.id}</#if>">
        <button type="submit" class="btn btn-primary">Подсчитать</button>
    </form>
</@layout.mainLayout>